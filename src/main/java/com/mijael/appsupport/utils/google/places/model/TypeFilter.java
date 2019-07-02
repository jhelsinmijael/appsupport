package com.mijael.appsupport.utils.google.places.model;

public enum TypeFilter {

    ADDRESS("address"),
    CITIES("cities"),
    ESTABLISHMENT("establishment"),
    GEOCODE("geocode"),
    REGIONS("regions");

    TypeFilter(String type){
        this.type = type;
    }

    private String type;

    public String getType(){
        return type;
    }

}
