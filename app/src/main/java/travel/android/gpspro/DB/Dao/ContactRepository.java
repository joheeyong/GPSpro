package travel.android.gpspro.DB.Dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import travel.android.gpspro.DB.Entity.Contact;

@Dao
public interface ContactRepository {

    @Insert
    long save(Contact contact);

    @Update
    int update(Contact contact);

    @Query("DELETE FROM contacts WHERE id = :contactId")
    int delete(long contactId);

    @Query("DELETE FROM contacts")
    int deleteAll();

    @Query("SELECT * FROM contacts")
    List<Contact> findAll();
    @Query("SELECT * FROM contacts WHERE idd=:idd")
    List<Contact> findAlll(int idd);

    @Query("SELECT * FROM contacts WHERE id = :contactId ")
    Contact findById(long contactId);

}
