package com.android.gpspro.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.Account;
import com.android.gpspro.AccountAdapter;
import com.android.gpspro.AccountViewModel;
import com.android.gpspro.AddClickPlaceActivity;
import com.android.gpspro.AddEditAccountActivity;
import com.android.gpspro.AddEditPlaceActivity;
import com.android.gpspro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

public class FragmentPage5 extends Fragment {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private static final int RESULT_OK =-1 ;
    private AccountViewModel accountViewModel;
    private Button btn_info;
    TextView qwer;
    String favSum = "0";
    Dialog dialog01;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup)inflater.inflate (R.layout.fragment_page_5, container, false);
        Bundle bundle = getArguments();
        String extitle = bundle.getString("extitle");
        FloatingActionButton buttonAddNote = rootView.findViewById(R.id.button_add_note);
        dialog01= new Dialog (getContext ());
        dialog01.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog01.setContentView (R.layout.dialog_accountinfo);
        btn_info =rootView.findViewById (R.id.btn_info);
        btn_info.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showDialog01();
            }
            private void showDialog01() {
                dialog01.show();
                Button noBtn = dialog01.findViewById(R.id.noBtn);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog01.dismiss();
                    }
                });

                dialog01.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog01.dismiss();
                    }
                });

            }
        });
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity (), AddEditAccountActivity.class);
                intent.putExtra ("extitle",extitle);
                startActivityForResult (intent,ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager (getContext ()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        final AccountAdapter adapter = new AccountAdapter();
        recyclerView.setAdapter(adapter);
        qwer = rootView.findViewById (R.id.qwer);
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);;
        accountViewModel.getmTotal(extitle).observe(getActivity (), new Observer<String> () {
            @Override
            public void onChanged(String string) {
                if (string == null) {
                    favSum = "0";
                    qwer.setText("아직 경비 등록이 되지 않았습니다.");
                } else {
//                    String money="999999999" ;
//                    String result_int = format.format(value);
                 long value = Long.parseLong(string);
                    DecimalFormat format = new DecimalFormat("###,###");
                    format.format(value);
                    String resilt_int =format.format (value);
                    favSum = string;
                    qwer.setText(resilt_int+" 원을 사용했습니다.");
                }
            }
        });
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        accountViewModel.getAllNotes(extitle).observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                adapter.submitList(accounts);
            }
        });
        new ItemTouchHelper (new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                accountViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Account account) {
                Intent intent = new Intent(getContext (), AddEditAccountActivity.class);
                intent.putExtra(AddEditAccountActivity.EXTRA_ID, account.getId());
                intent.putExtra(AddEditAccountActivity.EXTRA_TITLE, account.getTime());
                intent.putExtra(AddEditAccountActivity.EXTRA_DESCRIPTION, account.getCategory());
                intent.putExtra(AddEditAccountActivity.EXTRA_PRIORITY, account.getPrice());
                intent.putExtra(AddEditAccountActivity.EXTRA_USERID, account.getUserid());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
//                startActivity (intent);
            }
        });
        return rootView;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditAccountActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditAccountActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditAccountActivity.EXTRA_PRIORITY, 1);
            String userid = data.getStringExtra(AddEditAccountActivity.EXTRA_USERID);
            Account note = new Account (title, description,userid, priority);
            accountViewModel.insert(note);
//            Toast.makeText(getContext (), "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditAccountActivity.EXTRA_ID, -1);
            if (id == -1){
//                Toast.makeText(getContext (), "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = getArguments();
            String extitle = bundle.getString("extitle");
            String title = data.getStringExtra(AddEditAccountActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditAccountActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditAccountActivity.EXTRA_PRIORITY, 1);
            String userid = extitle;
            Account note = new Account (title, description,userid, priority);
//            accountViewModel.insert(note);
            note.setId(id);
            accountViewModel.update(note);
            Toast.makeText(getContext (), "Note Updated", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getContext (), "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}