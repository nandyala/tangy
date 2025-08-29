package com.ozstrategy.el.exception;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class NotSupportException extends RuntimeException {
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new NotSupportException object.
   */
  public NotSupportException() {
    super("Not Supported");
  }

  /**
   * Creates a new NotSupportException object.
   *
   * @param  reason  DOCUMENT ME!
   */
  public NotSupportException(String reason) {
    super(reason);
  }
}
