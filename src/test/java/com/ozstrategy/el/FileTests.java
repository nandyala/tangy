/**
 *
 */
package com.ozstrategy.el;

import com.ozstrategy.el.antlr.OzElLexer;
import com.ozstrategy.el.antlr.OzElParser;
import com.ozstrategy.el.antlr.OzVerboseListener;
import com.ozstrategy.el.impl.EvalVisitor;
import com.ozstrategy.el.impl.OzElValue;
import com.ozstrategy.el.impl.ResolverContext;
import com.ozstrategy.el.model.Account;
import com.ozstrategy.el.model.Responsible;
import com.ozstrategy.el.util.CalculateUtil;
import junit.framework.TestCase;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * DOCUMENT ME!
 *
 * @author   Rojer
 * @version  $Revision$, $Date$
 */
public class FileTests extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  private ResolverContext resolver = null;

  private StopWatch stopWatch = new StopWatch();

  /** DOCUMENT ME! */
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testFromFile() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Positive Cases Started....");
    }

    String fileName = "expression1.txt";

    ClassLoader classLoader = getClass().getClassLoader();
    String syntaxValue = IOUtils.toString(classLoader.getResourceAsStream(fileName));

    if (StringUtils.hasText(syntaxValue)) {
      Boolean result = runParserTest(syntaxValue);
    }

    fileName = "expression2.txt";

    syntaxValue = IOUtils.toString(classLoader.getResourceAsStream(fileName));

    if (StringUtils.hasText(syntaxValue)) {
      Boolean result = runParserTest(syntaxValue);
    }

    fileName = "expression3.txt";

    syntaxValue = IOUtils.toString(classLoader.getResourceAsStream(fileName));

    if (StringUtils.hasText(syntaxValue)) {
      Boolean result = runParserTest(syntaxValue);
    }
  }


  //~ ------------------------------------------------------------------------------------------------------------------

  protected boolean runParserTest(String expr) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Parsing Expression : '" + expr + "' ......");
    }
    stopWatch.reset();
    stopWatch.start();

    CharStream        input  = new ANTLRInputStream(expr);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());


    boolean     testPass = false;
    EvalVisitor visitor  = new EvalVisitor();

    if (resolver != null) {
      visitor.setResolvers(resolver);
    }

    try {
      ParseTree tree = parser.entry(); // parse

      OzElValue retValue = visitor.visit(tree);

      stopWatch.stop();

      assertNotNull(retValue);
      logger.info("'" + expr + "' --> " + retValue.getValue());
      logger.info("Stopwatch time: " + stopWatch);
      testPass = true;
    } catch (RuntimeException e) {
      if (logger.isDebugEnabled()) {
        logger.debug(e.toString());
// e.printStackTrace();
      }
    } catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug(e.toString());
// e.printStackTrace();
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug(testPass ? "Passed.\n" : "Failed.\n");
    }

    return testPass;
  } // end method runParserTest

  //~ ------------------------------------------------------------------------------------------------------------------

  public void setUp() throws Exception {
    resolver = new ResolverContext();
    resolver.initWithCalculateUtil();

    resolver.addDefinition("balance", "account.balance", BigDecimal.class);
    resolver.addDefinition("pastDue", "account.pastDue", BigDecimal.class);
    resolver.addValue("percentage", new BigDecimal("0.10"));
    resolver.addValue("accountDays", 99);
    resolver.addValue("accountStatus", "balance and test");
    resolver.addValue("hasPayment", true);
    resolver.addValue("isOldAccount", false);
    resolver.addValue("originalAccountNumber", "1135218");
//    resolver.addValue("today", new Date());
    resolver.addValue("responsible", new Responsible());

    Responsible responsible1 = new Responsible(40, CalculateUtil.toDate("1973-01-01"), "Mike", "Mike", "AAAA");
    Responsible responsible2 = new Responsible(37, CalculateUtil.toDate("1976-06-01"), "Jean", "Jean", "BBBB");
    Account     account      = new Account();
    account.addResponsible(responsible1);
    account.addResponsible(responsible2);
    resolver.addValue("account", account);
  }
}
