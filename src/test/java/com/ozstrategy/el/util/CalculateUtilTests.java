package com.ozstrategy.el.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/7/13 Time: 10:23 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class CalculateUtilTests extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  String DATE_FORMAT = "yyyyMMdd";

  /** DOCUMENT ME! */
  SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

  /** DOCUMENT ME! */
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testAddBusinessDays() throws Exception {
    String strStartDate = "20130607";
    Date   srcDate      = sdf.parse(strStartDate);

    if (logger.isDebugEnabled()) {
      logger.debug("From Date: " + strStartDate);
    }

    // get next work date
    Date targetBizDay = CalculateUtil.addBusinessDays(srcDate, 1);
    assertEquals("20130610", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addBusinessDays(srcDate, 2);
    assertEquals("20130611", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addBusinessDays(srcDate, 7);
    assertEquals("20130618", sdf.format(targetBizDay));

    strStartDate = "20130608";
    srcDate      = sdf.parse(strStartDate);

    if (logger.isDebugEnabled()) {
      logger.debug("From Date: " + strStartDate);
    }

    targetBizDay = CalculateUtil.addBusinessDays(srcDate, 1);
    assertEquals("20130610", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addBusinessDays(srcDate, 2);
    assertEquals("20130611", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addBusinessDays(srcDate, 7);
    assertEquals("20130618", sdf.format(targetBizDay));

  } // end method testAddBusinessDays

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testAddDays() throws Exception {
    String strStartDate = "20130607";
    Date   srcDate      = sdf.parse(strStartDate);

    if (logger.isDebugEnabled()) {
      logger.debug("From Date: " + strStartDate);
    }

    // get next work date
    Date targetBizDay = CalculateUtil.addDays(srcDate, 1);
    assertEquals("20130608", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addDays(srcDate, 2);
    assertEquals("20130609", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addDays(srcDate, 7);
    assertEquals("20130614", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addDays(srcDate, 30);
    assertEquals("20130707", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addDays(srcDate, 60);
    assertEquals("20130806", sdf.format(targetBizDay));
  } // end method testAddDays

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testAddMonths() throws Exception {
    String strStartDate = "20130607";
    Date   srcDate      = sdf.parse(strStartDate);

    if (logger.isDebugEnabled()) {
      logger.debug("From Date: " + strStartDate);
    }

    // get next work date
    Date targetBizDay = CalculateUtil.addMonths(srcDate, 1);
    assertEquals("20130707", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addMonths(srcDate, 2);
    assertEquals("20130807", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addMonths(srcDate, 12);
    assertEquals("20140607", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addMonths(srcDate, 60);
    assertEquals("20180607", sdf.format(targetBizDay));

    strStartDate = "20130731";
    srcDate      = sdf.parse(strStartDate);

    if (logger.isDebugEnabled()) {
      logger.debug("From Date: " + strStartDate);
    }

    targetBizDay = CalculateUtil.addMonths(srcDate, 1);
    assertEquals("20130831", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.addMonths(srcDate, 2);
    assertEquals("20130930", sdf.format(targetBizDay));
  } // end method testAddMonths

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testDateCompare() throws Exception {
    Date date1 = new Date();
    Thread.sleep(1000);

    Date date2 = new Date();

    assertFalse(CalculateUtil.dateTimeEqual(date1, date2));
    assertFalse(CalculateUtil.dateTimeGreater(date1, date2));
    assertFalse(CalculateUtil.dateTimeGreaterOrEqual(date1, date2));
    assertTrue(CalculateUtil.dateTimeLess(date1, date2));
    assertTrue(CalculateUtil.dateTimeLessOrEqual(date1, date2));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testGetDateOnlyDate() throws Exception { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testGetDayDifference() throws Exception {
    String strStartDate = "20130607";
    Date   startDate    = sdf.parse(strStartDate);
    String strEndDate   = "20130608";
    Date   endDate      = sdf.parse(strEndDate);

    if (logger.isDebugEnabled()) {
      logger.debug("Start Date: '" + strStartDate + "' - End Date: '" + strEndDate + "'");
    }

    assertEquals(1, CalculateUtil.getDayDifference(startDate, endDate));
    assertEquals(-1, CalculateUtil.getDayDifference(startDate, endDate, false));
    assertEquals(1, CalculateUtil.getDayDifference(endDate, startDate));
    assertEquals(1, CalculateUtil.getDayDifference(endDate, startDate, true));
    assertEquals(1, CalculateUtil.getDayDifference(endDate, startDate, false));

    strEndDate = "20130707";
    endDate    = sdf.parse(strEndDate);

    if (logger.isDebugEnabled()) {
      logger.debug("Start Date: '" + strStartDate + "' - End Date: '" + strEndDate + "'");
    }

    assertEquals(30, CalculateUtil.getDayDifference(startDate, endDate));
    assertEquals(-30, CalculateUtil.getDayDifference(startDate, endDate, false));
    assertEquals(30, CalculateUtil.getDayDifference(endDate, startDate));
    assertEquals(30, CalculateUtil.getDayDifference(endDate, startDate, true));
    assertEquals(30, CalculateUtil.getDayDifference(endDate, startDate, false));
  } // end method testGetDayDifference

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testIsDateOnlyString() throws Exception {
    assertTrue(CalculateUtil.isValidDateOnlyString("2013-06-07"));
    assertTrue(CalculateUtil.isValidDateOnlyString("2013-12-30"));
    assertTrue(CalculateUtil.isValidDateOnlyString("12/31/2013"));

    assertFalse(CalculateUtil.isValidDateOnlyString("20130607"));
    assertFalse(CalculateUtil.isValidDateOnlyString("31/12/2013"));

    assertFalse(CalculateUtil.isValidDateOnlyString("2013-06-07 12:30:00"));
    assertFalse(CalculateUtil.isValidDateOnlyString("2013-06-07T12:30:00"));
    assertFalse(CalculateUtil.isValidDateOnlyString("12/31/2013 12:30:00"));
    assertFalse(CalculateUtil.isValidDateOnlyString("12/31/2013T12:30:00"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testIsDateTimeString() throws Exception {
    assertTrue(CalculateUtil.isValidDateTimeString("2013-06-07 12:30:00"));
    assertTrue(CalculateUtil.isValidDateTimeString("2013-06-07T12:30:00"));
    assertTrue(CalculateUtil.isValidDateTimeString("12/31/2013 12:30:00"));
    assertTrue(CalculateUtil.isValidDateTimeString("12/31/2013T12:30:00"));

    assertFalse(CalculateUtil.isValidDateTimeString("12/31/2013"));
    assertFalse(CalculateUtil.isValidDateTimeString("20130607"));
    assertFalse(CalculateUtil.isValidDateTimeString("31/12/2013"));
    assertFalse(CalculateUtil.isValidDateTimeString("2013-12-30"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testMinusDays() throws Exception {
    String strStartDate = "20130607";
    Date   srcDate      = sdf.parse(strStartDate);

    if (logger.isDebugEnabled()) {
      logger.debug("From Date: " + strStartDate);
    }

    // get next work date
    Date targetBizDay = CalculateUtil.minusDays(srcDate, 1);
    assertEquals("20130606", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.minusDays(srcDate, 2);
    assertEquals("20130605", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.minusDays(srcDate, 7);
    assertEquals("20130531", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.minusDays(srcDate, 30);
    assertEquals("20130508", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.minusDays(srcDate, 60);
    assertEquals("20130408", sdf.format(targetBizDay));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testMinusMonths() throws Exception {
    String strStartDate = "20130607";
    Date   srcDate      = sdf.parse(strStartDate);

    if (logger.isDebugEnabled()) {
      logger.debug("From Date: " + strStartDate);
    }

    // get next work date
    Date targetBizDay = CalculateUtil.minusMonths(srcDate, 1);
    assertEquals("20130507", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.minusMonths(srcDate, 2);
    assertEquals("20130407", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.minusMonths(srcDate, 12);
    assertEquals("20120607", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.minusMonths(srcDate, 60);
    assertEquals("20080607", sdf.format(targetBizDay));

    strStartDate = "20130731";
    srcDate      = sdf.parse(strStartDate);

    if (logger.isDebugEnabled()) {
      logger.debug("From Date: " + strStartDate);
    }

    targetBizDay = CalculateUtil.minusMonths(srcDate, 1);
    assertEquals("20130630", sdf.format(targetBizDay));

    targetBizDay = CalculateUtil.minusMonths(srcDate, 2);
    assertEquals("20130531", sdf.format(targetBizDay));
  } // end method testMinusMonths

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testToDateOnly() throws Exception {
    String           dateFormat = "yyyyMMdd HH:mm:ss";
    SimpleDateFormat sdf        = new SimpleDateFormat(dateFormat);

    Date srcDate = sdf.parse("20130607 13:14:15");
    Date dstDate = CalculateUtil.toDateOnly(srcDate);
    assertEquals("20130607 00:00:00", sdf.format(dstDate));

    dstDate = CalculateUtil.toDateOnly(srcDate, 9);
    assertEquals("20130607 09:00:00", sdf.format(dstDate));
  }
} // end class CalculateUtilTests
