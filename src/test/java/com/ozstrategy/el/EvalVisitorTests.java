package com.ozstrategy.el;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.junit.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/8/13 Time: 2:40 PM To change this template use File | Settings | File
 * Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class EvalVisitorTests extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */

  /** DOCUMENT ME! */
  private Logger          logger   = LoggerFactory.getLogger(this.getClass());
  private ResolverContext resolver = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testAssignment() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      // Null related
      // compare
      { "a := 999999.99;", new BigDecimal("999999.99") },   //
      { "a := 999999.99;a;", new BigDecimal("999999.99") }, //
      { "a := 1;", 1 },                                     //
      { "a := 1; a += 1;", 2 },                             //
      { "a := 1; a += 1;a;", 2 },                           //
      { "a := 1; a -= 1;", 0 },                             //
      { "a := 1; a -= 1;a;", 0 },                           //
      { "a := 1; a *= 2;", 2 },                             //
      { "a := 1; a *= 2;a;", 2 },                           //
      { "a := 4; a /= 2;", 2 },                             //
      { "a := 4; a /= 2;a;", 2 },                           //
      { "a := 1; a %= 2;", 1 },                             //
      { "a := 1; a %= 2;a;", 1 },                           //
      { "a := 1; ++a;", 2 },                                //
      { "a := 1; ++a;a;", 2 },                              //
      { "a := 1; --a;", 0 },                                //
      { "a := 1; --a;a;", 0 },                              //
      { "a := 1; a++;", 1 },                                //
      { "a := 1; a++;a;", 2 },                              //
      { "a := 1; a--;", 1 },                                //
      { "a := 1; a--;a;", 0 },                              //
      { "a := new Long(1);", 1L },                          //
      { "a := new Long(1); a += 1;", 2L },                  //
      { "a := new Long(1); a += 1;a;", 2L },                //
      { "a := new Long(1); a -= 1;", 0L },                  //
      { "a := new Long(1); a -= 1;a;", 0L },                //
      { "a := new Long(1); a *= 2;", 2L },                  //
      { "a := new Long(1); a *= 2;a;", 2L },                //
      { "a := new Long(4); a /= 2;", 2L },                  //
      { "a := new Long(4); a /= 2;a;", 2L },                //
      { "a := new Long(1); a %= 2;", 1L },                  //
      { "a := new Long(1); a %= 2;a;", 1L },                //
      { "a := new Long(1); ++a;", 2L },                     //
      { "a := new Long(1); ++a;a;", 2L },                   //
      { "a := new Long(1); --a;", 0L },                     //
      { "a := new Long(1); --a;a;", 0L },                   //
      { "a := new Long(1); a++;", 1L },                     //
      { "a := new Long(1); a++;a;", 2L },                   //
      { "a := new Long(1); a--;", 1L },                     //
      { "a := new Long(1); a--;a;", 0L },                   //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testAssignment

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testBatchExpressions() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Batch Expression Started...");
    }

    String[] cases = {
      "{{balance}}",
      "{{balance},{-balance}}",
      "{{balance},{-balance},{1000/2},{balance > 100}, {not (balance > 100)}, {balance = 999}}",
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Batch Expression Completed.<<<<");
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testDoWhile() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      // Null related
      // compare
      { "sum := 0;i := 0;do{ sum ++; i++;} while(i < 10); sum;", 10 },   //
      { "sum := 0;i := 0;do{ sum += i; i++;} while(i < 10); sum;", 45 }, //
      { "sum := 0;i := 0;do{ sum += i++;} while(i < 10); sum;", 45 },    //
      { "sum := 0;i := 0;do{ sum += ++i;} while(i < 10); sum;", 55 },    //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testDoWhile

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testFunction.
   *
   * @throws  Exception  exception
   */
  public void testExcelFunction() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Function Started...");
    }

    Object[][] cases = {
      { "calcFV( 0.10/4, 16, -2000, 0, 1 )", new BigDecimal("39729.4609") },            // //
      { "calcNPER(0.06/4,-1200,9000,5000,1)", new BigDecimal("11.9037") },              //
      { "calcNPV(0.02,-5000,800,950,1080,1120,1500)", new BigDecimal("106.30909890") }, //
      { "calcPMT(0.035/4,8,0,5000,1)", new BigDecimal("-600.852") },                    //
      { "calcPPMT(0.035/4,1,8,0,5000,1)", new BigDecimal("-600.8520") },                //
      { "calcPV(0.1/4,16,2000,0,1)", new BigDecimal("-26762.7555") },                   //
      { "calcRate(24,-800,0,20000,1,0.1)", new BigDecimal("0.0033") },                  //
      { "calcPMT(0, 100, 2117.48,null,null)", new BigDecimal("-21.1748") },             //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));

        BigDecimal dif = ((BigDecimal) expr[1]).subtract((BigDecimal) ret).abs();
// logger.info("Result difference is '" + dif.toPlainString() + "'");
        Assert.assertTrue(dif.compareTo(new BigDecimal("0.001")) == -1);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Function Completed.<<<<");
    }
  } // end method testExcelFunction

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testExpressionPass() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    String[] cases = {
      "new Long(1000)",          //
      "new Integer(1000)",       //
      "\"this is a string.\"",   //
      "(\"this is a string.\")", //
      "true",
      "1 = 1",
      "100 = 100",
      "(1 = 1) = true",
      "(1 = 1) = \"true\"",
      "(1 = 1) = 'true'",
      "(1 > 1) = true",
      "(1 > 1) = \"true\"",
      "(1 > 1) = 'true'",
      "1000",
      "1000.0",
      "1000 + 111",
      "1000.0 + 111",
      "1000.0 + 0.99 + 0.01",
      "1000.0 - 0.99 - 0.01",
      "10*100",
      "10.01*100",
      "10/100",
      "10%5",
      "10%6",
      ".5 + 100",                //
      "100 + .50",               //
      "100 +.50",                //
      "100+.50",
      "1000.0 > 999.999",
      "1000 > 100 and 5.0 < 10",
      "1000 = 100",
      "1000 != 100",
      "1000 >= 100",
      "1000 <= 100",
      "(1000 <= 100) = true",
      "1000 > 100 = 1000 > 100",
      "(1000 > 100) = (1000 > 100)",
      "1000 > 100 = 1000 < 100",
      "(1000 > 100) = (1000 < 100)",
      "1000 > 100 = true",
      "1000 > 100 = false",
      "1000 < 100 = false",
      "1000 > 100 = true and 5.0 < 10 = false",
      "if (1000 > 100) 200; else 300;",
      "if (1000 < 100) 200; else 300;",
      "if (1000 < 100) 200; else if(300 > 1000) 300; else if(100<1000) 400; else 900;",
      "if (1000 < 100) 200; else if(300 > 1000) 300; else if(100>1000) 400; else 900;;",
      "{100+200;200+300;}",
      "{100+200;200+300;;}",
      "if (1000 < 100) 200; else {110 + 200; 300+400;}",
      "if (1000 < 100) 200; else {110 + 200; 300+400;;}",
      "(200 > 100)? 200:100",
      "(200 < 100)? 200:100",
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testExpressionPass

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testExpressionWithVariables() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    String[] cases = {
      "balance",
      "- balance",
      "-balance",
      "balance > 100",
      "not (balance > 100)",
      "balance = 999",
      "balance > 100 and accountDays < 10",
      "(balance > 100 and accountDays < 10)",
      "accountStatus = \"balance and test\"",
      "balance > 1000",
      "balance + 1000",
      "balance - pastDue > 1000",
      "balance / balance",
      "hasPayment and isOldAccount",
      "hasPayment or not isOldAccount",
      "balance * percentage",
      "balance = null",
      "balance = nuLl",
      "hasPayment",
      "hasPayment = true",
      "hasPayment = tRue",
      "hasPayment != tRue",

      "balance > 100 and accountDays < 10",
      "balance > 100 or accountDays < 10",
      ".5 * balance",
      "balance * .5",
      "balance *.5",
      "balance*.5",
      "balance+5.",
      "balance*5.",
      "(balance > 100) and (accountDays < 10)",
      "(balance > 100) && (accountDays < 10)",
      "balance > 100 && accountDays < 10",
      "((balance > 100) and (accountDays < 10)) and true",
      "((balance > 100) and (accountDays < 10)) or false",
      "((balance > 100) && (accountDays < 10)) || false",
      "1 + 2; balance /2; pastDue;",
      "1 + 2;balance /2;pastDue;",
      "1 + 2;pastDue = balance /2;pastDue;",
      "100 + balance;",
      "1 + 2; balance /2; pastDue;",
      "1 + 2; balance /2;; pastDue;",
      "(balance > 1000)?1000:-1000",
      "(balance < 1000)?1000:-1000",
      "(balance > 1000)?1000:(balance < 1000)?1000:-1000",
      "if( balance > 1000) balance; else balance / 2;",
      "if( balance > 1000) balance; else balance / 2 ; balance + 200;",
      "if( balance > 1000) balance; else balance / 2;",
      "if( balance > 1000) {balance + 1;balance/2;} else balance / 2 ;",
      "if( balance > 1000) balance; else balance / 2;balance > 1000;",
      "if( balance > 1000) { balance; balance /2; } else {balance / 2; balance /3;}",
      "if( balance > 1000) { balance; balance /2; } else {balance / 2; balance /3;;}",
      "if (balance < 100 and accountDays < 10) {\"H\";} else if (balance > 100 and accountDays < 10) {\"M\";} else if (balance > 100 and accountDays < 100) {\"L\";}else {\"N\";}",

      "max(1,2,3) = 3",
      "max(1.1,2.2,3.3) = 3.3",
      "max(1,2,3.3) = 3.3",
      "max(1+8,balance,pastDue) = 3",
      "today()",
      "today() > yesterday()",
      "today() >= yesterday()",
      "today() < yesterday()",
      "today() > \"2012-01-01\"",
      "today() >= \"2012-01-01\"",
      "today() = \"2012-01-01\"",
      "\"2012-01-01\" < today()",
      "\"2012-01-01\" <= today()",
      "\"2012-01-01\" = today()",
      "today() > today",
      "today() < today",
      "today = today()",
      "getDay(today)",
      "getDay(today())",
      "getDay(today) < 5",
      "createDate(1997, 10, 5)",
      "createDate(getYear(today), getMonth(today), 5)",

      "c:=1;if(balance < 500) c:=2; c;",
      "c:=1;if(balance > 500) c:=2; c;",
      "c:=1;if(balance < 500) c:=2; else {c:=3; c:=4;} ; c:=2;if(balance > 500) c:=2; else {c:=5; c:=6;} ; c;",
      "c:=2;if(balance < 500) c:=2; if(balance > 500) c:=5; c;",
      "c:=3;if(balance < 500) c:=2; else {c:=3; c:=4;} ;c;",
      "c:=4;if(balance < 500) {c:=2;} else {c:=3; c:=4;} ;c;",
      "c:=5;if(balance < 500) {c:=2;} else {c:=3; c:=4;} ;c;",
      "c:=6;if(balance < 500) {c:=2;} else {c:=3; c:=4;} ;c;",
      "c:=7;if(balance < 500) {c:=2; c:= c+1;} else {c:=3; c:=4;} ;c;",
      "c:=8;if(balance < 500) {c:=2; c:= c+1;} else {c:=3; c:=4;} ;c;",

      "if(getDay(today) < 5)\n"
      + "    createDate(getYear(today), getMonth(today), 5);\n"
      + "else\n"
      + "  createDate(getYear(today), getMonth(today), 5);",
      "if(getDay(today) < 5)\n"
      + "    createDate(getYear(today), getMonth(today), 5);\n"
      + "else\n"
      + "  createDate(getYear(today), getMonth(today), 5);",

      "account.balance > 1000",
      "(account.balance > 1000)",
      "account.balance",                                               //
      "(account.balance)",                                             //
      "account.getBalance()",                                          //
      "account.getBalance() > 1000",
      "responsible.account.getBalance() > 1000",                       //
      "responsible.getAccount().balance > 1000",                       //
      "responsible.getAccount().getBalance() > 1000",                  //
      "responsible.getAccount().isStatus(\"active\")",                 //
      "responsible.getAccount().isStatus(\"tttttt\")",                 //
      "responsible.getAccount().isStatus(account.getAccountStatus())", //

      "responsible.setLongValue(new Long(100))",                                //
      "responsible.setIntValue(new Integer(100))",                              //
      "responsible.setLongValue(100)",                                          //
      "responsible.setLongValue(new Integer(100))",                             //
      "responsible.setIntValue(new Integer(100)); responsible.intValue = 100;", //

      "foreach(r : [1,2,3]) {total := total + r;}",
      "foreach(r : account.responsibles) {count := count + 1;}",
      "foreach(r : account.responsibles) {totalAge := totalAge + r.age;}",
      "foreach(responsible : account.responsibles) {totalAge := totalAge + responsible.age;}",
      "(foreach(responsible : account.responsibles) {totalAge := totalAge + responsible.age;})",
      "1 in [1,2,3]",
      "1.0 in [1.0,2.0,3.0]",
      "1.0 in [1.0,2,3]",
      "1.0 in [1.0,\"abc\",3]",
      "4 in [1,2,3]",
      "4.0 in [1.0,2.0,3.0]",
      "2.0 in [1.0,2,3]",
      "3.0 in [1.0,\"abc\",3]",
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testExpressionWithVariables

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testForEach() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      // Null related
      // compare
      { "sum := 0;foreach(i : [0,1,2,3,4,5,6,7,8,9]){ sum ++;} sum;", 10 }, //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testForEach

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testForLoop() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      // Null related
      // compare
      { "sum := 0;for(i := 0; i < 10; i++){ sum ++;} sum;", 10 },   //
      { "sum := 0;for(i := 0; i < 10; i+=2){ sum ++;} sum;", 5 },   //
      { "sum := 0;for(i := 0; i < 10; i++){ sum += i;} sum;", 45 }, //
      { "sum := 0;for(i := 10; i > 0; i--){ sum += i;} sum;", 55 }, //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testForLoop

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testFunction.
   *
   * @throws  Exception  exception
   */
  public void testFunction() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Function Started...");
    }

    Object[][] cases = {
      { "getWeekDifference(createDate(2015,9,15), createDate(2015,10,15), false)", -4 }, //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Function Completed.<<<<");
    }
  } // end method testFunction

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testGetArrayValue.
   *
   * @throws  Exception  exception
   */
  public void testGetArrayValue() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      { "[0,1,2,3,4,5,6,7,8,9][2]", 2 },                                                               //
      { "[0,1,2,3,4,5,6,7,8,9][9]", 9 },                                                               //
      { "array := [0,1,2,3,4,5,6,7,8,9]; array[5];", 5 },                                              //
      { "array := [0,1,2,3,4,5,6,7,8,9]; array[9];", 9 },                                              //
      { "responsibles := account.responsibles; responsibles[0].firstName;", "Mike" },                  //
      { "no := 0; responsibles := account.responsibles; no ++; responsibles[no].firstName;", "Jean" }, // }
      { "((account.responsibles)[0]).firstName", "Mike" },                                             //
      { "(account.responsibles)[0].firstName", "Mike" },                                               //
      { "account.responsibles[0].firstName", "Mike" },                                                 //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testGetArrayValue

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testReturn() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      // Null related
      // compare
      { "sum := 0;i := 0;while(i < 10){ sum ++; i++;return sum;} ; sum;", 1 }, //
      { "a := 0; return a; a:= a + 1;return a;", 0 },                          //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testReturn

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testSetValue() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      // Null related
      // compare
      { "account.setBalance(999999.99);account.balance;", new BigDecimal("999999.99") }, //
// { "balance := 999999.99;account.balance;", new BigDecimal("999999.99") },          //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testSetValue

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testSpecialExpression() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      { "99 = \"99\"", Boolean.TRUE },          //
      { "accountDays = 99", Boolean.TRUE },     //
      { "accountDays = \"99\"", Boolean.TRUE }, //

      // Null related
      // compare
      { "null = null", Boolean.TRUE },   //
      { "null != null", Boolean.FALSE }, //
      { "null = 1", Boolean.FALSE },     //
      { "null != 1", Boolean.TRUE },     //
      { "1 = null", Boolean.FALSE },     //
      { "1 != null", Boolean.TRUE },     //
      { "null = true", Boolean.FALSE },  //
      { "null != true", Boolean.TRUE },  //
      { "true = null", Boolean.FALSE },  //
      { "true != null", Boolean.TRUE },  //

      { "null > null", Boolean.FALSE }, //
      { "null < null", Boolean.FALSE }, //
      { "null > 1", Boolean.FALSE },    //
      { "null < 1", Boolean.FALSE },    //
      { "1 > null", Boolean.FALSE },    //
      { "1 < null", Boolean.FALSE },    //

      { "false and null", Boolean.FALSE }, //
      { "true or null", Boolean.TRUE },    //
      { "true and null", null },           //
      { "false or null", null },           //

// calculation
      { "null + null", null }, //
      { "null - null", null }, //
      { "null * null", null }, //
      { "null / null", null }, //
      { "null + 100", null },  //
      { "null - 100", null },  //
      { "null * 100", null },  //
      { "null / 100", null },  //
      { "100 + null", null },  //
      { "100 - null", null },  //
      { "100 * null", null },  //
      { "100 / null", null },  //

// Boolean related
      { "true = false", Boolean.FALSE },     //
      { "true = \"false\"", Boolean.FALSE }, //
      { "true = 'false'", Boolean.FALSE },   //
      { "true = true", Boolean.TRUE },       //
      { "true = \"true\"", Boolean.TRUE },   //
      { "true = 'true'", Boolean.TRUE },     //
      { "true != false", Boolean.TRUE },     //
      { "true != \"false\"", Boolean.TRUE }, //
      { "true != 'false'", Boolean.TRUE },   //

      { "2 = true", null },    //
      { "true = 1", null },    //
      { "true = 0", null },    //
      { "true = -1", null },   //
      { "2 = false", null },   //
      { "false = 1", null },   //
      { "false = 0", null },   //
      { "false = -1", null },  //
      { "true != 2", null },   //
      { "true != 1", null },   //
      { "true != 0", null },   //
      { "true != -1", null },  //
      { "false != 2", null },  //
      { "false != 1", null },  //
      { "false != 0", null },  //
      { "false != -1", null }, //

      { "true < 2", null },     //
      { "true > 2", null },     //
      { "true > false", null }, //
      { "true > -1", null },    //
      { "2 > true", null },     //
      { "false > true", null }, //
      { "-1 > true", null },    //
      { "true > 0", null },     //
      { "0 > true", null },     //

      { "true + 2", null },                      //
      { "2 + true", null },                      //
      { "false + 2", null },                     //
      { "2 + false", null },                     //
      { "null = null and 2 > 1", Boolean.TRUE }, //
      { "null = null or 2 > 1", Boolean.TRUE },  //

      // True/False related
      { "2 < 1 or 2 > 1", Boolean.TRUE },          //
      { "2 < 1 or 0 > 1 or 2 > 1", Boolean.TRUE }, //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        Assert.assertEquals(expr[1], ret);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testSpecialExpression

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testWhile() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    Object[][] cases = {
      // Null related
      // compare
      { "sum := 0;i := 0;while(i < 10){ sum ++; i++;} ; sum;", 10 }, //
      { "sum := 0;i := 0;while(i < 10){ sum += i; i++;} sum;", 45 }, //
      { "sum := 0;i := 0;while(i < 10){ sum += i++;}  sum;", 45 },   //
      { "sum := 0;i := 0;while(i < 10){ sum += ++i;}; sum;", 55 },   //
    };

    for (Object[] expr : cases) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testWhile

  public void testMapValue()throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Map Value Started...");
    }

    // Positive Cases
    Object[][] casesPass = {
            {"user.name", "John"},//
            {"user.name = 'John'", true},//
            {"user.Name = 'John'", true}, //
            {"user.NaMe = 'John'", true}, //
            {"user.name = 'John1'", false}, //
            {"user.age = 30", true},//
            {"user.age < 31", true},//
            {"user.age = 31", false},//
            {"user.name = 'John' and user.age = 30", true},//
    };

    for (Object[] expr : casesPass) {
      try {
        Object ret = runEvaluateTest((String) expr[0]);
        logger.info("'" + (String) expr[0] + " ' = " + ((ret == null) ? "null" : ret.toString()));
        Assert.assertEquals(expr[1], ret);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Test Map Value Completed.<<<<");
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   expr  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  protected Object runEvaluateTest(String expr) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Evaluate Expression : '" + expr + "' ......");
    }

    CharStream        input  = new ANTLRInputStream(expr);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    EvalVisitor visitor = new EvalVisitor();

    // Loading Sample data
    initVariables();

    if (resolver != null) {
      visitor.setResolvers(resolver);
    }

    ParseTree tree = parser.program(); // parse

    OzElValue retValue = visitor.visit(tree);

    assertNotNull(retValue);

    return retValue.getValue();

  } // end method runEvaluateTest

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   expr  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  protected boolean runParserTest(String expr) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Parsing Expression : '" + expr + "' ......");
    }

    CharStream        input  = new ANTLRInputStream(expr);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());


    boolean     testPass = false;
    EvalVisitor visitor  = new EvalVisitor();

    // Loading Sample data
    initVariables();

    if (resolver != null) {
      visitor.setResolvers(resolver);
    }

    try {
      ParseTree tree = parser.entry(); // parse

      OzElValue retValue = visitor.visit(tree);

      assertNotNull(retValue);
      logger.info("'" + expr + "' --> " + retValue.getValue());
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

  private void initVariables() {
    resolver = new ResolverContext();
    resolver.initWithCalculateUtil();

    resolver.addDefinition("balance", "account.balance", BigDecimal.class);
    resolver.addDefinition("pastDue", "account.pastDue", BigDecimal.class);
    resolver.addValue("percentage", new BigDecimal("0.10"));
    resolver.addValue("accountDays", 99);
    resolver.addValue("accountStatus", "balance and test");
    resolver.addValue("hasPayment", true);
    resolver.addValue("isOldAccount", false);
// resolver.addValue("today", new Date());
    resolver.addValue("responsible", new Responsible());

    Responsible responsible1 = new Responsible(40, CalculateUtil.toDate("1973-01-01"), "Mike", "Mike", "AAAA");
    Responsible responsible2 = new Responsible(37, CalculateUtil.toDate("1976-06-01"), "Jean", "Jean", "BBBB");
    Account     account      = new Account();
    account.addResponsible(responsible1);
    account.addResponsible(responsible2);
    resolver.addValue("account", account);

    Map<String, Object> user = new HashMap<String, Object>();
    user.put("Name", "John");
    user.put("Age", 30);

    resolver.addValue("user", user);
  }
} // end class EvalVisitorTests
