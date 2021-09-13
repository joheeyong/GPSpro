package travel.android.gpspro.DB.Database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import travel.android.gpspro.DB.Dao.TravelDao;
import travel.android.gpspro.DB.Entity.Travel;

@Database(entities = {Travel.class}, version = 2)
public abstract class TravelDatabase extends RoomDatabase {

    private static TravelDatabase instance;

    public abstract TravelDao travelDao();

    public static synchronized TravelDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TravelDatabase.class, "travel_database")
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

        private TravelDao travelDao;
        private PopulateDbAsyncTask(TravelDatabase db){
            travelDao = db.travelDao ();
        }

        @Override
        protected Void doInBackground(Void... voids){
            return null;
        }
    }
}
