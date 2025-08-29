package com.ozstrategy.el.impl;

/**
 * Created with IntelliJ IDEA. User: rojer Date: 11/13/13 Time: 2:47 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class OzElContext {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected String name;

  /** DOCUMENT ME! */
  protected OzElValue ozElType;

  /** DOCUMENT ME! */
  protected OzElValue ozElValue;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new OzElContext object.
   *
   * @param  name       DOCUMENT ME!
   * @param  ozElValue  DOCUMENT ME!
   */
  public OzElContext(String name, OzElValue ozElValue) {
    this.name      = name;
    this.ozElValue = ozElValue;

    if (!ozElValue.isExpressionValue()) {
      this.ozElType = ozElValue;
    }
  }

  /**
   * Creates a new OzElContext object.
   *
   * @param  name        DOCUMENT ME!
   * @param  ozElValue   DOCUMENT ME!
   * @param  resultType  DOCUMENT ME!
   */
  public OzElContext(String name, OzElValue ozElValue, Class resultType) {
    this.name      = name;
    this.ozElType  = OzElValue.createOzElType(name, resultType);

    this.ozElValue = ozElValue;
  }

  /**
   * Creates a new OzElContext object.
   *
   * @param  name        DOCUMENT ME!
   * @param  expression  DOCUMENT ME!
   * @param  resultType  DOCUMENT ME!
   * @param  isFunction  DOCUMENT ME!
   */
  public OzElContext(String name, String expression, Class resultType, boolean isFunction) {
    this.name     = name;
    this.ozElType = OzElValue.createOzElType(name, resultType);

    if (!isFunction) {
      this.ozElValue = OzElValue.createExpression(name, expression);
    } else {
      this.ozElValue = OzElValue.createFunction(name, expression);
    }
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getName() {
    return name;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public OzElValue getOzElType() {
    return ozElType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public OzElValue getOzElValue() {
    return ozElValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Object getValue() {
    return (ozElValue == null) ? null : ozElValue.getValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  name  DOCUMENT ME!
   */
  public void setName(String name) {
    this.name = name;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  ozElValue  DOCUMENT ME!
   */
  public void updateValue(OzElValue ozElValue) {
    this.ozElValue = ozElValue;

    if (!ozElValue.isExpressionValue()) {
      this.ozElType = ozElValue;
    }
  }
} // end class OzElContext
