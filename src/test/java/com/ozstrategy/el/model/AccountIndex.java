package com.ozstrategy.el.model;

/**
 * Created with IntelliJ IDEA. User: rojer Date: 10/11/13 Time: 10:38 AM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class AccountIndex extends BaseObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected String accountIndexCode = "Index";

  /** DOCUMENT ME! */
  protected Long accountIndexId;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new AccountDetail object.
   */
  public AccountIndex() {
    accountIndexId = ++idGen;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------


  //~ ------------------------------------------------------------------------------------------------------------------
  public String getAccountIndexCode() {
    return accountIndexCode;
  }

  public void setAccountIndexCode(String accountIndexCode) {
    this.accountIndexCode = accountIndexCode;
  }

  public Long getAccountIndexId() {
    return accountIndexId;
  }

  public void setAccountIndexId(Long accountIndexId) {
    this.accountIndexId = accountIndexId;
  }
} // end class AccountDetail
