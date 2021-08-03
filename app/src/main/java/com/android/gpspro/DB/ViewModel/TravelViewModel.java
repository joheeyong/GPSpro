package com.android.gpspro.DB.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.gpspro.DB.Entity.Travel;
import com.android.gpspro.DB.Repository.TravelRepository;

import java.util.List;

public class TravelViewModel extends AndroidViewModel {
    private TravelRepository repository;
    private LiveData<List<Travel>> allTravels;

    public TravelViewModel(@NonNull Application application) {
        super(application);
        repository = new TravelRepository(application);
        allTravels = repository.getAllTravels();
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

    public LiveData<List<Travel>> getAllTravels() {
        return allTravels;
    }
}