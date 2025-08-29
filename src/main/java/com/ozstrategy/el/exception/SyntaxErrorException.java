package com.ozstrategy.el.exception;

/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/26/13 Time: 2:36 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class SyntaxErrorException extends RuntimeException {
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new SyntaxErrorException object.
   */
  public SyntaxErrorException() {
    super();
  }

  /**
   * Creates a new SyntaxErrorException object.
   *
   * @param  message  DOCUMENT ME!
   */
  public SyntaxErrorException(String message) {
    super(message);
  }
}
