package com.android.gpspro.DB.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "geocoding")
public class Place implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idd;
    private String title;
    private String description;
    private Double lat;
    private Double lng;


    public Place(int idd, String title,String description, Double lat, Double lng) {
        this.idd=idd;
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lng = lng;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdd() {return idd;}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }




    @Override
    public String toString() {
        return
         title+("\n");
    }
}
