package com.ozstrategy.el.antlr;

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozstrategy.el.exception.SyntaxErrorException;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/25/13 Time: 6:38 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class OzVerboseListener extends BaseErrorListener {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   recognizer          DOCUMENT ME!
   * @param   offendingSymbol     DOCUMENT ME!
   * @param   line                DOCUMENT ME!
   * @param   charPositionInLine  DOCUMENT ME!
   * @param   msg                 DOCUMENT ME!
   * @param   e                   DOCUMENT ME!
   *
   * @throws  SyntaxErrorException  DOCUMENT ME!
   */
  @Override public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
    int line, int charPositionInLine, String msg,
    RecognitionException e) {
    if (recognizer instanceof Parser) {
      List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
      Collections.reverse(stack);

      if (logger.isDebugEnabled()) {
        logger.debug("rule stack: " + stack);
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("line " + line + ":" + charPositionInLine + " at "
        + offendingSymbol + ": " + msg);
    }

    throw new SyntaxErrorException("line " + line + ":" + charPositionInLine + ": " + msg);
  }
} // end class OzVerboseListener
