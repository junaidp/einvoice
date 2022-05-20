package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;

@Document
//@XmlRootElement
public class Customer {

    @Id
    private String id;
    private String customer;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String phoneMain;
    private String relatedParty;
    private String accountNumber;
    private String website;
    private String notes;
    private String currency;
    private String billingAddress1;
//    private String billingAddress2;
//    private String billingCountry;
//    private String billingProvince;
//    private String billingCity;
//    private String billingPostal;
//    private String shippingName;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingCountry;
    private String shippingProvince;
    private String shippingCity;
    private String shippingPostal;
    private String deliveryInstructions;
    private String companyID;
    private String vatNumber_Customer;
    private String additionalNumber_Customer;
    private String otherSellerid_Customer;

    public String getRelatedParty() {
        return relatedParty;
    }

    public String getVatNumber_Customer() {
        return vatNumber_Customer;
    }

    public void setVatNumber_Customer(String vatNumber_Customer) {
        this.vatNumber_Customer = vatNumber_Customer;
    }

    public String getAdditionalNumber_Customer() {
        return additionalNumber_Customer;
    }

    public void setAdditionalNumber_Customer(String additionalNumber_Customer) {
        this.additionalNumber_Customer = additionalNumber_Customer;
    }

    public String getOtherSellerid_Customer() {
        return otherSellerid_Customer;
    }

    public void setOtherSellerid_Customer(String otherSellerid_Customer) {
        this.otherSellerid_Customer = otherSellerid_Customer;
    }

    public String isRelatedParty() {
        return relatedParty;
    }

    public void setRelatedParty(String relatedParty) {
        this.relatedParty = relatedParty;
    }

    public String getPhoneMain() {
        return phoneMain;
    }

    public void setPhoneMain(String phoneMain) {
        this.phoneMain = phoneMain;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBillingAddress1() {
        return billingAddress1;
    }

    public void setBillingAddress1(String billingAddress1) {
        this.billingAddress1 = billingAddress1;
    }

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress) {
        this.shippingAddress1 = shippingAddress;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        this.shippingAddress2 = shippingAddress2;
    }

    public String getShippingProvince() {
        return shippingProvince;
    }

    public void setShippingProvince(String shippingProvince) {
        this.shippingProvince = shippingProvince;
    }

    public String getShippingPostal() {
        return shippingPostal;
    }

    public void setShippingPostal(String shippingPostal) {
        this.shippingPostal = shippingPostal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }
}
