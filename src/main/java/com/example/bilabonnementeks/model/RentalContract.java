package com.example.bilabonnementeks.model;

import java.util.Date;

public class RentalContract {

    //Attributter
    private int contractId;
    private int currentKm;
    private int includedKm;
    private int carRentPrice;
    private int carId;

    //userId skal måske slettes hvis den kan laves i user klassen og forbindes derigennem
    private int userId;
    private String pickupLocation;
    private String dropoffLocation;
    private Date startDate;
    private Date endDate;

    //Skal måske bruges til kundeoplysninger i lejekontrakten
    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerAddress;

    public RentalContract(){}

    //Getters og Setters
    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }

    int getCurrentKm() { return currentKm; }
    public void setCurrentKm(int currentKm) { this.currentKm = currentKm; }

    public int getIncludedKm() { return includedKm; }
    public void setIncludedKm(int includedKm) { this.includedKm = includedKm; }

    public int getCarRentPrice() { return carRentPrice; }
    public void setCarRentPrice(int carRentPrice) { this.carRentPrice = carRentPrice; }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    //Ikke sikkert den skal bruges, hvis den kan komme ind under user model klassen og forbindes derigennem
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    //-----


    //Ikke sikkert den skal bruges, hvis den kan komme ind under customer model klassen og forbindes derigennem
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    //------


    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public java.sql.Date getStartDate() { return startDate; }
    public void setStartDate(java.sql.Date startDate) { this.startDate = startDate; }

    public java.sql.Date getEndDate() { return endDate; }
    public void setEndDate(java.sql.Date endDate) { this.endDate = endDate; }

    //Ikke sikkert det skal bruges hvis den kan komme ind under customer model klassen og forbindes derigennem
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhoneNumber() { return customerPhoneNumber; }
    public void setCustomerPhoneNumber(String customerPhoneNumber) { this.customerPhoneNumber = customerPhoneNumber; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }
    //------


}
