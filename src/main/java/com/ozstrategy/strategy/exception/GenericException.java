package com.ozstrategy.strategy.exception;

/**
 * Created by IntelliJ IDEA. User: rojer Date: Mar 17, 2010 Time: 2:40:47 PM To change this template use File | Settings
 * | File Templates.
 */
public class GenericException extends RuntimeException {
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be
   * initialized by a call to {@link #initCause}.
   *
   * @param  message  the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
   *                  method.
   */
  public GenericException(String message) {
    super(message);
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getOriginalMessage() {
    return super.getMessage();
  }
} // end class GenericException
