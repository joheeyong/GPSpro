package travel.android.gpspro.Activity;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.gpspro.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import travel.NetworkStatus;

public class AddEditPlaceActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "EXTRA_ID";
    public static final String EXTRA_IDD =
            "EXTRA_IDD";
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
    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView edit_lat;
    private TextView edit_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userid");
        int idd = intent.getIntExtra("idd",1000);
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
        int status = NetworkStatus.getConnectivityStatus (getApplicationContext ());
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){

        Geocoder geocoder= new Geocoder(this, Locale.KOREA);
        List<Address> addresses=null;
        String addr= editTextTitle.getText().toString();
        if (addr.trim().isEmpty() ) {
            Toast.makeText(this, "주소 확인을 클릭하세요", Toast.LENGTH_SHORT).show();
        }

        try {
            addresses=geocoder.getFromLocationName(addr,3); //최대 3개까지 받는데, 0~3개까지 있으면 받는다.


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
        }

        if (addresses != null) {
            if (addresses.size () == 0) {
                Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
            } else {
                Address addrr = addresses.get (0);
                double lat = addrr.getLatitude ();
                double lon = addrr.getLongitude ();
                String sss = String.format ("geo:%f,%f", lat, lon);
            edit_lat.setText (String.valueOf(lat));
            edit_lng.setText (String.valueOf(lon));
                Intent intent = new Intent (
                        Intent.ACTION_VIEW,
                        Uri.parse (sss));
                startActivity (intent);
                editTextDescription.setText (addr);
                editTextTitle.setText (addresses.get (0).getAdminArea ()+" "+addresses.get (0).getPostalCode ()+" "+addresses.get (0).getFeatureName ());
            }
        }}else{
            Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결이 끊겼습니다.",Toast.LENGTH_SHORT);
            toast.show();
        }
        return 0;
    }
    private void savePlace() {

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String lat = edit_lat.getText().toString();
        String lng = edit_lng.getText().toString();
        String idd = String.valueOf (intent.getIntExtra("idd",1000));

        if (title.trim().isEmpty() || lng.trim().isEmpty()) {
            Toast.makeText(this, "주소를 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_USERID, userid);
        data.putExtra(EXTRA_LAT, lat);
        data.putExtra(EXTRA_LNG, lng);
        data.putExtra("userid", userid);
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


