package com.ozstrategy.strategy.exception;


/**
 * Created by IntelliJ IDEA. User: rojer Date: Mar 18, 2010 Time: 9:43:01 PM To change this template use File | Settings
 * | File Templates.
 */
public class EmptyExpressionException extends GenericException {
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be
   * initialized by a call to {@link #initCause}.
   */
  public EmptyExpressionException() {
    super("emptyExpression");
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Returns the detail message string of this throwable.
   *
   * @return  the detail message string of this <tt>Throwable</tt> instance (which may be <tt>null</tt>).
   */
  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
