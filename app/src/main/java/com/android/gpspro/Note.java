package com.android.gpspro;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

/* This class table of the Database */
@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String date;
    private Double star;
    public Note(String title, String description, String date, Double star) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.star = star;
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

    public String getDate() { return  date; }

    public Double getStar() { return star; }
}
