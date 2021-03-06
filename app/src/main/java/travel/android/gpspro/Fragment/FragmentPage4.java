package travel.android.gpspro.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.android.gpspro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import travel.android.gpspro.Activity.MainActivity;
import travel.android.gpspro.Activity.PhotoViewActivity;
import travel.android.gpspro.Adapter.ContactAdapter;
import travel.android.gpspro.DB.Database.ContactAppDatabase;
import travel.android.gpspro.DB.Entity.Contact;
import travel.android.gpspro.DB.ViewModel.ContactService;
import travel.android.gpspro.Other.ImageUpload;

public class FragmentPage4 extends Fragment {
    private static final String TAG = "Main_Activity";
    private FragmentPage4 mContext = FragmentPage4.this;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContactAdapter adapter;
    private FloatingActionButton fab;
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

        String userid = bundle.getString("userid");
        getActivity ().setTitle ("?????? "+userid+" ??????");
        ViewGroup rootView = (ViewGroup)inflater .inflate (R.layout.fragment_page_4, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_contacts);
        fab = rootView.findViewById(R.id.fab);
        ivProfile = rootView.findViewById(R.id.iv_profile);
        qwer = rootView.findViewById (R.id.qwer);
        adapter = new ContactAdapter(mContext, contacts);

        contactAppDatabase = Room.databaseBuilder(getContext (), ContactAppDatabase.class, "ContactDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        contactService = new ContactService(contactAppDatabase.contactRepository());

        initData();
        boldSwitch = rootView.findViewById(R.id.boldSwitch);
        boldSwitch.setOnCheckedChangeListener(new FragmentPage4.boldSwitchListener ());
        recyclerView = rootView.findViewById(R.id.recycler_view_contacts);
        fab = rootView.findViewById(R.id.fab);
        ivProfile = rootView.findViewById(R.id.iv_profile);
        adapter = new ContactAdapter(mContext, contacts);
        initDesign();
        initListener();
        tedPermission();

        return rootView;

    }

    private void initData(){
        Bundle bundle = getArguments();
        int idd = bundle.getInt("idd");
        String email = bundle.getString("userid");
        contacts = contactService.allview (idd);
    }

    private void initDesign(){
        mLayoutManager = new LinearLayoutManager (getContext ());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager (2 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }


    private void initListener(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactDialog();
            }
        });
    }

    public void addContactDialog(){
        View dialogView = LayoutInflater.from(getContext ()).inflate(R.layout.layout_add_contact, null);
        final EditText etName = dialogView.findViewById(R.id.name);


        ivProfile = dialogView.findViewById(R.id.iv_profile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUpload.goToAlbum(mContext);
            }
        });
        Bundle bundle = getArguments();
        int idd = bundle.getInt("idd");
        AlertDialog.Builder dlg = new AlertDialog.Builder(getContext ());
        dlg.setTitle("?????? ?????????");
        dlg.setView(dialogView);
        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imageRealPath==null){
                    Toast.makeText (getContext (), "????????? ??????????????????.", Toast.LENGTH_SHORT).show ();
                    return;
                } else{
                    createContact(etName.getText().toString(), idd, imageRealPath);
                    notifyListener();
                }

            }
        });
        dlg.setNegativeButton("??????", null);
        dlg.show();
    }

    public void editContactDialog(final Contact contact) {

        if (editmod == false) {
            Bundle bundle = getArguments ();
            int idd = bundle.getInt ("idd");
            Intent intent = new Intent (getActivity (), PhotoViewActivity.class);
            intent.putExtra ("idd", idd);
            startActivity (intent);
        } else if (editmod == true) {
            View dialogView = LayoutInflater.from(getContext ()).inflate(R.layout.layout_add_contact, null);
            final EditText etName = dialogView.findViewById(R.id.name);
            final EditText etEmail = dialogView.findViewById(R.id.email);
            ivProfile = dialogView.findViewById(R.id.iv_profile);
            etName.setText(contact.getName());

            if(contact.getProfileURL() == null || contact.getProfileURL().equals("")){
                ivProfile.setImageResource(R.drawable.logopic);
            }else{
                ImageUpload.setImage(contact.getProfileURL(), ivProfile);
            }

            ivProfile = dialogView.findViewById(R.id.iv_profile);
            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageUpload.goToAlbum(mContext);
                }
            });
            Bundle bundle = getArguments();
            int idd = bundle.getInt("idd");
            AlertDialog.Builder dlg = new AlertDialog.Builder(getContext ());
            dlg.setTitle("?????? ?????????");
            dlg.setView(dialogView);
            dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (imageRealPath==null){
                        Toast.makeText (getContext (), "????????? ?????????????????????.", Toast.LENGTH_SHORT).show ();
                        return;
                    } else{
                        updateContact(contact.getId(), etName.getText().toString(), idd, imageRealPath);
                        notifyListener();
                    }

                }
            });
            dlg.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    contactService.delete (contact.getId());
                    notifyListener();
                }
            });
            dlg.show();
        }
    }

    private void createContact(String name, int idd, String profileUrl) {
        long contactId = contactService.insert (new Contact(name, idd, profileUrl));
        Contact contact = contactService.proview (contactId);
        adapter.addItem(contact);
        notifyListener();
        imageRealPath = null;
    }

    private void updateContact(long contactId, String name, int idd, String profileURL){
        Contact updateContact = new Contact(
                contactId,
                name,
                idd,
                profileURL
        );
        contactService.update (updateContact);
        notifyListener();
        imageRealPath = null;
    }

    public void notifyListener(){
        Bundle bundle = getArguments();
        int idd = bundle.getInt("idd");
        adapter.addItems(contactService.allview (idd));
        adapter.notifyDataSetChanged();
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
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
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