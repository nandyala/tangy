package com.ozstrategy.el.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections.ListUtils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/7/13 Time: 5:07 PM To change this template use File | Settings | File
 * Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class CalculateUtil {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  public static final List<String> dateTimeFormats = Arrays.asList(
      "yyyy-MM-dd HH:mm:ss",
      "yyyy-MM-dd'T'HH:mm:ss",
      "MM/dd/yyyy HH:mm:ss",
      "MM/dd/yyyy'T'HH:mm:ss");

  /** DOCUMENT ME! */
  public static final List<String> dateOnlyFormats = Arrays.asList(
      "yyyy-MM-dd",
      "MM/dd/yyyy");

  /** DOCUMENT ME! */
  public static final List<String> dateFormats = ListUtils.union(dateTimeFormats, dateOnlyFormats);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * This method is used to add number of business days to a given date. The definition of "business day" is any way
   * that is not Saturday or Sunday.
   *
   * @param   date  DOCUMENT ME!
   * @param   days  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date addBusinessDays(Date date, int days) {
    if (date == null) {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    // Sunday: 1 Saturday :7
    int rest = 7 - dayOfWeek;

    if (days < rest) {
      return addDays(date, days);
    } else if (days == rest) {
      return addDays(date, rest + 2);
    } else {
      date = addDays(date, rest); // handles rest -1 business days

      int businessDaysHandled = 0;

      if (rest > 0) {
        // rest > 0, handled rest -1 business days
        businessDaysHandled = rest - 1;
      }

      int weeks     = (days - businessDaysHandled) / 5;
      int remainder = (days - businessDaysHandled) % 5;

      if (remainder == 0) {
        // remainder == 0, then deduct Sunday
        remainder = -1;
      } else {
        // remainder > 0, then add a Sunday
        remainder += 1;
      }

      return addDays(date, (weeks * 7) + remainder);
    } // end if-else
  }   // end method addBusinessDays

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * This method is used to add number of business days to a given date. The definition of "business day" is any way
   * that is not Saturday or Sunday.
   *
   * @param   strDate  DOCUMENT ME!
   * @param   days     DOCUMENT ME!
   *
   * @return  this method is used to add number of business days to a given date.
   */
  public static Date addBusinessDays(String strDate, int days) {
    if (hasText(strDate)) {
      Date date = toDate(strDate);

      if (date != null) {
        return addBusinessDays(date, days);
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   * @param   days  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date addDays(Date date, int days) {
    if (date == null) {
      return null;
    }

    DateTime d = new DateTime(date);

    return d.plusDays(days).toDate();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   strDate  DOCUMENT ME!
   * @param   days     DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date addDays(String strDate, int days) {
    if (hasText(strDate)) {
      Date date = toDate(strDate);

      if (date != null) {
        return addDays(date, days);
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date    DOCUMENT ME!
   * @param   months  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date addMonths(Date date, int months) {
    if (date == null) {
      return null;
    }

    DateTime d = new DateTime(date);

    return d.plusMonths(months).toDate();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   strDate  DOCUMENT ME!
   * @param   months   DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date addMonths(String strDate, int months) {
    if (hasText(strDate)) {
      Date date = toDate(strDate);

      if (date != null) {
        return addMonths(date, months);
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * FV Returns the future value of an investment FV(rate,nper,pmt,pv,type).
   *
   * <p>Rate is the interest rate per period. Nper is the total number of payment periods in an annuity. Pmt is the
   * payment made each period; it cannot change over the life of the annuity . Typically, pmt contains principal and
   * interest but no other fees or taxes. If pmt is omitted, you must include the pv argument. Pv is the present value,
   * or the lump-sum amount that a series of future payments is worth right now. If pv is omitted, it is assumed to be 0
   * (zero), and you must include the pmt argument. Type is the number 0 or 1 and indicates when payments are due. If
   * type is omitted, it is assumed to be 0.</p>
   *
   * @param   rate  DOCUMENT ME!
   * @param   nper  DOCUMENT ME!
   * @param   pmt   DOCUMENT ME!
   * @param   pv    DOCUMENT ME!
   * @param   type  DOCUMENT ME!
   *
   * @return  fV Returns the future value of an investment FV(rate,nper,pmt,pv,type).
   *
   * @author  kaia
   */
  public static BigDecimal calcFV(BigDecimal rate, Integer nper, BigDecimal pmt, BigDecimal pv, Integer type) {
    if (pv == null) {
      pv = new BigDecimal(0);
    }

    if (type == null) {
      type = 0;
    }

    double dRate = rate.doubleValue();
    double dNper = nper.doubleValue();
    double dPv   = pv.doubleValue();
    double dPmt  = pmt.doubleValue();

    double A = Math.pow(1 + dRate, dNper);
    double B = dPmt * (1 + (dRate * type));
    B *= (Math.pow(1 + dRate, dNper) - 1) / dRate;

    BigDecimal result = new BigDecimal(-((dPv * A) + B));
// BigDecimal A = new BigDecimal(Math.pow(
// (new BigDecimal(1).add(rate)).doubleValue(), nper));
// BigDecimal B = pmt.multiply(
// (new BigDecimal(1).add(rate.multiply(new BigDecimal(type)))));
// B = B.multiply(new BigDecimal(
// Math.pow((new BigDecimal(1).add(rate)).doubleValue(),
// nper)).subtract(new BigDecimal(1)).divide(rate, 8, RoundingMode.HALF_UP));
//
// BigDecimal result = pv.multiply(A).add(B).negate();

    return result;
  } // end method calcFV

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * NPER Returns the number of periods for an investment NPER(rate, pmt, pv, fv, type).
   *
   * <p>Rate is the interest rate per period. Pmt is the payment made each period; it cannot change over the life of the
   * annuity. Typically, pmt contains principal and interest but no other fees or taxes. Pv is the present value, or the
   * lump-sum amount that a series of future payments is worth right now. Fv is the future value, or a cash balance you
   * want to attain after the last payment is made. If fv is omitted, it is assumed to be 0 (the future value of a loan,
   * for example, is 0). Type is the number 0 or 1 and indicates when payments are due.</p>
   *
   * @param   rate  DOCUMENT ME!
   * @param   pmt   DOCUMENT ME!
   * @param   pv    DOCUMENT ME!
   * @param   fv    DOCUMENT ME!
   * @param   type  DOCUMENT ME!
   *
   * @return  nPER Returns the number of periods for an investment NPER(rate, pmt, pv, fv, type).
   */
  public static BigDecimal calcNPER(BigDecimal rate, BigDecimal pmt, BigDecimal pv, BigDecimal fv, Integer type) {
    if (fv == null) {
      fv = new BigDecimal("0.0");
    }

    if ((type == null) || (type == 0)) {
      type = 0;
    } else {
      type = 1;
    }

// BigDecimal A      = (pmt.multiply(
// ((new BigDecimal(1)).add(
// (new BigDecimal(type)).multiply(rate))))).subtract(
// rate.multiply(fv));
// BigDecimal B      = (pmt.multiply(
// ((new BigDecimal(1)).add(
// (new BigDecimal(type)).multiply(rate))))).add(
// rate.multiply(pv));
// BigDecimal C      = (new BigDecimal(1)).add(rate);
// BigDecimal result = new BigDecimal(Math.log(
// A.doubleValue() / B.doubleValue())
// / Math.log(C.doubleValue()));

    double dRate = rate.doubleValue();
    double dPmt  = pmt.doubleValue();
    double dPv   = pv.doubleValue();
    double dFv   = fv.doubleValue();

    double A      = (dPmt * (1 + (type * dRate))) - (dRate * dFv);
    double B      = (dPmt * (1 + (type * dRate))) + (dRate * dPv);
    double C      = 1 + dRate;
    double result = Math.log(A / B) / Math.log(C);

    return new BigDecimal(result);
  } // end method calcNPER

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * NPV(rate,value1,value2, ...) Calculates the net present value of an investment by using a discount rate and a
   * series of future payments (negative values) and income (positive values).
   *
   * <p>Rate is the rate of discount over the length of one period. Value1, value2, ... are 1 to 29 arguments
   * representing the payments and income. Value1, value2, ... must be equally spaced in time and occur at the end of
   * each period</p>
   *
   * <p>Returns the net present value of an investment based on a series of periodic cash flows and a discount rate =
   * Sum(values / (1 + rate) )</p>
   *
   * @param   rate    DOCUMENT ME!
   * @param   values  DOCUMENT ME!
   *
   * @return  nPV(rate, value1, value2, ...) Calculates the net present value of an investment by using a discount rate
   *          and a series of future payments (negative values) and income (positive values).
   *
   * @author  kaia
   */
  public static BigDecimal calcNPV(BigDecimal rate, BigDecimal... values) {
    int n = Math.min(values.length, 30); // at most 30 values
// BigDecimal result = new BigDecimal(0);

    // double rate = PtgCalculator.getDoubleValue(params[0], calcId);
    // int n = Math.min(params.length, 30); // at most 29 values
    // double result = 0;
    // for (int i = 1; i < n; i++) {
    // double valuei = PtgCalculator.getDoubleValue(params[i], calcId);
    // result += valuei / Math.pow(1 + rate, i);
    // }
    // PtgNumber pnum = new PtgNumber(result);

    double dRate  = rate.doubleValue();
    double result = 0.0D;

    for (int i = 1; i <= n; i++) {
      double dValue = values[i - 1].doubleValue();
      result += dValue / (Math.pow(1 + dRate, i));

// result = result.add(values[i - 1].divide(
// new BigDecimal(Math.pow(((new BigDecimal(1)).add(rate)).doubleValue(), i)), 8, RoundingMode.HALF_UP));
    }

    return new BigDecimal(result);
  } // end method calcNPV

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * PMT Returns the periodic payment for an annuity.
   *
   * <p>pmt= - (fv + (1+rate)**nper * pv) * rate / ( ( (1+rate)**nper - 1 ) * (1+rate*type) )</p>
   *
   * <p>if fv or type are omitted they should be treated as 0 values.</p>
   *
   * @param   rate  operands DOCUMENT ME!
   * @param   nper  calcId DOCUMENT ME!
   * @param   pv    DOCUMENT ME!
   * @param   fv    DOCUMENT ME!
   * @param   type  DOCUMENT ME!
   *
   * @return  pMT Returns the periodic payment for an annuity.
   */
  public static BigDecimal calcPMT(BigDecimal rate, BigDecimal nper, BigDecimal pv, BigDecimal fv, BigDecimal type) {
    if (fv == null) {
      fv = new BigDecimal(0);
    }

    if (type == null) {
      type = new BigDecimal(0);
    }

    double dRate = rate.doubleValue();
    double dNper = nper.doubleValue();
    double dPv   = pv.doubleValue();
    double dFv   = fv.doubleValue();
    double dType = type.doubleValue();

    double Rn = Math.pow(1 + dRate, dNper);
    double A  = (-dFv * dRate) - (dPv * Rn * dRate);
    double B  = (Rn - 1) * (1 + (dRate * dType));

    double result = 0.0D;

    if (dRate == 0.0D) {
      result = -(dFv + dPv) / dNper;
    } else {
      result = A / B;
    }

    return new BigDecimal(result);
  } // end method calcPMT

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * PPMT Returns the payment on the principal for an investment for a given period PPMT(rate,per,nper,pv,fv,type) Rate
   * is the interest rate per period. Per specifies the period and must be in the range 1 to nper. Nper is the total
   * number of payment periods in an annuity. Pv is the present value ó the total amount that a series of future
   * payments is worth now. Fv is the future value, or a cash balance you want to attain after the last payment is made.
   * If fv is omitted, it is assumed to be 0 (zero), that is, the future value of a loan is 0. Type is the number 0 or 1
   * and indicates when payments are due
   *
   * @param   rate  operands DOCUMENT ME!
   * @param   per   calcId DOCUMENT ME!
   * @param   nper  DOCUMENT ME!
   * @param   pv    DOCUMENT ME!
   * @param   fv    DOCUMENT ME!
   * @param   type  DOCUMENT ME!
   *
   * @return  pPMT Returns the payment on the principal for an investment for a given period
   *          PPMT(rate,per,nper,pv,fv,type) Rate is the interest rate per period.
   */
  public static BigDecimal calcPPMT(BigDecimal rate, Integer per, Integer nper, BigDecimal pv, BigDecimal fv,
    Integer type) {
    if (fv == null) {
      fv = new BigDecimal(0);
    }

    if (type == null) {
      type = 0;
    }

    BigDecimal result;

    double dRate = rate.doubleValue();
    double dNper = nper.doubleValue();
    double dPv   = pv.doubleValue();
    double dFv   = fv.doubleValue();
    double dType = type.doubleValue();

    // // 1st, get payment for entire period
    double Rn  = Math.pow(1 + dRate, dNper);
    double A   = (-dFv * dRate) - (dPv * Rn * dRate);
    double B   = (Rn - 1) * (1 + (dRate * dType));
    double pmt = A / B;

    double n;

    if (type == 0) {
      n = per;
    } else {
      n = per - 1;
    }

    // FVa
    A =  Math.pow(1 + dRate, n);
    B =  pmt * (1 + (dRate * type));
    B *= (Math.pow(1 + dRate, n) - 1) / dRate;

    double fva = -((dPv * A) + B);

    // FVb
    A =  Math.pow(1 + dRate, n - 1);
    B =  pmt * (1 + (dRate * type));
    B *= (Math.pow(1 + dRate, n - 1) - 1) / dRate;

    double fvb = -((dPv * A) + B);

    result = new BigDecimal(fvb - fva);
//
// BigDecimal Rn = new BigDecimal(Math.pow(
// (new BigDecimal(1).add(rate)).doubleValue(), nper));
// BigDecimal A  = (fv.negate().multiply(rate)).subtract(pv.multiply(Rn).multiply(rate));
// BigDecimal B  = (Rn.subtract(new BigDecimal(1))).multiply(
// (new BigDecimal(1)).add(rate.multiply(new BigDecimal(type))));
//
// BigDecimal pmt = A.divide(B, 8, RoundingMode.HALF_UP);
//
// double n;
//
// if (type == 0) {
// n = per;
// } else {
// n = per - 1;
// }
//
// // FVa
// A = new BigDecimal(Math.pow((new BigDecimal(1).add(rate)).doubleValue(),
// n));
// B = pmt.multiply((new BigDecimal(1).add(
// rate.multiply(new BigDecimal(type)))));
// B = B.multiply(
// ((new BigDecimal(
// Math.pow(
// ((new BigDecimal(1)).add(rate)).doubleValue(),
// n)).subtract(new BigDecimal(1)))).divide(rate, 8, RoundingMode.HALF_UP));
//
// BigDecimal fva = (pv.multiply(A).add(B)).negate();
//
// // FVb
// A = new BigDecimal((Math.pow(
// (new BigDecimal(1).add(rate)).doubleValue(), n - 1)));
// B = pmt.multiply((new BigDecimal(1)).add(
// rate.multiply(new BigDecimal(type))));
// B = B.multiply(
// (new BigDecimal(
// Math.pow(((new BigDecimal(1)).add(rate)).doubleValue(),
// n - 1)).subtract(new BigDecimal(1))).divide(rate, 8, RoundingMode.HALF_UP));
//
// BigDecimal fvb = (pv.multiply(A).add(B)).negate();
// result = fvb.subtract(fva);

    return result;

  } // end method calcPPMT

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * PV(rate,nper,pmt,fv,type) Returns the present value of an investment Rate is the interest rate per period. For
   * example, if you obtain an automobile loan at a 10 percent annual interest rate and make monthly payments, your
   * interest rate per month is 10%/12, or 0.83%. You would enter 10%/12, or 0.83%, or 0.0083, into the formula as the
   * rate. Nper is the total number of payment periods in an annuity. For example, if you get a four-year car loan and
   * make monthly payments, your loan has 4*12 (or 48) periods. You would enter 48 into the formula for nper. Pmt is the
   * payment made each period and cannot change over the life of the annuity. Typically, pmt includes principal and
   * interest but no other fees or taxes. For example, the monthly payments on a $10,000, four-year car loan at 12
   * percent are $263.33. You would enter -263.33 into the formula as the pmt. If pmt is omitted, you must include the
   * fv argument. Fv is the future value, or a cash balance you want to attain after the last payment is made. If fv is
   * omitted, it is assumed to be 0 (the future value of a loan, for example, is 0). For example, if you want to save
   * $50,000 to pay for a special project in 18 years, then $50,000 is the future value. You could then make a
   * conservative guess at an interest rate and determine how much you must save each month. If fv is omitted, you must
   * include the pmt argument. Type is the number 0 or 1 and indicates when payments are due. 0 or omitted At the end of
   * the period 1 At the beginning of the period
   *
   * @param   rate  operands DOCUMENT ME!
   * @param   nper  calcId DOCUMENT ME!
   * @param   pmt   DOCUMENT ME!
   * @param   fv    DOCUMENT ME!
   * @param   type  DOCUMENT ME!
   *
   * @return  pV(rate, nper, pmt, fv, type) Returns the present value of an investment Rate is the interest rate per
   *          period.
   *
   * @author  kaia
   */
  public static BigDecimal calcPV(BigDecimal rate, BigDecimal nper, BigDecimal pmt, BigDecimal fv, Integer type) {
    if (pmt == null) {
      pmt = new BigDecimal(0);
    }

    if (fv == null) {
      fv = new BigDecimal(0);
    }

    if (type == null) {
      type = 0;
    }

// BigDecimal A = new BigDecimal(Math.pow(((new BigDecimal(1)).add(rate)).doubleValue(), nper.doubleValue()));
// BigDecimal B = pmt.multiply((new BigDecimal(1)).add(rate.multiply(new BigDecimal(type))));
// B = B.multiply(
// (new BigDecimal(Math.pow(((new BigDecimal(1)).add(rate)).doubleValue(), nper.doubleValue())).subtract(
// new BigDecimal(1))).divide(rate, 8, RoundingMode.HALF_UP));
//
// BigDecimal result = (fv.negate().subtract(B)).divide(A, 8, RoundingMode.HALF_UP);

    double dRate = rate.doubleValue();
    double dNper = nper.doubleValue();
    double dPmt  = pmt.doubleValue();
    double dFv   = fv.doubleValue();

    double A = Math.pow(1 + dRate, dNper);
    double B = dPmt * (1 + (dRate * type));
    B *= (Math.pow(1 + dRate, dNper) - 1) / dRate;

    BigDecimal result = new BigDecimal((-dFv - B) / A);

    return result;

  } // end method calcPV

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns the interest rate per period of an annuity. RATE is calculated by iteration and can have zero or more
   * solutions. If the successive results of RATE do not converge to within 0.0000001 after 20 iterations, RATE returns
   * the #NUM! error value.
   *
   * @param   nper   is the total number of payment periods in an annuity.
   * @param   pmt    is the payment made each period and cannot change over the life of the annuity. Typically, pmt
   *                 includes principal and interest but no other fees or taxes. If pmt is omitted, you must include the
   *                 fv argument.
   * @param   pv     is the present value — the total amount that a series of future payments is worth now.
   * @param   fv     is the future value, or a cash balance you want to attain after the last payment is made. If fv is
   *                 omitted, it is assumed to be 0 (the future value of a loan, for example, is 0).
   * @param   type   is the number 0 or 1 and indicates when payments are due. 0 or omitted At the end of the period 1
   *                 At the beginning of the period
   * @param   guess  is your guess for what the rate will be. If you omit guess, it is assumed to be 10 percent. If RATE
   *                 does not converge, try different values for guess. RATE usually converges if guess is between 0 and
   *                 1.
   *
   * @return  DOCUMENT ME!
   */
  public static BigDecimal calcRate(BigDecimal nper, BigDecimal pmt, BigDecimal pv, BigDecimal fv, Integer type,
    BigDecimal guess) {
    // FROM MS http://office.microsoft.com/en-us/excel-help/rate-HP005209232.aspx
    int    FINANCIAL_MAX_ITERATIONS = 20;        // Bet accuracy with 128
    double FINANCIAL_PRECISION      = 0.0000001; // 1.0e-8

    double _nper = nper.doubleValue();
    double _pmt  = pmt.doubleValue();
    double _pv   = pv.doubleValue();
    double _fv   = (fv == null) ? 0 : fv.doubleValue();
    double _type = (type == null) ? 0 : type;
// double _guess = ;

    double y;
    double y0;
    double y1;
    double x0;
    double x1    = 0;
    double f     = 0;
    double i     = 0;
    double _rate = (guess == null) ? 0.1 : guess.doubleValue();

    if (Math.abs(_rate) < FINANCIAL_PRECISION) {
      y = (_pv * (1 + (_nper * _rate))) + (_pmt * (1 + (_rate * _type)) * _nper) + _fv;
    } else {
      f = Math.exp(_nper * Math.log(1 + _rate));
      y = (_pv * f) + (_pmt * ((1 / _rate) + _type) * (f - 1)) + _fv;
    }

    y0 = _pv + (_pmt * _nper) + _fv;
    y1 = (_pv * f) + (_pmt * ((1 / _rate) + _type) * (f - 1)) + _fv;

    // find root by Newton secant method
    i  = x0 = 0.0;
    x1 = _rate;

    while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS)) {
      _rate = ((y1 * x0) - (y0 * x1)) / (y1 - y0);
      x0    = x1;
      x1    = _rate;

      if (Math.abs(_rate) < FINANCIAL_PRECISION) {
        y = (_pv * (1 + (_nper * _rate))) + (_pmt * (1 + (_rate * _type)) * _nper) + _fv;
      } else {
        f = Math.exp(_nper * Math.log(1 + _rate));
        y = (_pv * f) + (_pmt * ((1 / _rate) + _type) * (f - 1)) + _fv;
      }

      y0 = y1;
      y1 = y;
      ++i;
    }

    if (Double.isNaN(_rate) || Double.isInfinite(_rate)) {
      return null;
    } else {
      return new BigDecimal(_rate);
    }
  } // end method calcRate

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date1  DOCUMENT ME!
   * @param   date2  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static int compareDateTime(Date date1, Date date2) {
    return date1.compareTo(date2);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   year   DOCUMENT ME!
   * @param   month  DOCUMENT ME!
   * @param   date   DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date createDate(int year, int month, int date) {
    Calendar cal = Calendar.getInstance();
    cal.clear();
    cal.set(year, month - 1, date, 0, 0, 0);

    return cal.getTime();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Is date1 after date2.
   *
   * @param   date1  Date1
   * @param   date2  Date2
   *
   * @return  true - if date1 is equals date2; false - if date1 is not equals date2; null - if any date is null
   */
  public static Boolean dateTimeEqual(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null)) {
      return date1.equals(date2);
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Is date1 after date2.
   *
   * @param   date1  Date1
   * @param   date2  Date2
   *
   * @return  true - if date1 is after date2; false - if date1 is before date2; null - if any date is null
   */
  public static Boolean dateTimeGreater(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null)) {
      return date1.after(date2);
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Is date1 after date2.
   *
   * @param   date1  Date1
   * @param   date2  Date2
   *
   * @return  true - if date1 is after or equals date2; false - if date1 is before date2; null - if any date is null
   */
  public static Boolean dateTimeGreaterOrEqual(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null)) {
      return (date1.after(date2) || date1.equals(date2));
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Is date1 after or equal date2.
   *
   * @param   date1  Date1
   * @param   date2  Date2
   *
   * @return  true - if date1 is before date2; false - if date1 is after date2; null - if any date is null
   */
  public static Boolean dateTimeLess(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null)) {
      return date1.before(date2);
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Is date1 after or equal date2.
   *
   * @param   date1  Date1
   * @param   date2  Date2
   *
   * @return  true - if date1 is before or equals date2; false - if date1 is after date2; null - if any date is null
   */
  public static Boolean dateTimeLessOrEqual(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null)) {
      return (date1.before(date2) || date1.equals(date2));
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get age base on the birthday.
   *
   * @param   birthday  DOCUMENT ME!
   *
   * @return  get age base on the birthday
   */
  public static int getAge(Date birthday) {
    Calendar calendar = Calendar.getInstance();
    int      year     = calendar.get(Calendar.YEAR);
    int      month    = calendar.get(Calendar.MONTH);
    int      date     = calendar.get(Calendar.DATE);

    calendar.setTime(birthday);

    int age = year - calendar.get(Calendar.YEAR);

    // adjust based on month and date
    if (month == calendar.get(Calendar.MONTH)) {
      // need to compare the date
      if (date < calendar.get(Calendar.DATE)) {
        age--;
      }
    } else if (month < calendar.get(Calendar.MONTH)) {
      age--;
    }

    return age;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Calculate the payment day based on frequency and duration.
   *
   * @param   frequency  DOCUMENT ME!
   * @param   duration   DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date getCalculatedFinalInstallmentDueDate(Integer frequency, Integer duration) {
    Date date = new Date();

    if ((frequency != null) && (frequency != 0)) {
      if (frequency > 0) {
        date = addDays(date, frequency * (duration - 1));
      } else if (frequency < 0) {
        date = addMonths(date, ((-frequency) * duration) - 1);
      }
    } else {
      date = addMonths(date, duration - 1);
    }

    return date;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get date of month for today.
   *
   * @return  get date of month for today
   */
  public static int getDateOfMonth() {
    Calendar cal = Calendar.getInstance();

    return cal.get(Calendar.DAY_OF_MONTH);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * get the start date time (00:00:00.000) of the date.
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  get the start date time (00:00:00.000) of the date
   */
  public static Date getDateStart(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static int getDay(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    return cal.get(Calendar.DAY_OF_MONTH);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get date difference.
   *
   * @param   start  DOCUMENT ME!
   * @param   end    DOCUMENT ME!
   *
   * @return  get date difference
   */
  public static int getDayDifference(Date start, Date end) {
    return getDayDifference(start, end, true);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get date difference.
   *
   * @param   start            DOCUMENT ME!
   * @param   end              DOCUMENT ME!
   * @param   isAbsoluteValue  DOCUMENT ME!
   *
   * @return  get date difference
   */
  public static int getDayDifference(Date start, Date end,
    boolean isAbsoluteValue) {
    DateTime startDateTime = new DateTime(toDateOnly(start));
    DateTime endDateTime   = new DateTime(toDateOnly(end));

    Days days       = Days.daysBetween(endDateTime, startDateTime);
    int  difference = days.getDays();

    if (isAbsoluteValue) {
      return Math.abs(difference);
    } else {
      return difference;
    }
  } // end method getDayDifference

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer getDayOfDate(Date date) {
    if (date == null) {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   strDate  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer getDayOfDate(String strDate) {
    if (hasText(strDate)) {
      Date date = toDate(strDate);

      if (date != null) {
        return getDayOfDate(date);
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer getDaysInMonth(Date date) {
    if (date == null) {
      return null;
    }

    return getLastDayOfMonth(date);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static int getLastDayOfCurrentMonth() {
    return getLastDayOfMonth(new Date());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer getLastDayOfMonth(Date date) {
    if (date == null) {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);

    calendar.add(Calendar.DAY_OF_MONTH, -1);

    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   strDate  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer getLastDayOfMonth(String strDate) {
    if (hasText(strDate)) {
      Date date = toDate(strDate);

      if (date != null) {
        return getLastDayOfMonth(date);
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static int getMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    return cal.get(Calendar.MONTH) + 1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get week difference, Note: Sunday is the first day of a week if start day is Saturday, the end day is Sunday then
   * should return 1, consider one week different
   *
   * @param   start  DOCUMENT ME!
   * @param   end    DOCUMENT ME!
   *
   * @return  get week difference, Note:
   */
  public static int getWeekDifference(Date start, Date end) {
    return getWeekDifference(start, end, true);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get week difference, Note: Sunday is the first day of a week if start day is Saturday, the end day is Sunday then
   * should return 1, consider one week different
   *
   * @param   start            DOCUMENT ME!
   * @param   end              DOCUMENT ME!
   * @param   isAbsoluteValue  DOCUMENT ME!
   *
   * @return  get week difference, Note:
   */
  public static int getWeekDifference(Date start, Date end,
    boolean isAbsoluteValue) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(start);

    int dateOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    int difDay     = getDayDifference(start, end, isAbsoluteValue);

    return (dateOfWeek - 1 + difDay) / 7;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static int getYear(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    return cal.get(Calendar.YEAR);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   str  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static boolean hasText(String str) {
    return ((str == null) || str.trim().equals("")) ? false : true;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   string     DOCUMENT ME!
   * @param   subStr     DOCUMENT ME!
   * @param   fromIndex  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer indexOf(String string, String subStr, Integer fromIndex) {
    if ((string == null) || (subStr == null)) {
      return null;
    }

    if ((fromIndex == null) || (fromIndex < 0)) {
      return string.indexOf(subStr);
    }

    return string.indexOf(subStr, fromIndex);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean inList(String... values) {
    if (values[0] == null) {
      return false;
    } else {
      for (int i = 1; i < values.length; i++) {
        if (values[0].equals(values[i])) {
          return true;
        }
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   str1  DOCUMENT ME!
   * @param   str2  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean inString(String str1, String str2) {
    return (str1.indexOf(str2) >= 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Friday of even week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Friday of even week, same week is the first week
   */
  public static boolean isEvenFriday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) != 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Monday of even week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Monday of even week, same week is the first week
   */
  public static boolean isEvenMonday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) != 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Saturday of even week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Saturday of even week, same week is the first week
   */
  public static boolean isEvenSaturday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) != 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Sunday of even week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Sunday of even week, same week is the first week
   */
  public static boolean isEvenSunday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) != 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Thursday of even week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Thursday of even week, same week is the first week
   */
  public static boolean isEvenThursday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.THURSDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) != 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Tuesday of even week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Tuesday of even week, same week is the first week
   */
  public static boolean isEvenTuesday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.TUESDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) != 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Wednesday of even week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Wednesday of even week, same week is the first week
   */
  public static boolean isEvenWednesday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.WEDNESDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) != 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Friday.
   *
   * @return  check whether today is Friday
   */
  public static boolean isFriday() {
    Calendar calendar = Calendar.getInstance();

    return (Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

// ---------------------------------------------------------------------------------------------------

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Monday.
   *
   * @return  check whether today is Monday
   */
  public static boolean isMonday() {
    Calendar calendar = Calendar.getInstance();

    return (Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Friday of odd week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Friday of odd week, same week is the first week
   */
  public static boolean isOddFriday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) == 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Monday of odd week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Monday of odd week, same week is the first week
   */
  public static boolean isOddMonday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) == 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Saturday of odd week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Saturday of odd week, same week is the first week
   */
  public static boolean isOddSaturday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) == 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Sunday of odd week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Sunday of odd week, same week is the first week
   */
  public static boolean isOddSunday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) == 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Thursday of odd week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Thursday of odd week, same week is the first week
   */
  public static boolean isOddThursday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.THURSDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) == 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Tuesday of odd week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Tuesday of odd week, same week is the first week
   */
  public static boolean isOddTuesday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.TUESDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) == 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Wednesday of odd week, same week is the first week.
   *
   * @param   boarding  DOCUMENT ME!
   *
   * @return  check whether today is Wednesday of odd week, same week is the first week
   */
  public static boolean isOddWednesday(Date boarding) {
    Calendar calendar = Calendar.getInstance();

    if (Calendar.WEDNESDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
      if ((getWeekDifference(boarding, new Date()) % 2) == 0) {
        // today is an odd week from boarding date
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Saturday.
   *
   * @return  check whether today is Saturday
   */
  public static boolean isSaturday() {
    Calendar calendar = Calendar.getInstance();

    return (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------
  // Added by Etisbew on 09/11/2009 for CTA-Start

  /**
   * Check whether the given date is Saturday.
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  check whether today is Saturday
   *
   *          <p>returns true if it is saturday</p>
   */
  public static boolean isSaturday(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    return (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Sunday.
   *
   * @return  check whether today is Sunday
   */
  public static boolean isSunday() {
    Calendar calendar = Calendar.getInstance();

    return (Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Sunday.
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  check whether today is Sunday
   */
  public static boolean isSunday(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    return (Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // Added by Etisbew on 09/11/2009 for CTA-End

  /**
   * Check whether today is Thursday.
   *
   * @return  check whether today is Thursday
   */
  public static boolean isThursday() {
    Calendar calendar = Calendar.getInstance();

    return (Calendar.THURSDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date1  DOCUMENT ME!
   * @param   date2  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean isTimeEarlier(Date date1, Date date2) {
    return (date1.compareTo(date2) < 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date1  DOCUMENT ME!
   * @param   date2  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean isTimeLater(Date date1, Date date2) {
    return (date1.compareTo(date2) > 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date1  DOCUMENT ME!
   * @param   date2  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean isTimeSame(Date date1, Date date2) {
    return (date1.compareTo(date2) == 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Tuesday.
   *
   * @return  check whether today is Tuesday
   */
  public static boolean isTuesday() {
    Calendar calendar = Calendar.getInstance();

    return (Calendar.TUESDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   dateString  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static boolean isValidDateOnlyString(String dateString) {
    if (hasText(dateString)) {
      for (String dateFormat : dateOnlyFormats) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        try {
          Date testDate = sdf.parse(trim(dateString));

          if (!sdf.format(testDate).equals(trim(dateString))) {
            return false;
          }

          return true;
        } catch (ParseException e) {
          // not match for this pattern, try next one
        }
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   dateString  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static boolean isValidDateString(String dateString) {
    if (hasText(dateString)) {
      if (isValidDateTimeString(dateString)) {
        return true;
      } else if (isValidDateOnlyString(dateString)) {
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   dateString  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static boolean isValidDateTimeString(String dateString) {
    if (hasText(dateString)) {
      for (String dateFormat : dateTimeFormats) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        try {
          Date testDate = sdf.parse(trim(dateString));

          if (!sdf.format(testDate).equals(trim(dateString))) {
            return false;
          }

          return true;
        } catch (ParseException e) {
          // not match for this pattern, try next one
        }
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is Wednesday.
   *
   * @return  check whether today is Wednesday
   */
  public static boolean isWednesday() {
    Calendar calendar = Calendar.getInstance();

    return (Calendar.WEDNESDAY == calendar.get(Calendar.DAY_OF_WEEK));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is weekday.
   *
   * @return  check whether today is weekday
   */
  public static boolean isWeekday() {
    return isMonday() || isTuesday()
      || isWednesday() || isThursday()
      || isFriday();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check whether today is weekend.
   *
   * @return  check whether today is weekend
   */
  public static boolean isWeekend() {
    return isSunday() || isSaturday();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   string     DOCUMENT ME!
   * @param   subStr     DOCUMENT ME!
   * @param   fromIndex  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer lastIndexOf(String string, String subStr, Integer fromIndex) {
    if ((string == null) || (subStr == null)) {
      return null;
    }

    if ((fromIndex == null) || (fromIndex < 0)) {
      return string.lastIndexOf(subStr);
    }

    return string.lastIndexOf(subStr, fromIndex);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns the leftmost length characters of string.
   *
   * @param   str     DOCUMENT ME!
   * @param   length  DOCUMENT ME!
   *
   * @return  returns the leftmost length characters of string.
   */
  public static String left(String str, int length) {
    if (str == null) {
      return null;
    }

    if (length <= 0) {
      return "";
    } else if (length > str.length()) {
      return str;
    } else {
      return str.substring(0, length);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * return the max value.
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  return the max value
   */
  public static Double max(Double... values) {
    try {
      Double maxValue = values[0];

      for (Double value : values) {
        if (maxValue < value) {
          maxValue = value;
        }
      }

      return maxValue;
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer max(Integer... values) {
    try {
      Integer maxValue = values[0];

      for (Integer value : values) {
        if (maxValue < value) {
          maxValue = value;
        }
      }

      return maxValue;
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Long max(Long... values) {
    try {
      Long maxValue = values[0];

      for (Long value : values) {
        if (maxValue < value) {
          maxValue = value;
        }
      }

      return maxValue;
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static BigDecimal max(BigDecimal... values) {
    try {
      BigDecimal maxValue = values[0];

      for (BigDecimal value : values) {
        if (maxValue.compareTo(value) < 0) {
          maxValue = value;
        }
      }

      return maxValue;
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * From the string, returns a certain number of characters, beginning at a certain character. The length argument
   * determines the number of characters returned, and the offset argument determines the beginning point. Counting is
   * zero based - the first character in the string is considered to be at position 1.
   *
   * @param   str     DOCUMENT ME!
   * @param   offset  DOCUMENT ME!
   * @param   length  DOCUMENT ME!
   *
   * @return  from the string, returns a certain number of characters, beginning at a certain character.
   */
  public static String mid(String str, int offset, int length) {
    if (str == null) {
      return null;
    }

    offset--;

    // the offset passed in from user should be 1..length
    if (offset < 0) {
      offset = 0;
    }

    if (offset > str.length()) {
      // the offset out of the string length, return empty
      return "";
    } else if ((length > str.length())
          || ((offset + length) > str.length())) {
      return str.substring(offset);
    }

    return str.substring(offset, offset + length);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * return the min value.
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  return the min value
   */
  public static BigDecimal min(BigDecimal... values) {
    try {
      BigDecimal minValue = values[0];

      for (BigDecimal value : values) {
        if (minValue.compareTo(value) > 0) {
          minValue = value;
        }
      }

      return minValue;
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static double min(double... values) {
    double minValue = values[0];

    for (double value : values) {
      if (minValue > value) {
        minValue = value;
      }
    }

    return minValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static int min(int... values) {
    int minValue = values[0];

    for (int value : values) {
      if (minValue > value) {
        minValue = value;
      }
    }

    return minValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   values  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static long min(long... values) {
    long minValue = values[0];

    for (long value : values) {
      if (minValue > value) {
        minValue = value;
      }
    }

    return minValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   * @param   days  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date minusDays(Date date, int days) {
    if (date == null) {
      return null;
    }

    DateTime d = new DateTime(date);

    return d.minusDays(days).toDate();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date    DOCUMENT ME!
   * @param   months  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date minusMonths(Date date, int months) {
    if (date == null) {
      return null;
    }

    DateTime d = new DateTime(date);

    return d.minusMonths(months).toDate();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get current time.
   *
   * @return  get current time
   */
  public static Date now() {
    return new Date();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   from  DOCUMENT ME!
   * @param   to    DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static int randomIntGenerator(int from, int to) {
    return (int) (Math.round((Math.random() * (to + 1 - from)) + from));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns the rightmost length characters of the string.
   *
   * @param   str     DOCUMENT ME!
   * @param   length  DOCUMENT ME!
   *
   * @return  returns the rightmost length characters of the string
   */
  public static String right(String str, int length) {
    if (str == null) {
      return null;
    }

    if (length <= 0) {
      return "";
    } else if (length > str.length()) {
      return str;
    } else {
      return str.substring(str.length() - length);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value   DOCUMENT ME!
   * @param   digits  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static BigDecimal round(BigDecimal value, int digits) {
    return value.setScale(digits, BigDecimal.ROUND_HALF_UP);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value   DOCUMENT ME!
   * @param   digits  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static BigDecimal roundDown(BigDecimal value, int digits) {
    return value.setScale(digits, BigDecimal.ROUND_DOWN);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Long roundDownInteger(BigDecimal value) {
    return value.setScale(0, BigDecimal.ROUND_DOWN).longValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Long roundInteger(BigDecimal value) {
    return value.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value   DOCUMENT ME!
   * @param   digits  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static BigDecimal roundUp(BigDecimal value, int digits) {
    return value.setScale(digits, BigDecimal.ROUND_UP);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Long roundUpInteger(BigDecimal value) {
    return value.setScale(0, BigDecimal.ROUND_UP).longValue();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns the sub string from start position to end position Positions are 1 based.
   *
   * @param   str         DOCUMENT ME!
   * @param   startIndex  DOCUMENT ME!
   * @param   endIndex    DOCUMENT ME!
   *
   * @return  returns the sub string from start position to end position Positions are 1 based.
   */
// public static String substr(String str, int startIndex, int endIndex) {
// return substring(str,startIndex,endIndex);
// }
//
// public static String range(String str, int startIndex, int endIndex) {
// return substring(str,startIndex,endIndex);
// }

  /**
   * Returns the sub string from start position to end position Positions are 1 based.
   *
   * @param   str         DOCUMENT ME!
   * @param   startIndex  DOCUMENT ME!
   * @param   endIndex    DOCUMENT ME!
   *
   * @return  returns the sub string from start position to end position Positions are 1 based.
   */
  public static String substring(String str, int startIndex, int endIndex) {
    if (str == null) {
      return null;
    }

    // adjust to 0 based
    startIndex--;
    endIndex--;

    if ((startIndex >= str.length()) || (startIndex > endIndex)) {
      return "";
    }

    if (startIndex < 0) {
      startIndex = 0;
    }

    if (endIndex < 0) {
      endIndex = 0;
    }

    if ((endIndex + 1) >= str.length()) {
      return str.substring(startIndex);
    } else {
      return str.substring(startIndex, endIndex + 1);
    }
  } // end method substring

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static BigDecimal toBigDecimal(Object value) {
    if (value == null) {
      return null;
    }

    try {
      if (value instanceof BigDecimal) {
        return (BigDecimal) value;
      }

      return new BigDecimal(value.toString());
    } catch (RuntimeException e) {
      // Could not be convert
    } // end try-catch

    return null;
  } // end method toBigDecimal

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   dataString  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date toDate(String dataString) {
    if (hasText(dataString)) {
      for (String dateFormat : dateFormats) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        try {
          Date date = sdf.parse(trim(dataString));

          return date;
        } catch (ParseException e) {
          // not match for this pattern, try next one
        }
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date toDateOnly(Date date) {
    return toDateOnly(date, 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date       DOCUMENT ME!
   * @param   hourOfDay  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Date toDateOnly(Date date, int hourOfDay) {
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal.getTime();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get today.
   *
   * @return  get today
   */
  public static Date today() {
    return toDateOnly(new Date());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Integer toInteger(Object value) {
    if (value == null) {
      return null;
    }

    try {
      if (value instanceof Integer) {
        return (Integer) value;
      } else {
        return Integer.valueOf(value.toString());
      }
    } catch (NumberFormatException e) {
      // Could be convert to Integer
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Long toLong(Object value) {
    if (value == null) {
      return null;
    }

    try {
      if (value instanceof Long) {
        return (Long) value;
      } else {
        return Long.valueOf(value.toString());
      }
    } catch (NumberFormatException e) {
      // Could be convert to Integer
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get tommorrow.
   *
   * @return  get tommorrow
   */
  public static Date tomorrow() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, 1);

    return toDateOnly(cal.getTime());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   input  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static String trim(String input) {
    if (input != null) {
      String output = input.trim();
      output = output.replaceAll("^\"*\'*\"*|\"*\'*\"*$", "");

      return output;
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get yesterday.
   *
   * @return  get yesterday
   */
  public static Date yesterday() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);

    return toDateOnly(cal.getTime());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // ~
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  private static LocalDate getDateOnlyDate(Date date) {
    if (date == null) {
      return null;
    }

    // Tried to use LocalDate now = new LocalDate(). However, in production
    // server Ubuntu 7.1 + jdk 1.6.0_03-b05, the LocalDate is always one day
    // in
    // advance.
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTime(date);

    LocalDate ld = new LocalDate(cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

    return ld;
  }
} // end class CalculateUtil
