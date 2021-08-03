package com.android.gpspro.DB.Database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.gpspro.DB.Entity.Account;
import com.android.gpspro.DB.Dao.AccountDao;

@Database(entities = {Account.class}, version = 1)
public abstract class AccountDatabase extends RoomDatabase {

    private static AccountDatabase instance;

    public abstract AccountDao accountDao();

    public static synchronized AccountDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AccountDatabase.class, "account_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomcallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private AccountDao accountDaoDao;
        private PopulateDbAsyncTask(AccountDatabase db){
            accountDaoDao = db.accountDao();
        }

        @Override
        protected Void doInBackground(Void... voids){
            return null;
        }
    }
}