package travel.android.gpspro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.gpspro.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditAccountActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.bdtask.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.bdtask.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.bdtask.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.bdtask.architectureexample.EXTRA_PRIORITY";
    public static final String EXTRA_IDD =
            "com.bdtask.architectureexample.EXTRA_UDD";



    private RadioButton rg_Transport, rg_lodgment,rg_food,rg_shopping,rg_tourism,rg_other;
    private DecimalFormat decimalFormat = new DecimalFormat ("#,###");
    private String result="";
    private TextView editTextTitle,editTextDescription;
    private EditText et_won;
    private TextView tv_won;
    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Intent intent = getIntent();
        String idd = intent.getStringExtra("idd");
        setTitle ("여행 경비 등록");
        rg_Transport = findViewById(R.id.rg_Transport);
        rg_lodgment= findViewById(R.id.rg_lodgment);
        rg_food= findViewById(R.id.rg_food);
        rg_shopping= findViewById(R.id.rg_shopping);
        rg_tourism= findViewById(R.id.rg_tourism);
        rg_other= findViewById(R.id.rg_other);
        et_won =findViewById (R.id.et_won);
        calendarView = findViewById (R.id.calendar);
        tv_won = findViewById (R.id.tv_won);
        editTextTitle =findViewById (R.id.edit_text_title);



        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy - MM - dd");
        Date date = new Date(calendarView.getDate ());
        editTextTitle.setText (formatter.format (date));
        calendarView.setOnDateChangeListener (new CalendarView.OnDateChangeListener () {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String day;
                day= year +" - " + (month+1) +" - "+dayOfMonth;
                editTextTitle.setText (day);
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    tv_won.setText(result+"원이 선택 되었습니다.");
                    if(et_won.length ()<1){
                        tv_won.setText("");
                    }
//                    et_won.setSelection(result.length());
                    String[] results = result.split("\\.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        et_won.addTextChangedListener(watcher);

            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rg_Transport){
                    editTextDescription.setText("교통");
                }
                if (checkedId==R.id.rg_lodgment){
                    editTextDescription.setText("숙박");
                }
                if (checkedId==R.id.rg_food){
                    editTextDescription.setText("식비");
                }
                if (checkedId==R.id.rg_shopping){
                    editTextDescription.setText("쇼핑");
                }
                if (checkedId==R.id.rg_tourism){
                    editTextDescription.setText("관광");
                }
                if (checkedId==R.id.rg_other){
                    editTextDescription.setText("기타");
                }
            }
        });


        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        tv_won = findViewById (R.id.tv_won);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle ("여행 경비 수정");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            String num= String.valueOf (intent.getIntExtra(EXTRA_PRIORITY,1));
            et_won.setText(num);
        }else {

        }


    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = Integer.parseInt (et_won.getText().toString());
        String priorityy= et_won.getText().toString();

        if (title.trim().isEmpty()||description.trim().isEmpty() || priorityy.trim ().isEmpty () ) {
            Toast.makeText(this, "빈칸이 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getIntent();
        String idd = String.valueOf (intent.getIntExtra("idd",1000));
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra (EXTRA_IDD,idd);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}


