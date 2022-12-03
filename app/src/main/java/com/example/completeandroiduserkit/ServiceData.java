package com.example.completeandroiduserkit;

public class ServiceData {
    private String sName;
    private String sService;
    private String sLocation;
    private String sPhone;
    private String sExperience;
    private String sAge;
    private String sGender;
    private String sAddress;
    private String sDescription;
    private String sUid;
    private String sImage;
    private String key;

    public ServiceData(String sName, String sService, String sLocation, String sPhone, String sExperience, String sAge, String sGender, String sAddress, String sDescription, String sUid, String sImage) {
        this.sName = sName;
        this.sService = sService;
        this.sLocation = sLocation;
        this.sPhone = sPhone;
        this.sExperience = sExperience;
        this.sAge = sAge;
        this.sGender = sGender;
        this.sAddress = sAddress;
        this.sDescription = sDescription;
        this.sUid = sUid;
        this.sImage = sImage;
        this.key = key;
    }



    public  ServiceData(){

    }


    public String getsName() {
        return sName;
    }

    public String getsService() {
        return sService;
    }

    public String getsLocation() {
        return sLocation;
    }

    public String getsPhone() {
        return sPhone;
    }

    public String getsExperience() {
        return sExperience;
    }

    public String getsAge() {
        return sAge;
    }

    public String getsGender() {
        return sGender;
    }

    public String getsAddress() {
        return sAddress;
    }

    public String getsDescription() {
        return sDescription;
    }

    public String getsUid() {
        return sUid;
    }

    public String getsImage() {
        return sImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }




}
