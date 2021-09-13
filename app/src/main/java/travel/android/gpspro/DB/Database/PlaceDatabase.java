package travel.android.gpspro.DB.Database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import travel.android.gpspro.DB.Dao.PlaceDao;
import travel.android.gpspro.DB.Entity.Place;

@Database(entities = {Place.class}, version = 1)
public abstract class PlaceDatabase extends RoomDatabase {
    public abstract PlaceDao placeDao();
    private static PlaceDatabase instance;

    public static synchronized PlaceDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlaceDatabase.class, "place_database")
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
