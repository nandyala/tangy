package com.ozstrategy.el.exception;


import com.ozstrategy.el.impl.OzElValue;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class ReturnException extends RuntimeException {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  OzElValue retValue = null;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new NotSupportException object.
   *
   * @param  retValue  DOCUMENT ME!
   */
  public ReturnException(OzElValue retValue) {
    this.retValue = retValue;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public OzElValue getRetValue() {
    return retValue;
  }
} // end class ReturnException
