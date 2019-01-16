package com.example.user.legaldesire.models;

/**
 * Created by USER on 10-01-2019.
 */

public class AppointmentDataModel {
    String message;
    String mail;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public AppointmentDataModel(String message, String mail, String status,String name,String number) {
        this.message = message;
        this.mail = mail;
        this.status = status;
        this.name=name;
        this.number=number;
    }

    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    String status;

}
