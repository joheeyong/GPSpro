package com.android.gpspro.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.android.gpspro.AddEditNoteActivity;
import com.android.gpspro.AddEditPlaceActivity;
import com.android.gpspro.Contact;
import com.android.gpspro.ContactAdapter;
import com.android.gpspro.ContactAppDatabase;
import com.android.gpspro.ContactService;
import com.android.gpspro.ImageUpload;
import com.android.gpspro.MainActivity;
import com.android.gpspro.Note;
import com.android.gpspro.NoteAdapter;
import com.android.gpspro.PhotoViewActivity;
import com.android.gpspro.R;
import com.android.gpspro.test;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import static com.android.gpspro.test.EDIT_NOTE_REQUEST;

public class FragmentPage4 extends Fragment {
    private static final String TAG = "Main_Activity";
    private FragmentPage4 mContext = FragmentPage4.this;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContactAdapter adapter;
    private FloatingActionButton fab;
//    private ContactViewMoel contactViewMoel;
    private ContactAppDatabase contactAppDatabase;
    private ContactService contactService;
    private List<Contact> contacts;
    private TextView qwer;
    private ImageView ivProfile;
    private String imageRealPath;
    boolean editmod=false;
    private Switch boldSwitch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        String email = bundle.getString("extitle");
        getActivity ().setTitle ("나의 "+email+" 여행");
        ViewGroup rootView = (ViewGroup)inflater .inflate (R.layout.fragment_page_4, container, false);
//        toolbar = rootView.findViewById(R.id.toolbar);
        recyclerView = rootView.findViewById(R.id.recycler_view_contacts);
        fab = rootView.findViewById(R.id.fab);
        ivProfile = rootView.findViewById(R.id.iv_profile);
        qwer = rootView.findViewById (R.id.qwer);
//        contactViewMoel.getRow().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                if(integer>0){
//                    qwer.setText(String.valueOf(integer)+"개의 여행지를 방문했습니다.");
//                }
//            }
//        });
        adapter = new ContactAdapter(mContext, contacts);

        // DB관련
        contactAppDatabase = Room.databaseBuilder(getContext (), ContactAppDatabase.class, "ContactDB")
                .allowMainThreadQueries() // 실제로 서비스할 때는 전부 쓰레드 사용해야함.
                .fallbackToDestructiveMigration()
                .build(); //실행되도 객체 있으면 있는거 다시 주입

        contactService = new ContactService(contactAppDatabase.contactRepository());

        initData();
//        initDesign();
        //        mLayoutManager = new LinearLayoutManager (getContext ());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(adapter);
//        toolbar = rootView.findViewById(R.id.toolbar);
        boldSwitch = rootView.findViewById(R.id.boldSwitch);
        boldSwitch.setOnCheckedChangeListener(new FragmentPage4.boldSwitchListener ());
        recyclerView = rootView.findViewById(R.id.recycler_view_contacts);
        fab = rootView.findViewById(R.id.fab);
        ivProfile = rootView.findViewById(R.id.iv_profile);
        adapter = new ContactAdapter(mContext, contacts);
        initDesign();
        initListener();
        tedPermission();
//        count();


        return rootView;

    }


    private void initData(){
        Bundle bundle = getArguments();
        String email = bundle.getString("extitle");
        contacts = contactService.연락처전체보기(email);
    }
    //
    private void initDesign(){

        mLayoutManager = new LinearLayoutManager (getContext ());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager (2 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
//        recyclerView.setVisibility (View.GONE);
    }


    private void initListener(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactDialog();
            }
        });
    }
//    int getNumFiles() {
//        List<Contact> lst = contactService.getValue();  // files is of type LiveData<List<AFile>> files;
//        if (lst == null) {
//            return 0;
//        } else {
//            return lst.size();
//        }
//    }
    public void addContactDialog(){
        View dialogView = LayoutInflater.from(getContext ()).inflate(R.layout.layout_add_contact, null);
        final EditText etName = dialogView.findViewById(R.id.name);

        // 갤러리 사진 가져오기
        ivProfile = dialogView.findViewById(R.id.iv_profile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUpload.goToAlbum(mContext);
            }
        });
        Bundle bundle = getArguments();
        String email = bundle.getString("extitle");
        AlertDialog.Builder dlg = new AlertDialog.Builder(getContext ());
        dlg.setTitle("여행 갤러리");
        dlg.setView(dialogView);
        dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imageRealPath==null){
                    Toast.makeText (getContext (), "사진을 지정해주세요.", Toast.LENGTH_SHORT).show ();
                    return;
                } else{
                    createContact(etName.getText().toString(), email, imageRealPath);
                    notifyListener();
                }

            }
        });
        dlg.setNegativeButton("닫기", null);
        dlg.show();
    }

    public void editContactDialog(final Contact contact) {

        if (editmod == false) {
            Bundle bundle = getArguments ();
            String email = bundle.getString ("extitle");
            Intent intent = new Intent (getActivity (), PhotoViewActivity.class);
            intent.putExtra ("extitle", email);
            startActivity (intent);
        } else if (editmod == true) {
            View dialogView = LayoutInflater.from(getContext ()).inflate(R.layout.layout_add_contact, null);
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
            Bundle bundle = getArguments();
            String email = bundle.getString("extitle");
            AlertDialog.Builder dlg = new AlertDialog.Builder(getContext ());
            dlg.setTitle("여행 갤러리");
            dlg.setView(dialogView);
            dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (imageRealPath==null){
                        Toast.makeText (getContext (), "사진을 재설정해주세요.", Toast.LENGTH_SHORT).show ();
                        return;
                    } else{
                        updateContact(contact.getId(), etName.getText().toString(), email, imageRealPath);
                        notifyListener();
                    }

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
        }
//    public int count() {
//        int cnt = 0;
//        Cursor cursor = contactService.countt ();
//        cnt=cursor.getCount ();
//        Toast.makeText(getActivity (), cnt, Toast.LENGTH_LONG).show();
//        return cnt;
//    }

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        contactService.연락처전체삭제();
        notifyListener();
        Log.d(TAG, "onOptionsItemSelected: 삭제됨");
        return true;
    }


    public void notifyListener(){
        Bundle bundle = getArguments();
        String email = bundle.getString("extitle");
        adapter.addItems(contactService.연락처전체보기(email)); // adapter에 내용 변경
        adapter.notifyDataSetChanged(); // UI 갱신
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
        if (requestCode == ImageUpload.PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            imageRealPath = ImageUpload.getRealPathFromURI(photoUri, (MainActivity) getContext ());
            ImageUpload.setImage(imageRealPath, ivProfile);
        }

    }

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

        TedPermission.with(getContext ())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    class boldSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                editmod=true;
            }

            else{
                editmod=false;
            }
        }
    }

}