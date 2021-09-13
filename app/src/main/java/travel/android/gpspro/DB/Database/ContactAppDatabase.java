package travel.android.gpspro.DB.Database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import travel.android.gpspro.DB.Dao.ContactRepository;
import travel.android.gpspro.DB.Entity.Contact;

@Database(entities = {Contact.class}, version = 2)
public abstract class ContactAppDatabase extends RoomDatabase {
    public abstract ContactRepository contactRepository();
}
