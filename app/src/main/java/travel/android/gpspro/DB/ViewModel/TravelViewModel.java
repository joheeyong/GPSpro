package travel.android.gpspro.DB.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import travel.android.gpspro.DB.Entity.Travel;
import travel.android.gpspro.DB.Repository.TravelRepository;

public class TravelViewModel extends AndroidViewModel {
    private TravelRepository repository;
    private LiveData<List<Travel>> allTravels;

    public TravelViewModel(@NonNull Application application) {
        super(application);
        repository = new TravelRepository(application);
        allTravels = repository.getAllTravels();
    }
    public LiveData<List<Travel>> getAllTravels() {
        return allTravels;
    }

    public void insert(Travel travel) {
        repository.insert(travel);
    }

    public void update(Travel travel) {
        repository.update(travel);
    }

    public void delete(Travel travel) {
        repository.delete(travel);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }
}
