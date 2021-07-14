package com.android.gpspro;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "geocoding")
public class Place {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String userid;
    private Double lat;
    private Double lng;
    private int count;
    private int priority;

    public Place(String title, String description, String userid, Double lat, Double lng, int count, int priority) {
        this.title = title;
        this.userid = userid;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.priority = priority;
        this.count =count;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUserid() {
        return userid;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public int getCount() {
        return count;
    }
    public int getPriority() {
        return priority;
    }


    @Override
    public String toString() {
        return
                + id +
                        title +
                        description +
                        userid+
                        lat +
                        lng +
                        count+
                        priority
                ;
    }
}
