package com.android.gpspro;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "account_table")
public class Account {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String time;
    private String category;
    private String userid;
    private int price;

    public Account(String time, String category,String userid, int price) {
        this.time = time;
        this.category = category;
        this.price = price;
        this.userid = userid;
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

    public String getUserid() { return  userid;}

    public int getPrice() {
        return price;
    }
}
