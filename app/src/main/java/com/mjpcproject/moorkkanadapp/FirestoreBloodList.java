package com.mjpcproject.moorkkanadapp;

public class FirestoreBloodList {

    private String name;
    private String phone;
    private String blood;

    private FirestoreBloodList(){}

    private FirestoreBloodList(String name, String phone, String blood){
        this.name = name;
        this.phone = phone;
        this.blood = blood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }
}
