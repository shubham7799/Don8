package com.example.test1;

public class fooddata {
    String email,name,quantity,validity;
    public fooddata(String email,String name,String quantity,String validity){
        this.email=email;
        this.name=name;
        this.quantity=quantity;
        this.validity=validity;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getValidity() {
        return validity;
    }
}
