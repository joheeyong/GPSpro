package travel.android.gpspro.DB.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import travel.android.gpspro.DB.Entity.Travel;

@Dao
public interface TravelDao {

    @Insert
    void insert(Travel travel);

    @Update
    void update(Travel travel);

    @Delete
    void delete(Travel travel);

    @Query("DELETE FROM Travel_table")
    void deleteAllTravels();

    @Query("SELECT * FROM Travel_table order by date DESC")
    LiveData<List<Travel>> getAllTravels();


}
