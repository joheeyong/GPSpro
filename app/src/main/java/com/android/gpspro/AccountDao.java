package com.android.gpspro;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDao {

    @Insert
    void insert(Account account);
    @Update
    void update(Account account);
    @Delete
    void delete(Account account);
    @Query("DELETE FROM account_table")
    void deleteAllNotes();
    @Query("SELECT * FROM account_table WHERE userid=:userid")
    LiveData<List<Account>> getAllNotes(String userid);
    @Query("SELECT SUM(price) as favSum FROM account_table WHERE userid=:userid")
    LiveData<String> getFavSum(String userid);

}
