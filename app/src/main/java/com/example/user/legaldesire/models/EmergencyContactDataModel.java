package com.example.user.legaldesire.models;

public class EmergencyContactDataModel {
    String name,contact,key;

    public EmergencyContactDataModel(String name, String contact,String key) {
        this.name = name;
        this.contact = contact;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
