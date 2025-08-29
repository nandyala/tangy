package com.ozstrategy.el.impl;

/**
 * Created by rojer on 14-9-9.
 */
public class RuntimeMethod extends ExposedMethod {
  protected String instanceName;

  /**
   * Creates a new ExposedMethod object.
   *
   * @param methodName  DOCUMENT ME!
   * @param methodClass DOCUMENT ME!
   */
  public RuntimeMethod(String instanceName, String methodName, Class methodClass) {
    super(methodName, methodClass);
    this.instanceName = instanceName;
  }

  public String getInstanceName() {
    return instanceName;
  }

  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }
}
