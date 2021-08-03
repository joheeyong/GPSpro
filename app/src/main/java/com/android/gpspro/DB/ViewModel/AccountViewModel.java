package com.android.gpspro.DB.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.gpspro.DB.Entity.Account;
import com.android.gpspro.DB.Repository.AccountRepository;

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

    public LiveData<List<Account>> getAllNotes(int idd) {
        return repository.getAllNotes(idd);
    }


    public LiveData<String> getmTotal(int idd){
        return repository.getTotal(idd);
    }

    public LiveData<String> getmTotall(int idd, String pood) {
        return repository.getTotalpood(idd, pood);
    }
}
