package travel.android.gpspro.DB.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import travel.android.gpspro.DB.Entity.Account;

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
    @Query("SELECT * FROM account_table WHERE idd=:idd")
    LiveData<List<Account>> getAllNotes(int idd);
    @Query("SELECT SUM(price) as favSum FROM account_table WHERE idd=:idd")
    LiveData<String> getFavSum(int idd);

    @Query("SELECT SUM(price) as favSum FROM account_table WHERE idd=:idd AND category=:pood")
    LiveData<String> getFoodSum(int idd, String pood);
}
