package com.ozstrategy.el.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.math.BigDecimal;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.ehcache.UserManagedCache;
import org.ehcache.ValueSupplier;

import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;

import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expiry;

import org.reflections.Reflections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozstrategy.el.annotation.OzCacheable;
import com.ozstrategy.el.annotation.OzExposedClass;
import com.ozstrategy.el.annotation.OzExposedMethod;
import com.ozstrategy.el.antlr.OzElLexer;
import com.ozstrategy.el.antlr.OzElParser;
import com.ozstrategy.el.antlr.OzVerboseListener;
import com.ozstrategy.el.exception.UnknownVariableException;
import com.ozstrategy.el.util.CalculateUtil;
import com.ozstrategy.el.util.FunctionUtil;
import com.ozstrategy.el.util.NoCaseHashMap;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 7/6/13 Time: 10:10 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class ResolverContext {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static NoCaseHashMap<ExposedMethod> exposedMethods = new NoCaseHashMap<ExposedMethod>();
  private static NoCaseHashMap<OzElContext>   sharedContext  = new NoCaseHashMap<OzElContext>();

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private boolean batchMode = true;
  private boolean cacheMode = false;

  private NoCaseHashMap<OzElContext> context = new NoCaseHashMap<OzElContext>();

  // private NoCaseHashMap<OzElValue>   exprCache = new NoCaseHashMap<OzElValue>();
  private UserManagedCache<String, OzElValue> exprCache = null;
  private Logger                              logger    = LoggerFactory.getLogger(ResolverContext.class);

  private NoCaseHashMap<ExposedMethod> overrideMethods = new NoCaseHashMap<ExposedMethod>();

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ResolverContext object.
   */
  public ResolverContext() {
    updateExposedValues();

  } // end ResolverContext

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * addExposedClass - Add static methods of an exposed class.
   *
   * @param  clazz          Class
   * @param  annotatedOnly  boolean
   */
  public static synchronized void addExposedClass(Class clazz, boolean annotatedOnly) {
    for (Method method : clazz.getMethods()) {
      if ((!annotatedOnly) || method.isAnnotationPresent(OzExposedMethod.class)) {
        // if annotatedOnly is true then only add those method with annotations
        addExposedMethod(method);
      }
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addExposedMethod - Add a exposed method if it is a static one.
   *
   * @param  method  Method
   */
  public static synchronized void addExposedMethod(Method method) {
    int    modifiers  = method.getModifiers();
    String methodName = method.getName().toLowerCase();

    if (Modifier.isStatic(modifiers)) {
      boolean cacheable = method.isAnnotationPresent(OzCacheable.class);
      exposedMethods.put(methodName, new ExposedMethod(methodName, method.getDeclaringClass()));

      if (method.getParameterCount() == 0) {
        // add shortcut variables for method
        sharedContext.put(methodName, new OzElContext(methodName, methodName, method.getReturnType(), true));
      }
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addExposedPackage - Add all exposed methods for exposed classes under one package.
   *
   * @param  packageName  String
   */
  public static synchronized void addExposedPackage(String packageName) {
    Reflections        reflections = new Reflections(packageName);
    Set<Class<?>>      classes     = reflections.getTypesAnnotatedWith(OzExposedClass.class);
    Iterator<Class<?>> it          = classes.iterator();

    while (it.hasNext()) {
      Class<?> clazz = it.next();
      addExposedClass(clazz, true);
    } // end while
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * initWithCalculateUtil - init exposed methods with calculate utils.
   */
  public static synchronized void initWithCalculateUtil() {
    addExposedClass(CalculateUtil.class, false);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addDefinition - Add an variable definition.
   *
   * @param  name        String
   * @param  expression  String
   * @param  resultType  Class
   */
  public void addDefinition(String name, String expression, Class resultType) {
    context.put(name, new OzElContext(name, expression, resultType, false));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addFunction - Add an function definition.
   *
   * @param  name        String
   * @param  expression  String
   * @param  resultType  Class
   */
  public void addFunction(String name, String expression, Class resultType) {
    addFunction(name, expression, resultType, true);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addFunction - Add an function definition.
   *
   * @param  name        String
   * @param  expression  String
   * @param  resultType  Class
   * @param  cacheable   boolean
   */
  public void addFunction(String name, String expression, Class resultType, boolean cacheable) {
    String[] buf = expression.split("\\.");

    ExposedMethod exposedMethod = null;
    boolean       matched       = false;

    if (buf.length == 1) {
      // just using defined method name
      exposedMethod = exposedMethods.get(buf[0].split("\\(")[0]);

      if (exposedMethod != null) {
        overrideMethods.put(name, exposedMethod);

        matched = true;
      }
    } else if (buf.length == 2) {
      // using complete method with instance name
      OzElContext elContext  = getContext(buf[0]);
      String      methodName = buf[1].split("\\(")[0];

      if (elContext != null) {
        // the context is defined
        // then check the method
        exposedMethod = exposedMethods.get(methodName);

        if (exposedMethod != null) {
          // found a method with the different name
          if (name.equals(methodName)) {
            matched = true;
          }
          // then check the context is the same class?
          else if (exposedMethod.getMethodClass().equals(elContext.getOzElType().getClazz())) {
            // there are same class, so alias will be created
            overrideMethods.put(name, exposedMethod);
            matched = true;
          } else {
            // no match found, ignore this method
            exposedMethod = null;
          }
        }

        if (exposedMethod == null) {
          // can not found matched method,
          // create a runtime alias method
          // add to runtime map
          RuntimeMethod runtimeMethod = new RuntimeMethod(buf[0], methodName, elContext.getOzElType().getClazz());
          overrideMethods.put(name, runtimeMethod);
          matched = true;
        }
      } // end if
    }   // end if-else

    if (matched) {
      context.put(name, new OzElContext(name, expression, resultType, true));
    }
  } // end method addFunction

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addValue - Add a variable with value.
   *
   * @param  name   String
   * @param  value  Object
   */
  public void addValue(String name, Object value) {
    if (value == null) {
      context.put(name, new OzElContext(name, new OzElValue()));
    } else if (value instanceof OzElValue) {
      context.put(name, new OzElContext(name, (OzElValue) value));
    } else {
      context.put(name, new OzElContext(name, new OzElValue(name, value), value.getClass()));
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addValues - Load variables from map.
   *
   * @param  values  Map
   */
  public void addValues(Map<String, Object> values) {
    if (values != null) {
      for (Map.Entry<String, Object> entry : values.entrySet()) {
        addValue(entry.getKey(), entry.getValue());
      }
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * callFunction - Call a function with params.
   *
   * @param   functionName  String
   * @param   params        String
   *
   * @return  OzElValue
   */
  public OzElValue callFunction(String functionName, String params) {
    OzElValue ozElValue = null;

    OzElContext ozElContext = sharedContext.get(functionName);

    if (ozElContext == null) {
      ozElContext = getContext(functionName);
    }

    if (ozElContext != null) {
      ozElValue = ozElContext.getOzElValue();

      if ((ozElValue != null) && (ozElValue.isFunctionValue() || ozElValue.isExpressionValue())) {
        OzElValue value = null;

        if (params == null) {
          value = evalExpr(ozElValue.getStrValue());
        } else {
          // remove "("
          String[] buf = ozElValue.getStrValue().split("\\(");
          value = evalExpr(buf[0] + "(" + params + ")");
        }

        return value;
      }
    }

    return ozElValue;
  } // end method callFunction

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * clearDefinition - Clear all definition.
   */
  public void clearDefinition() {
    context.clear();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * clearVariableCache - Clear Cache for Variable.
   *
   * @param  variableName  DOCUMENT ME!
   */
  public void clearVariableCache(String variableName) {
    if (cacheMode) {
      exprCache.remove(variableName);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * contains - Check there is a variable in the context.
   *
   * @param   name  String
   *
   * @return  boolean
   */
  public boolean contains(String name) {
    return context.containsKey(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * detectReturnType - Detect the return type for the expression.
   *
   * @param   expression  String
   *
   * @return  Class
   */
  public Class detectReturnType(String expression) {
    if (logger.isDebugEnabled()) {
      logger.debug("Detect Return for Expression:" + expression);
    }

    CharStream        input  = new ANTLRInputStream(expression);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);

    lexer.removeErrorListeners();
    lexer.addErrorListener(new OzVerboseListener());
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    VerifyVisitor visitor = new VerifyVisitor();

    // Loading Context
    visitor.setResolvers(this);

    try {
      ParseTree tree     = parser.entry(); // parse
      OzElValue retValue = visitor.visit(tree);
      Class     retClass = retValue.getClazz();

      if (logger.isDebugEnabled()) {
        logger.debug("Return Class:" + retClass);
      }

      return retClass;
    } catch (RuntimeException e) {
      if (logger.isDebugEnabled()) {
        logger.debug(e.toString());
      }
    } catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug(e.toString());
      }
    } // end try-catch

    return null;
  } // end method detectReturnType

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * disableCache - Disable internal cache.
   */
  public void disableCache() {
    if (cacheMode) {
      if (exprCache != null) {
        exprCache.close();
      }

      cacheMode = false;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * enableCache - Enable the internal cache.
   *
   * @param  ttl  int - cache life in seconds
   */
  public void enableCache(final int ttl) {
    if (!cacheMode) {
      cacheMode = true;
    } else {
      if (exprCache != null) {
        exprCache.close();
      }
    }

    exprCache = UserManagedCacheBuilder.newUserManagedCacheBuilder(String.class, OzElValue.class).withResourcePools(
        ResourcePoolsBuilder.heap(1000)).withExpiry(new Expiry<String, OzElValue>() {
          @Override public Duration getExpiryForCreation(String key, OzElValue value) {
            return Duration.of(ttl, TimeUnit.SECONDS);
          }

          @Override public Duration getExpiryForAccess(String key, ValueSupplier<? extends OzElValue> value) {
            return null;
          }

          @Override public Duration getExpiryForUpdate(String key, ValueSupplier<? extends OzElValue> oldValue,
            OzElValue newValue) {
            return null;
          }
        }).build(false);

    exprCache.init();
  } // end method enableCache

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * endBatch - Batch mode ended.
   */
  public void endBatch() {
    batchMode = false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * eval - Eval the expression.
   *
   * @param   expression  String
   *
   * @return  Object
   */
  public Object eval(String expression) {
    if (!batchMode) {
      updateExposedValues();
    }

    OzElValue value = evalExpr(expression);

    return (value != null) ? value.getValue() : null;
  }

  /**
   * Mock eval expression. When this method is called, its silent when reach exception.
   *
   * @param   expression  String
   *
   * @return  Object
   */
  public Object mockEval(String expression) {
    if (!batchMode) {
      updateExposedValues();
    }

    OzElValue value = evalExpr(expression, true);

    return (value != null) ? value.getValue() : null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * eval - Eval the expressions with the same context.
   *
   * @param   expressions  String[]
   *
   * @return  List
   */
  public List<Object> eval(String[] expressions) {
    if (!batchMode) {
      updateExposedValues();
    }

    List<Object> results = null;

    if (expressions == null) {
      return results;
    } else if (expressions.length == 1) {
      results = new ArrayList<Object>();

      OzElValue value = evalExpr(expressions[0]);

      results.add((value != null) ? value.getValue() : null);

      return results;
    } else {
      StringBuilder sb = new StringBuilder();
      sb.append("{");

      boolean first = true;

      for (String expression : expressions) {
        if (!first) {
          sb.append(",");
        } else {
          first = false;
        }

        sb.append("{").append(expression).append("}");
      }

      sb.append("}");

      OzElValue value = evalExpr(sb.toString());

      if (value.getValue() != null) {
        results = new ArrayList<Object>();
        results.addAll((List) value.getValue());
      }

      return results;
    } // end if-else
  }   // end method eval

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * eval - Evaluate the expression with the designed type.
   *
   * @param   <T>         Class
   * @param   expression  String
   * @param   toType      Class
   *
   * @return  T
   */
  public <T> T eval(String expression, Class<T> toType) {
    Object value = eval(expression);

    if (value != null) {
      if (toType.equals(value.getClass())) {
        return (T) value;
      } else {
        if (toType.equals(BigDecimal.class)) {
          return (T) (new BigDecimal(value.toString()));
        } else if (toType.equals(Long.class) || toType.equals(long.class)) {
          return (T) (Long.valueOf(value.toString()));
        } else if (toType.equals(Integer.class) || toType.equals(int.class)) {
          return (T) (Integer.valueOf(value.toString()));
        } else if (toType.equals(String.class)) {
          return (T) (value.toString());
        }
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getExposedMethod - Lookup in the exposed Method.
   *
   * @param   methodName  String
   *
   * @return  ExposedMethod
   */
  public ExposedMethod getExposedMethod(String methodName) {
    return exposedMethods.get(methodName);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getNames - Get all variable names in the context.
   *
   * @return  Set
   */
  public Set<String> getNames() {
    return context.keySet();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   expression  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Set<String> getNormalizedVariables(String expression) {
    if (logger.isDebugEnabled()) {
      logger.debug("Normalized Expression : '" + expression + "' ......");
    }

    CharStream        input  = new ANTLRInputStream(expression);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);

    lexer.removeErrorListeners();
    lexer.addErrorListener(new OzVerboseListener());
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    VerifyVisitor visitor = new VerifyVisitor();
    visitor.setResolvers(this);

    ParseTree tree = parser.entry(); // parse
    visitor.visit(tree);

    Set<String> usedVariables = new HashSet<String>();
    usedVariables.addAll(visitor.getUsedVariables());
    usedVariables.addAll(visitor.getNormalizedCascadedVariables());
    usedVariables.addAll(visitor.getUsedFunctions());
    usedVariables.addAll(visitor.getNormalizedCascadedFunctions());

    return usedVariables;
  } // end method getNormalizedVariables

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getObjectValue - Get the value for the named variable.
   *
   * @param   name  String
   *
   * @return  Object
   */
  public Object getObjectValue(String name) {
    OzElValue ozElValue = getValue(name);

    if (ozElValue != null) {
      return ozElValue.getValue();
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getRuntimeMethod - Lookup overwritten runtime Method.
   *
   * @param   methodName  String
   *
   * @return  ExposedMethod
   */
  public ExposedMethod getRuntimeMethod(String methodName) {
    return overrideMethods.get(methodName);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getUsedVariables - List all used variables in the expression.
   *
   * @param   expression  String
   *
   * @return  Set
   */
  public Set<String> getUsedVariables(String expression) {
    if (logger.isDebugEnabled()) {
      logger.debug("Verify Expression : '" + expression + "' ......");
    }

    CharStream        input  = new ANTLRInputStream(expression);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);

    lexer.removeErrorListeners();
    lexer.addErrorListener(new OzVerboseListener());
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    VerifyVisitor visitor = new VerifyVisitor();
    visitor.setResolvers(this);

    ParseTree tree = parser.entry(); // parse
    visitor.visit(tree);

    Set<String> usedVariables = new LinkedHashSet<>();
    usedVariables.addAll(visitor.getUsedVariables());
    usedVariables.addAll(visitor.getUsedFunctions());
    usedVariables.addAll(visitor.getOriginalCascadedVariables());
    usedVariables.addAll(visitor.getOriginalCascadedFunctions());
    usedVariables.addAll(visitor.getDefinedVariables());

    return usedVariables;
  } // end method getUsedVariables

  /**
   * getVariablePaths - List all variable Paths in the expression.
   *
   * @param   expression  String
   *
   * @return  Set
   */
  public Set<String> getVariablePaths(String expression) {
    if (logger.isDebugEnabled()) {
      logger.debug("Verify Expression : '" + expression + "' ......");
    }

    CharStream        input  = new ANTLRInputStream(expression);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);

    lexer.removeErrorListeners();
    lexer.addErrorListener(new OzVerboseListener());
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    VerifyVisitor visitor = new VerifyVisitor();
    visitor.setResolvers(this);

    ParseTree tree = parser.entry(); // parse
    visitor.visit(tree);

    Set<String> variablePaths = new LinkedHashSet<>();
    variablePaths.addAll(visitor.getCallPaths());

    return variablePaths;
  } // end method getUsedVariables

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getValue - Get OzElValue for variable.
   *
   * @param   name  String
   *
   * @return  OzElValue
   */
  public OzElValue getValue(String name) {
    OzElValue ozElValue = null;

    if (cacheMode && exprCache.containsKey(name)) {
      ozElValue = exprCache.get(name);

      if (logger.isDebugEnabled()) {
        logger.debug("Load value from cache for name : '" + name + "' --> "
          + ((ozElValue == null) ? null : ozElValue.getValue()));
      }

      return ozElValue;
    } else {
      OzElContext ozElContext = sharedContext.get(name);

      if (ozElContext == null) {
        ozElContext = getContext(name);
      }

      if (ozElContext != null) {
        ozElValue = ozElContext.getOzElValue();

        if (ozElValue != null) {
          if (ozElValue.isExpressionValue()) {
            OzElValue value = evalExpr(ozElValue.getStrValue());

            if (cacheMode && value.isCacheable()) {
              exprCache.put(name, value);
            }

            return value;
          } else if (ozElValue.isFunctionValue()) {
            String expr = ozElValue.getStrValue();

            if (!expr.endsWith(")")) {
              expr += "()";
            }

            OzElValue value = evalExpr(expr);

            return value;
          }
        }
      } // end if

      return ozElValue;
    } // end if-else
  }   // end method getValue

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getValueType - Get the OzElValue Type for the variable.
   *
   * @param   name  String
   *
   * @return  OzElValue
   */
  public OzElValue getValueType(String name) {
    OzElContext ozElContext = getContext(name);

    if (ozElContext != null) {
      OzElValue ozElValue = ozElContext.getOzElType();
      if(ozElValue.isMapValue()){
        ozElValue = ozElContext.getOzElValue();
      }
      return ozElValue;
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * startBatch - Starting the batch mode.
   */
  public void startBatch() {
    batchMode = true;

    updateExposedValues();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * updateExposedValues - Refresh the exposed values.
   */
  public void updateExposedValues() {
    // clear all expression cache
    if (cacheMode) {
      exprCache.close();
      exprCache.init();
    }

    String[] names = getExposedValueNames();

    if ((names != null) && (names.length > 0)) {
      for (String name : names) {
        try {
          Method method = FunctionUtil.getGetterMethod(getClass(), name);

          Object value = null;
          value = method.invoke(this, new Object[] {});

          if (logger.isDebugEnabled()) {
            logger.debug("Found '" + name + "', updated....");
          }

          context.put(name, new OzElContext(name, new OzElValue(name, value), method.getReturnType()));
        } catch (IllegalAccessException e) {
          if (logger.isDebugEnabled()) {
            logger.debug("'" + name + "' not found...Ignore...");
          }
        } catch (InvocationTargetException e) {
          if (logger.isDebugEnabled()) {
            logger.debug("'" + name + "' not found...Ignore...");
          }
        } catch (NullPointerException e) {
          if (logger.isDebugEnabled()) {
            logger.debug("'" + name + "' not found...Ignore...");
          }
        } catch (NoSuchFieldException e) {
          if (logger.isDebugEnabled()) {
            logger.debug("'" + name + "' not found...Ignore...");
          }
        } // end try-catch
      }   // end for
    }     // end if
  }       // end method updateExposedValues

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * verify - Verify the expression.
   *
   * @param   expression  String
   *
   * @return  boolean
   *
   * @throws  UnknownVariableException  exception
   */
  public boolean verify(String expression) {
    if (logger.isDebugEnabled()) {
      logger.debug("Verify Expression : '" + expression + "' ......");
    }

    CharStream        input  = new ANTLRInputStream(expression);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);

    lexer.removeErrorListeners();
    lexer.addErrorListener(new OzVerboseListener());
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    VerifyVisitor visitor = new VerifyVisitor();
    visitor.setResolvers(this);

    if (logger.isDebugEnabled()) {
      logger.debug("Parsing Expression...");
    }

    try {
      ParseTree tree = parser.entry(); // parse
      visitor.visit(tree);

      if (logger.isDebugEnabled()) {
        logger.debug("Verify Completed...");
      }
    } catch (NullPointerException e) {
      if (logger.isDebugEnabled()) {
        logger.debug(e.getMessage(), e);
      }

      if (visitor.hasUnknownVariables()) {
        throw new UnknownVariableException(visitor.getUnknownVariableNames());
      }

      return false;
    }

    if (visitor.hasUnknownVariables()) {
      throw new UnknownVariableException(visitor.getUnknownVariableNames());
    }

    return true;
  } // end method verify

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * evalExpr - Eval the expression.
   *
   * @param   expression  String
   *
   * @return  OzElValue
   */
  protected OzElValue evalExpr(String expression) {
    return evalExpr(expression, false);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * evalExpr - Eval the expression.
   *
   * @param   expression  String
   * @param   silent      boolean
   *
   * @return  OzElValue
   */
  protected OzElValue evalExpr(String expression, boolean silent) {
    if (logger.isDebugEnabled()) {
      logger.debug("Eval Expression : '" + expression + "' ......");
    }

    OzElValue retValue = null;

    if (expression == null) {
      return null;
    } else if (expression.trim().length() == 0) {
      return new OzElValue();
    }

    if (cacheMode && exprCache.containsKey(expression)) {
      retValue = exprCache.get(expression);

      if (logger.isDebugEnabled()) {
        logger.debug("Load value from cache for Expression : '" + expression + "' --> "
          + ((retValue == null) ? null : retValue.getValue()));
      }

      return retValue;
    } else {
      try {
        CharStream        input  = new ANTLRInputStream(expression);
        OzElLexer         lexer  = new OzElLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OzElParser        parser = new OzElParser(tokens);

        lexer.removeErrorListeners();
        lexer.addErrorListener(new OzVerboseListener());
        parser.removeErrorListeners();
        parser.addErrorListener(new OzVerboseListener());

        EvalVisitor visitor = new EvalVisitor(silent);

        visitor.setResolvers(this);

        ParseTree tree = parser.entry(); // parse
        retValue = visitor.visit(tree);

        if (cacheMode && retValue.isCacheable()) {
          exprCache.put(expression, retValue);

          if (logger.isDebugEnabled()) {
            logger.debug("Store value to cache for  Expression : '" + expression + "'");
          }
        }
      } catch (Exception e) {
        logger.error("Return null due to " + e.getMessage());

        if (logger.isDebugEnabled()) {
          logger.debug(e.getMessage(), e);
        }
      } // end try-catch

      if (logger.isDebugEnabled()) {
        logger.debug("Eval '" + expression + "' --> " + ((retValue == null) ? null : retValue.getValue()));
      }

      return retValue;
    } // end if-else
  }   // end method evalExpr

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   name  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected OzElContext getContext(String name) {
    return context.get(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getExposedValueNames - Get all exposed value names\.
   *
   * @return  String[]
   */
  protected String[] getExposedValueNames() {
    return new String[] {};
  }
} // end class ResolverContext
