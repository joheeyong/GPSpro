package com.android.gpspro.DB.ViewModel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.gpspro.DB.Entity.Place;
import com.android.gpspro.DB.Repository.PlaceRepository;

import java.util.List;
public class PlaceViewModel extends AndroidViewModel {

    private PlaceRepository repository;
    private LiveData<List<Place>> allPlaces;
    public PlaceViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
        allPlaces = repository.getAllNotes();
    }



    public void insert(Place place) {
        repository.insert(place);
    }

    public void update(Place place) {
        repository.update(place);
    }

    public void delete(Place place) {
        repository.delete(place);
    }

    public void deleteAllPlaces() {
        repository.deleteAllNotes();
    }

//    public LiveData<List<Place>> getAllPlaces() {
//        return allPlaces;
//    }

    public LiveData<List<Place>> getAllPlaces(int idd) {
        return repository.getAllNotes(idd);
    }

    public LiveData<Integer> getRowCount(int idd) {
        return repository.getRowCount(idd); }

}
