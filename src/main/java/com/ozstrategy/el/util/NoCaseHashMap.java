package com.ozstrategy.el.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 5/28/13 Time: 3:51 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class NoCaseHashMap<T> extends ConcurrentHashMap<String, T> {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  java.util.HashMap#containsKey(java.lang.Object)
   */
  @Override public boolean containsKey(Object key) {
    String strKey = (String) key;

    return super.containsKey(strKey.toLowerCase());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.util.HashMap#get(java.lang.Object)
   */
  @Override public T get(Object key) {
    String strKey = (String) key;

    return super.get(strKey.toLowerCase());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   *
   * DOCUMENT ME!
   *
   * @param   key    DOCUMENT ME!
   * @param   value  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public T put(String key, T value) {
    return super.put(key.toLowerCase(), value);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   *
   * DOCUMENT ME!
   *
   * @param  m  DOCUMENT ME!
   */
  @Override public void putAll(Map<? extends String, ? extends T> m) {
    for (Map.Entry entry : m.entrySet()) {
      put((String) entry.getKey(), (T) entry.getValue());
    }

    super.putAll(m);
  }
} // end class NoCaseHashMap
