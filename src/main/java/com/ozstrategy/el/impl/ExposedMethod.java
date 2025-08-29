package com.ozstrategy.el.impl;

/**
 * Created with IntelliJ IDEA. User: rojer Date: 5/22/14 Time: 2:37 AM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class ExposedMethod {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** Class name of the method. */
  protected Class methodClass;

  /** Name of the method. */
  protected String methodName;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ExposedMethod object.
   *
   * @param  methodName   DOCUMENT ME!
   * @param  methodClass  DOCUMENT ME!
   */
  public ExposedMethod(String methodName, Class methodClass) {
    this.methodName  = methodName;
    this.methodClass = methodClass;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Class getMethodClass() {
    return methodClass;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getMethodName() {
    return methodName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------
  /**
   * DOCUMENT ME!
   *
   * @param  methodClass  DOCUMENT ME!
   */
  public void setMethodClass(Class methodClass) {
    this.methodClass = methodClass;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  methodName  DOCUMENT ME!
   */
  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }
} // end class ExposedMethod
