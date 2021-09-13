package travel.android.gpspro.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import travel.android.gpspro.Adapter.TravelAdapter;
import travel.android.gpspro.DB.Entity.Travel;
import travel.android.gpspro.DB.ViewModel.TravelViewModel;

public class TravelActivity extends AppCompatActivity {

    public static final int ADD_TRAVEL_REQUEST = 1;
    public static final int EDIT_TRAVEL_REQUEST = 2;
    private TextView tv_empty, tv_empty2;
    private ImageView iv_empty;
    private Button btn_addtravel;
    private TravelViewModel noteViewModel;
    boolean editmod=false;
    private Switch sw_mod;
    private Button btn_alldel;
    private FloatingActionButton fbtn_addtravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_item);

        iv_empty= findViewById (R.id.iv_empty);
        tv_empty = findViewById (R.id.tv_empty);
        tv_empty2 = findViewById (R.id.tv_empty2);
        btn_addtravel = findViewById (R.id.btn_addtravel);
        btn_alldel = findViewById (R.id.btn_alldel);
        fbtn_addtravel = findViewById(R.id.fbtn_addtravel);

        btn_alldel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(TravelActivity.this);
                dlg.setTitle("전체 삭제");
                dlg.setMessage("모든 여행 기록을 삭제합니다.");
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        noteViewModel.deleteAllNotes();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        sw_mod = findViewById(R.id.sw_bold);
        sw_mod.setOnCheckedChangeListener(new TravelActivity.boldSwitchListener ());
        btn_addtravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TravelActivity.this, AddEditTravelActivity.class);
                startActivityForResult(intent, ADD_TRAVEL_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager (this));
        recyclerView.setHasFixedSize(true);

        final TravelAdapter adapter = new TravelAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(TravelViewModel.class);
        noteViewModel.getAllTravels().observe(this, new Observer<List<Travel>> () {
            @Override
            public void onChanged(List<Travel> travels) {
                adapter.submitList(travels);
                if(travels.size() > 0) {
                    sw_mod.setVisibility (View.VISIBLE);
                    btn_alldel.setVisibility (View.VISIBLE);
                    iv_empty.setVisibility (View.GONE);
                    tv_empty.setVisibility (View.GONE);
                    tv_empty2.setVisibility (View.GONE);
                    btn_addtravel.setVisibility (View.GONE);
                    fbtn_addtravel.setVisibility (View.VISIBLE);}
                else {
                    sw_mod.setVisibility (View.GONE);
                    btn_alldel.setVisibility (View.GONE);
                    iv_empty.setVisibility (View.VISIBLE);
                    tv_empty.setVisibility (View.VISIBLE);
                    tv_empty2.setVisibility (View.VISIBLE);
                    btn_addtravel.setVisibility (View.VISIBLE);
                    fbtn_addtravel.setVisibility (View.GONE);
                    adapter.submitList(travels);
                }
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

                AlertDialog.Builder dlg = new AlertDialog.Builder(TravelActivity.this);
                dlg.setTitle("여행 삭제"); //제목
                dlg.setMessage("정말 삭제 하시겠습니까?"); // 메시지// 아이콘 설정

                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        noteViewModel.delete(adapter.getTravelAt(viewHolder.getAdapterPosition()));
                    }
                });
                dlg.setNegativeButton("취소",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        restart();
                    }});
                dlg.show ();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TravelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Travel travel) {
                if (editmod==false) {
                    String extitle = travel.getTitle ();
                    int id = travel.getId ();
                    Intent intent = new Intent (TravelActivity.this, MainActivity.class);
                    intent.putExtra ("extitle", extitle);
                    intent.putExtra ("idd",id);
                    startActivity (intent);
                }else if (editmod ==true){
                    Intent intent = new Intent(TravelActivity.this, AddEditTravelActivity.class);
                    intent.putExtra(AddEditTravelActivity.EXTRA_ID, travel.getId());
                    intent.putExtra(AddEditTravelActivity.EXTRA_TITLE, travel.getTitle());
                    intent.putExtra(AddEditTravelActivity.EXTRA_DESCRIPTION, travel.getDescription());
                    intent.putExtra(AddEditTravelActivity.EXTRA_DATE, travel.getDate());
                    intent.putExtra(AddEditTravelActivity.EXTRA_STAR, travel.getStar ());
                    startActivityForResult(intent, EDIT_TRAVEL_REQUEST);
                }
            }
        });

        adapter.setOnItemLongClickListener(new TravelAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Travel travel) {
                Intent intent = new Intent(TravelActivity.this, AddEditTravelActivity.class);
                intent.putExtra(AddEditTravelActivity.EXTRA_ID, travel.getId());
                intent.putExtra(AddEditTravelActivity.EXTRA_TITLE, travel.getTitle());
                intent.putExtra(AddEditTravelActivity.EXTRA_DESCRIPTION, travel.getDescription());
                intent.putExtra(AddEditTravelActivity.EXTRA_DATE, travel.getDate());
                intent.putExtra(AddEditTravelActivity.EXTRA_STAR, travel.getStar ());
                startActivityForResult(intent, EDIT_TRAVEL_REQUEST);
            }

        });

        fbtn_addtravel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelActivity.this, AddEditTravelActivity.class);
                startActivityForResult(intent, ADD_TRAVEL_REQUEST);
            }
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TRAVEL_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTravelActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTravelActivity.EXTRA_DESCRIPTION);
            String date = data.getStringExtra(AddEditTravelActivity.EXTRA_DATE);
            Double star = Double.valueOf (data.getStringExtra(AddEditTravelActivity.EXTRA_STAR));

            Travel travel = new Travel(title, description, date, star);
            noteViewModel.insert(travel);

        } else if (requestCode == EDIT_TRAVEL_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTravelActivity.EXTRA_ID, -1);

            if (id == -1){
                return;
            }

            String title = data.getStringExtra(AddEditTravelActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTravelActivity.EXTRA_DESCRIPTION);
            String date = data.getStringExtra(AddEditTravelActivity.EXTRA_DATE);
            Double star = Double.valueOf (data.getStringExtra(AddEditTravelActivity.EXTRA_STAR));
            Travel travel = new Travel(title, description, date, star);
            travel.setId(id);
            noteViewModel.update(travel);
            restart();
        } else {
        }

    }

    private void restart() {
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
