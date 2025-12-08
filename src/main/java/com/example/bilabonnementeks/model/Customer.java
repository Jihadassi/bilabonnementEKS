package com.example.bilabonnementeks.model;

public class Customer {

    private int customerId;
private String fullName;
private String email;
private String phoneNumber;
private String address;

public Customer() {}

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerID) {
        this.customerId = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
