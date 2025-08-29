package com.ozstrategy.strategy;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer
 * @version  $Revision$, $Date$
 */
public interface PreProcessor {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Pre-process expression.
   *
   * @param   content  to be processed
   *
   * @return  processed result
   */
  String process(String content);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Pre-process with additional mapping process and replace.
   *
   * @param   content          to be process
   * @param   spaceReplace     replace white space
   * @param   quoteReplace     Quote replace
   * @param   operatorReplace  operator replace
   *
   * @return  DOCUMENT ME!
   */
  String process(String content, boolean spaceReplace, boolean quoteReplace, boolean operatorReplace);
} // end interface PreProcessor
