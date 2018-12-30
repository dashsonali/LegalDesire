package com.example.user.legaldesire.models;

/**
 * Created by USER on 27-12-2018.
 */

public class LawyerData {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String email;
    private String areaOfPractice;
    private String contact;
    private Float rating;
    private String locate="NoLocation";


    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getNoOfRaters() {
        return noOfRaters;
    }

    public LawyerData(String name,String email, String areaOfPractice, String contact, Float rating, String noOfRaters ,String locate) {
        this.email = email;
        this.areaOfPractice = areaOfPractice;
        this.contact = contact;
        this.rating = rating;
        this.locate = locate;
        this.noOfRaters = noOfRaters;
        this.name = name;
    }

    public void setNoOfRaters(String noOfRaters) {
        this.noOfRaters = noOfRaters;
    }

    private String noOfRaters;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreaOfPractice() {
        return areaOfPractice;
    }

    public void setAreaOfPractice(String areaOfPractice) {
        this.areaOfPractice = areaOfPractice;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    private String name;

}
