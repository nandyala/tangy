package com.ozstrategy.el.model;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/20/13 Time: 5:47 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class Account extends BaseObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected Date accountDate = new Date();

  /** DOCUMENT ME! */
  protected Integer accountDays = 20;

  /** DOCUMENT ME! */
  protected AccountDetail accountDetail = new AccountDetail();

  /** DOCUMENT ME! */
  protected AccountIndex accountIndex = null;

  /** DOCUMENT ME! */
  protected Long accountNum;

  /** DOCUMENT ME! */
  protected String accountStatus = "Active";

  /** DOCUMENT ME! */
  protected BigDecimal balance = new BigDecimal("999.99");

  /** DOCUMENT ME! */
  protected Boolean hasPayment = false;

  /** DOCUMENT ME! */
  protected boolean oldAccount = true;

  /** DOCUMENT ME! */
  protected BigDecimal pastDue = new BigDecimal("100");

  /** DOCUMENT ME! */
  protected List<Responsible> responsibles = new ArrayList<Responsible>();

  protected Map<String, Responsible> responsibleMap = new LinkedHashMap<>();
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new Account object.
   */
  public Account() {
    this.accountNum = ++idGen;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  responsible  DOCUMENT ME!
   */
  public void addResponsible(Responsible responsible) {
    responsible.setAccount(this);
    this.responsibles.add(responsible);
    this.responsibleMap.put(responsible.getFirstName(), responsible);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal curBalance() {
    return balance;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Date getAccountDate() {
    return accountDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Integer getAccountDays() {
    return accountDays;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public AccountDetail getAccountDetail() {
    return accountDetail;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Long getAccountNum() {
    return accountNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getAccountStatus() {
    return accountStatus;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal getBalance() {
    return balance;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   age  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Responsible getFirstResponsibleOlderThan(int age) {
    for (Responsible responsible : responsibles) {
      if (responsible.getAge() >= age) {
        return responsible;
      }
    }

    return null;
  }

  public Responsible getFirstResponsible(){
    return responsibles.get(0);
  }
  // ---------------------------------------

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Boolean getHasPayment() {
    return hasPayment;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal getPastDue() {
    return pastDue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public List<Responsible> getResponsibles() {
    return responsibles;
  }

  public Map<String, Responsible> getResponsibleMap() {
    return responsibleMap;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   status  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Boolean hasStatus(String status) {
    return accountStatus.equals(status);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public boolean isOldAccount() {
    return oldAccount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   status  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Boolean isStatus(String status) {
    return accountStatus.equalsIgnoreCase(status);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountDate  DOCUMENT ME!
   */
  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountDays  DOCUMENT ME!
   */
  public void setAccountDays(Integer accountDays) {
    this.accountDays = accountDays;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountDetail  DOCUMENT ME!
   */
  public void setAccountDetail(AccountDetail accountDetail) {
    this.accountDetail = accountDetail;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountNum  DOCUMENT ME!
   */
  public void setAccountNum(Long accountNum) {
    this.accountNum = accountNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountStatus  DOCUMENT ME!
   */
  public void setAccountStatus(String accountStatus) {
    this.accountStatus = accountStatus;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  balance  DOCUMENT ME!
   */
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  hasPayment  DOCUMENT ME!
   */
  public void setHasPayment(Boolean hasPayment) {
    this.hasPayment = hasPayment;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  oldAccount  DOCUMENT ME!
   */
  public void setOldAccount(boolean oldAccount) {
    this.oldAccount = oldAccount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  pastDue  DOCUMENT ME!
   */
  public void setPastDue(BigDecimal pastDue) {
    this.pastDue = pastDue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  responsibles  DOCUMENT ME!
   */
  public void setResponsibles(List<Responsible> responsibles) {
    this.responsibles = responsibles;
  }
} // end class Account
