package com.ozstrategy.el.model;

/**
 * Created by rojer on 2017/5/30.
 */
public class Address {
    String address1;
    String address2;
    String city;
    String province;
    String country;

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress1() {

        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public Address(String address1, String address2, String city, String province, String country) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.province = province;
        this.country = country;
    }
}
