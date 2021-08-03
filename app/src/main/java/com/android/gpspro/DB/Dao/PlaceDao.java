package com.android.gpspro.DB.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.gpspro.DB.Entity.Place;

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

    @Query("SELECT * FROM geocoding WHERE idd=:idd")
    LiveData<List<Place>> getAllplaces(int idd);

    @Query("SELECT Count(id) FROM geocoding WHERE idd=:idd")
    LiveData<Integer> getRowCount(int idd);



//    @Query("SELECT * FROM geocoding WHERE userid= :userid")
//    LiveData<List<Place>> fetchAllTasks(String userid);

}
