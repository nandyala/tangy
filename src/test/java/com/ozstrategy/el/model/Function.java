package com.ozstrategy.el.model;

import java.math.BigDecimal;

import java.util.Calendar;
import java.util.Date;

import com.ozstrategy.el.annotation.OzCacheable;
import com.ozstrategy.el.annotation.OzContextParam;
import com.ozstrategy.el.annotation.OzExposedMethod;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 2/19/14 Time: 5:00 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class Function {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   loanAmount     DOCUMENT ME!
   * @param   loanStartDate  DOCUMENT ME!
   * @param   dates          DOCUMENT ME!
   * @param   amounts        DOCUMENT ME!
   * @param   guess          DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzExposedMethod public static Boolean calcXIRR(BigDecimal loanAmount, Date loanStartDate, Date[] dates,
    BigDecimal[] amounts, BigDecimal guess) {
    if ((loanAmount == null) || (loanStartDate == null) || (dates == null) || (amounts == null) || (guess == null)) {
      return null;
    }

    return true;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   account  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzCacheable @OzExposedMethod public static int countResponsible(@OzContextParam("account") Account account) {
    return account.getResponsibles().size();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   account  DOCUMENT ME!
   * @param   dummy1   DOCUMENT ME!
   * @param   dummy2   DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzCacheable @OzExposedMethod public static int countResponsibleWith2Dummy(@OzContextParam("account") Account account,
    Long dummy1,
    String dummy2) {
    return account.getResponsibles().size();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   account  DOCUMENT ME!
   * @param   dummy    DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzCacheable @OzExposedMethod public static int countResponsibleWithDummy(@OzContextParam("account") Account account,
    String dummy) {
    return account.getResponsibles().size();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   account      DOCUMENT ME!
   * @param   responsible  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzExposedMethod public static int countResponsibleWithResponsible(@OzContextParam("account") Account account,
    @OzContextParam("responsible") Responsible responsible) {
    return account.getResponsibles().size();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   account      DOCUMENT ME!
   * @param   responsible  DOCUMENT ME!
   * @param   dummy1       DOCUMENT ME!
   * @param   dummy2       DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzExposedMethod public static int countResponsibleWithResponsibleAnd2Dummy(
    @OzContextParam("account") Account account,
    @OzContextParam("responsible") Responsible responsible, Long dummy1,
    String dummy2) {
    return account.getResponsibles().size();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   year   DOCUMENT ME!
   * @param   month  DOCUMENT ME!
   * @param   day    DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzCacheable @OzExposedMethod public static Date createDate(Integer year, Integer month, Integer day) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, month - 1, day, 0, 0, 0);

    return cal.getTime();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   state  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzCacheable @OzExposedMethod public static Boolean hasState(String[] state) {
    if ("DE".equalsIgnoreCase(state[0])) {
      return true;
    } else {
      return false;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   date  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @OzExposedMethod public static Boolean isMonday(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY);
  }

} // end class Function
