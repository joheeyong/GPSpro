package travel.android.gpspro.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.gpspro.R;

import java.util.List;

import travel.android.gpspro.Adapter.ContactAdapterPhoto;
import travel.android.gpspro.DB.Database.ContactAppDatabase;
import travel.android.gpspro.DB.Entity.Contact;
import travel.android.gpspro.DB.ViewModel.ContactService;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
