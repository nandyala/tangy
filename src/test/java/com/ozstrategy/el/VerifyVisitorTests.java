package com.ozstrategy.el;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ozstrategy.el.model.Address;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozstrategy.el.antlr.OzElLexer;
import com.ozstrategy.el.antlr.OzElParser;
import com.ozstrategy.el.antlr.OzVerboseListener;
import com.ozstrategy.el.impl.OzElValue;
import com.ozstrategy.el.impl.ResolverContext;
import com.ozstrategy.el.impl.VerifyVisitor;
import com.ozstrategy.el.model.Account;
import com.ozstrategy.el.model.Responsible;
import com.ozstrategy.el.util.CalculateUtil;

import junit.framework.TestCase;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/8/13 Time: 2:40 PM To change this template use File | Settings | File
 * Templates.
 *
 * @author Rojer Luo
 * @version $Revision$, $Date$
 */
public class VerifyVisitorTests extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */

  /**
   * DOCUMENT ME!
   */
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private ResolverContext resolver = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public void testBatchExpression() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Batch Expression...");
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
   * @throws Exception DOCUMENT ME!
   */
  public void testExpressionPass() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    String[] cases = {
        "substr(\"abc\",1,2)", //
        "range(\"abc\",1,2)", //
        "new Long(100)",    //
        "new Integer(100)", //
        "\"this is a string.\"",
        "(\"this is a string.\")",
        "'this is a string.'",
        "1000",
        "(1000)",
        "1000.0",
        "1000 + 111",
        "1000.0 + 111",
        "1000.0 + 0.99 + 0.01",
        "1000.0 - 0.99 - 0.01",
        "1000.0 +0.9+ 0.1",
        "1000.0 + 1.0",
        "1000 + 1.0",
        "1000+1.0",
        "1000.0 - 1.0",
        "1000 - 1.0",
        "1000-1.0",
        "10*100",
        "10.01*100",
        "10/100",
        "10%5",
        "10%6",
        "0.1*0.2",
        "10.9/109",
        ".5 + 100",
        "100 + .50",
        "100 +.50",
        "100+.50",
        "1000.0 > 999.999",
        "(1000.0) > (999.999)",
        "1000 > 100 and 5.0 < 10",
        "(1000 > 100 and 5.0 < 10)",
        "1000 > 100 && 5.0 < 10",
        "1000 > 100 || 5.0 < 10",
        "1000 > 100 or 5.0 < 10",
        "1000 > 100 or .50 < 10",
        "1000 > 100 or (.50 < 10)",
        "1000 = 100",
        "1000 =100",
        "1000 != 100",
        "1000 >= 100",
        "1000 <= 100",
        "(1000 <= 100) = true",
        "1000 > 100 = 1000 > 100",
        "(1000 > 100) = (1000 > 100)",
        "1000 > 100 = 1000 < 100",
        "(1000 > 100) = (1000 < 100)",
        "1000 <> 100 and 5.0 <> 10",
        "1000 > 100 = true",
        "1000 = 1000",
        "1000 <> 100",
        "1000 > 100 <> true",
        "1000 > 100 = false",
        "1000 > 100 <> false",
        "1000 < 100 = false",
        "1000 < 100 = true",
        "1000 < 100 <> false",
        "1000 < 100 = false",
        "1000 > 100 = true and 5.0 < 10 = false",
        "1000 > 100 = true && 5.0 < 10 = false",
        "1000 > 100 = true || 5.0 < 10 = false",
        "1000 > 100 = true or 5.0 < 10 = false",
        "1000 < 100 = false and 5.0 > 10 = false",
        "1000 < 100 = false && 5.0 > 10 = false",
        "1000 < 100 = false || 5.0 > 10 = false",
        "1000 < 100 = false or 5.0 > 10 = false",

        "if (1000 > 100) 200; else 300;",
        "if (1000 < 100) 200; else 300;",
        "if (1000 = 100) 200; else 300;",
        "if (1000 < 100) {200;} else {300;}",
        "if (1000 < 100) 200; else if(300 > 1000) 300; else if(100<1000) 400; else 900;",
        "if (1000 < 100) 200; else if(300 > 1000) 300; else if(100>1000) 400; else 900;",
        "if (1000 =100) 200; else if(300 = 1000) 300; else if(100 = 1000) 400; else 900;",
        "{100+200;200+300;}",
        "{100+200;200+300;;}",
        "if (1000 < 100) 200; else {110 + 200; 300+400;}",
        "if (1000 < 100) 200; else {110 + 200; 300+400;;}",
        "(if (1000 < 100) 200; else {110 + 200; 300+400;;})",
        "(200 > 100)? 200:100",
        "(200 < 100)? 200:100",
        "(200 = 100)? 200:100",
        "responsible.setIntValue(new Long(100))", //
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
   * @throws Exception DOCUMENT ME!
   */
  public void testExpressionReject() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    String[] cases = {
        "$1234",
        "~1234",
        "`1234",
        "@1234",
        "#1234",
        "200nd100",
        "200 nd100",
        "10.0%6",
        "200 and 100",

        "123='123'",
        "(1>2)?(1/'abc'):(1*'abc')",
        "int 'abc'",
        "!123",
        "if 123=123",
        "if {123=123}",
        "~'abc'",

// "'a'+'b'",
// "'a'^'b'",
        "123 && 12",
        "true |= false",
        "integer i=12",


        "int float=123",
        "int a/bc=123",
        "int a=    ",
        "int 12= a ",
        "max(1,2)?100:200",
        "max1,2,3=3",
        "double monthlyPayment=loanAmount*monthlyInterest/(1-1/(Math.pow(1+monthlyInterestRate),numberOfYears*12));",
//
//// "balance = true",

// "isOddMonday = 123",
// "hasContactableAddress='abc'",


        "nextworkdate = 0",

        "addBusinessDays(Date '2010-10-10',5)",


        "addBusinessDays(String today, getMonth(today))",
        "createDate('abc', 'abc', 'abc')",
        "today isMonday",
        "max(today,yesterday)",
        "max(today,yestoday)",
        "min(today,yestoday)",
        "now(today())",
        "account.balance := true",
// "getDaysInMonth = true",
        "addDays(today,11.2323232)>0",

        "responsible.getaccount>0",
        "responsible.active='Y'",
        "getYear(account.balance)",
        "randomIntGenerator(responsible.getfirstname(), responsible.getlastname())",
        "porfolioSurvey.name=T!@#",
        "porfolioSurvey.name=\n",
        "account.balance=account.createdate",
        "$balance",
        "￥balance",
        "balance()",
        "account.consentDate",
        "responsible.zipcode=true",
        "$${agency.name}",

        "while now()",
        "if(i=1 to 12)",
        "for() else{}",
        "if (PortfolioSurveyAnswer.surveyCode<>null) {Variable=null}",
        "if (PortfolioSurveyAnswer.surveyCode<>null) {Variable=TaskCode}",
        "if(balance>today){}",
        "for(isEvenMonday=true){}",
        "while(hasContactablePhone){}",
        "if(1=1){c:=2}\n continue",
        "foreach(r : [1,2,3]) {total := total + r;}",
    };

    for (String expr : cases) {
      assertFalse(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testExpressionReject

  public void testObjectMethodReject() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Object Method Reject Started...");
    }

    String[] cases = {
        "balance.toString()",
        "accountDays.toString()",
        "accountStatus.toString()",
        "hasPayment.toString()",
        "today.toString()",
        "today.getYear()",
    };

    for (String expr : cases) {
      assertFalse(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Object Method Reject Ended.<<<<");
    }
  } // end method testExpressionReject

  public void testDefinedVariables() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Defined Variables Started...");
    }

    String[] cases = {
      "count := 0;foreach(r : account.responsibles) {count := count + 1;}",
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Defined Variables Ended.<<<<");
    }
  } // end method testExpressionReject

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public void testVariables() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression With Variables Started...");
    }

    String[] cases = {
        "if(inString(accountStatus,\"PD0\")=true or inString(accountStatus,\"PD1\")=true){ accountStatus;} else{ \"0\";}", //
        "balance",
        "balance > 100 and accountDays < 10",
        "balance - pastDue > 1000",
        "balance / balance",
        "123 / balance",
        "hasPayment and isOldAccount",
        "balance * percentage",
        "balance > 100 and accountDays < 10",
        "balance > 100 or accountDays < 10",

        "1 + 2; balance /2; pastDue;",
        "if (balance < 100 and accountDays < 10) {\"H\";} else if (balance > 100 and accountDays < 10) {\"M\";} else if (balance > 100 and accountDays < 100) {\"L\";}else {\"N\";}",

        "max(1,2,3) = 3;",
        "max(1+8,balance,pastDue) = 3",
        "today();",
        "today() > yesterday()",
        "nextworkdate>today = false",
        "(nextworkdate) >today = false",
        "isTimeEarlier(today,yesterday)",
        "getDay(today)",
        "createDate(1997, 10, 5)",
        "createDate((getYear(today)), (getMonth(today)), 5)",
        "compareDateTime(today, yesterday)>0",
        "addDays(today, 11)",
        "roundDown(account.balance,1)",
        "roundDownInteger(111.01)=roundInteger(111.6)",

        "if(getDay(today) < 5)\n"
            + "    createDate(getYear(today), getMonth(today), 5);\n"
            + "else\n"
            + "  createDate(getYear(today), getMonth(today), 5);",
        "if(getDay(today) < 5)\n"
            + "    createDate(getYear(today), getMonth(today), 5);\n"
            + "else\n"
            + "  createDate(getYear(today), getMonth(today), 5);",

        "account.balance > 1000",
        "account.getBalance() > 1000",
        "account.oldAccount=true",
        "responsible.account.getBalance() > 1000",
        "responsible.getAccount().balance > 1000",
        "responsible.getAccount().getBalance() > 1000",
        "responsible.getAccount().isStatus(\"active\")",
        "responsible.getAccount().isStatus(account.getAccountStatus())",
        "responsible.setLongValue(new Long(100))",    //

        "count := 0;foreach(r : account.responsibles) {count := count + 1;}",
        "totalAge := 0;foreach(r : account.responsibles) {totalAge := totalAge + r.age;}",
        "allNames := \"\";foreach(r : account.responsibles) {allNames += r.firstName + \".\" + r.lastName;}",
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testVariables

  public void testCascadedVariables() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression With Variables Started...");
    }

    String[] cases = {
        "true",
        "balance",
        "balance > 1000",
        "account.balance > 1000",
        "account.oldAccount=true",
        "responsible.account.balance > 1000",
        "responsible.getAccount().balance > 1000",
        "responsible.getAccount().isStatus(account.accountStatus)",

        "count := 0;foreach(r : account.responsibles) {count := count + 1;}",
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testVariables

  public void testCascadedFunctions() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression With Variables Started...");
    }

    String[] cases = {
        "account.getBalance() > 1000",
        "responsible.account.getBalance() > 1000",
        "responsible.getAccount().balance > 1000",
        "responsible.getAccount().getBalance() > 1000",
        "responsible.getAccount().isStatus(\"active\")",
        "responsible.getAccount().isStatus(account.getAccountStatus())",
        "responsible.setLongValue(new Long(100))",    //
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testVariables

  /**
   * DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public void testExpressionWithVariables() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression With Variables Started...");
    }

    String[] cases = {
        "a:=0;a++;a--;++a;--a;",                                                                                           //
        "a:=0;a+=1;a-=2;a*=3;a/=4;a%=5;",                                                                                  //
        "sum := 0;i := 0;do{ sum ++; i++;} while(i < 10); sum;",                                                           //
        "sum := 0;foreach(i : [0,1,2,3,4,5,6,7,8,9]){ sum ++;} sum;",                                                      //
        "sum := 0;for(i := 0; i < 10; i++){ sum ++;} sum;",                                                                //
        "sum := 0;i := 0;while(i < 10){ sum ++; i++;return sum;} ; sum;",                                                  //
        "\"balance and test\" = 'balance and test'",                                                                       //
        "if(inString(accountStatus,\"PD0\")=true or inString(accountStatus,\"PD1\")=true){ accountStatus;} else{ \"0\";}", //
        "if(inString(accountStatus,\"PD0\")=true or inString(accountStatus,\"PD1\")=true) accountStatus; else \"0\";",     //
        "if(inString(accountStatus,'PD0')=true or inString(accountStatus,'PD1')=true) accountStatus;",                     //
        "if(inString(accountStatus,'PD0')=true or inString(accountStatus,'PD1')=true){ accountStatus;}",                   //
        "if(inString(accountStatus,'PD0')) accountStatus; else 0;",                                                        //
        "if(inString(accountStatus,'PD0')) accountStatus; else \"0\";",                                                    //
        "'abc'",
        "if(true) {'abc';}",                                                                                               //
        "if(true) {'0';}",                                                                                                 //
        "if(false) {'0';}",                                                                                                //
        "if(inString(accountStatus,\"PD0\")) {'abc';}",                                                                    //
        "if(inString(accountStatus,'PD0')) {'abc';}",                                                                      //
        "if(inString(accountStatus,'PD0')) 'abc';",                                                                        //
        "if(inString(accountStatus,'PD0')) accountStatus; else '0';",                                                      //
        "if(inString(accountStatus,'PD0')=true) accountStatus; else '0';",                                                 //
        "if(inString(accountStatus,'PD0')=true or inString(accountStatus,'PD1')=true) accountStatus; else '0';",           //
        "if(inString(accountStatus,'PD0')=true or inString(accountStatus,'PD1')=true){ accountStatus;} else{ '0';}",       //
        "balance",
        "- balance",
        "-balance",
        "balance > 100",
        "not (balance > 100)",
        "balance = 999",
        "balance > 100 and accountDays < 10",
        "accountStatus = \"balance and test\"",
        "accountStatus = 'balance and test'",

        "balance > 1000",
        "balance + 1000",
        "balance + (1000)",
        "balance + 1000.00",
        "balance - pastDue > 1000",
        "balance - pastDue < 1000",
        "balance - pastDue = 1000",
        "balance / balance",
        "123 / balance",
        "hasPayment and isOldAccount",
        "hasPayment && isOldAccount",
        "hasPayment or not isOldAccount",
        "hasPayment || not isOldAccount",
        "balance * percentage",
        "balance = null",
        "balance = nuLl",
        "balance <> null",
        "hasPayment",
        "hasPayment = true",
        "hasPayment = \"true\"",
        "hasPayment = tRue",
        "hasPayment = false",
        "hasPayment != tRue",
        "W_Q02 = \"Successful\"",
        "W_Q02 = \"Unsuccessful\" and W_Q04 = \"No\"",
        "W_Q01 = \"No Change to RFD\"",

        "balance > 100 and accountDays < 10",
        "balance > 100 or accountDays < 10",
        ".5 * balance",
        "balance * .5",
        "balance *.5",
        "balance*.5",
        "balance+5.",
        "balance*5.",
        "balance/ .5",
        "balance- 5.",
        "(balance > 100) and (accountDays < 10)",
        "(balance > 100) && (accountDays < 10)",
        "balance > 100 && accountDays < 10",
        "(balance > 100) || (accountDays < 10)",
        "balance > 100 || accountDays < 10",
        "((balance > 100) and (accountDays < 10)) and true",
        "((balance > 100) and (accountDays < 10)) or false",
        "((balance > 100) && (accountDays < 10)) || false",
        "((balance > 100) or (accountDays < 10)) || false",

        "1 + 2; balance /2; pastDue;",
        "1 + 2;balance /2;pastDue;",
        "1 + 2;pastDue := balance /2;pastDue;",
        "1 + 2;pastDue := balance /2;pastDue;;",
        "1 + 2;pastDue = balance /2;pastDue;",
        "1 + 2;pastDue = balance /2;pastDue;;",
        "100 + balance;",
        "1 + 2; balance /2; pastDue;;",
        "1 + 2; balance /2;; pastDue;",
        "(balance > 1000)?1000:-1000;",
        "(balance < 1000)?1000:-1000;",
        "(balance > 1000)?1000:(balance < 1000)?1000:-1000;",
        "(balance > 1000)?(1000):(balance < 1000)?1000:-1000;",
        "if( balance > 1000) balance; else balance / 2;",
        "if( balance > 1000) balance; else balance / 2 ; balance + 200;",
        "if( balance > 1000) balance; else balance / 2;",
        "if( balance > 1000) balance; else (balance / 2);",
        "if( balance > 1000) {balance + 1;balance/2;} else balance / 2 ;",
        "if( balance > 1000) balance; else balance / 2;balance > 1000;",
        "if( balance > 1000) { balance; balance /2; } else {balance / 2; balance /3;}",
        "if (balance < 100 and accountDays < 10) {\"H\";} else if (balance > 100 and accountDays < 10) {\"M\";} else if (balance > 100 and accountDays < 100) {\"L\";}else {\"N\";}",

        "max(1,2,3) = 3;",
        "max(1.1,2.2,3.3) = 3.3",
        "max(1,2,3.3) = 3.3;",
        "max(1+8,balance,pastDue) = 3",
        "today();",
        "today() > yesterday()",
        "today() >= yesterday()",
        "today() < yesterday()",
        "today() > \"2012-01-01\"",
        "today() >= \"2012-01-01\"",
        "today() = \"2012-01-01\"",
        "\"2012-01-01\" < today()",
        "\"2012-01-01\" <= today()",
        "\"2012-01-01\" = today()",
        "\"2012-01-01\" <= (today())",
        "\"2012-01-01\" = today()",
        "today() > today",
        "today() < today",
        "today = today()",
        "(yesterday>today) = false",
        " yesterday>today = false",
        "nextworkdate>today = false",
        "(nextworkdate) >today = false",
        "isTimeEarlier(today,yesterday)",
        "isTimeEarlier (today, yesterday) = true",
        "today-yesterday>0",
        "today-yesterday>0 = true",
        "(today-yesterday>0) = true",
        "getDay(today)",
        "getDay(today())",
        "getDay(today) < 5",
        "getDay(today) >= 5",
        "createDate(1997, 10, 5)",
        "createDate(getYear(today), getMonth(today), 5)",
        "createDate(getYear(today()), getMonth(today), 5)",
        "createDate(getYear(today()), getMonth(today()), 5)",
        "createDate((getYear(today)), (getMonth(today)), 5)",
        "compareDateTime(today, yesterday)>0",
        "compareDateTime(today, yesterday)>=0",
        "compareDateTime(today, yesterday) > 0",
        "account.balance+account.pastDue<>null",
        "addDays(today, 11)",
        "addDays(today, 11)>today",
        "addDays(today, 11)>today()",
        "roundDown(account.balance,1)",
        "roundDown(account.balance,1)>0 = true",
        "roundDown(account.balance,1)>0 = false",
        "roundDownInteger(111.01)=roundInteger(111.6)",

        "c:=1;if(balance < 500) c:=2; c;",
        "c:=1;if(balance > 500) c:=2; c;",
        "c:=1;if(balance < 500) c:=2; else {c:=3; c:=4;} ; c:=2;if(balance > 500) c:=2; else {c:=5; c:=6;} ; c;",
        "c:=2;if(balance < 500) c:=2; if(balance > 500) c:=5; c;",
        "c:=3;if(balance < 500) c:=2; else {c:=3; c:=4;} ;c;",
        "c:=4;if(balance < 500) {c:=2;} else {c:=3; c:=4;} ;c;",
        "c:=5;if(balance < 500) {c:=2;} else {c:=3; c:=4;} ;c;",
        "c:=6;if(balance < 500) {c:=2;} else {c:=3; c:=4;} ;c;",
        "c:=7;if(balance < 500) {c:=2; c:= c+1;} else {c:=3; c:=4;} c;",
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
        "account.balance",
        "account.getBalance()",
        "account.getBalance() > 1000",
        "account.oldAccount",
        "account.oldAccount=true",
        "responsible.account.getBalance() > 1000",
        "responsible.getAccount().balance > 1000",
        "responsible.getAccount().getBalance() > 1000",
        "responsible.getAccount().isStatus(\"active\")",
        "responsible.getAccount().isStatus(\"tttttt\")",
        "responsible.getAccount().isStatus(account.getAccountStatus())",
        "responsible.setLongValue(new Long(100))",    //
        "responsible.setIntValue(new Integer(100))",  //
        "responsible.setLongValue(new Integer(100))", //

        "total := 0; foreach(r : [1,2,3]) {total := total + r;}",
        "total := 0;foreach(r : [1.0,2.0,3.0]) {total := total + r;}",
        "count := 0;foreach(r : account.responsibles) {count := count + 1;}",
        "count := 0;foreach(r : account.responsibles) {count := (count + 1);}",
        "totalAge := 0;foreach(r : account.responsibles) {totalAge := totalAge + r.age;}",
        "totalAge := 0;foreach(r : account.responsibles) {totalAge := totalAge + r.getAge();}",
        "totalAge := 0;foreach(responsible : account.responsibles) {totalAge := totalAge + responsible.age;}",
        "1 in [1,2,3]",
        "1.0 in [1.0,2.0,3.0]",
        "1.0 in [1.0,2,3]",
        "1.0 in [1.0,\"abc\",3]",
        "4 in [1,2,3]",
        "4.0 in [1.0,2.0,3.0]",
        "2.0 in [1.0,2,3]",
        "3.0 in [1.0,\"abc\",3]",
        "'abc' in [1.0,\"abc\",3]",
        "'a' in [1.0,\"abc\",3]"
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
   * @throws Exception DOCUMENT ME!
   */
  public void testSpecialExpressionPass() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    String[] cases = {
        "null = null",   //
        "null != null",  //
        "null = 1",      //
        "null != 1",     //
        "1 = null",      //
        "1 != null",     //
        "null = true",   //
        "null != true",  //
        "false != true", //
        "true = null",   //
        "true != null",  //

        "null > null", //
        "null < null", //
        "null > 1",    //
        "null < 1",    //
        "1 > null",    //
        "1 < null",    //

        "true and null",  //
        "false and null", //
        "true and null",  //
        "true or null",   //

// calculation
        "null + null", //
        "null - null", //
        "null * null", //
        "null / null", //
        "null + 100",  //
        "null - 100",  //
        "null * 100",  //
        "null / 100",  //
        "100 + null",  //
        "100 - null",  //
        "100 * null",  //
        "100 / null",  //

// Boolean related
        "true = false",       //
        "true != false",      //
        "true = \"true\"",    //
        "true != \"false\"",  //
        "true != false",      //
        "false != \"false\"", //
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testSpecialExpressionPass

  public void testArrayExpressionPass() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    String[] cases = {
        "[0,1,2,3,4,5,6,7,8,9][2]", //
        "[0,1,2,3,4,5,6,7,8,9][9]", //
        "array := [0,1,2,3,4,5,6,7,8,9]; array[5];",//
        "array := [0,1,2,3,4,5,6,7,8,9]; array[9];",//
        "responsibles := account.responsibles; responsibles[0].firstName;", //
        "no := 0; responsibles := account.responsibles; no ++; responsibles[no].firstName;", //
        "((account.responsibles)[0]).firstName", //
        "(account.responsibles)[0].firstName",//
        "account.responsibles[0].firstName",  //
    };

    for (String expr : cases) {
      assertTrue(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testSpecialExpressionPass

  public void testMapValue()throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Map Value Started...");
    }

    // Positive Cases
    String[] casesPass = {
            "user.name = 'John'", //
            "user.Name = 'John'", //
            "user.NaMe = 'John'", //
            "user.age = 30",//
            "user.name = 'John' and user.age = 30",//
    };

    for (String expr : casesPass) {
      assertTrue(runParserTest(expr));
    }

    // Positive Cases
    String[] casesFail = {
            "user.name1 = 'John'", //
            "user.ago = 30",//
            "user.nam1e = 'John' and user.age1 = 30",//
    };
    for (String expr : casesFail) {
      assertFalse(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Map Value Completed.<<<<");
    }
  }
  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public void testSpecialExpressionReject() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Expression Started...");
    }

    String[] cases = {
        "1 - true",
        "2 * true",
        "2 / true",
        "false - 1",
        "false * 2",
        "false / 2",
        "true and 2",
        "false or 2",
        "null > true",  //
        "false < null", //
        "null + true",  //
        "null - false", //
        "null * true",  //
        "null / false", //
        "true + null",  //
        "false - null", //
        "true * null",  //
        "false / null", //

        "2 = true",    //
        "true = 1",    //
        "true = 0",    //
        "true = -1",   //
        "2 = false",   //
        "false = 1",   //
        "false = 0",   //
        "false = -1",  //
        "true != 2",   //
        "true != 1",   //
        "true != 0",   //
        "true != -1",  //
        "false != 2",  //
        "false != 1",  //
        "false != 0",  //
        "false != -1", //

        "true < 2",     //
        "true > 2",     //
        "true > false", //
        "true > -1",    //
        "2 > true",     //

        "true + 2",  //
        "2 + true",  //
        "false + 2", //
        "2 + false", //
    };


    for (String expr : cases) {
      assertFalse(runParserTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Expression Completed.<<<<");
    }
  } // end method testSpecialExpressionReject
  //~ ------------------------------------------------------------------------------------------------------------------
  public void testVariablePaths() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Variable Paths Started...");
    }

    String[][] cases = {
      {"account.balance", "account.balance"},
      {"account.responsibles[0].firstName", "account.responsibles","account.responsibles[0]","account.responsibles[0].firstName"},
      {"account.getResponsibles()[0].firstName", "account.responsibles","account.responsibles[0]","account.responsibles[0].firstName"},
      {"account.getFirstResponsible().firstName", "account.firstResponsible","account.firstResponsible.firstName"},
      {"account.getResponsibles()[0].address.address1", "account.responsibles","account.responsibles[0]","account.responsibles[0].address","account.responsibles[0].address.address1"},
      {"foreach(r : account.responsibles) {r.firstName + \".\" + r.lastName;}", "account.responsibles","account.responsibles[0]","account.responsibles[0].firstName","account.responsibles[0].lastName"},
      {"allNames := \"\";foreach(r : account.responsibles) {allNames += r.firstName + \".\" + r.lastName;}", "account.responsibles","account.responsibles[0]","account.responsibles[0].firstName","account.responsibles[0].lastName"},
    };

    for (String[] expr : cases) {
      assertTrue(runPathsTest(expr));
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Test Variable Paths  Completed.<<<<");
    }

  }

  /**
   * DOCUMENT ME!
   *
   * @param expr DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  protected boolean runParserTest(String expr) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Parsing Expression : '" + expr + "' ......");
    }

    CharStream input = new ANTLRInputStream(expr);
    OzElLexer lexer = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser parser = new OzElParser(tokens);

    lexer.removeErrorListeners();
    lexer.addErrorListener(new OzVerboseListener());
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    boolean testPass = false;
    VerifyVisitor visitor = new VerifyVisitor();

    // Loading Sample data
    initVariables();

    if (resolver != null) {
      visitor.setResolvers(resolver);
    }

    try {
      ParseTree tree = parser.entry(); // parse
      OzElValue retValue = visitor.visit(tree);

      if (visitor.hasUsedVariables() &&logger.isDebugEnabled()) {
        logger.debug("Used Variables:" + visitor.getUsedVariables());
      }

      if (visitor.hasCascadedVariables() && logger.isDebugEnabled()) {
        logger.debug("Used Cascaded Variables:" + visitor.getUsedCascadeVariableNames());
      }

      if (visitor.hasUsedFunctions() && logger.isDebugEnabled()) {
        logger.debug("Used Function(s): " + visitor.getUsedFunctions());
      }

      if (visitor.hasCascadedFunctions() && logger.isDebugEnabled()) {
        logger.debug("Used Cascaded Function(s): " + visitor.getUsedCascadedFunctionNames());
      }

      if (visitor.hasDefinedVariables() && logger.isDebugEnabled()) {
        logger.debug("Defined Variable(s): " + visitor.getDefinedVariables());
      }

      if (visitor.hasUnknownVariables() && logger.isDebugEnabled()) {
        logger.debug("Unknown Variable(s): " + visitor.getUnknownVariables());
      }

      if (visitor.hasUnknownVariables()) {
        testPass = false;
      } else {
        assertNotNull(retValue);
        testPass = true;
      }

      logger.info("'" + expr + "' --> (" + retValue.getClazz() + ")");

    } catch (RuntimeException e) {
      logger.error(e.toString());
    } catch (Exception e) {
      logger.error(e.toString());
    } // end try-catch

    logger.info(testPass ? "Passed.\n" : "Failed.\n");

    return testPass;
  } // end method runParserTest

  protected boolean runPathsTest(String[] exprs) throws Exception {
    String expr = exprs[0];
    if (logger.isDebugEnabled()) {
      logger.debug("Parsing Expression : '" + expr + "' ......");
    }

    CharStream input = new ANTLRInputStream(expr);
    OzElLexer lexer = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser parser = new OzElParser(tokens);

    lexer.removeErrorListeners();
    lexer.addErrorListener(new OzVerboseListener());
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    boolean testPass = false;
    VerifyVisitor visitor = new VerifyVisitor();

    // Loading Sample data
    initVariables();

    if (resolver != null) {
      visitor.setResolvers(resolver);
    }

    try {
      ParseTree tree = parser.entry(); // parse
      OzElValue retValue = visitor.visit(tree);

      List variablePaths = visitor.getCallPaths();

      List paths = Arrays.asList(exprs);
      paths = paths.subList(1, paths.size());
      testPass = variablePaths.containsAll(paths);

      logger.info("'" + expr + "' --> (" + retValue.getClazz() + ")");
      logger.info("'" + variablePaths + "' --> (" + paths + ")");

    } catch (RuntimeException e) {
      logger.error(e.toString());
    } catch (Exception e) {
      logger.error(e.toString());
    } // end try-catch

    logger.info(testPass ? "Passed.\n" : "Failed.\n");

    return testPass;
  } // end method runParserTest

  //~ ------------------------------------------------------------------------------------------------------------------

  private void initVariables() {
    ResolverContext.initWithCalculateUtil();

    resolver = new ResolverContext();

    resolver.addValue("balance", new OzElValue("balance", new BigDecimal("999.99")));
    resolver.addValue("pastDue", new OzElValue("pastDue", new BigDecimal("99.99")));
    resolver.addValue("percentage", new OzElValue("percentage", new BigDecimal("0.10")));
    resolver.addValue("accountDays", new OzElValue("accountDays", 99));
    resolver.addValue("accountStatus", new OzElValue("accountStatus", "balance and test"));
    resolver.addValue("W_Q01", new OzElValue("W_Q01", "No Change to RFD"));
    resolver.addValue("W_Q02", new OzElValue("W_Q02", "Successful"));
    resolver.addValue("W_Q04", new OzElValue("W_Q04", "Yes"));
    resolver.addValue("hasPayment", new OzElValue("hasPayment", true));
    resolver.addValue("isOldAccount", new OzElValue("isOldAccount", false));
    resolver.addValue("today", new OzElValue("today", new Date()));
    resolver.addValue("yesterday", new OzElValue("yesterday", CalculateUtil.minusDays(new Date(), 1)));
    resolver.addValue("nextworkdate", new OzElValue("nextworkdate", CalculateUtil.addDays(new Date(), 1)));
    resolver.addValue("responsible", new OzElValue("responsible", new Responsible()));

    resolver.addValue("CommonFunction", new CalculateUtil());
    resolver.addFunction("substr", "substring(str, startIndex, endIndex)", String.class);
    resolver.addFunction("range", "CommonFunction.substring(str, startIndex, endIndex)", String.class);

    Address address1 = new Address("address line1-1", "address line1-2", "City1", "Province1", "Country1");
    Address address2 = new Address("address line2-1", "address line2-2", "City2", "Province2", "Country2");
    Responsible responsible1 = new Responsible(40, CalculateUtil.toDate("1973-01-01"), "Mike", "Mike", "AAAA");
    Responsible responsible2 = new Responsible(37, CalculateUtil.toDate("1976-06-01"), "Jean", "Jean", "BBBB");

    responsible1.setAddress(address1);
    responsible2.setAddress(address2);

    Account account = new Account();
    account.addResponsible(responsible1);
    account.addResponsible(responsible2);
    resolver.addValue("account", new OzElValue("account", account));

    Map<String, Object> user = new HashMap<String, Object>();
    user.put("Name", "John");
    user.put("Age", 30);

    resolver.addValue("user", user);
  }
} // end class VerifyVisitorTests
