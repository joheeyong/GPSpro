package com.android.gpspro;


import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceDao {
    @Insert
    void insert(Place place);

    @Update
    void update(Place place);

    @Delete
    void delete(Place place);

    @Query("DELETE FROM geocoding")
    void deleteAllNotes();

    @Query("SELECT * FROM geocoding")
    LiveData<List<Place>> getAllplaces();

    @Query("SELECT * FROM geocoding WHERE userid= :userid")
    LiveData<List<Place>> fetchAllTasks(String userid);

}
