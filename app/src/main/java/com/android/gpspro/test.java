package com.android.gpspro;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.android.gpspro.Fragment.FragmentPage2.EDIT_PLACE_REQUEST;

public class test extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private TextView emptyView, emptyView2;
    private ImageView emptyImage;
    private Button emptyButton;
    private NoteViewModel noteViewModel;
    boolean editmod=false;
    public static final int EDIT_PLACE_REQUEST = 2;
    private Switch boldSwitch;
    private Button btn_testdel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.title_item); // 커스텀 사용할 파일 위치
        emptyImage= findViewById (R.id.empty_image);
        emptyView2 = findViewById (R.id.empty_view2);
        emptyView = findViewById (R.id.empty_view);
        emptyButton = findViewById (R.id.empty_btn);
        btn_testdel = findViewById (R.id.btn_testdel);
        btn_testdel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(test.this);
                dlg.setTitle("전체 삭제"); //제목
                dlg.setMessage("모든 여행 기록을 삭제합니다."); // 메시지// 아이콘 설정
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        noteViewModel.deleteAllNotes();
                        Toast.makeText(test.this, "모든 여행을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    }


                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        boldSwitch = findViewById(R.id.boldSwitch);
        boldSwitch.setOnCheckedChangeListener(new boldSwitchListener());
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(test.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager (this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>> () {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
                if(notes.size() > 0) {
                    boldSwitch.setVisibility (View.VISIBLE);
                    btn_testdel.setVisibility (View.VISIBLE);
                    emptyImage.setVisibility (View.GONE);
                    emptyView.setVisibility (View.GONE);
                    emptyView2.setVisibility (View.GONE);
                    emptyButton.setVisibility (View.GONE);
                    buttonAddNote.setVisibility (View.VISIBLE);}
                else {
                    boldSwitch.setVisibility (View.GONE);
                    btn_testdel.setVisibility (View.GONE);
                    emptyView.setVisibility (View.VISIBLE);
                    emptyImage.setVisibility (View.VISIBLE);
                    emptyView2.setVisibility (View.VISIBLE);
                    emptyButton.setVisibility (View.VISIBLE);
                    buttonAddNote.setVisibility (View.GONE);
                    adapter.submitList(notes);
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
//                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(test.this, "여행을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dlg = new AlertDialog.Builder(test.this);
                dlg.setTitle("여행 삭제"); //제목
                dlg.setMessage("정말 삭제 하시겠습니까?"); // 메시지// 아이콘 설정
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //토스트 메시지
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(test.this, "여행을 삭제했습니다.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(test.this,"확인을 눌르셨습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        }).attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                if (editmod==false) {
                    String extitle = note.getTitle ();
                    Intent intent = new Intent (test.this, MainActivity.class);
                    intent.putExtra ("extitle", extitle);
                    startActivity (intent);
                }else if (editmod ==true){
                    Intent intent = new Intent(test.this, AddEditNoteActivity.class);
                    intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                    intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                    intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                    intent.putExtra(AddEditNoteActivity.EXTRA_DATE, note.getDate());
                    intent.putExtra(AddEditNoteActivity.EXTRA_STAR, note.getStar ());
                    startActivityForResult(intent, EDIT_NOTE_REQUEST);
                }
            }
        });

        adapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Note note) {
                Intent intent = new Intent(test.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_DATE, note.getDate());
                intent.putExtra(AddEditNoteActivity.EXTRA_STAR, note.getStar ());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }

        });
//        adapter.setOnItemLongClickListener (new NoteAdapter.OnItemLongClickListener () {
//            @Override
//            public void onItemLongClick(Note none) {
//                AlertDialog.Builder dlg = new AlertDialog.Builder(test.this);
//                dlg.setTitle("여행 삭제"); //제목
//                dlg.setMessage("정말 삭제 하시겠습니까?"); // 메시지// 아이콘 설정
////                버튼 클릭시 동작
//                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int which) {
//                        //토스트 메시지
//                        noteViewModel.deleteAllNotes();
//                        Toast.makeText(test.this, "모든 여행을 삭제했습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                dlg.setNegativeButton("취소", null);
//                dlg.show();
//            }
//        });

        emptyButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(test.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
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

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);
            Double star = Double.valueOf (data.getStringExtra(AddEditNoteActivity.EXTRA_STAR));

            Note note = new Note(title, description, date, star);
            noteViewModel.insert(note);

            Toast.makeText(this, "여행을 저장했습니다.", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id == -1){
                Toast.makeText(this, "수정을 실패했습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);
            Double star = Double.valueOf (data.getStringExtra(AddEditNoteActivity.EXTRA_STAR));
            Note note = new Note(title, description, date, star);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "수정에 성공했습니다.", Toast.LENGTH_SHORT).show();
            restart();


        } else {
            Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void restart() {

            Intent i = getBaseContext().getPackageManager().
                    getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.delete_all_places:
//                noteViewModel.deleteAllNotes();
//                Toast.makeText(this, "모든 여행을 삭제했습니다.", Toast.LENGTH_SHORT).show();
//            case R.id.edit_all_places:
//                if(editmod==false){
//                    item.setTitle ("편집 모드 취소");
//                    editmod=true;
//                } else if( editmod ==true){
//                    item.setTitle ("편집 모드");
//                    editmod=false;
//                }
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//    }
}
