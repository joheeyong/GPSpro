package com.android.gpspro;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private AccountRepository repository;
    private LiveData<List<Account>> allNotes;

    public AccountViewModel(@NonNull Application application) {
        super (application);
        repository = new AccountRepository (application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Account account) {
        repository.insert(account);
    }
    public void update(Account account) {
        repository.update(account);
    }

    public void delete(Account account) {
        repository.delete(account);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Account>> getAllNotes(String userid) {
        return repository.getAllNotes(userid);
    }


    public LiveData<String> getmTotal(String userid){
        return repository.getTotal(userid);
    }
}
