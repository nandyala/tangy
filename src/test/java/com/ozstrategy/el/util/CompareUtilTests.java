package com.ozstrategy.el.util;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/8/13 Time: 8:57 AM To change this template use File | Settings | File
 * Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class CompareUtilTests extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  String DATE_FORMAT = "yyyyMMdd";

  /** DOCUMENT ME! */
  SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testDateCompare() throws Exception {
    String dataTimeString  = "2013-06-07 12:30:00";
    String dataTimeString2 = "2013-06-07 11:30:00";
    String dataStartString = "2013-06-07 00:00:00";
    String dataOnlyString  = "2013-06-07 12:30:00";
    Date   leftDate        = CalculateUtil.toDate(dataTimeString);
    Date   rightDate       = CalculateUtil.toDate(dataStartString);

    assertTrue(CompareUtil.equalsTo(leftDate, dataOnlyString));
    assertTrue(CompareUtil.equalsTo(dataOnlyString, leftDate));
    assertFalse(CompareUtil.notEqualsTo(leftDate, dataOnlyString));
    assertFalse(CompareUtil.notEqualsTo(dataOnlyString, leftDate));
    assertFalse(CompareUtil.greaterThan(leftDate, dataOnlyString));
    assertFalse(CompareUtil.greaterThan(dataOnlyString, leftDate));
    assertFalse(CompareUtil.lessThan(leftDate, dataOnlyString));
    assertFalse(CompareUtil.lessThan(dataOnlyString, leftDate));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(leftDate, dataOnlyString));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(dataOnlyString, leftDate));
    assertTrue(CompareUtil.lessThanOrEqualsTo(leftDate, dataOnlyString));
    assertTrue(CompareUtil.lessThanOrEqualsTo(dataOnlyString, leftDate));

    assertFalse(CompareUtil.equalsTo(leftDate, dataTimeString2));
    assertFalse(CompareUtil.equalsTo(dataTimeString2, leftDate));
    assertTrue(CompareUtil.notEqualsTo(leftDate, dataTimeString2));
    assertTrue(CompareUtil.notEqualsTo(dataTimeString2, leftDate));
    assertFalse(CompareUtil.lessThan(leftDate, dataTimeString2));
    assertTrue(CompareUtil.lessThan(dataTimeString2, leftDate));
    assertTrue(CompareUtil.greaterThan(leftDate, dataTimeString2));
    assertFalse(CompareUtil.greaterThan(dataTimeString2, leftDate));
    assertFalse(CompareUtil.lessThanOrEqualsTo(leftDate, dataTimeString2));
    assertTrue(CompareUtil.lessThanOrEqualsTo(dataTimeString2, leftDate));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(leftDate, dataTimeString2));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(dataTimeString2, leftDate));

    assertTrue(CompareUtil.equalsTo(CalculateUtil.toDateOnly(leftDate), rightDate));
    assertTrue(CompareUtil.notEqualsTo(leftDate, rightDate));
  } // end method testDateCompare

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testEqualsTo() throws Exception {
    String strLeftValue  = "100.08";
    String strRightValue = "100.08";

    assertTrue(CompareUtil.equalsTo(new BigDecimal(strLeftValue), new BigDecimal(strRightValue)));
    assertTrue(CompareUtil.equalsTo(new BigDecimal("100.00"), new BigDecimal("100")));
    assertTrue(CompareUtil.equalsTo(new BigDecimal("100.00"), new Long(100)));
    assertTrue(CompareUtil.equalsTo(new BigDecimal("100.00"), new Integer(100)));
    assertTrue(CompareUtil.equalsTo(new Long(100), new BigDecimal("100.00")));
    assertTrue(CompareUtil.equalsTo(new Integer(100), new BigDecimal("100.00")));
    assertTrue(CompareUtil.equalsTo(new Integer(100), new Integer(100)));
    assertTrue(CompareUtil.equalsTo(new Integer(100), new Long(100)));
    assertTrue(CompareUtil.equalsTo(new Long(100), new Integer(100)));

    assertTrue(CompareUtil.equalsTo(Boolean.FALSE, Boolean.FALSE));
    assertFalse(CompareUtil.equalsTo(Boolean.FALSE, Boolean.TRUE));

    assertTrue(CompareUtil.equalsTo("AbcDEF", "AbcDEF"));
    assertFalse(CompareUtil.equalsTo("AbcDEF", "AbcDE"));

    Date leftDate    = sdf.parse("20130608");
    Date rightDate   = sdf.parse("20130608");
    Date anotherDate = sdf.parse("20130708");

    assertTrue(CompareUtil.equalsTo(leftDate, rightDate));
    assertFalse(CompareUtil.equalsTo(leftDate, anotherDate));
  } // end method testEqualsTo

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testGreaterThan() throws Exception {
    assertTrue(CompareUtil.greaterThan(new BigDecimal("100.091"), new BigDecimal("100.09")));
    assertTrue(CompareUtil.greaterThan(new BigDecimal("100.01"), new Long(100)));
    assertTrue(CompareUtil.greaterThan(new BigDecimal("100.01"), new Integer(100)));
    assertTrue(CompareUtil.greaterThan(new Long(100), new BigDecimal("99.99")));
    assertTrue(CompareUtil.greaterThan(new Integer(100), new BigDecimal("99.9999")));
    assertTrue(CompareUtil.greaterThan(new Integer(100), new Integer(99)));
    assertTrue(CompareUtil.greaterThan(new Integer(100), new Long(99)));
    assertTrue(CompareUtil.greaterThan(new Long(100), new Integer(99)));

    assertFalse(CompareUtil.greaterThan(new BigDecimal("100.09"), new BigDecimal("100.091")));
    assertFalse(CompareUtil.greaterThan(new Long(100), new BigDecimal("100.01")));
    assertFalse(CompareUtil.greaterThan(new Integer(100), new BigDecimal("100.01")));
    assertFalse(CompareUtil.greaterThan(new BigDecimal("99.99"), new Long(100)));
    assertFalse(CompareUtil.greaterThan(new BigDecimal("99.9999"), new Integer(100)));
    assertFalse(CompareUtil.greaterThan(new Integer(99), new Integer(100)));
    assertFalse(CompareUtil.greaterThan(new Long(99), new Integer(100)));
    assertFalse(CompareUtil.greaterThan(new Integer(99), new Long(100)));

    assertTrue(CompareUtil.greaterThan("AbcDEFB", "AbcDEF"));
    assertTrue(CompareUtil.greaterThan("AbcDEFB", "AbcDEFA"));
    assertFalse(CompareUtil.greaterThan("AbcDEF", "AbcDEFB"));
    assertFalse(CompareUtil.greaterThan("AbcDEFA", "AbcDEFB"));

    Date leftDate    = sdf.parse("20130607");
    Date rightDate   = sdf.parse("20130608");
    Date anotherDate = sdf.parse("20130708");

    assertTrue(CompareUtil.greaterThan(rightDate, leftDate));
    assertTrue(CompareUtil.greaterThan(anotherDate, rightDate));
    assertFalse(CompareUtil.greaterThan(leftDate, rightDate));
    assertFalse(CompareUtil.greaterThan(rightDate, anotherDate));
  } // end method testGreaterThan

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testGreaterThanOrEqualsTo() throws Exception {
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("100.091"), new BigDecimal("100.09")));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("100.091"), new BigDecimal("100.091")));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("100.01"), new Long(100)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("100.00"), new Long(100)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("100.01"), new Integer(100)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("100.00"), new Integer(100)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Long(100), new BigDecimal("99.99")));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Long(100), new BigDecimal("100.00")));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Integer(100), new BigDecimal("99.9999")));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Integer(100), new BigDecimal("100.00")));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Integer(100), new Integer(99)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Integer(100), new Integer(100)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Integer(100), new Long(99)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Integer(100), new Long(100)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Long(100), new Integer(99)));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(new Long(100), new Integer(100)));

    assertFalse(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("100.09"), new BigDecimal("100.091")));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(new Long(100), new BigDecimal("100.01")));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(new Integer(100), new BigDecimal("100.01")));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("99.99"), new Long(100)));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(new BigDecimal("99.9999"), new Integer(100)));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(new Integer(99), new Integer(100)));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(new Long(99), new Integer(100)));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(new Integer(99), new Long(100)));

    assertTrue(CompareUtil.greaterThanOrEqualsTo("AbcDEFB", "AbcDEF"));
    assertTrue(CompareUtil.greaterThanOrEqualsTo("AbcDEF", "AbcDEF"));
    assertTrue(CompareUtil.greaterThanOrEqualsTo("AbcDEFB", "AbcDEFA"));
    assertTrue(CompareUtil.greaterThanOrEqualsTo("AbcDEFB", "AbcDEFB"));
    assertFalse(CompareUtil.greaterThanOrEqualsTo("AbcDEF", "AbcDEFB"));
    assertFalse(CompareUtil.greaterThanOrEqualsTo("AbcDEFA", "AbcDEFB"));

    Date leftDate    = sdf.parse("20130607");
    Date rightDate   = sdf.parse("20130608");
    Date anotherDate = sdf.parse("20130708");

    assertTrue(CompareUtil.greaterThanOrEqualsTo(rightDate, leftDate));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(leftDate, leftDate));
    assertTrue(CompareUtil.greaterThanOrEqualsTo(anotherDate, rightDate));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(leftDate, rightDate));
    assertFalse(CompareUtil.greaterThanOrEqualsTo(rightDate, anotherDate));
  } // end method testGreaterThanOrEqualsTo

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testLessThan() throws Exception {
    assertTrue(CompareUtil.lessThan(new BigDecimal("100.09"), new BigDecimal("100.091")));
    assertTrue(CompareUtil.lessThan(new Long(100), new BigDecimal("100.01")));
    assertTrue(CompareUtil.lessThan(new Integer(100), new BigDecimal("100.01")));
    assertTrue(CompareUtil.lessThan(new BigDecimal("99.99"), new Long(100)));
    assertTrue(CompareUtil.lessThan(new BigDecimal("99.9999"), new Integer(100)));
    assertTrue(CompareUtil.lessThan(new Integer(99), new Integer(100)));
    assertTrue(CompareUtil.lessThan(new Long(99), new Integer(100)));
    assertTrue(CompareUtil.lessThan(new Integer(99), new Long(100)));

    assertFalse(CompareUtil.lessThan(new BigDecimal("100.091"), new BigDecimal("100.09")));
    assertFalse(CompareUtil.lessThan(new BigDecimal("100.01"), new Long(100)));
    assertFalse(CompareUtil.lessThan(new BigDecimal("100.01"), new Integer(100)));
    assertFalse(CompareUtil.lessThan(new Long(100), new BigDecimal("99.99")));
    assertFalse(CompareUtil.lessThan(new Integer(100), new BigDecimal("99.9999")));
    assertFalse(CompareUtil.lessThan(new Integer(100), new Integer(99)));
    assertFalse(CompareUtil.lessThan(new Integer(100), new Long(99)));
    assertFalse(CompareUtil.lessThan(new Long(100), new Integer(99)));

    assertTrue(CompareUtil.lessThan("AbcDEF", "AbcDEFB"));
    assertTrue(CompareUtil.lessThan("AbcDEFA", "AbcDEFB"));
    assertFalse(CompareUtil.lessThan("AbcDEFB", "AbcDEF"));
    assertFalse(CompareUtil.lessThan("AbcDEFB", "AbcDEFA"));

    Date leftDate    = sdf.parse("20130607");
    Date rightDate   = sdf.parse("20130608");
    Date anotherDate = sdf.parse("20130708");

    assertTrue(CompareUtil.lessThan(leftDate, rightDate));
    assertTrue(CompareUtil.lessThan(rightDate, anotherDate));
    assertFalse(CompareUtil.lessThan(rightDate, leftDate));
    assertFalse(CompareUtil.lessThan(anotherDate, rightDate));
  } // end method testLessThan

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testLessThanOrEqualsTo() throws Exception {
    assertTrue(CompareUtil.lessThanOrEqualsTo(new BigDecimal("100.09"), new BigDecimal("100.091")));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new BigDecimal("100.09"), new BigDecimal("100.09")));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Long(100), new BigDecimal("100.01")));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Long(100), new BigDecimal("100.00")));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Integer(100), new BigDecimal("100.01")));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Integer(100), new BigDecimal("100")));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new BigDecimal("99.99"), new Long(100)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new BigDecimal("100.00"), new Long(100)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new BigDecimal("99.9999"), new Integer(100)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new BigDecimal("100.00"), new Integer(100)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Integer(99), new Integer(100)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Integer(99), new Integer(99)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Long(99), new Integer(100)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Long(99), new Integer(99)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Integer(99), new Long(100)));
    assertTrue(CompareUtil.lessThanOrEqualsTo(new Integer(99), new Long(99)));

    assertFalse(CompareUtil.lessThanOrEqualsTo(new BigDecimal("100.091"), new BigDecimal("100.09")));
    assertFalse(CompareUtil.lessThanOrEqualsTo(new BigDecimal("100.01"), new Long(100)));
    assertFalse(CompareUtil.lessThanOrEqualsTo(new BigDecimal("100.01"), new Integer(100)));
    assertFalse(CompareUtil.lessThanOrEqualsTo(new Long(100), new BigDecimal("99.99")));
    assertFalse(CompareUtil.lessThanOrEqualsTo(new Integer(100), new BigDecimal("99.9999")));
    assertFalse(CompareUtil.lessThanOrEqualsTo(new Integer(100), new Integer(99)));
    assertFalse(CompareUtil.lessThanOrEqualsTo(new Integer(100), new Long(99)));
    assertFalse(CompareUtil.lessThanOrEqualsTo(new Long(100), new Integer(99)));

    assertTrue(CompareUtil.lessThanOrEqualsTo("AbcDEF", "AbcDEFB"));
    assertTrue(CompareUtil.lessThanOrEqualsTo("AbcDEF", "AbcDEF"));
    assertTrue(CompareUtil.lessThanOrEqualsTo("AbcDEFA", "AbcDEFB"));
    assertTrue(CompareUtil.lessThanOrEqualsTo("AbcDEFA", "AbcDEFA"));
    assertFalse(CompareUtil.lessThanOrEqualsTo("AbcDEFB", "AbcDEF"));
    assertFalse(CompareUtil.lessThanOrEqualsTo("AbcDEFB", "AbcDEFA"));

    Date leftDate    = sdf.parse("20130607");
    Date rightDate   = sdf.parse("20130608");
    Date anotherDate = sdf.parse("20130708");

    assertTrue(CompareUtil.lessThanOrEqualsTo(leftDate, rightDate));
    assertTrue(CompareUtil.lessThanOrEqualsTo(leftDate, leftDate));
    assertTrue(CompareUtil.lessThanOrEqualsTo(rightDate, anotherDate));
    assertFalse(CompareUtil.lessThanOrEqualsTo(rightDate, leftDate));
    assertFalse(CompareUtil.lessThanOrEqualsTo(anotherDate, rightDate));
  } // end method testLessThanOrEqualsTo

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testNotEqualsTo() throws Exception {
    String strLeftValue  = "100.08";
    String strRightValue = "100.08";

    assertFalse(CompareUtil.notEqualsTo(new BigDecimal(strLeftValue), new BigDecimal(strRightValue)));
    assertFalse(CompareUtil.notEqualsTo(new BigDecimal("100.00"), new BigDecimal("100")));
    assertFalse(CompareUtil.notEqualsTo(new BigDecimal("100.00"), new Long(100)));
    assertFalse(CompareUtil.notEqualsTo(new BigDecimal("100.00"), new Integer(100)));
    assertFalse(CompareUtil.notEqualsTo(new Long(100), new BigDecimal("100.00")));
    assertFalse(CompareUtil.notEqualsTo(new Integer(100), new BigDecimal("100.00")));
    assertFalse(CompareUtil.notEqualsTo(new Integer(100), new Integer(100)));
    assertFalse(CompareUtil.notEqualsTo(new Integer(100), new Long(100)));
    assertFalse(CompareUtil.notEqualsTo(new Long(100), new Integer(100)));

    assertFalse(CompareUtil.notEqualsTo(Boolean.FALSE, Boolean.FALSE));
    assertTrue(CompareUtil.notEqualsTo(Boolean.FALSE, Boolean.TRUE));

    assertFalse(CompareUtil.notEqualsTo("AbcDEF", "AbcDEF"));
    assertTrue(CompareUtil.notEqualsTo("AbcDEF", "AbcDE"));

    Date leftDate    = sdf.parse("20130608");
    Date rightDate   = sdf.parse("20130608");
    Date anotherDate = sdf.parse("20130708");

    assertFalse(CompareUtil.notEqualsTo(leftDate, rightDate));
    assertTrue(CompareUtil.notEqualsTo(leftDate, anotherDate));
  } // end method testNotEqualsTo

} // end class CompareUtilTests
