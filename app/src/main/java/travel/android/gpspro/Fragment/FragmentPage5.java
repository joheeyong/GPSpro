package travel.android.gpspro.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

import travel.android.gpspro.Activity.AddEditAccountActivity;
import travel.android.gpspro.Adapter.AccountAdapter;
import travel.android.gpspro.DB.Entity.Account;
import travel.android.gpspro.DB.ViewModel.AccountViewModel;

public class FragmentPage5 extends Fragment {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private static final int RESULT_OK =-1 ;
    private AccountViewModel accountViewModel;
    private Button btn_info;
    TextView qwer;
    String favSum;
    String favSumm;
    Dialog dialog01;
    String resilt_int;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup)inflater.inflate (R.layout.fragment_page_5, container, false);
        Bundle bundle = getArguments();
        int idd =bundle.getInt ("idd");
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
                Button noBtn = dialog01.findViewById(R.id.btn_close);
                TextView tv_allcount =dialog01.findViewById (R.id.tv_allcount);
                TextView tv_1count = dialog01.findViewById (R.id.tv_1count);
                TextView tv_2count = dialog01.findViewById (R.id.tv_2count);
                TextView tv_3count = dialog01.findViewById (R.id.tv_3count);
                TextView tv_4count = dialog01.findViewById (R.id.tv_4count);
                TextView tv_5count = dialog01.findViewById (R.id.tv_5count);
                TextView tv_6count = dialog01.findViewById (R.id.tv_6count);

                accountViewModel = ViewModelProviders.of(getActivity ()).get(AccountViewModel.class);;
                accountViewModel.getmTotal(idd).observe(getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSum = "0";
                            tv_allcount.setText("아직 사용한 금액이 없습니다.");
                        } else {

                            long value = Long.parseLong(string);
                            DecimalFormat format = new DecimalFormat("###,###");
                            format.format(value);
                            String resilt_int =format.format (value);
                            favSum = string;
                            tv_allcount.setText("이번 여행에서 총 "+resilt_int+" 원을 사용했습니다.");
                        }
                    }
                });

                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "교통").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_1count.setText ("교통 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_1count.setText ("교통 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "숙박").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_2count.setText ("숙박 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_2count.setText ("숙박 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "식비").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_3count.setText ("식비 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_3count.setText ("식비 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "쇼핑").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_4count.setText ("쇼핑 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_4count.setText ("쇼핑 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "관광").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_5count.setText ("관광 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_5count.setText ("관광 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "기타").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_6count.setText ("기타 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_6count.setText ("기타 : " + resilt_int + " 원");
                        }
                    }
                });



                noBtn.setOnClickListener(new View.OnClickListener() {
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
                intent.putExtra ("idd",idd);
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
        accountViewModel.getmTotal(idd).observe(getActivity (), new Observer<String> () {
            @Override
            public void onChanged(String string) {
                if (string == null) {
                    favSum = "0";
                    qwer.setText("아직 경비 등록이 되지 않았습니다.");
                } else {
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
        accountViewModel.getAllNotes(idd).observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
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
                intent.putExtra(AddEditAccountActivity.EXTRA_IDD, account.getIdd());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
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
            Bundle bundle = getArguments();
            int idd =bundle.getInt ("idd");
            String title = data.getStringExtra(AddEditAccountActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditAccountActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditAccountActivity.EXTRA_PRIORITY, 1);
            Account note = new Account (title, description,idd, priority);
            accountViewModel.insert(note);
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditAccountActivity.EXTRA_ID, -1);
            if (id == -1){
                return;
            }
            Bundle bundle = getArguments();
            int idd =bundle.getInt ("idd");
            String title = data.getStringExtra(AddEditAccountActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditAccountActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditAccountActivity.EXTRA_PRIORITY, 1);
            Account note = new Account (title, description,idd, priority);
            note.setId(id);
            accountViewModel.update(note);
        } else {
        }
    }

}