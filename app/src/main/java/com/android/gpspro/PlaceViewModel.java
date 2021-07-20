package com.android.gpspro;


import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

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

    public LiveData<List<Place>> getAllPlaces() {
        return allPlaces;
    }

}
