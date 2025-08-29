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
public class ExcelUtilTests extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  String DATE_FORMAT = "yyyyMMdd";

  /** DOCUMENT ME! */
  SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

  /** DOCUMENT ME! */
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * testFV.
   *
   * @throws  Exception  exception
   */
  public void testFV() throws Exception {
    BigDecimal result;
    result = CalculateUtil.calcFV(new BigDecimal(0.10 / 4), 16, new BigDecimal("-2000"), new BigDecimal("0"), 1);
    result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
    assertEquals("39729.4609", result.toPlainString());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testNPER.
   *
   * @throws  Exception  exception
   */
  public void testNPER() throws Exception {
    BigDecimal result;

    result = CalculateUtil.calcNPER(new BigDecimal(0.06 / 4), new BigDecimal("-1200"), new BigDecimal("9000"),
        new BigDecimal("5000"), 1);
    result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
    assertEquals("11.9037", result.toPlainString());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testNPV.
   *
   * @throws  Exception  exception
   */
  public void testNPV() throws Exception {
    BigDecimal result;

    result = CalculateUtil.calcNPV(new BigDecimal("0.02"), new BigDecimal("-5000"), new BigDecimal("800"),
        new BigDecimal("950"), new BigDecimal("1080"), new BigDecimal("1120"), new BigDecimal("1500"));
    result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
    assertEquals("106.3091", result.toPlainString());

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testPMT.
   *
   * @throws  Exception  exception
   */
  public void testPMT() throws Exception {
    BigDecimal result;
    result = CalculateUtil.calcPMT(new BigDecimal(0.035 / 4), new BigDecimal("8"), new BigDecimal("0"),
        new BigDecimal("5000"), new BigDecimal("1"));
    result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
    assertEquals("-600.8520", result.toPlainString());

    result = CalculateUtil.calcPMT(new BigDecimal(0), new BigDecimal("100"), new BigDecimal("2117.48"),
        null, null);
    result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
    assertEquals("-21.1748", result.toPlainString());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testPPMT.
   *
   * @throws  Exception  exception
   */
  public void testPPMT() throws Exception {
    BigDecimal result;
    result = CalculateUtil.calcPPMT(new BigDecimal(0.035 / 4), 1, 8, new BigDecimal("0"), new BigDecimal("5000"), 1);
    result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
    assertEquals("-600.8520", result.toPlainString());

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testPV.
   *
   * @throws  Exception  exception
   */
  public void testPV() throws Exception {
    BigDecimal result;
    result = CalculateUtil.calcPV(new BigDecimal(0.1 / 4), new BigDecimal("16"), new BigDecimal("2000"),
        new BigDecimal("0"), 1);
    result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
    assertEquals("-26762.7555", result.toPlainString());

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * testRate.
   *
   * @throws  Exception  exception
   */
  public void testRate() throws Exception {
    BigDecimal result;
    result = CalculateUtil.calcRate(new BigDecimal("24"), new BigDecimal("-800"), new BigDecimal("0"),
        new BigDecimal("20000"), 1, new BigDecimal("0.1"));
    result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
    assertEquals("0.0033", result.toPlainString());
  }
} // end class ExcelUtilTests
