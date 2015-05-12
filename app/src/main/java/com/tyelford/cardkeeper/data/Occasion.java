package com.tyelford.cardkeeper.data;

/**
 * Created by elfordty on 7/05/2015.
 */
public class Occasion {

    private String occName;
    private String occDate;
    private String occLocation;
    private String occNotes;

    //Default Constructor
    public Occasion(){}

    //Full Constructor
    public Occasion(String occName, String occDate, String occLocation, String occNotes) {
        this.occName = occName;
        this.occDate = occDate;
        this.occLocation = occLocation;
        this.occNotes = occNotes;
    }

    /*GETTER AND SETTER METHODS*/
    public String getOccName() {
        return occName;
    }

    public void setOccName(String occName) {
        this.occName = occName;
    }

    public String getOccDate() {
        return occDate;
    }

    public void setOccDate(String occDate) {
        this.occDate = occDate;
    }

    public String getOccLocation() {
        return occLocation;
    }

    public void setOccLocation(String occLocation) {
        this.occLocation = occLocation;
    }

    public String getOccNotes() {
        return occNotes;
    }

    public void setOccNotes(String occNotes) {
        this.occNotes = occNotes;
    }
}
