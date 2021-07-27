package com.android.gpspro;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountRepository {

    private AccountDao accountDao;
    private LiveData<List<Account>> allAccounts;

    public AccountRepository(Application application) {
        AccountDatabase database = AccountDatabase.getInstance(application);
        accountDao = database.accountDao ();
    }

    public void insert(Account account) {
        new InsertNoteAsyncTask(accountDao).execute(account);
    }

    public void update(Account account) { new UpdateNoteAsyncTask(accountDao).execute(account); }

    public void delete(Account account) { new DeleteNoteAsyncTask (accountDao).execute(account); }

    public void deleteAllNotes() { new DeleteAllNotesAsyncTask (accountDao).execute(); }

    public LiveData<List<Account>> getAllNotes() {
        return allAccounts;
    }

    public LiveData<String> getTotal(String userid) {
        return accountDao.getFavSum(userid);
    }

    public LiveData<List<Account>> getAllNotes(String userid) {
        return accountDao.getAllNotes (userid);
    }


    private static class InsertNoteAsyncTask extends AsyncTask<Account, Void, Void> {

        private AccountDao accountDao;

        private InsertNoteAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.insert(accounts[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Account, Void, Void> {

        private AccountDao accountDao;

        private UpdateNoteAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.update (accounts[0]);
            return null;
        }

}
    private static class DeleteNoteAsyncTask extends AsyncTask<Account, Void, Void> {
        private AccountDao accountDao;

        private DeleteNoteAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.delete (accounts[0]);
            return null;
        }
    }
    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {

        private AccountDao accountDao;

        private DeleteAllNotesAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            accountDao.deleteAllNotes();
            return null;
        }
    }
}
