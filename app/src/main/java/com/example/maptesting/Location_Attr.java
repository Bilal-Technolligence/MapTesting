package com.example.maptesting;

/**
 * Created by Hanzalah on 3/2/2019.
 */

public class Location_Attr {
    private String Id ;
    private String Name;
    private String Longitude;
    private String Latitude;
    private String Type;


    public Location_Attr() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Location_Attr(String id, String name, String longitude, String latitude, String type) {
        Id = id;
        Name = name;
        Longitude = longitude;
        Latitude = latitude;
        Type = type;
    }
}
