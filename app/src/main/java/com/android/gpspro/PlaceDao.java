package com.android.gpspro;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class PlaceDao extends MainActivity{
    @Insert
    public abstract void insert(Place place);

    @Update
    public abstract void update(Place place);

    @Delete
    public abstract void delete(Place place);

    //@Query is used to create custom query
    @Query("DELETE FROM geocoding")
    public abstract void deleteAllNotes();

    @Query("SELECT * FROM geocoding")
    public abstract LiveData<List<Place>> getAllplaces();


    @Query ("SELECT * FROM geocoding")
    public abstract List<Place> getAll();

    @Query("SELECT * FROM geocoding")
    public abstract List<Place> loadFullName();

    @Query ("SELECT * FROM geocoding")
    public abstract Place[] loadAllUser();


}
