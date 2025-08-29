package com.ozstrategy.el;

import com.ozstrategy.el.impl.ResolverContext;
import com.ozstrategy.el.model.Account;
import com.ozstrategy.el.model.AccountDetail;
import com.ozstrategy.el.model.Function;
import com.ozstrategy.el.model.Responsible;
import com.ozstrategy.el.util.CalculateUtil;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 5/24/13 Time: 4:36 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class EvalTest extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  ResolverContext context = null;
  private Logger  logger  = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  junit.framework.TestCase#setUp()
   */
  @Override public void setUp() throws Exception {
    context = new ResolverContext();

    // add method
    context.addExposedClass(CalculateUtil.class, false);
    context.addExposedClass(Function.class, true);

    context.enableCache(1);

    Responsible responsible1 = new Responsible(40, CalculateUtil.toDate("1973-01-01"), "Mike", "Mike", "AAAA");
    Responsible responsible2 = new Responsible(37, CalculateUtil.toDate("1976-06-01"), "Jean", "Jean", "BBBB");
    Account     account      = new Account();
    account.addResponsible(responsible1);
    account.addResponsible(responsible2);
    context.addValue("account", account);

// context.addDefinition("today", "today()", Date.class);

    context.addDefinition("balance", "account.balance", BigDecimal.class);
    context.addDefinition("accountDate", "account.getaccountDate()", Date.class);
    context.addDefinition("accountDetail", "account.accountDetail.accountDetailCode", String.class);
    context.addDefinition("accountAge", "account.accountDetail.accountAge", Integer.class);
    context.addDefinition("accountAge2", "account.AccountDetail.getAccountAge()", Integer.class);
    context.addDefinition("accountAge3", "account.getAccountDetail().getAccountAge()", Integer.class);
    context.addDefinition("accountIndexCode", "account.getAccountIndex().getAccountIndexCode()", Integer.class);
// context.addDefinition("hasStatus", "account.hasState(status)", Boolean.class);

    context.addValue("CommonFunction", new CalculateUtil());
    context.addFunction("substr", "substring(str, startIndex, endIndex)", String.class);
    context.addFunction("range", "CommonFunction.substring(str, startIndex, endIndex)", String.class);

    context.addFunction("myHasState", "Function.hasState(state)", Boolean.class);
    context.addFunction("hasStatus", "account.hasStatus(status)", Boolean.class);
    context.addFunction("countResponsible", "Function.countResponsible()", int.class);
    context.addFunction("countResponsibleWithDummy", "Function.countResponsibleWithDummy()", int.class);
    context.addFunction("countResponsibleWith2Dummy", "Function.countResponsibleWith2Dummy()", int.class);
    context.addFunction("getAccountDetail", "account.getAccountDetail()", AccountDetail.class);

    context.addFunction("calcXIRR", "Function.calcXIRR()", Boolean.class);
  } // end method setUp

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */

  public void testAnnotation() throws Exception {
    assertEquals(2, context.eval("countResponsible()"));
    assertEquals(2, context.eval("countResponsibleWithResponsible()"));
    assertEquals(2, context.eval("countResponsibleWithDummy(\"test\")"));
    assertEquals(2, context.eval("countResponsibleWith2Dummy(12345, \"test2\")"));
    assertEquals(2, context.eval("countResponsibleWithResponsibleAnd2Dummy(12345, \"test2\")"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Test Cache.
   *
   * @throws  Exception
   */
  public void testCache() throws Exception {
    context.enableCache(1);
    assertEquals(new BigDecimal("999.99"), context.eval("balance"));

    // Should load from cache
    assertEquals(new BigDecimal("999.99"), context.eval("balance"));
    assertEquals(new BigDecimal("999.99"), context.eval("account.balance"));

    // sleep to make the cache invalid
    Thread.sleep(5000);
    assertEquals(new BigDecimal("999.99"), context.eval("balance"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testCallFunctionWithArray() throws Exception {
    Object        result = null;
    StringBuilder sb     = new StringBuilder();

    sb.append("loanAmount := -10000;").append("\n");
    sb.append("loanStartDate := createDate(2008, 1, 1);").append("\n");

    result = context.eval(sb.toString() + ";loanStartDate;");
    assertNotNull(result);

    sb.append("amounts := [2750, 4250, 3250, 2750];").append("\n");

    result = context.eval(sb.toString() + ";amounts;");
    assertNotNull(result);

    sb.append(
      "dates := [createDate(2008, 2, 1), createDate(2008, 9, 30), createDate(2009, 1, 15), createDate(2009, 3, 1)];")
      .append("\n");

    result = context.eval(sb.toString() + ";dates;");
    assertNotNull(result);

    sb.append("ret := calcXIRR(loanAmount, loanStartDate, dates, amounts, 0.1);").append("\n");

    result = context.eval(sb.toString() + ";ret;");
    assertNotNull(result);
  } // end method testCallFunctionWithArray

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testDetectReturn() throws Exception {
    assertEquals(BigDecimal.class, context.detectReturnType("account.balance"));
    assertEquals(BigDecimal.class, context.detectReturnType("account.balance + account.pastDue"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testEval() throws Exception {
    logger.info("Create Date:" + context.eval("createDate(2014,5,20)"));
    logger.info("Create Date:" + context.eval("createDate(2014,5 + 1,20)"));
    logger.info("today:" + context.eval("today"));
    logger.info("today():" + context.eval("today()"));
    logger.info("accountDate:" + context.eval("accountDate"));

    assertNotNull(context.eval("1.0/3.0"));
    assertNotNull(context.eval("1.0/3"));
    assertNotNull(context.eval("1/3"));

//
    assertEquals("23", context.eval("substr(\"12345\",2,3)"));
    assertEquals(true, context.eval("range(\"12345\",2,3) = \"23\""));

    assertEquals(true, context.eval("\"ABC\" = \"abc\""));
    assertEquals(true, context.eval("accountDate = today"));
    assertEquals(true, context.eval("accountDate = today()"));
    assertEquals(false, context.eval("accountDate > today()"));
    assertEquals(false, context.eval("accountDate < today()"));
    assertEquals(false, context.eval("accountAge > 100"));
    assertEquals(false, context.eval("accountAge> 100"));
    assertEquals(false, context.eval("accountAge >100"));
    assertEquals(true, context.eval("accountAge2 < 100"));
    assertEquals(true, context.eval("accountAge2< 100"));
    assertEquals(false, context.eval("accountAge3 > 100 "));
    assertEquals("Detail", context.eval("accountDetail"));

    assertEquals(new BigDecimal("999.99"), context.eval("balance"));
    assertEquals(true, context.eval("balance = 999.99"));

// assertEquals(true, context.eval("balance = \"999.99\""));
    assertEquals(true, context.eval("(balance = 999.99) = true"));
    assertEquals(true, context.eval("(balance = 999.99) = \"true\""));
    assertEquals(new BigDecimal("999.99"), context.eval("account.balance"));
    assertEquals(false, context.eval("account.balance > 1000"));
    assertEquals(false, context.eval("balance > 1000"));
    assertEquals("Detail", context.eval("account.accountDetail.accountDetailCode"));
    assertEquals("Detail", context.eval("account.getAccountDetail().getAccountDetailCode()"));

// test load from cache
    assertEquals(new BigDecimal("999.99"), context.eval("balance"));
    assertEquals(new BigDecimal("999.99"), context.eval("account.balance"));

    assertEquals(Boolean.TRUE, context.eval("hasState(\"DE\", \"CA\")"));
    assertEquals(Boolean.TRUE, context.eval("hasState(\"DE\")"));
    assertEquals(Boolean.FALSE, context.eval("hasState(\"CA\")"));

    assertEquals(Boolean.TRUE, context.eval("hasStatus(\"Active\")"));
    assertEquals(Boolean.FALSE, context.eval("hasStatus(\"NA\")"));

// assertEquals(Boolean.FALSE, context.eval("isSunday(today())"));
// assertEquals(Boolean.FALSE, context.eval("isSunday(today)"));

    assertEquals("AbcDef", context.eval("\"Abc\" + \"Def\""));

    assertEquals(Boolean.TRUE, context.eval("true and true and 'true' and \"true\""));
    assertEquals(Boolean.FALSE, context.eval("false and \"true\" and false and true and true"));
    assertEquals(Boolean.FALSE, context.eval("true and true and true and false"));
    assertEquals(Boolean.TRUE, context.eval("true or true or true or false"));
    assertEquals(Boolean.TRUE, context.eval("false or true or true or false"));
    assertEquals(Boolean.TRUE, context.eval("false or false or true or false"));

    List<Object> results = context.eval(new String[] { "account.balance > 1000", "balance > 1000", "balance" });
    assertEquals(Boolean.FALSE, results.get(0));
    assertEquals(Boolean.FALSE, results.get(1));
    assertEquals(new BigDecimal("999.99"), results.get(2));

    logger.info("today:" + context.eval("today"));

    context.addValue("y", null);
    Object value = context.eval("inList(y, 'b', 'a');");
    assertEquals(false, value);
  } // end method testEval

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testEvalCast() throws Exception {
    assertEquals(new Integer(99), context.eval("99"));
    assertEquals(new Long(99), context.eval("99", Long.class));
    assertEquals(new BigDecimal("99.9"), context.eval("99.9", BigDecimal.class));
    assertEquals(new BigDecimal("99"), context.eval("99", BigDecimal.class));
    assertEquals("99", context.eval("99", String.class));
  }

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testStringCompare() throws Exception {
    assertEquals(Boolean.TRUE, context.eval("'1' < '2'"));
    assertEquals(Boolean.TRUE, context.eval("1 < 5"));
    assertEquals(Boolean.TRUE, context.eval("'1' < '5'"));
    assertEquals(Boolean.TRUE, context.eval("'11' < '5'"));
    assertEquals(Boolean.TRUE, context.eval("'5' > '1'"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testFunction.
   *
   * @throws  Exception  exception
   */
  public void testFunction() throws Exception {
    assertEquals("Detail", context.eval("getAccountDetail().getAccountDetailCode()"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testInArray.
   *
   * @throws  Exception  exception
   */
  public void testInArray() throws Exception {
    StopWatch stopWatch = new StopWatch();

    for (int i = 0; i < 1; i++) {
      // test
      int    count    = 5000;
      String elString = generateExpr(count, false);
      logger.info(elString);

      stopWatch.start("Array of " + count + " Long(" + i + ")");
      assertEquals(true, context.eval(elString));
      stopWatch.stop();

      elString = generateExpr(count, true);

      if (logger.isDebugEnabled()) {
        logger.debug(elString);
      }

      stopWatch.start("Array of " + count + " String(" + i + ")");
      assertEquals(true, context.eval(elString));
      stopWatch.stop();

      count    = 10000;
      elString = generateExpr(count, false);
      logger.info(elString);

      stopWatch.start("Array of " + count + " Long(" + i + ")");
      assertEquals(true, context.eval(elString));
      stopWatch.stop();

      elString = generateExpr(count, true);

      if (logger.isDebugEnabled()) {
        logger.debug(elString);
      }

      stopWatch.start("Array of " + count + " String(" + i + ")");
      assertEquals(true, context.eval(elString));
      stopWatch.stop();

      count    = 20000;
      elString = generateExpr(count, false);
      logger.info(elString);

      stopWatch.start("Array of " + count + " Long(" + i + ")");
      assertEquals(true, context.eval(elString));
      stopWatch.stop();

      elString = generateExpr(count, true);

      if (logger.isDebugEnabled()) {
        logger.debug(elString);
      }

      stopWatch.start("Array of " + count + " String(" + i + ")");
      assertEquals(true, context.eval(elString));
      stopWatch.stop();
    } // end for

    logger.info(stopWatch.prettyPrint());
  } // end method testInArray

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testNormalize() throws Exception {
    verifyNormalize("balance", "balance");
    verifyNormalize("account.getbalance()", "account.balance");
    verifyNormalize("account.getbalance()", "account.getBalance()");
    verifyNormalize("account.getaccountdetail().getaccountdetailcode()", "account.accountDetail.accountDetailCode");
    verifyNormalize("account.getaccountdetail().getaccountdetailcode()",
      "account.getAccountDetail().getAccountDetailCode()");
    verifyNormalize("account.getaccountdetail().getaccountdetailcode()",
      "account.accountDetail.getAccountDetailCode()");
    verifyNormalize("account.getaccountdetail().getaccountdetailcode()",
      "account.getAccountDetail().accountDetailCode");
    verifyNormalize("account.getaccountdetail().getaccountdetailcode()",
      "account.getAccountDetail().getAccountDetailCode()");
    verifyNormalize("account.getfirstresponsibleolderthan().getname()",
      "account.getFirstResponsibleOlderThan(40).name");
    verifyNormalize("account.getfirstresponsibleolderthan().getname()",
      "account.getFirstResponsibleOlderThan(40).getName");
    verifyNormalize("getaccountdetail().getaccountdetailcode()", "getAccountDetail().getAccountDetailCode()");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testNull.
   *
   * @throws  Exception  exception
   */
  public void testNull() throws Exception {
    assertNull(context.eval("accountIndexCode"));
    assertNull(context.eval("account.getAccountIndex().getAccountIndexCode()"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testNormalize.
   *
   * @throws  Exception  exception
   */
  public void testUsedVariables() throws Exception {
    verifyUsedVariable("balance", "balance");
    verifyUsedVariable("account.curbalance", "account.curBalance");
    verifyUsedVariable("account.curbalance()", "account.curBalance()");
    verifyUsedVariable("account.balance", "account.balance");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testVerify() throws Exception {
    assertEquals(true, context.verify("balance"));
    assertEquals(true, context.verify("account.balance"));
    assertEquals(true, context.verify("account.balance + account.pastDue"));
    assertEquals(true, context.verify("getAccountDetail().getAccountDetailCode()"));

    try {
      context.verify("account.balance1");
      fail("Verify should fail for invalid expression : 'account.balance1'");
    } catch (Exception e) { }

    try {
      context.verify("account1.balance");
      fail("Verify should fail for invalid expression : 'account1.balance'");
    } catch (Exception e) { }

    try {
      context.verify("accountDetailCode");
      fail("Verify should fail for invalid expression : 'accountDetailCode'");
    } catch (Exception e) { }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String generateExpr(int count, boolean useString) {
    // build data
    int           off      = Math.round(new Float(count * Math.random()));
    String        lookupAC = "";
    StringBuilder sb       = new StringBuilder();

    for (int i = 0; i < count; i++) {
      String acString = generateStringAccountNum();

      if (i == off) {
        lookupAC = acString;
      }

      if (useString) {
        sb.append(",").append("\"").append(acString).append("\"");
      } else {
        sb.append(",").append(acString);
      }
    }

    sb.deleteCharAt(0);
    sb.append("]");

    if (useString) {
      sb.insert(0, "\"" + lookupAC + "\" in [");
    } else {
      sb.insert(0, lookupAC + " in [");
    }

    return sb.toString();
  } // end method generateExpr

  //~ ------------------------------------------------------------------------------------------------------------------

  private String generateStringAccountNum() {
    Long acNo = new Double(1000000000 * Math.random()).longValue();

    return acNo.toString();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void verifyNormalize(String expr1, String expr2) {
    assertEquals(expr1, (String) (context.getNormalizedVariables(expr2).toArray())[0]);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void verifyUsedVariable(String expr1, String expr2) {
    assertEquals(expr1, (String) (context.getUsedVariables(expr2).toArray())[0]);
  }
} // end class EvalTest
