package com.ozstrategy.el.impl;

/*
  Created with IntelliJ IDEA.
  User: rojer
  Date: 5/24/13
  Time: 4:32 PM
  To change this template use File | Settings | File Templates.
 */

import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ozstrategy.el.util.NoCaseHashMap;
import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozstrategy.el.antlr.OzElBaseVisitor;
import com.ozstrategy.el.antlr.OzElParser;
import com.ozstrategy.el.exception.NotSupportException;
import com.ozstrategy.el.util.FunctionUtil;
import com.ozstrategy.el.util.NoCaseHashSet;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class VerifyVisitor extends OzElBaseVisitor<OzElValue> {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** Variables that supported by context. */
  protected NoCaseHashSet declaredVariables = new NoCaseHashSet();

  /** Logger.* */
  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  /** Cascaded Functions used in the expression. */
  protected NoCaseHashSet normalizedCascadedFunctions = new NoCaseHashSet();

  /** Cascaded Variables used in the expression. */
  protected NoCaseHashSet normalizedCascadedVariables = new NoCaseHashSet();

  /** Functions used in the expression. */
  protected NoCaseHashSet originalCascadedFunctions = new NoCaseHashSet();

  /** Variables used in the expression. */
  protected NoCaseHashSet originalCascadedVariables = new NoCaseHashSet();

  /** Resolver Context. */

  protected ResolverContext resolvers = new ResolverContext();

  /** Variables that supported by context. */
  protected NoCaseHashSet supportedVariables = new NoCaseHashSet();

  /** Temp Variables in the context. */
  protected NoCaseHashSet tempVariables = new NoCaseHashSet();

  /** Defined Variables in the context. */
  protected NoCaseHashSet definedVariables = new NoCaseHashSet();

  /** Unknown Variables in the context. */
  protected NoCaseHashSet unknownVariables = new NoCaseHashSet();

  /** Functions used in the expression. */
  protected NoCaseHashSet usedFunctions = new NoCaseHashSet();

  /** Variables used in the expression. */
  protected NoCaseHashSet usedVariables = new NoCaseHashSet();

  protected ArrayList<String> variablePaths = new ArrayList<>();

  protected HashMap<ParserRuleContext, String> callPaths = new HashMap<>();
  protected NoCaseHashMap<ParserRuleContext> forContexts = new NoCaseHashMap<>();
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new EvalVisitor object.
   */
  public VerifyVisitor() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getDeclaredVariableNames() {
    return getStringArray(declaredVariables);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * All variables declared in expressions.
   *
   * @return  declared variables
   */
  public NoCaseHashSet getDeclaredVariables() {
    return declaredVariables;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get all cascaded functions referenced.
   *
   * @return  used cascaded function
   */
  public NoCaseHashSet getNormalizedCascadedFunctions() {
    return normalizedCascadedFunctions;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get all cascaded variables referenced.
   *
   * @return  used cascaded variables
   */
  public NoCaseHashSet getNormalizedCascadedVariables() {
    return normalizedCascadedVariables;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public NoCaseHashSet getOriginalCascadedFunctions() {
    return originalCascadedFunctions;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get all original variables referenced.
   *
   * @return  used original variables
   */
  public NoCaseHashSet getOriginalCascadedVariables() {
    return originalCascadedVariables;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get All unknown variables.
   *
   * @return  unknown variables
   */
  public String getUnknownVariableNames() {
    return getStringArray(unknownVariables);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get All variables referenced.
   *
   * @return  used variables
   */
  public NoCaseHashSet getUnknownVariables() {
    return unknownVariables;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get Cascaded function names in an array.
   *
   * @return  used function names
   */
  public String getUsedCascadedFunctionNames() {
    return getStringArray(normalizedCascadedFunctions);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get cascaded variable names in an array.
   *
   * @return  used variable names
   */
  public String getUsedCascadeVariableNames() {
    return getStringArray(normalizedCascadedVariables);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get function names in an array.
   *
   * @return  used function names
   */
  public String getUsedFunctionNames() {
    return getStringArray(usedFunctions);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get all functions referenced.
   *
   * @return  used functions
   */
  public NoCaseHashSet getUsedFunctions() {
    return usedFunctions;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get variable names in an array.
   *
   * @return  used variable names
   */
  public String getUsedVariableNames() {
    return getStringArray(usedVariables);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get all variables referenced.
   *
   * @return  used variables
   */
  public NoCaseHashSet getUsedVariables() {
    return usedVariables;
  }

  /**
   * Get all variables defined in context.
   * @return
   */
  public NoCaseHashSet getDefinedVariables() {
    return definedVariables;
  }

  /**
   * Get all variable paths.
   *
   * @return
   */
  public ArrayList<String> getVariablePaths() {
    return variablePaths;
  }

  /**
   * Get all vaeiable call paths
   *
   * @return
   */
  public ArrayList<String> getCallPaths() {
    ArrayList<String> paths = new ArrayList();
    paths.addAll(callPaths.values());
    Collections.sort(paths);
    return paths;
  }

//  public List getVariablePaths(){
//    ArrayList<String> list = new ArrayList<>();
//    return list;
//  }
  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check if there is any cascaded functions.
   *
   * @return  true - if there is cascaded fcuntions
   */
  public boolean hasCascadedFunctions() {
    return (normalizedCascadedFunctions.size() > 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check if there is any cascaded variable.
   *
   * @return  true - if there is cascaded variable
   */
  public boolean hasCascadedVariables() {
    return (normalizedCascadedVariables.size() > 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check if there is any unknown variable.
   *
   * @return  true - if there is unknown variable
   */
  public boolean hasUnknownVariables() {
    return (unknownVariables.size() > 0);
  }

  /**
   * Check if there is any defined variable.
   *
   * @return  true - if there is defined variable
   */
  public boolean hasDefinedVariables() {
    return (definedVariables.size() > 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check if there is any functions used.
   *
   * @return  true - if there is functions used
   */
  public boolean hasUsedFunctions() {
    return (usedFunctions.size() > 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check if there is any used variable.
   *
   * @return  true - if there is used variable
   */
  public boolean hasUsedVariables() {
    return (usedVariables.size() > 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Inject resolver context.
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
   * | expression LBRACK expression RBRACK #AccessElement.
   *
   * @param   ctx  $param.type$
   *
   * @return  | expression LBRACK expression RBRACK #AccessElement.
   *
   * @throws  NotSupportException  exception
   */
  @Override public OzElValue visitAccessElement(OzElParser.AccessElementContext ctx) {
    OzElValue arrayElValue = visit(ctx.expression(0));

    OzElValue indexElValue = visit(ctx.expression(1));

    String elementName = ctx.expression(0).getText();
    String indexName = ctx.expression(1).getText();

    if (!indexElValue.isIntegerValue()) {
      // index need to be a integer value
      throw new NotSupportException("Index '" + indexName + "' is not support.");
    }

    if (arrayElValue.isListValue()) {

      logCallPaths(ctx, ctx.expression(0), "[" + indexName + "]");
      return OzElValue.createOzElType(null, arrayElValue.getElementClazz());
    }


    throw new NotSupportException("Element of '" + elementName + "' is not found.");
  } // end method visitAccessElement

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression DOT ID #AccessProperty.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitAccessProperty(OzElParser.AccessPropertyContext ctx) {
    boolean   topAccessProperty = !(ctx.getParent() instanceof OzElParser.AccessPropertyContext);
    String    cascadedInstance  = null;
    OzElValue instanceElValue   = visit(ctx.expression(0));

    OzElParser.ExpressionContext propertyContext = ctx.expression(1);

    if (propertyContext instanceof OzElParser.CallFunctionContext) {
      return visit(propertyContext);
    } else {
      String propertyName = propertyContext.getText();
      Class  clazz        = null;

      if (instanceElValue.isFunctionValue()) {
        clazz = (Class) instanceElValue.getValue();
      } else if (instanceElValue.isMapValue()) {
        Map<String, Object> instanceValue = instanceElValue.getMapValue();
        Object propertyValue = instanceValue.get(propertyName);
        clazz            = propertyValue.getClass();
        cascadedInstance = instanceElValue.getPathOrName();
        logProperties(ctx, cascadedInstance, propertyName, null, topAccessProperty);
        logCallPaths(ctx, cascadedInstance, propertyName, null);

        return OzElValue.createOzElType(propertyName, clazz);
      } else if (instanceElValue.isObjectValue()) {
        clazz            = instanceElValue.getClazz();
        cascadedInstance = instanceElValue.getPathOrName();
      } else {
        // It is a variable name
        String instanceName = instanceElValue.hasName() ? instanceElValue.getName() : instanceElValue.getStringValue();

        if (instanceName != null) {
          clazz = ((OzElValue) resolvers.getValue(instanceName)).getClazz();
        }

        cascadedInstance = instanceName;
      }

      Class[] argTypes = {};

      Method method     = null;
      String methodName = propertyName;

      try {
        methodName = "get" + propertyName;
        method     = FunctionUtil.findMethod(clazz, methodName, argTypes);

        if (method == null) {
          methodName = "is" + propertyName;
          method     = FunctionUtil.findMethod(clazz, methodName, argTypes);
        }

        if (method == null) {
          method = FunctionUtil.findMethod(clazz, propertyName, argTypes);
        }

        if (method != null) {
          String path = logProperties(ctx, cascadedInstance, propertyName, method, topAccessProperty);
          logCallPaths(ctx, ctx.expression(0), cascadedInstance, method);

          return OzElValue.createOzElType(propertyName, method.getGenericReturnType()).setPath(path);
        }
      } catch (NoSuchMethodException e) { } // end try-catch

      throw new NotSupportException("The property '" + propertyName + "' does not exist.");
    } // end if-else
  }   // end method visitAccessProperty

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression op=(PLUS | MINUS) expression #AddSub.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitAddSub(OzElParser.AddSubContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));
    String    leftText  = ctx.expression(0).getText();
    String    rightText = ctx.expression(1).getText();

    if ((leftSide != null) && leftSide.isBooleanValue()) {
      throw new NotSupportException("Boolean value '" + leftText + "'can not do math calculation.");
    }

    if ((rightSide != null) && rightSide.isBooleanValue()) {
      throw new NotSupportException("Boolean value '" + rightText + "'can not be math calculation.");
    }

    if (((leftSide == null) || leftSide.isNullValue()) || ((rightSide == null) || (rightSide.isNullValue()))) {
      return new OzElValue();
    }

    if (leftSide.isDecimalCompatibleValue()) {
      // only allow decimal expression
      if (rightSide.isDecimalCompatibleValue()) {
        if (leftSide.isDecimalValue() || rightSide.isDecimalValue()) {
          return new OzElValue(OzElValue.SUPPORT_TYPE.DECIMAL, BigDecimal.class);
        } else if (leftSide.isLongValue() || rightSide.isLongValue()) {
          return new OzElValue(OzElValue.SUPPORT_TYPE.LONG, Long.class);
        } else if (leftSide.isIntegerValue() || rightSide.isIntegerValue()) {
          return new OzElValue(OzElValue.SUPPORT_TYPE.INTEGER, Integer.class);
        }
      } // end if
      else if (ctx.op.getType() == OzElParser.PLUS) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.STRING, String.class);
      } else {
        throw new NotSupportException("'" + rightText + "' - Decimal value needed.");
      }
    }   // end for
    else if (leftSide.isDateValue()) {
      // only allow Date +/- num & Date - Date
      if (rightSide.isLongCompatibleValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.DATE, Date.class);
      }   // end if
      else if (rightSide.isDateValue()) {
        if (ctx.op.getType() == OzElParser.MINUS) {
          return new OzElValue(OzElValue.SUPPORT_TYPE.INTEGER, Integer.class);
        } // en
      }   // end if-else
    }     // end for
    else if ((ctx.op.getType() == OzElParser.PLUS)) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.STRING, String.class);
    }     // end if-else

    throw new NotSupportException("'" + leftText + "' - Decimal or Date value needed.");
  } // end method visitAddSub

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression AND expression #And.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitAnd(OzElParser.AndContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    String    leftText  = ctx.expression(0).getText();
    OzElValue rightSide = visit(ctx.expression(1));
    String    rightText = ctx.expression(1).getText();

    if (leftSide.isBooleanValue()) {
      if ((rightSide == null) || rightSide.isNullValue() || rightSide.isBooleanValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.BOOLEAN, Boolean.class);
      } else {
        throw new NotSupportException("'" + rightText + "' - Needed to be Boolean value.");
      }
    } else {
      throw new NotSupportException("'" + leftText + "' - Needed to be Boolean value.");
    }
  }

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
    String    variableName = ctx.variable().getText().toLowerCase();
    OzElValue exprValue    = visit(ctx.expression());
    String    exprText     = ctx.expression().getText();


    switch (ctx.op.getType()) {
      case OzElParser.ASSIGN: {
        if (!tempVariables.contains(variableName)) {
          tempVariables.add(variableName);
          definedVariables.add(variableName + ":" + exprText);
        }

        resolvers.addValue(variableName, exprValue);

        return exprValue;
      }

      case OzElParser.PLUSEQUAL:
      case OzElParser.MINUSEQUAL:
      case OzElParser.STAREQUAL:
      case OzElParser.SLASHEQUAL:
      case OzElParser.MODEQUAL: {
        OzElValue varValue = resolvers.getValue(variableName);

        resolvers.addValue(variableName, varValue);

        if (varValue.isDecimalValue() || exprValue.isDecimalValue()) {
          return new OzElValue(OzElValue.SUPPORT_TYPE.DECIMAL, BigDecimal.class);
        } else if (varValue.isLongValue() || exprValue.isLongValue()) {
          return new OzElValue(OzElValue.SUPPORT_TYPE.LONG, Long.class);
        } else if (varValue.isIntegerValue() || exprValue.isIntegerValue()) {
          return new OzElValue(OzElValue.SUPPORT_TYPE.INTEGER, Integer.class);
        } else if ((varValue.isStringValue() || exprValue.isStringValue())&&(OzElParser.PLUSEQUAL == ctx.op.getType())) {
          return new OzElValue(OzElValue.SUPPORT_TYPE.INTEGER, Integer.class);
        } else {
          throw new NotSupportException("'" + exprText + "' - Needed to be Decimal/Long/Integer value.");
        }
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

    int statementSize = ctx.statement().size();

    for (int i = 0; i < statementSize; i++) {
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
   * | booleanLiteral #BoolValue.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitBoolValue(OzElParser.BoolValueContext ctx) {
    // just return
    return super.visitBoolValue(ctx);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression LPAREN expressionList? RPAREN #CallFunction
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitCallFunction(OzElParser.CallFunctionContext ctx) {
    boolean topAccessProperty = !(ctx.getParent().getParent() instanceof OzElParser.AccessPropertyContext);
    String  cascadedInstance  = null;
    String  functionName      = ctx.ID().getText();

    Class   methodClass = null;
    String  methodName  = functionName;
    Class[] argTypes    = {};

    OzElValue params = (ctx.expressionList() == null) ? null : visit(ctx.expressionList());

    if ((params != null) && (params.isListValue())) {
      List<OzElValue> list = params.getListValue();
      int             size = list.size();
      argTypes = new Class[size];

      for (int i = 0; i < size; i++) {
        OzElValue param = list.get(i);

        if (param != null) {
          argTypes[i] = param.getValueType();
        } else {
          argTypes[i] = Object.class;
        }
      }
    }

    if (ctx.getParent() instanceof OzElParser.AccessPropertyContext) {
      OzElParser.AccessPropertyContext propertyContext = (OzElParser.AccessPropertyContext) ctx.getParent();
      OzElParser.ExpressionContext     expressionBase  = propertyContext.expression(0);

      if (expressionBase != ctx) {
        OzElValue instanceElValue = visit(expressionBase);

        methodClass      = instanceElValue.getClazz();
        methodName       = functionName;
        cascadedInstance = instanceElValue.getPathOrName();
      }
    }

    if (methodClass == null) {
      ExposedMethod overrideMethod = resolvers.getRuntimeMethod(methodName);

      if (overrideMethod != null) {
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
    }

    Method method = null;

    try {
      method = FunctionUtil.findMethod(methodClass, methodName, argTypes);

      if (method != null) {
        if (logger.isDebugEnabled()) {
          logger.debug("Found Method:" + method.toString());
          logger.debug("Method return:" + method.getGenericReturnType());
        }

        String methodClassName = method.getDeclaringClass().getName();
        if(methodClassName.startsWith("java.")){
          throw new NotSupportException("Function '" + methodName + "' is not supported.");
        }

        String fnName = ctx.getText();
        String parentFnName = ctx.getParent().getText();
        String path = logFunctions(fnName, cascadedInstance, functionName, method, topAccessProperty);

        if (cascadedInstance != null || parentFnName.endsWith("]."+fnName)) {
          path = logFunctions(parentFnName, cascadedInstance, functionName, method, topAccessProperty);
        }

        logCallPaths(ctx.getParent(), cascadedInstance, method);
        return OzElValue.createOzElType(method.getName(), method.getGenericReturnType()).setPath(path);
      } else {
        if (argTypes.length == 0) {
          methodName = "get" + functionName;
          method     = FunctionUtil.findMethod(methodClass, methodName, argTypes);

          if (method == null) {
            methodName = "is" + functionName;
            method     = FunctionUtil.findMethod(methodClass, methodName, argTypes);
          }
        }

        if (method != null) {
          String path = logFunctions(ctx.getText(), cascadedInstance, functionName, method, topAccessProperty);

          if (cascadedInstance != null) {
            path = logFunctions(ctx.getParent().getText(), cascadedInstance, functionName, method, topAccessProperty);
          }

          logCallPaths(ctx, cascadedInstance, method);
          return OzElValue.createOzElType(methodName, method.getGenericReturnType()).setPath(path);
        }

        throw new NotSupportException("Function '" + methodName + "' is not supported.");
      } // end if-else
    } catch (NoSuchMethodException e) {
      logger.error(e.getMessage(), e);
    } catch (NullPointerException e) {
      logger.error(e.getMessage(), e);
    }   // end try-catch

    throw new NotSupportException("Function '" + methodName + "' is not supported.");
  } // end method visitCallFunction

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression op=(LE | GE | GT | LT | IN) expression #Compare.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitCompare(OzElParser.CompareContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));
    String    leftText  = ctx.expression(0).getText();
    String    rightText = ctx.expression(1).getText();


    if (ctx.op.getType() == OzElParser.IN) {
      if (rightSide.isListValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.BOOLEAN, Boolean.class);
      }
    }

    if ((leftSide != null) && leftSide.isBooleanValue()) {
      throw new NotSupportException("Boolean value '" + leftText + "' can not be compared.");
    }

    if ((rightSide != null) && rightSide.isBooleanValue()) {
      throw new NotSupportException("Boolean value '" + rightText + "' can not be compared.");
    }

    if (((leftSide == null) || leftSide.isNullValue()) || ((rightSide == null) || rightSide.isNullValue())) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.BOOLEAN, Boolean.class);
    } else if ((leftSide.isDecimalCompatibleValue() && rightSide.isDecimalCompatibleValue())
          || (leftSide.isDateValue() && rightSide.isDateValue())
          || (leftSide.isDateValue() && rightSide.isStringValue())
          || (leftSide.isStringValue() && rightSide.isDateValue())
          || (leftSide.isStringValue() && rightSide.isStringValue())) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.BOOLEAN, Boolean.class);
    } else {
      throw new NotSupportException("'" + leftText + "' or '" + rightText
        + "' need be matched for comparison.");
    }
  } // end method visitCompare

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * : expression
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public OzElValue visitConstantExpression(OzElParser.ConstantExpressionContext ctx) {
    // Just return
    return super.visitConstantExpression(ctx);
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
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitEqual(OzElParser.EqualContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));

    if (logger.isDebugEnabled()) {
      logger.debug("left:" + ((leftSide == null) ? null : leftSide.toString()));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("right:" + ((rightSide == null) ? null : rightSide.toString()));
    }

    if (((leftSide == null) || leftSide.isNullValue()) || ((rightSide == null) || rightSide.isNullValue())) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.BOOLEAN, Boolean.class);
    }

    if ((leftSide.isDecimalCompatibleValue() && rightSide.isDecimalCompatibleValue())
          || (leftSide.isBooleanValue() && rightSide.isBooleanValue())
          || (leftSide.isBooleanValue()
            && (rightSide.isStringValue()
              && (rightSide.getStrValue().equalsIgnoreCase("true")
                || (rightSide.getStrValue().equalsIgnoreCase("false")))))
          || (leftSide.isDateValue() && rightSide.isDateValue())
          || (leftSide.isDateValue() && rightSide.isStringValue())
          || (leftSide.isStringValue() && rightSide.isDateValue())
          || (leftSide.isStringValue() && rightSide.isStringValue())) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.BOOLEAN, Boolean.class);
    } else {
      throw new NotSupportException("Value type must be matched for comparison.");
    }
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
    OzElParser.VariableContext variableContext = ctx.variable();
    OzElParser.ExpressionContext expressionContext = ctx.expression();
    String    variableName = variableContext.getText().toLowerCase();
    String    expText = expressionContext.getText();
    definedVariables.add(variableName + ":" + expText + "[0]");

    OzElValue expr         = visit(expressionContext);

    List<Object> list = new ArrayList<Object>();

    Object value = expr.getValue();
    Class  clazz = expr.getClazz();

    if (value != null) {
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
    } else {
      if (clazz != null) {
        if(Map.class.isAssignableFrom(clazz)){
          throw new NotSupportException("Map valus is not support for foreach loop.");
//          Class key = expr.getKeyClazz();
//          Class element = expr.getElementClazz();
//          list.add(OzElValue.createOzElType(null, (new KeyValue(key, element)).getClass()));
        }
        else if (Collection.class.isAssignableFrom(clazz)) {
          list.add(OzElValue.createOzElType(null, expr.getElementClazz()));
        }
      }
    }

    if (!tempVariables.contains(variableName)) {
      tempVariables.add(variableName);
    }

    logCallPaths(variableContext, expressionContext, "[0]");
    forContexts.put(variableName, variableContext);
    return new OzElValue(variableName, list);
  } // end method visitForeachControl

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitForInit(com.ozstrategy.el.antlr.OzElParser.ForInitContext)
   */
  @Override public OzElValue visitForInit(OzElParser.ForInitContext ctx) {
    OzElParser.ExpressionListContext expressionListContext = ctx.expressionList();

    if (expressionListContext != null) {
      for (OzElParser.ExpressionContext expressionContext : expressionListContext.expression()) {
        visit(expressionContext);
      }
    }

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
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitIdentPrimary(com.ozstrategy.el.antlr.OzElParser.IdentPrimaryContext)
   */
  @Override public OzElValue visitIdentPrimary(OzElParser.IdentPrimaryContext ctx) {
    boolean   topAccessProperty = !(ctx.getParent().getParent() instanceof OzElParser.AccessPropertyContext);
    String    name              = ctx.getText();
    OzElValue retValue          = resolvers.getValueType(name);

    if (!tempVariables.contains(name.toLowerCase())) {
      if (!resolvers.contains(name.toLowerCase())) {
        unknownVariables.add(name);

        if (logger.isDebugEnabled()) {
          logger.debug("Unknown :'" + name + "' found...");
        }
      } else {
        if (topAccessProperty) {
          usedVariables.add(name);
        }

        if (logger.isDebugEnabled()) {
          logger.debug("Found :'" + name + "'...");
        }
      }
    }

    return retValue;
  } // end method visitIdentPrimary

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
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitMulDiv(OzElParser.MulDivContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));
    String    leftText  = ctx.expression(0).getText();
    String    rightText = ctx.expression(1).getText();

    if ((leftSide != null) && leftSide.isBooleanValue()) {
      throw new NotSupportException("Boolean value '" + leftText + "'can not do math calculation.");
    }

    if ((rightSide != null) && rightSide.isBooleanValue()) {
      throw new NotSupportException("Boolean value '" + rightText + "'can not be math calculation.");
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
              return new OzElValue(OzElValue.SUPPORT_TYPE.DECIMAL, BigDecimal.class);
            } else if (leftSide.isLongValue() || rightSide.isLongValue()) {
              return new OzElValue(OzElValue.SUPPORT_TYPE.LONG, Long.class);
            } else if (leftSide.isIntegerValue() || rightSide.isIntegerValue()) {
              return new OzElValue(OzElValue.SUPPORT_TYPE.INTEGER, Integer.class);
            }
          } else if (ctx.op.getType() == OzElParser.SLASH) {
            if (leftSide.isDecimalCompatibleValue() || rightSide.isDecimalCompatibleValue()) {
              return new OzElValue(OzElValue.SUPPORT_TYPE.DECIMAL, BigDecimal.class);
            }
          } else if (ctx.op.getType() == OzElParser.MOD) {
            if (leftSide.isLongCompatibleValue()) {
              if (rightSide.isLongCompatibleValue()) {
                return new OzElValue(OzElValue.SUPPORT_TYPE.LONG, Long.class);
              } else {
                throw new NotSupportException("'" + rightText + "' - Long value needed.");
              }
            } else {
              throw new NotSupportException("'" + leftText + "' - Long value needed.");
            }
          } // end if-else

          throw new NotSupportException("Operation not support");

        } catch (Exception e) {
          throw new NotSupportException(e.getLocalizedMessage());
        } // end try-catch
      }   // end if
      else {
        throw new NotSupportException("'" + rightText + "' - Decimal value needed.");
      }   // end if-else
    } else { // end for
      throw new NotSupportException("'" + leftText + "' - Decimal value needed.");
    }     // end if-else
  }       // end method visitMulDiv

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitNewExpr(com.ozstrategy.el.antlr.OzElParser.NewExprContext)
   */
  @Override public OzElValue visitNewExpr(OzElParser.NewExprContext ctx) {
    if (ctx.INTEGER() != null) {
      return OzElValue.createOzElType(null, Integer.class);
    }

    if (ctx.LONG() != null) {
      return OzElValue.createOzElType(null, Long.class);
    }

    return super.visitNewExpr(ctx);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | NOT expression #NotExpression.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitNotExpression(OzElParser.NotExpressionContext ctx) {
    OzElValue rightSide = visit(ctx.expression());

    if (rightSide.isBooleanValue()) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.BOOLEAN, Boolean.class);
    }

    throw new NotSupportException();
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
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitOr(OzElParser.OrContext ctx) {
    OzElValue leftSide  = visit(ctx.expression(0));
    OzElValue rightSide = visit(ctx.expression(1));
    String    leftText  = ctx.expression(0).getText();
    String    rightText = ctx.expression(1).getText();

    if (leftSide.isBooleanValue()) {
      if ((rightSide == null) || rightSide.isNullValue() || rightSide.isBooleanValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.BOOLEAN, Boolean.class);
      } else {
        throw new NotSupportException("'" + rightText + "' - Needed to be Boolean value.");
      }
    } else {
      throw new NotSupportException("'" + leftText + "' - Needed to be Boolean value.");
    }
  }

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
      if (elValue.isIntegerValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.INTEGER, Integer.class);
      } else if (elValue.isLongValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.LONG, Long.class);
      } else if (elValue.isDecimalValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.DECIMAL, BigDecimal.class);
      }
    } // end if-else

    throw new NotSupportException("'" + variableName + "' - Needed to be Decimal/Long/Integer value.");
    // return original value
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitPrefixOp(com.ozstrategy.el.antlr.OzElParser.PrefixOpContext)
   */
  @Override public OzElValue visitPrefixOp(OzElParser.PrefixOpContext ctx) {
    String    variableName = ctx.variable().getText();
    OzElValue elValue      = resolvers.getValue(variableName);

    if (elValue.isDecimalCompatibleValue()) {
      if (elValue.isIntegerValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.INTEGER, Integer.class);
      } else if (elValue.isLongValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.LONG, Long.class);
      } else if (elValue.isDecimalValue()) {
        return new OzElValue(OzElValue.SUPPORT_TYPE.DECIMAL, BigDecimal.class);
      }
    } // end if-else

    throw new NotSupportException("'" + variableName + "' - Needed to be Decimal/Long/Integer value.");
    // return original value
  }

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
    if (ctx.expression() != null) {
      return visit(ctx.expression());
    } else if (ctx.block() != null) {
      return visit(ctx.block());
    } else if (ctx.programStatement() != null) {
      for (OzElParser.ProgramStatementContext programStatementContext : ctx.programStatement()) {
        return visit(programStatementContext);
      }
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
    return visit(ctx.expression()); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * | expression QUESTION expression COLON expression #Select.
   *
   * @param   ctx  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
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

    throw new NotSupportException();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementBlock(com.ozstrategy.el.antlr.OzElParser.StatementBlockContext)
   */
  @Override public OzElValue visitStatementBlock(OzElParser.StatementBlockContext ctx) {
    for (OzElParser.StatementContext statementContext : ctx.blockStatement().block().statement()) {
      visit(statementContext);
    }

    return super.visitStatementBlock(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementDo(com.ozstrategy.el.antlr.OzElParser.StatementDoContext)
   */
  @Override public OzElValue visitStatementDo(OzElParser.StatementDoContext ctx) {
    OzElValue loopValue = visit(ctx.parExpression());

    if (!loopValue.isBooleanValue()) {
      throw new NotSupportException("'" + ctx.parExpression().getText() + "' - Needed to be Boolean value.");
    }

    return visit(ctx.statement());
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
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  @Override public OzElValue visitStatementFor(OzElParser.StatementForContext ctx) {
    OzElParser.ForControlContext forControlContext = ctx.forControl();
    OzElParser.ForInitContext    forInitContext    = forControlContext.forInit();
    OzElParser.ExpressionContext expressionContext = forControlContext.expression();

    // For Init portion
    if (forInitContext != null) {
      visit(forInitContext);
    }

    // For expression portion
    if (expressionContext != null) {
      OzElValue loopValue = visit(expressionContext);

      if (!loopValue.isBooleanValue()) {
        throw new NotSupportException("'" + expressionContext.getText() + "' - Needed to be Boolean value.");
      }
    }

    return visit(ctx.statement());
  }

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

      break;
    }

    visit(ctx.statement());

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
   * @throws  com.ozstrategy.el.exception.NotSupportException  DOCUMENT ME!
   * @throws  NotSupportException                              DOCUMENT ME!
   */
  @Override public OzElValue visitStatementIf(OzElParser.StatementIfContext ctx) {
    OzElValue exprValue = visit(ctx.parExpression());

    if (exprValue.isBooleanValue()) {
      OzElValue retValue = visit(ctx.statement(0));

      if (ctx.statement().size() > 1) {
        retValue = visit(ctx.statement(1));
      }

      return retValue;
    }

    throw new NotSupportException();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitStatementReturn(com.ozstrategy.el.antlr.OzElParser.StatementReturnContext)
   */
  @Override public OzElValue visitStatementReturn(OzElParser.StatementReturnContext ctx) {
    return visit(ctx.returnStatement());
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
    OzElValue loopValue = visit(ctx.parExpression());

    if (!loopValue.isBooleanValue()) {
      throw new NotSupportException("'" + ctx.parExpression().getText() + "' - Needed to be Boolean value.");
    }

    return visit(ctx.statement());
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
    OzElValue rightSide = visit(ctx.expression());

    if (rightSide.isIntegerValue()) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.INTEGER, Integer.class);
    }

    if (rightSide.isLongValue()) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.LONG, Long.class);
    }

    if (rightSide.isDecimalValue()) {
      return new OzElValue(OzElValue.SUPPORT_TYPE.DECIMAL, BigDecimal.class);
    }

    throw new NotSupportException("Decimal Value Needed.");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.antlr.OzElBaseVisitor#visitVariable(com.ozstrategy.el.antlr.OzElParser.VariableContext)
   */
  @Override public OzElValue visitVariable(OzElParser.VariableContext ctx) {
    return super.visitVariable(ctx); // To change body of overridden methods use File | Settings | File Templates.
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   collection  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected String getStringArray(Collection<String> collection) {
    StringBuilder sb = new StringBuilder();

    for (String item : collection) {
      sb.append(", ").append(item);
    }

    return sb.substring(2);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private static <T> List<T> createListOfType(Class<T> type) {
    return new ArrayList<T>();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String logFunctions(String originalFunction, String cascadedInstance, String functionName, Method method,
    boolean isTop) {
    originalCascadedFunctions.add(originalFunction);

    if (cascadedInstance == null) {
      if (isTop && !usedFunctions.contains(functionName)) {
        usedFunctions.add(functionName);
      }

      return functionName + "()";
    } else {
      String cascadeFunction = cascadedInstance + "." + method.getName() + "()";

      if (isTop && !normalizedCascadedFunctions.contains(cascadeFunction)) {
        normalizedCascadedFunctions.add(cascadeFunction);
      }

      return cascadeFunction;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------
  private void addVariablePath(String variablePath){
    if(!variablePaths.contains(variablePath)){
      variablePaths.add(variablePath);
    }
  }

  private String logProperties(ParserRuleContext ctx, String cascadedInstance, String propertyName, Method method,
    boolean isTop) {
    originalCascadedVariables.add(ctx.getText());

    if (cascadedInstance == null) {
      if (isTop && !usedVariables.contains(propertyName)) {
        usedVariables.add(propertyName);
      }

      return propertyName;
    } else if(method != null){
      String cascadedVariable = cascadedInstance + "." + method.getName() + "()";

      if (isTop && !normalizedCascadedVariables.contains(cascadedVariable)) {
        normalizedCascadedVariables.add(cascadedVariable);
      }

      if(method.getParameterCount() == 0){
        String variableName = method.getName();
        if(variableName.startsWith("get")){
          variableName = variableName.substring(3,4).toLowerCase() + variableName.substring(4);
        }
        else if(variableName.startsWith("is")){
          variableName = variableName.substring(2,3).toLowerCase() + variableName.substring(3);
        }

        addVariablePath(cascadedInstance + "." + variableName);
      }

      return cascadedVariable;
    }

    return null;
  }

  private void logCallPaths(ParserRuleContext ctx, String cascadedInstance, String propertyName, Method method) {
    logger.debug("Log Call Paths...");
    String path = cascadedInstance + ".";

    if ((method != null) && (method.getParameterCount() == 0)) {
      String variableName = method.getName();
      if (variableName.startsWith("get")) {
        variableName = variableName.substring(3, 4).toLowerCase() + variableName.substring(4);
      } else if (variableName.startsWith("is")) {
        variableName = variableName.substring(2, 3).toLowerCase() + variableName.substring(3);
      }

      path = cascadedInstance + "." + variableName;

      callPaths.put(ctx, path);
    }
  }

  private void logCallPaths(ParserRuleContext baseCtx, String instance, Method method) {
    if ((method != null) && (method.getParameterCount() == 0)) {
      String variableName = method.getName();
      if (variableName.startsWith("get")) {
        variableName = variableName.substring(3, 4).toLowerCase() + variableName.substring(4);
      } else if (variableName.startsWith("is")) {
        variableName = variableName.substring(2, 3).toLowerCase() + variableName.substring(3);
      }

      callPaths.put(baseCtx, instance + "." + variableName);
    }
  }

  private void logCallPaths(ParserRuleContext baseCtx, ParserRuleContext childCtx, String instance, Method method) {
    String currentPath = callPaths.get(childCtx);
    if (currentPath == null) {
      currentPath = instance;
    }
    if ((method != null) && (method.getParameterCount() == 0)) {
      String variableName = method.getName();
      if (variableName.startsWith("get")) {
        variableName = variableName.substring(3, 4).toLowerCase() + variableName.substring(4);
      } else if (variableName.startsWith("is")) {
        variableName = variableName.substring(2, 3).toLowerCase() + variableName.substring(3);
      }

      if(currentPath == null){
        ParserRuleContext forCtx = forContexts.get(childCtx.getText()) ;
        if(forCtx != null){
          currentPath = callPaths.get(forCtx);
        }
      }
      if(currentPath != null) {
        callPaths.put(baseCtx, currentPath + "." + variableName);
      }
    }
  }

  private void logCallPaths(ParserRuleContext baseCtx, ParserRuleContext childCtx, String pathAppended) {
    String currentPath = callPaths.get(childCtx);
    if(currentPath != null){
      callPaths.put(baseCtx, currentPath + pathAppended);
    }
  }
} // end class VerifyVisitor
