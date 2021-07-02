package com.android.gpspro;


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

    //@Query is used to create custom query
    @Query("DELETE FROM geocoding")
    void deleteAllNotes();

    @Query("SELECT * FROM geocoding ORDER BY priority DESC")
    LiveData<List<Place>> getAllplaces();

    @Query ("SELECT * FROM geocoding")
    List<Place> getAll();

    @Query("SELECT * FROM geocoding")
    public List<Place> loadFullName();

    @Query ("SELECT * FROM geocoding")
    public Place[] loadAllUser();


}
