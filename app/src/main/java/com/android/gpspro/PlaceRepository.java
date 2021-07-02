package com.android.gpspro;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaceRepository {

    private PlaceDao placeDao;
    private LiveData<List<Place>> allPlaces;

    public PlaceRepository(Application application) {
        PlaceDatabase database = PlaceDatabase.getInstance(application);
        placeDao = database.placeDao();
        allPlaces = placeDao.getAllplaces();
    }

    public void insert(Place place) {
        new InsertPlaceAsyncTask (placeDao).execute(place);
    }

    public void update(Place place) {
        new UpdatePlaceAsyncTask (placeDao).execute(place);
    }

    public void delete(Place place) {
        new DeletePlaceAsyncTask (placeDao).execute(place);
    }

    public void deleteAllNotes() {
        new DeleteAllPlacesAsyncTask (placeDao).execute();
    }

    public LiveData<List<Place>> getAllNotes() {
        return allPlaces;
    }

    private static class InsertPlaceAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao placeDao;

        private InsertPlaceAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeDao.insert(places[0]);
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
            placeDao.update(places[0]);
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
            placeDao.delete(places[0]);
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
            placeDao.deleteAllNotes();
            return null;
        }
    }
}
