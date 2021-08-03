package com.android.gpspro.DB.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "account_table")
public class Account {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String time;
    private String category;
    private int idd;
    private int price;

    public Account(String time, String category, int idd, int price) {
        this.time = time;
        this.category = category;
        this.price = price;
        this.idd = idd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public int getIdd() { return  idd;}

    public int getPrice() {
        return price;
    }
}
