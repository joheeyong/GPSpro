package com.android.gpspro.DB.Database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.gpspro.DB.Entity.Contact;
import com.android.gpspro.DB.Dao.ContactRepository;

@Database(entities = {Contact.class}, version = 2)
public abstract class ContactAppDatabase extends RoomDatabase {
    public abstract ContactRepository contactRepository();
}
