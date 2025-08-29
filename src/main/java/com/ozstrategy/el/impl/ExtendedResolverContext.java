package com.ozstrategy.el.impl;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by rojer on 16/4/22.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class ExtendedResolverContext extends ResolverContext {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected ResolverContext parentContext = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.impl.ResolverContext#getNames()
   */
  @Override public Set<String> getNames() {
    Set<String> names = new HashSet<String>();
    names.addAll(super.getNames());

    if (parentContext != null) {
      names.addAll(parentContext.getNames());
    }

    return names;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.impl.ResolverContext#getValue(java.lang.String)
   */
  @Override public OzElValue getValue(String name) {
    OzElValue retValue = super.getValue(name);

    if ((retValue == null) && (parentContext != null)) {
      retValue = parentContext.getValue(name);
    }

    return retValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.impl.ResolverContext#getValueType(java.lang.String)
   */
  @Override public OzElValue getValueType(String name) {
    OzElValue retValue = super.getValueType(name);

    if ((retValue == null) && (parentContext != null)) {
      retValue = parentContext.getValueType(name);
    }

    return retValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ozstrategy.el.impl.ResolverContext#getContext(java.lang.String)
   */
  @Override protected OzElContext getContext(String name) {
    OzElContext retContext = super.getContext(name);

    if ((retContext == null) && (parentContext != null)) {
      retContext = parentContext.getContext(name);
    }

    return retContext;
  }
} // end class ExtendedResolverContext
