package com.ozstrategy.el.util;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 5/28/13 Time: 4:48 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class MethodStub {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Object classInstance  = null;
  private Class  classReference;

  private Method method = null;
  private String name;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new MethodStub object.
   */
  public MethodStub() { }

  /**
   * Creates a new MethodStub object.
   *
   * @param  method  DOCUMENT ME!
   */
  public MethodStub(Method method) {
    this.classReference = method.getDeclaringClass();
    this.name           = method.getName();
    this.method         = method;
  }

  /**
   * Creates a new MethodStub object.
   *
   * @param  classReference  DOCUMENT ME!
   * @param  methodName      DOCUMENT ME!
   */
  public MethodStub(Class classReference, String methodName) {
    this.classReference = classReference;
    this.name           = methodName;
  }

  /**
   * Creates a new MethodStub object.
   *
   * @param  classInstance  DOCUMENT ME!
   * @param  methodName     DOCUMENT ME!
   */
  public MethodStub(Object classInstance, String methodName) {
    this.classInstance  = classInstance;
    this.classReference = classInstance.getClass();
    this.name           = methodName;
  }

  /**
   * Creates a new MethodStub object.
   *
   * @param  classInstance  DOCUMENT ME!
   * @param  method         DOCUMENT ME!
   */
  public MethodStub(Object classInstance, Method method) {
    this.classInstance  = classInstance;
    this.classReference = classInstance.getClass();
    this.name           = method.getName();
    this.method         = method;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   parameters  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  IllegalAccessException     DOCUMENT ME!
   * @throws  InvocationTargetException  DOCUMENT ME!
   */
  public Object call(Object[] parameters) throws IllegalAccessException, InvocationTargetException {
    return method.invoke(classInstance, parameters);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Object getClassInstance() {
    return classInstance;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Class getClassReference() {
    return classReference;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Method getMethod() {
    if (method == null) {
      for (Method method : classReference.getMethods()) {
        if (name.equals(method.getName())) {
          return this.method = method;
        }
      }
    }

    return method;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * @param  classInstance  DOCUMENT ME!
   */
  public void setClassInstance(Object classInstance) {
    this.classInstance = classInstance;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  classReference  DOCUMENT ME!
   */
  public void setClassReference(Class classReference) {
    this.classReference = classReference;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  method  DOCUMENT ME!
   */
  public void setMethod(Method method) {
    this.method = method;
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
} // end class MethodStub
