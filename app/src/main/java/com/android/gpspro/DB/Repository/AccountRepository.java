package com.android.gpspro.DB.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.android.gpspro.DB.Dao.AccountDao;
import com.android.gpspro.DB.Database.AccountDatabase;
import com.android.gpspro.DB.Entity.Account;

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

    public void update(Account account) { new UpdateNoteAsyncTask (accountDao).execute (account); }

    public void delete(Account account) { new DeleteNoteAsyncTask(accountDao).execute(account); }

    public void deleteAllNotes() { new DeleteAllNotesAsyncTask (accountDao).execute(); }

    public LiveData<List<Account>> getAllNotes() {
        return allAccounts;
    }

    public LiveData<String> getTotal(int idd) {
        return accountDao.getFavSum(idd);
    }

    public LiveData<List<Account>> getAllNotes(int idd) {
        return accountDao.getAllNotes (idd);
    }


    public LiveData<String> getTotalpood(int idd, String pood) {
        return accountDao.getFoodSum (idd,pood);
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

//    private static class UpdateNoteAsyncTask extends AsyncTask<Account, Void, Void> {
//
//        private AccountDao accountDao;
//
//        private UpdateNoteAsyncTask(AccountDao accountDao) {
//            this.accountDao = accountDao;
//        }
//
//        @Override
//        protected Void doInBackground(Account... accounts) {
//            accountDao.update (accounts[0]);
//            return null;
//        }
//
//}

    private static class UpdateNoteAsyncTask extends AsyncTask<Account, Void, Void> {

        private AccountDao accountDao;

        private UpdateNoteAsyncTask(AccountDao accountDao) { this.accountDao = accountDao;
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
