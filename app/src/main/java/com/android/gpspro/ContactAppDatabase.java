package com.android.gpspro;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.gpspro.Contact;

@Database(entities = {Contact.class}, version = 2)
public abstract class ContactAppDatabase extends RoomDatabase {
    public abstract ContactRepository contactRepository();
}
