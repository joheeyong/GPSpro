package com.android.gpspro;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddEditPlaceActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "EXTRA_ID";
    public static final String EXTRA_TITLE =
            "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "EXTRA_DESCRIPTION";
    public static final String EXTRA_USERID=
            "EXTRA_USERID";
    public static final String EXTRA_LAT =
            "EXTRA_LAT";
    public static final String EXTRA_LNG =
            "EXTRA_LNG";

    double lat1,lng1;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView edit_lat;
    private TextView edit_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("extitle");
        setTitle ("나의 "+userID+" 여행");

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        edit_lat = findViewById(R.id.edit_lat);
        edit_lng = findViewById(R.id.edit_lng);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        if (intent.hasExtra(EXTRA_ID)){
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            edit_lat.setText(intent.getStringExtra(EXTRA_LAT));
        }else {

        }


    }
    public double clickShowMap(View view) {
        String title = editTextTitle.getText().toString();
        if (title.trim().isEmpty() ) {
            Toast.makeText(this, "주소 확인을 클릭하세요", Toast.LENGTH_SHORT).show();
        }
        String addr= editTextTitle.getText().toString();
        Geocoder geocoder= new Geocoder(this, Locale.KOREA);

        try {
            List<Address> addresses=geocoder.getFromLocationName(addr,3); //최대 3개까지 받는데, 0~3개까지 있으면 받는다.
            StringBuffer buffer= new StringBuffer();
            for(Address t : addresses){
                buffer.append(t.getLatitude()+", "+t.getLongitude()+"\n");
            }
            lat1= addresses.get(0).getLatitude ();
            lng1= addresses.get(0).getLongitude ();
            Intent intent = new Intent();
            intent.setAction (Intent.ACTION_VIEW);
            Uri uri = Uri.parse ("geo:"+lat1+""+lng1+"?z=16"+"&q="+lat1+","+lng1+"(aaa)");
            intent.setData (uri);
            edit_lat.setText (String.valueOf(lat1));
            edit_lng.setText (String.valueOf(lng1));
            startActivity (intent);

        } catch (IOException e) {
            Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
        }

        List<Address> list = null;
        try {
            double d1 = Double.parseDouble(edit_lat.getText().toString());
            double d2 = Double.parseDouble(edit_lng.getText().toString());

            list = geocoder.getFromLocation(
                    d1, // 위도
                    d2, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size () == 0) {
                editTextTitle.setText ("해당되는 주소 정보는 없습니다");
            } else {
                editTextTitle.setText (list.get (0).getAdminArea () +" "+ list.get (0).getThoroughfare ()+" " + list.get (0).getPostalCode ()+" "+ title);
            }
        }
        return 0;
    }
    private void savePlace() {
        Intent intent = getIntent();
        String extitle = intent.getStringExtra("extitle");
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String lat = edit_lat.getText().toString();
        String lng = edit_lng.getText().toString();

        if (lat.trim().isEmpty() || lng.trim().isEmpty()) {
            Toast.makeText(this, "주소를 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_USERID, extitle);
        data.putExtra(EXTRA_LAT, lat);
        data.putExtra(EXTRA_LNG, lng);
        data.putExtra("extitle", extitle);
        //   data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_place:
                savePlace();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}


