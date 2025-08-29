package com.ozstrategy.strategy.el;

import com.ozstrategy.strategy.exception.GenericException;


/**
 * Created by IntelliJ IDEA. User: rojer Date: Mar 17, 2010 Time: 12:36:11 PM To change this template use File |
 * Settings | File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public interface ElService {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Detect expression result type.
   *
   * @param   expression  to detect return
   * @param   ctx         the context which the expression to be detect
   *
   * @return  expression return class type
   *
   * @throws  GenericException  GenericException DOCUMENT ME!
   */
  Class detectReturnType(Object expression, Object ctx) throws GenericException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Evaluate the expression.
   *
   * @param   expression  to evaluate
   * @param   vars        DOCUMENT ME!
   *
   * @return  evaluate result
   *
   * @throws  GenericException  any exception happens during evaluate
   */
  Object eval(Object expression, Object vars) throws GenericException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Evaluate the expression in the class type.
   *
   * @param   <T>         DOCUMENT ME!
   * @param   expression  to evaluate
   * @param   vars        DOCUMENT ME!
   * @param   toType      aClass T the class type
   *
   * @return  evaluate result in class type
   *
   * @throws  GenericException  any exception happens
   */
  <T> T eval(Object expression, Object vars, Class<T> toType) throws GenericException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Verify the syntax for the expression.
   *
   * @param   expression  for verify
   * @param   ctx         the context which expression verify against
   *
   * @throws  GenericException  any syntax error happens
   */
  void verify(Object expression, Object ctx) throws GenericException;
} // end interface ElService
