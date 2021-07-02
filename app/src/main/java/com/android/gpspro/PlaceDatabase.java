package com.android.gpspro;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Place.class}, version = 1)
public abstract class PlaceDatabase extends RoomDatabase {
    public abstract PlaceDao placeDao();
    //instance variable is created, so that it can turn "NoteDatabase" class into a singleton.
    //singleton doesn't let start multiple instance of a class.
    private static PlaceDatabase instance;

    //noteDao() returns NoteDao. And this method doesn't have a body.


    //synchronized means only one thread at a time can access this method.
    public static synchronized PlaceDatabase getInstance(Context context){
        //initialize instance if there is none
        if (instance == null){
            //new NoteDatabase can't be used because it is abstract. That's why we are using builder.
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlaceDatabase.class, "geocoding")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private PlaceDao placeDao;
        private PopulateDbAsyncTask(PlaceDatabase db){
            placeDao = db.placeDao();
        }

        @Override
        protected Void doInBackground(Void... voids){
            return null;
        }
    }
}
