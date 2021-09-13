package travel.android.gpspro.DB.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import travel.android.gpspro.DB.Dao.TravelDao;
import travel.android.gpspro.DB.Database.TravelDatabase;
import travel.android.gpspro.DB.Entity.Travel;

public class TravelRepository {

    private TravelDao travelDao;
    private LiveData<List<Travel>> allTravels;

    public TravelRepository(Application application) {
        TravelDatabase database = TravelDatabase.getInstance(application);
        travelDao = database.travelDao();
        allTravels = travelDao.getAllTravels();
    }

    public void insert(Travel travel) {
        new InsertTravelAsyncTask(travelDao).execute(travel);
    }

    public void update(Travel travel) {
        new UpdateTravelAsyncTask(travelDao).execute(travel);
    }

    public void delete(Travel travel) { new DeleteTravelAsyncTask(travelDao).execute(travel); }

    public void deleteAllNotes() {
        new DeleteAllTravelsAsyncTask(travelDao).execute();
    }

    public LiveData<List<Travel>> getAllTravels() {
        return allTravels;
    }

    private static class InsertTravelAsyncTask extends AsyncTask<Travel, Void, Void> {

        private TravelDao travelDao;

        private InsertTravelAsyncTask(TravelDao travelDao) {
            this.travelDao = travelDao;
        }

        @Override
        protected Void doInBackground(Travel... travels) {
            travelDao.insert(travels[0]);
            return null;
        }
    }

    private static class UpdateTravelAsyncTask extends AsyncTask<Travel, Void, Void> {

        private TravelDao travelDao;

        private UpdateTravelAsyncTask(TravelDao travelDao) {
            this.travelDao = travelDao;
        }

        @Override
        protected Void doInBackground(Travel... travels) {
            travelDao.update(travels[0]);
            return null;
        }
    }

    private static class DeleteTravelAsyncTask extends AsyncTask<Travel, Void, Void> {
        private TravelDao travelDao;
        private DeleteTravelAsyncTask(TravelDao travelDao) {
            this.travelDao = travelDao;
        }

        @Override
        protected Void doInBackground(Travel... travels) {
            travelDao.delete(travels[0]);
            return null;
        }
    }

    private static class DeleteAllTravelsAsyncTask extends AsyncTask<Void, Void, Void> {

        private TravelDao travelDao;

        private DeleteAllTravelsAsyncTask(TravelDao travelDao) {
            this.travelDao = travelDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            travelDao.deleteAllTravels();
            return null;
        }
    }
}
