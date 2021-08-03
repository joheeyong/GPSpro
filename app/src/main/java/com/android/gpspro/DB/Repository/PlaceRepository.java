package com.android.gpspro.DB.Repository;


import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.android.gpspro.DB.Dao.PlaceDao;
import com.android.gpspro.DB.Database.PlaceDatabase;
import com.android.gpspro.DB.Entity.Place;

import java.util.List;

public class PlaceRepository {
    private String DB_NAME = "geocoding";
    private PlaceDatabase placeDatabase;
    private PlaceDao placeDao;
    private LiveData<List<Place>> allPlaces;
    private LiveData<List<Place>> AllTasks;

    public PlaceRepository(Application application) {
        PlaceDatabase database = PlaceDatabase.getInstance(application);
        placeDao = database.placeDao();
//        allPlaces = placeDao.getAllplaces();
    }

    public PlaceRepository(Context context) {
        placeDatabase = Room.databaseBuilder (context, PlaceDatabase.class, DB_NAME).build ();
    }

    public void insert(Place place) {
        new InsertPlaceAsyncTask (placeDao).execute (place);
    }

    public void update(Place place) {
        new UpdatePlaceAsyncTask (placeDao).execute (place);
    }

    public void delete(Place place) {
        new DeletePlaceAsyncTask (placeDao).execute (place);
    }

    public void deleteAllNotes() {
        new DeleteAllPlacesAsyncTask (placeDao).execute ();
    }

    public LiveData<List<Place>> getAllNotes(int idd) {
        return placeDao.getAllplaces(idd);
    }

    public LiveData<Integer> getRowCount(int idd) {
        return placeDao.getRowCount (idd);
    }




    private static class InsertPlaceAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao placeDao;

        private InsertPlaceAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeDao.insert (places[0]);
            return null;
        }
    }

    //
    private static class UpdatePlaceAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao placeDao;

        private UpdatePlaceAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeDao.update (places[0]);
            return null;
        }
    }

    //

    private static class DeletePlaceAsyncTask extends AsyncTask<Place, Void, Void> {
        private PlaceDao placeDao;

        private DeletePlaceAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeDao.delete (places[0]);
            return null;
        }
    }

    //
    private static class DeleteAllPlacesAsyncTask extends AsyncTask<Void, Void, Void> {

        private PlaceDao placeDao;

        private DeleteAllPlacesAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            placeDao.deleteAllNotes ();
            return null;
        }
    }

//    public LiveData<List<Place>> fetchAllTasks(String userid) {
//        return placeDatabase.placeDao ().fetchAllTasks (userid);
//    }
    public LiveData<List<Place>> getAllNotes() {
        return allPlaces;
    }
}
