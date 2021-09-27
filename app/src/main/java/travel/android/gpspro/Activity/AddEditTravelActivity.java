package travel.android.gpspro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.gpspro.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditTravelActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "EXTRA_ID";
    public static final String EXTRA_TITLE =
            "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE =
            "EXTRA_DATE";
    public static final String EXTRA_STAR =
            "EXTRA_STAR";

    private EditText et_title,et_description;
    private TextView tv_date, tv_star;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_item);

        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_description);
        tv_date =findViewById (R.id.tv_date);
        tv_star = findViewById(R.id.tv_star);
        calendarView = findViewById (R.id.calendar);

        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy - M - d");
        Date date = new Date(calendarView.getDate ());
        tv_date.setText (formatter.format (date));
        calendarView.setOnDateChangeListener (new CalendarView.OnDateChangeListener () {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String day;
                day= year +" - " + (month+1) +" - "+dayOfMonth;
                tv_date.setText (day);
            }
        });


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            et_title.setText(intent.getStringExtra(EXTRA_TITLE));
            et_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            tv_date.setText(intent.getStringExtra(EXTRA_DATE));
            tv_star.setText("2.5");

        }else {
            setTitle("Add Note");
        }

        RatingBar rb = (RatingBar) findViewById(R.id.ratingBar1);
        rb.setRating((float) 2.5);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tv_star.setText(String.valueOf(rating));
            }
        });
    }

    private void saveTravel() {
        String title = et_title.getText().toString();
        String description = et_description.getText().toString();
        String date = tv_date.getText().toString();
        String star = tv_star.getText().toString();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a tile and description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_STAR, star);

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
                saveTravel();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}


