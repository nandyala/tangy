package com.ozstrategy.el.util;

import java.util.HashSet;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/21/13 Time: 10:39 AM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class NoCaseHashSet extends HashSet<String> {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   string  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public boolean add(String string) {
    return super.add(string.toLowerCase());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.util.HashSet#contains(java.lang.Object)
   */
  @Override public boolean contains(Object o) {
    String strObj = (String) o;

    return super.contains(strObj.toLowerCase());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.util.HashSet#remove(java.lang.Object)
   */
  @Override public boolean remove(Object o) {
    String strObj = (String) o;

    return super.remove(strObj.toLowerCase());
  }
} // end class NoCaseHashSet
