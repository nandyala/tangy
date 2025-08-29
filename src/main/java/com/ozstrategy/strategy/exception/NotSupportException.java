package com.ozstrategy.strategy.exception;

/**
 * Created by IntelliJ IDEA. User: rojer Date: Mar 30, 2010 Time: 11:54:40 AM To change this template use File |
 * Settings | File Templates.
 */
public class NotSupportException extends GenericException {
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be
   * initialized by a call to {@link #initCause}.
   *
   * @param  message  the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
   *                  method.
   */
  public NotSupportException(String message) {
    super(message);
  }
}
