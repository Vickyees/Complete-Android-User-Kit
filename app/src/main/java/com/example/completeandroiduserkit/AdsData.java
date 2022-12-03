package com.example.completeandroiduserkit;

public class AdsData {
    private String adName;
    private String adLocation;
    private String adDescription;
    private String adContact;
    private String adImage;
    private String adService;

    public void setAdKey(String adKey) {
        this.adKey = adKey;
    }

    private String adKey;

    public AdsData(String adName, String adLocation, String adService, String adDescription, String adContact, String adImage) {
        this.adName = adName;
        this.adLocation = adLocation;
        this.adService = adService;
        this.adDescription = adDescription;
        this.adContact = adContact;
        this.adImage = adImage;
    }


    public AdsData(){

    }

    public String getAdKey() {
        return adKey;
    }
    public String getAdService() {
        return adService;
    }


    public String getAdName() {
        return adName;
    }

    public String getAdLocation() {
        return adLocation;
    }

    public String getAdContact() {
        return adContact;
    }

    public String getAdImage() {
        return adImage;
    }

    public String getAdDescription() {
        return adDescription;
    }
}
