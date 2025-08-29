package com.ozstrategy.el.model;

/**
 * Created with IntelliJ IDEA. User: rojer Date: 10/11/13 Time: 10:38 AM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class AccountDetail extends BaseObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected Integer accountAge = 10;

  /** DOCUMENT ME! */
  protected String accountDetailCode = "Detail";

  /** DOCUMENT ME! */
  protected Long accountDetailId;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new AccountDetail object.
   */
  public AccountDetail() {
    accountDetailId = ++idGen;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Integer getAccountAge() {
    return accountAge;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getAccountDetailCode() {
    return accountDetailCode;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountAge  DOCUMENT ME!
   */
  public void setAccountAge(Integer accountAge) {
    this.accountAge = accountAge;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountDetailCode  DOCUMENT ME!
   */
  public void setAccountDetailCode(String accountDetailCode) {
    this.accountDetailCode = accountDetailCode;
  }
} // end class AccountDetail
