package com.ozstrategy.el.impl;

/**
 * Created with IntelliJ IDEA.
 * User: rojer
 * Date: 5/24/13
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozstrategy.el.annotation.OzCacheable;
import com.ozstrategy.el.annotation.OzContextParam;
import com.ozstrategy.el.antlr.OzElBaseVisitor;
import com.ozstrategy.el.antlr.OzElParser;
import com.ozstrategy.el.exception.NotSupportException;
import com.ozstrategy.el.exception.ReturnException;
import com.ozstrategy.el.util.CalculateUtil;
import com.ozstrategy.el.util.CompareUtil;
import com.ozstrategy.el.util.FunctionUtil;
import com.ozstrategy.el.util.NoCaseHashSet;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class EvalVisitor extends OzElBaseVisitor<OzElValue> {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** Resolver Context. */
  protected ResolverContext resolvers = null;

  /** Variables that supported by context. */
  protected NoCaseHashSet supportedVariables = new NoCaseHashSet();

  /** Unknown Variables in the context. */
  protected NoCaseHashSet unknownVariables = new NoCaseHashSet();

  /** Variables used in the expression. */
  protected NoCaseHashSet usedVariables = new NoCaseHashSet();

  /** Cache for functionCall parent context OzElValue. */
  private ConcurrentHashMap<OzElParser.ExpressionContext, OzElValue> contextValues = new ConcurrentHashMap();

  private Logger          logger        = LoggerFactory.getLogger(this.getClass());

  /** silent mode: its silent when reach exception, no exception will be thrown.*/
  private boolean         silent      = false;
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new EvalVisitor object.
   */
  public EvalVisitor() { }

  /**
   * Creates a new EvalVisitor object.
   *
   * @param  silent  boolean
   */
  public EvalVisitor(boolean silent) {
    this.silent = silent;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Get all unknown variables.
   *
   * @return  unknown variables
   */
  public NoCaseHashSet getUnknownVariables() {
    return unknownVariables;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get all used variables.
   *
   * @return  used variables
   */
  public NoCaseHashSet getUsedVariables() {
    return usedVariables;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Has unknown variables.
   *
   * @return  DOCUMENT ME!
   */
  public boolean hasUnknownVariables() {
    return (unknownVariables.size() > 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Inject resolvers.
   *
   * @param  resolvers  resolvers
   */
  public void setResolvers(ResolverContext resolvers) {
    this.resolvers = resolvers;

    for (String name : resolvers.getNames()) {
      supportedVariables.add(name);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * * | expression LBRACK expression RBRACK #AccessElement.
   *
   * @param   ctx  $param.type$
   *
   * @return  | expression LBRACK expression RBRACK #AccessElement.
   */
  @Override public OzElValue visitAccessElement(OzElParser.AccessElementContext ctx) {
    OzElValue arrayElValue = visit(ctx.expression(0));

    OzElValue indexElValue = visit(ctx.expression(1));

    String elementName = ctx.expression(0).getText();

    if (!indexElValue.isIntegerValue()) {
      logger.error("'" + elementName + "' is not a Integer value.");
    } else {
      Integer index = indexElValue.getIntegerValue();

      Object[] arrayValue = null;

      if (arrayElValue.isListValue()) {
        arrayValue = arrayElValue.getListValueInArray();
      } else if (arrayElValue.isObjectValue()) {
        Class  clazz    = arrayElValue.getClazz();
        Object objValue = arrayElValue.getValue();

        if (Collection.class.isAssignableFrom(clazz)) {
          arrayValue = ((Collection) objValue).toArray();
        } else if (clazz.isArray()) {
          arrayValue = ((Object[]) objValue);
        }
      }

      if ((arrayValue != null) && (arrayValue.length > index)) {
        return OzElValue.createOzElValue(arrayValue[index]).setResult(true);
      }
    }

    return new OzElValue();
  } // end method visitAccessElement

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression DOT ID #AccessProperty.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */

  @Override public OzElValue visitAccessProperty(OzElParser.AccessPropertyContext ctx) {
    OzElParser.ExpressionContext instanceContext = ctx.expression(0);
    OzElValue instanceElValue = visit(instanceContext);

    OzElParser.ExpressionContext propertyContext = ctx.expression(1);

    if (propertyContext instanceof OzElParser.CallFunctionContext) {
      contextValues.put(instanceContext, instanceElValue);
      return visit(ctx.expression(1));
    } else {
      String propertyName  = ctx.expression(1).getText();
      Object instanceValue = null;
      String instanceName  = null;

      if (instanceElValue.isMapValue()) {
        Map<String, Object> mapValue = instanceElValue.getMapValue();
        Object propertyValue = mapValue.get(propertyName);
        return OzElValue.createOzElValue(propertyValue).setResult(true);
      } else if (instanceElValue.isObjectValue()) {
        instanceValue = instanceElValue.getValue();
      } else {
        // It is a variable name
        instanceName  = instanceElValue.hasName() ? instanceElValue.getName() : instanceElValue.getStringValue();
        instanceValue = resolvers.getObjectValue(instanceName);
      }

      if (instanceValue != null) {
        Class[]  argTypes = {};
        Object[] args     = {};

        Method method     = null;
        String methodName = propertyName;

        try {
          if (argTypes.length == 0) {
            methodName = "get" + propertyName;
            method     = FunctionUtil.findMethod(instanceValue.getClass(), methodName, argTypes);

            if (method == null) {
              methodName = "is" + propertyName;
              method     = FunctionUtil.findMethod(instanceValue.getClass(), methodName, argTypes);
            }
          }

          if (method == null) {
            method = FunctionUtil.findMethod(instanceValue.getClass(), propertyName, argTypes);
          }

          if (method != null) {
            ArrayList<Object> callArgs = new ArrayList<Object>();

            if ((args != null) && (args.length > 0)) {
              for (Object arg : args) {
                callArgs.add(arg);
              }
            }

            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            int i = 0;

            for (Annotation[] annotations : parameterAnnotations) {
              for (Annotation annotation : annotations) {
                if (annotation instanceof OzContextParam) {
                  OzContextParam ozParam = (OzContextParam) annotation;

                  if (i <= callArgs.size()) {
                    callArgs.add(i, resolvers.getObjectValue(ozParam.value()));
                  }
                }

                i++;
              }
            }

            Object retValue = FunctionUtil.invokeMethod(instanceValue, method,
                    (callArgs.size() > 0) ? callArgs.toArray() : new Object[] {});

            return OzElValue.createOzElValue(retValue).setResult(true);
          }
        } catch (NoSuchMethodException e) {
          if (logger.isDebugEnabled()) {
            logger.debug(e.getMessage(), e);
          }
        } catch (IllegalAccessException e) {
          if (logger.isDebugEnabled()) {
            logger.debug(e.getMessage(), e);
          }
        } catch (InvocationTargetException e) {
          if (logger.isDebugEnabled()) {
            logger.debug(e.getMessage(), e);
          }
        } catch (NullPointerException e) {
          if (logger.isDebugEnabled()) {
            logger.debug(e.getMessage(), e);
          }
        } // end try-catch
      } else {
        if (!silent) {
          logger.error("'" + instanceName + "' is null.");
        }
      }   // end if-else
    }     // end if-else

    return new OzElValue();
  } // end method visitAccessProperty

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression op=(PLUS | MINUS) expression #AddSub.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitAddSub(OzElParser.AddSubContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));

    if ((leftSide != null) && leftSide.isBooleanValue()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Boolean value '" + leftSide.strValue + "'can not do math calculation.");
      }

      return new OzElValue();
    }

    if ((rightSide != null) && rightSide.isBooleanValue()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Boolean value '" + rightSide.strValue + "'can not be math calculation.");
      }

      return new OzElValue();
    }

    if (((leftSide == null) || leftSide.isNullValue()) || ((rightSide == null) || (rightSide.isNullValue()))) {
      return new OzElValue();
    }

    if (leftSide.isDecimalCompatibleValue()) {
      // only allow decimal expression
      if (rightSide.isDecimalCompatibleValue()) {
        try {
          if (ctx.op.getType() == OzElParser.PLUS) {
            if (leftSide.isDecimalValue() || rightSide.isDecimalValue()) {
              return new OzElValue(leftSide.getDecimalValue().add(rightSide.getDecimalValue()));
            } else if (leftSide.isLongValue() || rightSide.isLongValue()) {
              return new OzElValue(leftSide.getLongValue() + rightSide.getLongValue());
            } else if (leftSide.isIntegerValue() || rightSide.isIntegerValue()) {
              return new OzElValue(leftSide.getIntegerValue() + rightSide.getIntegerValue());
            }
          } else if (ctx.op.getType() == OzElParser.MINUS) {
            if (leftSide.isDecimalValue() || rightSide.isDecimalValue()) {
              return new OzElValue(leftSide.getDecimalValue().subtract(rightSide.getDecimalValue()));

            } else if (leftSide.isLongValue() || rightSide.isLongValue()) {
              return new OzElValue(leftSide.getLongValue() - rightSide.getLongValue());

            } else if (leftSide.isIntegerValue() || rightSide.isIntegerValue()) {
              return new OzElValue(leftSide.getIntegerValue() - rightSide.getIntegerValue());
            }

            if (logger.isDebugEnabled()) {
              logger.debug("'" + rightSide.getStrValue() + "' - Decimal value needed.");
            }
          } // end if-else
        } catch (Exception e) {
          if (logger.isDebugEnabled()) {
            logger.debug(e.getLocalizedMessage());
          }
        }   // end try-catch
      }     // end if
      else if ((ctx.op.getType() == OzElParser.PLUS)) {
        return new OzElValue("" + leftSide.getDecimalValue() + rightSide.getStrValue());
      }     // end if-else

      if (logger.isDebugEnabled()) {
        logger.debug("'" + rightSide.getStrValue() + "' - Decimal value needed.");
      }
    } // end for
    else if (leftSide.isDateValue()) {
      // only allow Date +/- num & Date - Date
      if (rightSide.isIntegerValue()) {
        try {
          if (ctx.op.getType() == OzElParser.PLUS) {
            return new OzElValue(CalculateUtil.addDays(leftSide.getDateValue(), rightSide.getIntegerValue()));

          } else if (ctx.op.getType() == OzElParser.MINUS) {
            return new OzElValue(CalculateUtil.minusDays(leftSide.getDateValue(), rightSide.getIntegerValue()));

          } // end if-else
        } catch (Exception e) {
          if (logger.isDebugEnabled()) {
            logger.debug(e.getLocalizedMessage());
          }
        }   // end try-catch
      }     // end if
      else if (rightSide.isDateValue()) {
        if (ctx.op.getType() == OzElParser.MINUS) {
          return new OzElValue(CalculateUtil.getDayDifference(leftSide.getDateValue(),
                rightSide.getDateValue()));
        }   // en
      }     // end if-else

      if (logger.isDebugEnabled()) {
        logger.debug("'" + rightSide.getStrValue() + "' - Integer Value or Date Value needed.");
      }
    } else if ((ctx.op.getType() == OzElParser.PLUS)) {
      return new OzElValue(leftSide.getStrValue() + rightSide.getValue().toString());

      // end for
    } // end if-else

    if (logger.isDebugEnabled()) {
      logger.debug("'" + leftSide.getStrValue() + "' - Decimal or Date value needed.");
    }

    return new OzElValue();
  } // end method visitAddSub

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression AND expression #And.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitAnd(OzElParser.AndContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));

    if (leftSide.isBooleanValue()) {
      boolean bLeft = leftSide.getBooleanValue();

      if ((rightSide == null) || rightSide.isNullValue() || rightSide.isBooleanValue()) {
        if (!bLeft) {
          return new OzElValue(Boolean.FALSE);
        } else {
          if ((rightSide == null) || rightSide.isNullValue()) {
            return new OzElValue();
          } else {
            return new OzElValue(rightSide.getBooleanValue());
          }
        }
      } else if (rightSide.isStringValue()) {
        if (rightSide.getStrValue().equalsIgnoreCase("true") || rightSide.getStrValue().equalsIgnoreCase("'true'")) {
          return leftSide;
        } else if (rightSide.getStrValue().equalsIgnoreCase("false")
              || rightSide.getStrValue().equalsIgnoreCase("'false'")) {
          return new OzElValue(Boolean.FALSE);
        }
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("'" + rightSide.strValue + "' - Needed to be Boolean value.");
        }
      }
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("'" + leftSide.strValue + "' - Needed to be Boolean value.");
      }
    } // end if-else

    return new OzElValue();
  } // end method visitAnd

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | LBRACK expressionList? RBRACK #ArrayPrimary.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  | LBRACK expression RBRACK #ArrayPrimary.
   *
   * @see     com.ozstrategy.el.antlr.OzElBaseVisitor#visitArrayPrimary(com.ozstrategy.el.antlr.OzElParser.ArrayPrimaryContext)
   */
  @Override public OzElValue visitArrayPrimary(OzElParser.ArrayPrimaryContext ctx) {
    if (ctx.expressionList() != null) {
      return visit(ctx.expressionList());
    }

    return new OzElValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitAssignmentExpr(com.ozstrategy.el.antlr.OzElParser.AssignmentExprContext)
   */
  @Override public OzElValue visitAssignmentExpr(OzElParser.AssignmentExprContext ctx) {
    String    variableName = ctx.variable().getText();
    OzElValue exprValue    = visit(ctx.expression());

    switch (ctx.op.getType()) {
      case OzElParser.ASSIGN: {
        resolvers.addValue(variableName, exprValue);

        if (logger.isDebugEnabled()) {
          logger.debug("Assignment: " + variableName + "=" + exprValue);
        }

        return exprValue;
      }

      case OzElParser.PLUSEQUAL: {
        OzElValue varValue = resolvers.getValue(variableName);

        if (varValue.isDecimalValue() || exprValue.isDecimalValue()) {
          varValue = new OzElValue(varValue.getDecimalValue().add(exprValue.getDecimalValue()));
        } else if (varValue.isLongValue() || exprValue.isLongValue()) {
          varValue = new OzElValue(varValue.getLongValue() + exprValue.getLongValue());
        } else if (varValue.isIntegerValue() || exprValue.isIntegerValue()) {
          varValue = new OzElValue(varValue.getIntegerValue() + exprValue.getIntegerValue());
        } else {
          return null;
        }

        resolvers.addValue(variableName, varValue);

        if (logger.isDebugEnabled()) {
          logger.debug("Assignment: " + variableName + "=" + varValue);
        }

        return varValue;
      }

      case OzElParser.MINUSEQUAL: {
        OzElValue varValue = resolvers.getValue(variableName);

        if (varValue.isDecimalValue() || exprValue.isDecimalValue()) {
          varValue = new OzElValue(varValue.getDecimalValue().subtract(exprValue.getDecimalValue()));
        } else if (varValue.isLongValue() || exprValue.isLongValue()) {
          varValue = new OzElValue(varValue.getLongValue() - exprValue.getLongValue());
        } else if (varValue.isIntegerValue() || exprValue.isIntegerValue()) {
          varValue = new OzElValue(varValue.getIntegerValue() - exprValue.getIntegerValue());
        } else {
          return null;
        }

        resolvers.addValue(variableName, varValue);

        if (logger.isDebugEnabled()) {
          logger.debug("Assignment: " + variableName + "=" + varValue);
        }

        return varValue;
      }

      case OzElParser.STAREQUAL: {
        OzElValue varValue = resolvers.getValue(variableName);

        if (varValue.isDecimalValue() || exprValue.isDecimalValue()) {
          varValue = new OzElValue(varValue.getDecimalValue().multiply(exprValue.getDecimalValue()));
        } else if (varValue.isLongValue() || exprValue.isLongValue()) {
          varValue = new OzElValue(varValue.getLongValue() * exprValue.getLongValue());
        } else if (varValue.isIntegerValue() || exprValue.isIntegerValue()) {
          varValue = new OzElValue(varValue.getIntegerValue() * exprValue.getIntegerValue());
        } else {
          return null;
        }

        resolvers.addValue(variableName, varValue);

        if (logger.isDebugEnabled()) {
          logger.debug("Assignment: " + variableName + "=" + varValue);
        }

        return varValue;
      }

      case OzElParser.SLASHEQUAL: {
        OzElValue varValue = resolvers.getValue(variableName);

        if (varValue.isDecimalValue() || exprValue.isDecimalValue()) {
          varValue = new OzElValue(varValue.getDecimalValue().divide(exprValue.getDecimalValue(), 8,
                BigDecimal.ROUND_HALF_EVEN));
        } else if (varValue.isLongValue() || exprValue.isLongValue()) {
          varValue = new OzElValue(varValue.getLongValue() / exprValue.getLongValue());
        } else if (varValue.isIntegerValue() || exprValue.isIntegerValue()) {
          varValue = new OzElValue(varValue.getIntegerValue() / exprValue.getIntegerValue());
        } else {
          return null;
        }

        resolvers.addValue(variableName, varValue);

        if (logger.isDebugEnabled()) {
          logger.debug("Assignment: " + variableName + "=" + varValue);
        }

        return varValue;
      }

      case OzElParser.MODEQUAL: {
        OzElValue varValue = resolvers.getValue(variableName);

        if (varValue.isLongValue() || exprValue.isLongValue()) {
          varValue = new OzElValue(varValue.getLongValue() % exprValue.getLongValue());
        } else if (varValue.isIntegerValue() || exprValue.isIntegerValue()) {
          varValue = new OzElValue(varValue.getIntegerValue() % exprValue.getIntegerValue());
        } else {
          return null;
        }

        resolvers.addValue(variableName, varValue);

        if (logger.isDebugEnabled()) {
          logger.debug("Assignment: " + variableName + "=" + varValue);
        }

        return varValue;
      }

      default:
    } // end switch

    return null;
  } // end method visitAssignmentExpr

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : statement (SEMI statement)*
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitBlock(OzElParser.BlockContext ctx) {
    OzElValue retValue = null;

    for (int i = 0; i < ctx.statement().size(); i++) {
      OzElValue value = visit(ctx.statement(i));

      if (value.isSkipValue()) {
        // skip the empty statement
        continue;
      }

      retValue = value;
    }

    return retValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : LCURLY block RCURLY
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitBlockStatement(OzElParser.BlockStatementContext ctx) {
    return visit(ctx.block());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitBoolValue(com.ozstrategy.el.antlr.OzElParser.BoolValueContext)
   */
  @Override public OzElValue visitBoolValue(OzElParser.BoolValueContext ctx) {
    return super.visitBoolValue(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression LPAREN expressionList? RPAREN #CallFunction
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */

  @Override public OzElValue visitCallFunction(OzElParser.CallFunctionContext ctx) {
    String functionName = ctx.ID().getText();

    Class    methodClass = null;
    String   methodName  = functionName;
    Class[]  argTypes    = {};
    Object[] args        = {};

    Object instanceValue = null;

    OzElValue params = (ctx.expressionList() == null) ? null : visit(ctx.expressionList());

    if ((params != null) && (params.isListValue())) {
      List<OzElValue> list = params.getListValue();
      int             size = list.size();
      argTypes = new Class[size];
      args     = new Object[size];

      for (int i = 0; i < size; i++) {
        OzElValue param = list.get(i);

        if (param != null) {
          argTypes[i] = param.getValueType();
          args[i]     = param.getValue();
        } else {
          argTypes[i] = null;
          args[i]     = null;
        }
      }
    }

    if (ctx.getParent() instanceof OzElParser.AccessPropertyContext) {
      OzElParser.AccessPropertyContext propertyContext = (OzElParser.AccessPropertyContext) ctx.getParent();
      OzElParser.ExpressionContext     expressionBase  = propertyContext.expression(0);

      if (expressionBase != ctx) {
        OzElValue instanceElValue = contextValues.get(expressionBase);

        if(instanceElValue == null) {
          instanceElValue = visit(expressionBase);
        }

        methodClass   = instanceElValue.getClazz();
        instanceValue = instanceElValue.getValue();
      }
    }

    if (methodClass == null) {
      // look for overwrite method first
      ExposedMethod overrideMethod = resolvers.getRuntimeMethod(methodName);

      if (overrideMethod != null) {
        if (overrideMethod instanceof RuntimeMethod) {
          instanceValue = resolvers.getValue(((RuntimeMethod) overrideMethod).getInstanceName()).getValue();
        }

        methodClass = overrideMethod.getMethodClass();
        methodName  = overrideMethod.getMethodName();
      } else {
        // look for the exposed methods
        ExposedMethod exposedMethod = resolvers.getExposedMethod(methodName);

        if (exposedMethod != null) {
          methodClass = exposedMethod.getMethodClass();
          methodName  = exposedMethod.getMethodName();
        }
      }
    } // end if-else

    if ((methodName != null) && (methodClass != null)) {
      Method method = null;

      try {
        method = FunctionUtil.findMethod(methodClass, methodName, argTypes);

        boolean cacheable = method.isAnnotationPresent(OzCacheable.class);

        if (method != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Found Method:" + method.toString());
            logger.debug("Method return:" + method.getReturnType());
          }

          ArrayList<Object> callArgs = new ArrayList<Object>();

          if ((args != null) && (args.length > 0)) {
            for (Object arg : args) {
              callArgs.add(arg);
            }
          }

          Annotation[][] parameterAnnotations = method.getParameterAnnotations();

          int i = 0;

          for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation : annotations) {
              if (annotation instanceof OzContextParam) {
                OzContextParam ozParam = (OzContextParam) annotation;

                if (i <= callArgs.size()) {
                  callArgs.add(i, resolvers.getObjectValue(ozParam.value()));
                }
              }

              i++;
            }
          }

          Object retValue = FunctionUtil.invokeMethod(instanceValue, method,
              (callArgs.size() > 0) ? callArgs.toArray() : new Object[] {});

          return OzElValue.createOzElValue(retValue).allowCache(cacheable);
        } // end if

        throw new NotSupportException("Function '" + methodName + "' is not supported.");
      } catch (NoSuchMethodException e) {
        if (!silent) {
          logger.error(e.getMessage(), e);
        }
      } catch (IllegalAccessException e) {
        if (!silent) {
          logger.error(e.getMessage(), e);
        }
      } catch (InvocationTargetException e) {
        if (e.getTargetException() instanceof NullPointerException) {
          return null;
        }

        if (!silent) {
          logger.error(e.getMessage(), e);
        }
      } catch (NullPointerException e) {
        if (!silent) {
          logger.error(e.getMessage(), e);
        }
      } // end try-catch
    }   // end if

    return new OzElValue();
// if (functionName != null) {
// ExposedMethod exposedMethod = resolvers.getExposedMethod(functionName);
//
// if (exposedMethod != null) {
// OzElValue params = (ctx.expressionList() == null) ? null : visit(ctx.expressionList());
//
// Class[]  argTypes = {};
// Object[] args     = {};
//
// if ((params != null) && (params.isListValue())) {
// List<OzElValue> list = params.getListValue();
// int             size = list.size();
// argTypes = new Class[size];
// args     = new Object[size];
//
// for (int i = 0; i < size; i++) {
// OzElValue param = list.get(i);
//
// argTypes[i] = param.getValueType();
// args[i]     = param.getValue();
// }
// }
//
// Method method = null;
//
// try {
// method = FunctionUtil.findMethod(exposedMethod.getMethodClass(), exposedMethod.getMethodName(), argTypes);
//
// if (method != null) {
// ArrayList<Object> callArgs = new ArrayList<Object>();
//
// if ((args != null) && (args.length > 0)) {
// for (Object arg : args) {
// callArgs.add(arg);
// }
// }
//
// Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//
// int i = 0;
//
// for (Annotation[] annotations : parameterAnnotations) {
// for (Annotation annotation : annotations) {
// if (annotation instanceof OzContextParam) {
// OzContextParam ozParam = (OzContextParam) annotation;
//
// if (i <= callArgs.size()) {
// callArgs.add(i, resolvers.getObjectValue(ozParam.name()));
// }
// }
//
// i++;
// }
// }
//
// Object retValue = FunctionUtil.invokeMethod(exposedMethod.getMethodClass(), method,
// (callArgs.size() > 0) ? callArgs.toArray() : new Object[] {});
//
// return OzElValue.createOzElValue(retValue);
// } // end if
// } catch (NoSuchMethodException e) {
// logger.error(e.getMessage(), e);
// } catch (IllegalAccessException e) {
// logger.error(e.getMessage(), e);
// } catch (InvocationTargetException e) {
// if (e.getTargetException() instanceof NullPointerException) {
// return null;
// }
//
// logger.error(e.getMessage(), e);
// } catch (NullPointerException e) {
// logger.error(e.getMessage(), e);
//
// return null;
// } // end try-catch
// }   // end if
// else {
// // try eval string
// if (resolvers.contains(functionName)) {
// return resolvers.callFunction(functionName,
// (ctx.expressionList() == null) ? null : ctx.expressionList().getText());
// }
// } // end if-else
// }   // end if
//
// OzElValue function = visit(ctx.ID());
//
// return function;
  } // end method visitCallFunction

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression op=(LE | GE | GT | LT | IN) expression #Compare.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitCompare(OzElParser.CompareContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));

    if ((leftSide != null) && leftSide.isBooleanValue()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Boolean value '" + leftSide.strValue + "'can not be compared.");
      }

      return new OzElValue();
    }

    if ((rightSide != null) && rightSide.isBooleanValue()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Boolean value '" + rightSide.strValue + "'can not be compared.");
      }

      return new OzElValue();
    }

    if (((leftSide == null) || leftSide.isNullValue()) || ((rightSide == null) || rightSide.isNullValue())) {
      return new OzElValue(false);
    }

    int type = ctx.op.getType();

    switch (type) {
      case OzElParser.LT: {
        return new OzElValue(CompareUtil.lessThan(leftSide.getValue(), rightSide.getValue()));
      }

      case OzElParser.LE: {
        return new OzElValue(CompareUtil.lessThanOrEqualsTo(leftSide.getValue(), rightSide.getValue()));
      }

      case OzElParser.GT: {
        return new OzElValue(CompareUtil.greaterThan(leftSide.getValue(), rightSide.getValue()));
      }

      case OzElParser.GE: {
        return new OzElValue(CompareUtil.greaterThanOrEqualsTo(leftSide.getValue(), rightSide.getValue()));
      }

      case OzElParser.IN: {
        if (rightSide.isListValue()) {
          List<OzElValue> list  = rightSide.getListValue();
          boolean         found = false;

// StopWatch stopWatch = new StopWatch();
//
// stopWatch.start("Using HashSet.");
//
// Set<Object> objectSet = new HashSet<Object>();
//
// for (OzElValue elValue : list) {
// objectSet.add(elValue.getValue());
// }
//
// found = objectSet.contains(leftSide.getValue());
// stopWatch.stop();
//
// stopWatch.start("Using Arrays.Sort");
//
          int      size  = list.size();
          Object[] array = new Object[size];

          for (int i = 0; i < size; i++) {
            array[i] = list.get(i).getValue();
          }

//
// Arrays.sort(array);
// found = Arrays.binarySearch(array, leftSide.getValue()) != -1;
// stopWatch.stop();
//
// stopWatch.start("Using Loop");
//
          found = CompareUtil.in(leftSide.getValue(), array);

// stopWatch.stop();
// logger.info(stopWatch.prettyPrint());
          return new OzElValue(found);
        } // end if
      }
    }     // end switch

    if (logger.isDebugEnabled()) {
      logger.debug("'" + leftSide.strValue + "' or '" + rightSide.strValue
        + "' need be matched for comparison.");
    }

    return new OzElValue();
  } // end method visitCompare

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitConstantExpression(com.ozstrategy.el.antlr.OzElParser.ConstantExpressionContext)
   */
  @Override public OzElValue visitConstantExpression(OzElParser.ConstantExpressionContext ctx) {
    return super.visitConstantExpression(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitEntry(com.ozstrategy.el.antlr.OzElParser.EntryContext)
   */
  @Override public OzElValue visitEntry(OzElParser.EntryContext ctx) {
    return visit(ctx.program());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression (EQUAL | NOT_EQUAL) expression #Equal.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitEqual(OzElParser.EqualContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));

    if (ctx.op.getType() == OzElParser.EQUAL) {
      if (((leftSide == null) || leftSide.isNullValue()) && ((rightSide == null) || rightSide.isNullValue())) {
        return new OzElValue(true);
      } else if ((leftSide == null) || leftSide.isNullValue()) {
        return new OzElValue(false);
      } else if ((rightSide == null) || rightSide.isNullValue()) {
        return new OzElValue(false);
      } else if (leftSide.isBooleanValue()) {
        if (rightSide.isBooleanValue()) {
          Boolean bRight = rightSide.getBooleanValue();

          if (Boolean.TRUE.equals(bRight)) {
            return leftSide;
          } else if (Boolean.FALSE.equals(bRight)) {
            return new OzElValue(!leftSide.getBooleanValue().booleanValue());
          }
        } else if (rightSide.isStringValue()) {
          if (rightSide.getStrValue().equalsIgnoreCase("true") || rightSide.getStrValue().equalsIgnoreCase("'true'")) {
            return leftSide;
          } else if (rightSide.getStrValue().equalsIgnoreCase("false")
                || rightSide.getStrValue().equalsIgnoreCase("'false'")) {
            return new OzElValue(!leftSide.getBooleanValue().booleanValue());
          }
        }

        if (logger.isDebugEnabled()) {
          logger.debug("'" + rightSide.getStrValue() + "' needed to be boolean value.");
        }

        return new OzElValue();
      } // end if-else
      else if (leftSide.isDecimalCompatibleValue() && rightSide.isBooleanValue()) {
        if (logger.isDebugEnabled()) {
          logger.debug("'" + rightSide.getStrValue() + "' needed to be decimal value.");
        }

        return new OzElValue();
      } // end if-else

      return new OzElValue(CompareUtil.equalsTo(leftSide.getValue(), rightSide.getValue()));
    } else if (ctx.op.getType() == OzElParser.NOT_EQUAL) {
      if (((leftSide == null) || leftSide.isNullValue()) && ((rightSide == null) || rightSide.isNullValue())) {
        return new OzElValue(false);
      } else if ((leftSide == null) || leftSide.isNullValue()) {
        return new OzElValue(true);
      } else if ((rightSide == null) || rightSide.isNullValue()) {
        return new OzElValue(true);
      } else if (leftSide.isBooleanValue()) {
        if (rightSide.isBooleanValue()) {
          Boolean bRight = rightSide.getBooleanValue();

          if (Boolean.FALSE.equals(bRight)) {
            return leftSide;
          } else if (Boolean.TRUE.equals(bRight)) {
            return new OzElValue(!leftSide.getBooleanValue());
          }
        } else if (rightSide.isStringValue()) {
          if (rightSide.getStrValue().equalsIgnoreCase("false")
                || rightSide.getStrValue().equalsIgnoreCase("'false'")) {
            return leftSide;
          } else if (rightSide.getStrValue().equalsIgnoreCase("true")
                || rightSide.getStrValue().equalsIgnoreCase("'true'")) {
            return new OzElValue(!leftSide.getBooleanValue());
          }
        }

        if (logger.isDebugEnabled()) {
          logger.debug("'" + rightSide.getStrValue() + "' needed to be boolean value.");
        }

        return new OzElValue();
      } // end if-else
      else if (leftSide.isDecimalCompatibleValue() && rightSide.isBooleanValue()) {
        if (logger.isDebugEnabled()) {
          logger.debug("'" + rightSide.getStrValue() + "' needed to be decimal value.");
        }

        return new OzElValue();
      } // end if-else

      return new OzElValue(CompareUtil.notEqualsTo(leftSide.getValue(), rightSide.getValue()));
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("Value type must be matched for comparison.");
      }
    } // end if-else

    return new OzElValue();
  } // end method visitEqual

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : expression (COMMA expression)*
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitExpressionList(OzElParser.ExpressionListContext ctx) {
    List<OzElValue> list = new ArrayList<OzElValue>();

    for (int i = 0; i < ctx.expression().size(); i++) {
      list.add(visit(ctx.expression(i)));
    }

    return new OzElValue(list);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : LPAREN expression RPAREN #ExprPrimary
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitExprPrimary(OzElParser.ExprPrimaryContext ctx) {
    // Just return
    return visit(ctx.expression());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | FALSE #FalseValue.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitFalseValue(OzElParser.FalseValueContext ctx) {
    return new OzElValue(Boolean.FALSE);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitForeachControl(com.ozstrategy.el.antlr.OzElParser.ForeachControlContext)
   */
  @Override public OzElValue visitForeachControl(OzElParser.ForeachControlContext ctx) {
    String    variableName = ctx.variable().getText();
    OzElValue expr         = visit(ctx.expression());

    List<Object> list = new ArrayList<Object>();

    Object value = expr.getValue();

    if (value instanceof Collection) {
      for (Object object : (Collection) value) {
        if (object instanceof OzElValue) {
          list.add(object);
        } else {
          list.add(new OzElValue(object));
        }
      }
    } else {
      if (value instanceof OzElValue) {
        list.add(value);
      } else {
        list.add(new OzElValue(value));
      }
    }

    return new OzElValue(variableName, list);
  } // end method visitForeachControl

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitForInit(com.ozstrategy.el.antlr.OzElParser.ForInitContext)
   */
  @Override public OzElValue visitForInit(OzElParser.ForInitContext ctx) {
    return super.visitForInit(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitForUpdate(com.ozstrategy.el.antlr.OzElParser.ForUpdateContext)
   */
  @Override public OzElValue visitForUpdate(OzElParser.ForUpdateContext ctx) {
    return super.visitForUpdate(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | ID #IdentParimary.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */

  @Override public OzElValue visitIdentPrimary(OzElParser.IdentPrimaryContext ctx) {
    String    name     = ctx.getText();
    OzElValue retValue = resolvers.getValue(name);

    if (!resolvers.contains(name.toLowerCase())) {
      if (!unknownVariables.contains(name)) {
        unknownVariables.add(name);

        if (logger.isDebugEnabled()) {
          logger.debug("Unknown :'" + name + "' found...");
        }
      }
    } else {
      if (!usedVariables.contains(name)) {
        usedVariables.add(name);

        if (logger.isDebugEnabled()) {
          logger.debug("Found :'" + name + "'...");
        }
      }
    }

    return retValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : INT #IntValue
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitIntValue(OzElParser.IntValueContext ctx) {
    String strValue = ctx.IntegerLiteral().getText();

    try {
      Integer intValue = Integer.parseInt(strValue);

      return new OzElValue(intValue);
    } catch (NumberFormatException e) {
      // Can not convert to Integer
      try {
        Long longValue = Long.parseLong(strValue);

        return new OzElValue(longValue);
      } catch (NumberFormatException e1) {
        // Can not convert to Long
        BigDecimal decimalValue = new BigDecimal(strValue);

        return new OzElValue(decimalValue);
      }
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | literal #LiteralPrimary.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitLiteralPrimary(OzElParser.LiteralPrimaryContext ctx) {
    // Just return
    return super.visitLiteralPrimary(ctx);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression (STAR | SLASH | MOD) expression #MulDiv.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitMulDiv(OzElParser.MulDivContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));

    if ((leftSide != null) && leftSide.isBooleanValue()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Boolean value '" + leftSide.strValue + "'can not do math calculation.");
      }

      return new OzElValue();
    }

    if ((rightSide != null) && rightSide.isBooleanValue()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Boolean value '" + rightSide.strValue + "'can not be math calculation.");
      }

      return new OzElValue();
    }

    if (((leftSide == null) || leftSide.isNullValue()) || ((rightSide == null) || (rightSide.isNullValue()))) {
      return new OzElValue();
    }

    if (leftSide.isDecimalCompatibleValue()) {
      // only allow decimal expression
      if (rightSide.isDecimalCompatibleValue()) {
        try {
          if (ctx.op.getType() == OzElParser.STAR) {
            if (leftSide.isDecimalValue() || rightSide.isDecimalValue()) {
              return new OzElValue(leftSide.getDecimalValue().multiply(rightSide.getDecimalValue()));
            } else if (leftSide.isLongValue() || rightSide.isLongValue()) {
              return new OzElValue(leftSide.getLongValue() * rightSide.getLongValue());
            } else if (leftSide.isIntegerValue() || rightSide.isIntegerValue()) {
              return new OzElValue(leftSide.getIntegerValue() * rightSide.getIntegerValue());
            }
          } else if (ctx.op.getType() == OzElParser.SLASH) {
            if (leftSide.isDecimalCompatibleValue() || rightSide.isDecimalCompatibleValue()) {
              return new OzElValue(leftSide.getDecimalValue().divide(rightSide.getDecimalValue(), 8,
                    BigDecimal.ROUND_HALF_EVEN));
            }
          } else if (ctx.op.getType() == OzElParser.MOD) {
            if (leftSide.isLongCompatibleValue()) {
              if (rightSide.isLongCompatibleValue()) {
                return new OzElValue(leftSide.getLongValue() % rightSide.getLongValue());
              } else {
                if (logger.isDebugEnabled()) {
                  logger.debug("'" + rightSide.getStrValue() + "' - Long value needed.");
                }
              }
            } else {
              if (logger.isDebugEnabled()) {
                logger.debug("'" + leftSide.getStrValue() + "' - Long value needed.");
              }
            }
          } // end if-else

          if (logger.isDebugEnabled()) {
            logger.debug("Operation not support.");
          }
        } catch (Exception e) {
          if (logger.isDebugEnabled()) {
            logger.debug(e.getLocalizedMessage());
          }
        } // end try-catch
      }   // end if
      else {
        if (logger.isDebugEnabled()) {
          logger.debug("'" + rightSide.getStrValue() + "' - Decimal value needed.");
        }
      }   // end if-else
    }     // end for
    else {
      if (logger.isDebugEnabled()) {
        logger.debug("'" + leftSide.strValue + "' - Decimal value needed.");
      }
    }     // end if-else

    return new OzElValue();
  } // end method visitMulDiv

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitNewExpr(com.ozstrategy.el.antlr.OzElParser.NewExprContext)
   */
  @Override public OzElValue visitNewExpr(OzElParser.NewExprContext ctx) {
    String strValue = ctx.IntegerLiteral().getText();

    if (ctx.LONG() != null) {
      Long longValue = Long.parseLong(strValue);

      return new OzElValue(longValue);
    } else if (ctx.INTEGER() != null) {
      Integer intValue = Integer.parseInt(strValue);

      return new OzElValue(intValue);
    }

    return visit(ctx.IntegerLiteral());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | NOT expression #NotExpression.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitNotExpression(OzElParser.NotExpressionContext ctx) {
    OzElValue rightSide = visit(ctx.expression());

    if (rightSide.isBooleanValue()) {
      return new OzElValue(!rightSide.getBooleanValue());
    }

    if (logger.isDebugEnabled()) {
      logger.debug("'" + rightSide.getStrValue() + "' needed to be boolean value.");
    }

    return new OzElValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | NULL #NullValue.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitNullValue(OzElParser.NullValueContext ctx) {
    return new OzElValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression OR expression #Or.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitOr(OzElParser.OrContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));

    if (leftSide.isBooleanValue()) {
      if ((rightSide == null) || rightSide.isNullValue() || rightSide.isBooleanValue()) {
        boolean bLeft = leftSide.getBooleanValue();

        if (bLeft) {
          return new OzElValue(Boolean.TRUE);
        } else {
          if ((rightSide == null) || rightSide.isNullValue()) {
            return new OzElValue();
          } else {
            return new OzElValue(rightSide.getBooleanValue());
          }
        }
      } else if (rightSide.isStringValue()) {
        if (rightSide.getStrValue().equalsIgnoreCase("true") || rightSide.getStrValue().equalsIgnoreCase("'true'")) {
          return new OzElValue(Boolean.TRUE);
        } else if (rightSide.getStrValue().equalsIgnoreCase("false")
              || rightSide.getStrValue().equalsIgnoreCase("'false'")) {
          return leftSide;
        }
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("'" + rightSide.strValue + "' - Needed to be Boolean value.");
        }
      }
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("'" + leftSide.strValue + "' - Needed to be Boolean value.");
      }
    } // end if-else

    return new OzElValue();
  } // end method visitOr

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : LPAREN expression RPAREN
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */

  @Override public OzElValue visitParExpression(OzElParser.ParExpressionContext ctx) {
    return visit(ctx.expression());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitPostfixOp(com.ozstrategy.el.antlr.OzElParser.PostfixOpContext)
   */
  @Override public OzElValue visitPostfixOp(OzElParser.PostfixOpContext ctx) {
    String    variableName = ctx.variable().getText();
    OzElValue elValue      = resolvers.getValue(variableName);

    if (elValue.isDecimalCompatibleValue()) {
      OzElValue retValue = elValue;

      if (elValue.isIntegerValue()) {
        if (ctx.MINUSMINUS() != null) {
          // do --
          resolvers.addValue(variableName, elValue.getIntegerValue() - 1);
        } else if (ctx.PLUSPLUS() != null) {
          // do ++
          resolvers.addValue(variableName, elValue.getIntegerValue() + 1);
        }
      } else if (elValue.isLongValue()) {
        if (ctx.MINUSMINUS() != null) {
          // do --
          resolvers.addValue(variableName, elValue.getLongValue() - 1);
        } else if (ctx.PLUSPLUS() != null) {
          // do ++
          resolvers.addValue(variableName, elValue.getLongValue() + 1);
        }
      } else if (elValue.isDecimalValue()) {
        BigDecimal bgValue = elValue.getDecimalValue();

        if (ctx.MINUSMINUS() != null) {
          // do --
          resolvers.addValue(variableName, bgValue.subtract(new BigDecimal("1.0")));
        } else if (ctx.PLUSPLUS() != null) {
          // do ++
          resolvers.addValue(variableName, bgValue.add(new BigDecimal("1.0")));
        }
      } // end if-else

      // return original value
      return retValue;
    } // end if

    return new OzElValue();
  } // end method visitPostfixOp

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitPrefixOp(com.ozstrategy.el.antlr.OzElParser.PrefixOpContext)
   */
  @Override public OzElValue visitPrefixOp(OzElParser.PrefixOpContext ctx) {
    String    variableName = ctx.variable().getText();
    OzElValue elValue      = resolvers.getValue(variableName);
    OzElValue retValue     = new OzElValue();

    if (elValue.isDecimalCompatibleValue()) {
      if (elValue.isIntegerValue()) {
        if (ctx.MINUSMINUS() != null) {
          // do --
          retValue = new OzElValue(variableName, elValue.getIntegerValue() - 1);
          resolvers.addValue(retValue.getName(), retValue);
        } else if (ctx.PLUSPLUS() != null) {
          // do ++
          retValue = new OzElValue(variableName, elValue.getIntegerValue() + 1);
          resolvers.addValue(retValue.getName(), retValue);
        }
      } else if (elValue.isLongValue()) {
        if (ctx.MINUSMINUS() != null) {
          // do --
          retValue = new OzElValue(variableName, elValue.getLongValue() - 1);
          resolvers.addValue(retValue.getName(), retValue);
        } else if (ctx.PLUSPLUS() != null) {
          // do ++
          retValue = new OzElValue(variableName, elValue.getLongValue() + 1);
          resolvers.addValue(retValue.getName(), retValue);
        }
      } else if (elValue.isDecimalValue()) {
        BigDecimal bgValue = elValue.getDecimalValue();

        if (ctx.MINUSMINUS() != null) {
          // do --
          retValue = new OzElValue(variableName, bgValue.subtract(new BigDecimal("1.0")));
          resolvers.addValue(retValue.getName(), retValue);
        } else if (ctx.PLUSPLUS() != null) {
          // do ++
          retValue = new OzElValue(variableName, bgValue.add(new BigDecimal("1.0")));
          resolvers.addValue(retValue.getName(), retValue);
        }
      } // end if-else
    }   // end if

    return retValue;
  } // end method visitPrefixOp

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : primary #PriExpr
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitPriExpr(OzElParser.PriExprContext ctx) {
    return visit(ctx.primary());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : block EOF
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitProgram(OzElParser.ProgramContext ctx) {
    try {
      if (ctx.expression() != null) {
        return visit(ctx.expression());
      } else if (ctx.block() != null) {
        return visit(ctx.block());
      } else if (ctx.programStatement() != null) {
        List<Object> results = new ArrayList<Object>();

        for (OzElParser.ProgramStatementContext programStatementContext : ctx.programStatement()) {
          OzElValue result = visit(programStatementContext);
          results.add((result != null) ? result.getValue() : null);
        }

        return new OzElValue(results);
      }
    } catch (ReturnException re) {
      return re.getRetValue();
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitProgramStatement(com.ozstrategy.el.antlr.OzElParser.ProgramStatementContext)
   */
  @Override public OzElValue visitProgramStatement(OzElParser.ProgramStatementContext ctx) {
    if (ctx.expression() != null) {
      return visit(ctx.expression());
    } else if (ctx.block() != null) {
      return visit(ctx.block());
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | REAL #RealValue.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitRealValue(OzElParser.RealValueContext ctx) {
    return new OzElValue(new BigDecimal(ctx.RealLiteral().getText()));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitReturnStatement(com.ozstrategy.el.antlr.OzElParser.ReturnStatementContext)
   */
  @Override public OzElValue visitReturnStatement(OzElParser.ReturnStatementContext ctx) {
    throw new ReturnException(visit(ctx.expression()));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression QUESTION expression COLON expression #Select.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitSelect(OzElParser.SelectContext ctx) {
    OzElValue condValue = visit(ctx.expression(0));

    if (condValue.isBooleanValue()) {
      if (Boolean.TRUE.equals(condValue.getBooleanValue())) {
        return visit(ctx.expression(1));
      } else {
        return visit(ctx.expression(2));
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("'" + condValue.getStrValue() + "' needed to be boolean value.");
    }

    return new OzElValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementBlock(com.ozstrategy.el.antlr.OzElParser.StatementBlockContext)
   */
  @Override public OzElValue visitStatementBlock(OzElParser.StatementBlockContext ctx) {
    return super.visitStatementBlock(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementDo(com.ozstrategy.el.antlr.OzElParser.StatementDoContext)
   */
  @Override public OzElValue visitStatementDo(OzElParser.StatementDoContext ctx) {
    OzElValue retValue;

    while (true) {
      // do for body
      retValue = visit(ctx.statement());

      // while parExpression
      OzElValue loopValue = visit(ctx.parExpression());

      if (loopValue.getBooleanValue().equals(Boolean.FALSE)) {
        break;
      }
    }

    return retValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | SEMI #StatementEmpty.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitStatementEmpty(OzElParser.StatementEmptyContext ctx) {
    return new OzElValue(OzElValue.SUPPORT_TYPE.SKIP, "Skip", "");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementExpr(com.ozstrategy.el.antlr.OzElParser.StatementExprContext)
   */
  @Override public OzElValue visitStatementExpr(OzElParser.StatementExprContext ctx) {
    return super.visitStatementExpr(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : expression
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitStatementExpression(OzElParser.StatementExpressionContext ctx) {
    return visit(ctx.expression());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | FOR LPAREN forControl RPAREN statement #StatementFor.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitStatementFor(OzElParser.StatementForContext ctx) {
    OzElValue                    retValue          = new OzElValue();
    OzElParser.ForControlContext forControlContext = ctx.forControl();
    OzElParser.ForInitContext    forInitContext    = forControlContext.forInit();
    OzElParser.ExpressionContext expressionContext = forControlContext.expression();
    OzElParser.ForUpdateContext  forUpdateContext  = forControlContext.forUpdate();

    // init For
    if (forInitContext != null) {
      visit(forInitContext);
    }

    // loop until expression evaluate to true
    while (true) {
      if (expressionContext != null) {
        OzElValue loopValue = visit(expressionContext);

        if (loopValue.getBooleanValue().equals(Boolean.FALSE)) {
          break;
        }
      }

      // do for body
      retValue = visit(ctx.statement());

      // for update
      if (forUpdateContext != null) {
        visit(forUpdateContext);
      }
    }

    return retValue;
  } // end method visitStatementFor

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementForeach(com.ozstrategy.el.antlr.OzElParser.StatementForeachContext)
   */
  @Override public OzElValue visitStatementForeach(OzElParser.StatementForeachContext ctx) {
    OzElValue retValue  = new OzElValue();
    OzElValue listValue = visit(ctx.foreachControl());

    String variableName = listValue.getName();

    for (OzElValue value : listValue.getListValue()) {
      resolvers.addValue(variableName, value);
      retValue = visit(ctx.statement());
    }

    return retValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | IF parExpression statement (ELSE statement)? #StatementIf
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitStatementIf(OzElParser.StatementIfContext ctx) {
    OzElValue exprValue = visit(ctx.parExpression());
    int       size      = ctx.statement().size();

    if (exprValue.isBooleanValue()) {
      if (Boolean.TRUE.equals(exprValue.getBooleanValue())) {
        return visit(ctx.statement(0));
      } else if (Boolean.FALSE.equals(exprValue.getBooleanValue())) {
        if (size == 2) {
          return visit(ctx.statement(1));
        } else {
          return new OzElValue();
        }
      }
    }

    return new OzElValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementReturn(com.ozstrategy.el.antlr.OzElParser.StatementReturnContext)
   */
  @Override public OzElValue visitStatementReturn(OzElParser.StatementReturnContext ctx) {
    // TODO
    return super.visitStatementReturn(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementSwitch(com.ozstrategy.el.antlr.OzElParser.StatementSwitchContext)
   */
  @Override public OzElValue visitStatementSwitch(OzElParser.StatementSwitchContext ctx) {
    return super.visitStatementSwitch(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementWhile(com.ozstrategy.el.antlr.OzElParser.StatementWhileContext)
   */
  @Override public OzElValue visitStatementWhile(OzElParser.StatementWhileContext ctx) {
    OzElValue retValue = new OzElValue();

    while (true) {
      // while parExpression
      OzElValue loopValue = visit(ctx.parExpression());

      if (loopValue.getBooleanValue().equals(Boolean.FALSE)) {
        break;
      }

      // do for body
      retValue = visit(ctx.statement());
    }

    return retValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | STRING #StringValue.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitStringValue(OzElParser.StringValueContext ctx) {
    String text = ctx.StringLiteral().getText();

    if (text != null) {
      // trim front/end '"'
      int length = text.length();

      if (text.startsWith("\"") && text.endsWith("\"")) {
        text = text.substring(1, length - 1);
      } else if (text.startsWith("'") && text.endsWith("'")) {
        text = text.substring(1, length - 1);
      }

      return new OzElValue(text);
    }

    return new OzElValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitSwitchBlock(com.ozstrategy.el.antlr.OzElParser.SwitchBlockContext)
   */
  @Override public OzElValue visitSwitchBlock(OzElParser.SwitchBlockContext ctx) {
    return super.visitSwitchBlock(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitSwitchBlockStatementGroup(com.ozstrategy.el.antlr.OzElParser.SwitchBlockStatementGroupContext)
   */
  @Override public OzElValue visitSwitchBlockStatementGroup(OzElParser.SwitchBlockStatementGroupContext ctx) {
    return super.visitSwitchBlockStatementGroup(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitSwitchCase(com.ozstrategy.el.antlr.OzElParser.SwitchCaseContext)
   */
  @Override public OzElValue visitSwitchCase(OzElParser.SwitchCaseContext ctx) {
    return super.visitSwitchCase(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitSwitchDefault(com.ozstrategy.el.antlr.OzElParser.SwitchDefaultContext)
   */
  @Override public OzElValue visitSwitchDefault(OzElParser.SwitchDefaultContext ctx) {
    return super.visitSwitchDefault(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : TRUE #TrueValue
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitTrueValue(OzElParser.TrueValueContext ctx) {
    return new OzElValue(Boolean.TRUE);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitUnaryOp(com.ozstrategy.el.antlr.OzElParser.UnaryOpContext)
   */
  @Override public OzElValue visitUnaryOp(OzElParser.UnaryOpContext ctx) {
    int negative = 1;

    if (ctx.MINUS() != null) {
      negative = -1;
    }

    OzElValue rightSide = visit(ctx.expression());

    if (rightSide.isIntegerValue()) {
      return new OzElValue(negative * rightSide.getIntegerValue());
    }

    if (rightSide.isLongValue()) {
      return new OzElValue(negative * rightSide.getLongValue());
    }

    if (rightSide.isDecimalValue()) {
      if (negative == -1) {
        return new OzElValue(rightSide.getDecimalValue().negate());
      } else {
        return new OzElValue(rightSide.getDecimalValue());
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("'" + rightSide.getStrValue() + "' - Decimal Value Needed.");
    }

    return new OzElValue();
  } // end method visitUnaryOp

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitVariable(com.ozstrategy.el.antlr.OzElParser.VariableContext)
   */
  @Override public OzElValue visitVariable(OzElParser.VariableContext ctx) {
    return super.visitVariable(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }
} // end class EvalVisitor
