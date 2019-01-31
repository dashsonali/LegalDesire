package com.example.user.legaldesire.models;

public class CasesModel {
    String casename,opposition_party,court,lawyer,date_of_initation,key,next_date;

    public CasesModel(String casename, String opposition_party, String court, String lawyer, String date_of_initation,String next_date,String key) {
        this.casename = casename;
        this.opposition_party = opposition_party;
        this.court = court;
        this.lawyer = lawyer;
        this.key = key;
        this.date_of_initation = date_of_initation;
        this.next_date = next_date;
    }

    public String getNext_date() {
        return next_date;
    }

    public void setNext_date(String next_date) {
        this.next_date = next_date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    public String getOppositon_party() {
        return opposition_party;
    }

    public void setOppositon_party(String oppositon_party) {
        this.opposition_party = oppositon_party;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getLawyer() {
        return lawyer;
    }

    public void setLawyer(String lawyer) {
        this.lawyer = lawyer;
    }

    public String getDate_of_initation() {
        return date_of_initation;
    }

    public void setDate_of_initation(String date_of_initation) {
        this.date_of_initation = date_of_initation;
    }
}
