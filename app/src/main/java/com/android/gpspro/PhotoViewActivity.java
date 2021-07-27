package com.android.gpspro;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends Activity {

    private static final String TAG = "test";
    private PhotoViewActivity mContext = PhotoViewActivity.this;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContactAdapterPhoto adapter;

    private ContactAppDatabase contactAppDatabase;
    private ContactService contactService;
    private List<Contact> contacts;

    // 사진 업로드
    private ImageView ivProfile;
    private String imageRealPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("extitle");
        // DB관련
        contactAppDatabase = Room.databaseBuilder(getApplicationContext(), ContactAppDatabase.class, "ContactDB")
                .allowMainThreadQueries() // 실제로 서비스할 때는 전부 쓰레드 사용해야함.
                .fallbackToDestructiveMigration()
                .build(); //실행되도 객체 있으면 있는거 다시 주입

        contactService = new ContactService(contactAppDatabase.contactRepository());

        initData();
        initObject();
//        initDesign();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(" Contact App");

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager (this,LinearLayoutManager.HORIZONTAL,true));
        recyclerView.setAdapter(adapter);
        tedPermission();
    }

    private void initData(){
        Intent intent = getIntent();
        String userID = intent.getStringExtra("extitle");
        contacts = contactService.연락처전체보기(userID);
    }

    private void initObject(){
        recyclerView = findViewById(R.id.recycler_view_contacts);
        ivProfile = findViewById(R.id.iv_profile);
        adapter = new ContactAdapterPhoto(mContext, contacts);
    }

    private void initDesign(){

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    public void addContactDialog(){
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_add_contact, null);
        final EditText etName = dialogView.findViewById(R.id.name);
        final EditText etEmail = dialogView.findViewById(R.id.email);

        // 갤러리 사진 가져오기
        ivProfile = dialogView.findViewById(R.id.iv_profile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUpload.goToAlbum(mContext);
            }
        });

        androidx.appcompat.app.AlertDialog.Builder dlg = new androidx.appcompat.app.AlertDialog.Builder(PhotoViewActivity.this);
        dlg.setTitle("연락처 등록");
        dlg.setView(dialogView);
        dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createContact(etName.getText().toString(), etEmail.getText().toString(), imageRealPath);
                notifyListener();
            }
        });
        dlg.setNegativeButton("닫기", null);
        dlg.show();
    }

    public void editContactDialog(final Contact contact){
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_add_contact, null);
        final EditText etName = dialogView.findViewById(R.id.name);
        final EditText etEmail = dialogView.findViewById(R.id.email);
        ivProfile = dialogView.findViewById(R.id.iv_profile);
        etName.setText(contact.getName());
        etEmail.setText(contact.getEmail());

        if(contact.getProfileURL() == null || contact.getProfileURL().equals("")){
            ivProfile.setImageResource(R.drawable.ic_person);
        }else{
            ImageUpload.setImage(contact.getProfileURL(), ivProfile);
        }

        // 갤러리 사진 수정하기
        ivProfile = dialogView.findViewById(R.id.iv_profile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUpload.goToAlbum(mContext);
            }
        });

        androidx.appcompat.app.AlertDialog.Builder dlg = new AlertDialog.Builder(PhotoViewActivity.this);
        dlg.setTitle("연락처 수정");
        dlg.setView(dialogView);
        dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateContact(contact.getId(), etName.getText().toString(), etEmail.getText().toString(), imageRealPath);
            }
        });
        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                contactService.연락처삭제(contact.getId());
                notifyListener();
            }
        });
        dlg.show();
    }

    private void createContact(String name, String email, String profileUrl) {
        long contactId = contactService.연락처등록(new Contact(name, email, profileUrl));
        Contact contact = contactService.연락처상세보기(contactId);
        adapter.addItem(contact);
        notifyListener();
        imageRealPath = null;
    }

    private void updateContact(long contactId, String name, String email, String profileURL){
        Contact updateContact = new Contact(
                contactId,
                name,
                email,
                profileURL
        );
        contactService.연락처수정(updateContact);
        notifyListener();
        imageRealPath = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        contactService.연락처전체삭제();
        notifyListener();
        Log.d(TAG, "onOptionsItemSelected: 삭제됨");
        return true;
    }

    public void notifyListener(){
        Intent intent = getIntent();
        String userID = intent.getStringExtra("extitle");
        // DB내용 변경 -> 어댑터 데이터 변경 -> UI갱신
        adapter.addItems(contactService.연락처전체보기(userID)); // adapter에 내용 변경
        adapter.notifyDataSetChanged(); // UI 갱신
    }

    // 이미지 선택 후 이미지 채우기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImageUpload.PICK_FROM_ALBUM) {
//            Uri photoUri = data.getData();
//            imageRealPath = ImageUpload.getRealPathFromURI(photoUri, test.this);
//            ImageUpload.setImage(imageRealPath, ivProfile);
//        }
    }

    // 권한 얻기
    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

}
