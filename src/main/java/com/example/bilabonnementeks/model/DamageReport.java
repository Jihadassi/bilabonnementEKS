package com.example.bilabonnementeks.model;

public class DamageReport {
    private int damageId;
    private String textDescription;
    private int damagePrice;
    private int carId;
    private int customerId;

// tom konstruktør
    public DamageReport(){}


// getters
public int getDamageId(){
         return damageId;
}

public String getTextDescription() {
     return textDescription;
}

public int getDamagePrice(){
     return damagePrice;
}

 public int getCarId(){
        return carId;
 }

public int getCustomerId(){
        return customerId;
}


// setters

public void setDamageId(int damageId){
        this.damageId= damageId;
}

public void setTextDescription(String textDescription){
        this.textDescription= textDescription;
}

public void setDamagePrice(int damagePrice){
        this.damagePrice= damagePrice;
}

public void setCarId(int carId){
        this.carId= carId;
}

public void setCustomerId(int customerId){
        this.customerId=customerId;
}


}






