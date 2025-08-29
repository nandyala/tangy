package com.ozstrategy.strategy.exception;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.StringUtils;


/**
 * Created by IntelliJ IDEA. User: rojer Date: Mar 18, 2010 Time: 9:43:01 PM To change this template use File | Settings
 * | File Templates.
 */
public class InvalidExpressionException extends GenericException {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  protected Set<String> expressions = new LinkedHashSet<String>();

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be
   * initialized by a call to {@link #initCause}.
   *
   * @param  message  the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
   *                  method.
   */
  public InvalidExpressionException(String message) {
    super(message);
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  expression  DOCUMENT ME!
   */
  public void addInvalidExpression(String expression) {
    expressions.add(expression);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Integer getCount() {
    return expressions.size();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getInvalidExpressions() {
    return StringUtils.collectionToCommaDelimitedString(expressions);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Set<String> getInvalidExpressionSet() {
    return expressions;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns the detail message string of this throwable.
   *
   * @return  the detail message string of this <tt>Throwable</tt> instance (which may be <tt>null</tt>).
   */
  @Override
  public String getMessage() {
    if (getCount() > 0) {
      return "Invalid Expressions: " + getInvalidExpressions();
    }

    return super.getMessage();
  }
} // end class InvalidExpressionException
