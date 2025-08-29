package com.ozstrategy.strategy.el.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozstrategy.el.impl.ResolverContext;

import com.ozstrategy.strategy.el.ElService;
import com.ozstrategy.strategy.exception.GenericException;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 7/2/13 Time: 9:42 PM To change this template use File | Settings | File
 * Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class ElServiceImpl implements ElService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.strategy.el.ElService#detectReturnType(java.lang.Object, java.lang.Object)
   */
  @Override public Class detectReturnType(Object expression, Object ctx) throws GenericException {
    if (logger.isDebugEnabled()) {
      logger.debug("Verify Expression:" + expression);
    }

    ResolverContext context = new ResolverContext();
    context.addValues((Map) ctx);

    return context.detectReturnType((String) expression);
  } // end method detectReturnType

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.strategy.el.ElService#eval(java.lang.Object, java.lang.Object)
   */
  @Override public Object eval(Object expression, Object vars) throws GenericException {
    if (logger.isDebugEnabled()) {
      logger.debug("Eval Expression : '" + expression + "' ......");
    }

    ResolverContext context = new ResolverContext();
    context.addValues((Map) vars);

    return context.eval((String) expression);
  } // end method eval

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.strategy.el.ElService#eval(java.lang.Object, java.lang.Object, java.lang.Class)
   */
  @Override public <T> T eval(Object expression, Object vars, Class<T> toType) throws GenericException {
    return (T) eval(expression, vars);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.strategy.el.ElService#verify(java.lang.Object, java.lang.Object)
   */
  @Override public void verify(Object expression, Object ctx) throws GenericException {
    if (logger.isDebugEnabled()) {
      logger.debug("Verify Expression:" + expression);
    }

    ResolverContext context = new ResolverContext();
    context.addValues((Map) ctx);

    context.verify((String) expression);
  } // end method verify
} // end class ElServiceImpl
