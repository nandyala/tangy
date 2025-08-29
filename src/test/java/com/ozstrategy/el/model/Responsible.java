package com.ozstrategy.el.model;

import java.util.Date;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/20/13 Time: 11:05 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author Rojer Luo
 * @version $Revision$, $Date$
 */
public class Responsible extends BaseObject {
    //~ Instance fields --------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected Account account = new Account();

    /**
     * DOCUMENT ME!
     */
    protected int age = 33;

    /**
     * DOCUMENT ME!
     */
    protected Date birthday;

    /**
     * DOCUMENT ME!
     */
    protected String firstName;

    /**
     * DOCUMENT ME!
     */
    protected Integer intValue;

    /**
     * DOCUMENT ME!
     */
    protected String lastName;

    /**
     * DOCUMENT ME!
     */
    protected Long longValue;

    /**
     * DOCUMENT ME!
     */
    protected String name = "Jack";

    /**
     * DOCUMENT ME!
     */
    protected Long responsibleId;

    protected Address address;
    //~ Constructors -----------------------------------------------------------------------------------------------------

    /**
     * Creates a new Responsible object.
     */
    public Responsible() {
    }

    /**
     * Creates a new Responsible object.
     *
     * @param age       DOCUMENT ME!
     * @param birthday  DOCUMENT ME!
     * @param name      DOCUMENT ME!
     * @param firstName DOCUMENT ME!
     * @param lastName  DOCUMENT ME!
     */
    public Responsible(int age, Date birthday, String name, String firstName, String lastName) {
        this.responsibleId = ++idGen;
        this.age = age;
        this.birthday = birthday;
        this.name = name;
        this.firstName = firstName;

        this.lastName = lastName;
    }

    //~ Methods ----------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Account getAccount() {
        return account;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAge() {
        return age;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getBirthday() {
        return birthday;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFirstName() {
        return firstName;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer getIntValue() {
        return intValue;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLastName() {
        return lastName;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long getLongValue() {
        return longValue;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long getResponsibleId() {
        return responsibleId;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param account DOCUMENT ME!
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param age DOCUMENT ME!
     */
    public void setAge(int age) {
        this.age = age;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param birthday DOCUMENT ME!
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param firstName DOCUMENT ME!
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param intValue DOCUMENT ME!
     */
    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param lastName DOCUMENT ME!
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param longValue DOCUMENT ME!
     */
    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param responsibleId DOCUMENT ME!
     */
    public void setResponsibleId(Long responsibleId) {
        this.responsibleId = responsibleId;
    }
} // end class Responsible
