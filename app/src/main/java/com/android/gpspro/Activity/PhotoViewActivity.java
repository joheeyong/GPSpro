package com.android.gpspro.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.gpspro.DB.Entity.Contact;
import com.android.gpspro.Adapter.ContactAdapterPhoto;
import com.android.gpspro.DB.Database.ContactAppDatabase;
import com.android.gpspro.DB.ViewModel.ContactService;
import com.android.gpspro.Other.ImageUpload;
import com.android.gpspro.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends Activity {

    private PhotoViewActivity mContext = PhotoViewActivity.this;
    private RecyclerView recyclerView;
    private ContactAdapterPhoto adapter;
    private ContactAppDatabase contactAppDatabase;
    private ContactService contactService;
    private List<Contact> contacts;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        contactAppDatabase = Room.databaseBuilder(getApplicationContext(), ContactAppDatabase.class, "ContactDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        contactService = new ContactService(contactAppDatabase.contactRepository());
        initData();
        initObject();
        recyclerView.setLayoutManager(new LinearLayoutManager (this,LinearLayoutManager.HORIZONTAL,true));
        recyclerView.setAdapter(adapter);
    }
    private void initData(){
        Intent intent = getIntent();
        int idd = intent.getIntExtra ("idd",1000);
        contacts = contactService.allview (idd);
    }
    private void initObject(){
        recyclerView = findViewById(R.id.recycler_view_contacts);
        ivProfile = findViewById(R.id.iv_profile);
        adapter = new ContactAdapterPhoto(mContext, contacts);
    }

}
