package com.android.gpspro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Query;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Test3Activity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.bdtask.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.bdtask.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.bdtask.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_USERID=
            "com.bdtask.architectureexample.EXTRA_USERID";

    public static final String EXTRA_LAT =
            "com.bdtask.architectureexample.EXTRA_LAT";
    public static final String EXTRA_LNG =
            "com.bdtask.architectureexample.EXTRA_LNG";
    public static final String EXTRA_COUNT =
            "com.bdtask.architectureexample.EXTRA_COUNT";
    public static final String EXTRA_PRIORITY =
            "com.bdtask.architectureexample.EXTRA_PRIORITY";
    private PlaceViewModel placeViewModel;
    double lat1,lng1;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView edit_lat;
    private TextView edit_lng;
    private NumberPicker numberPickerPriority;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        setTitle (userID+"님 환영합니다.");


        back = findViewById (R.id.back);
        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
////                Toast.makeText(getApplicationContext (), "주소 확인을 클릭하세요", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent (getApplicationContext (), FragmentPage1.class);
//                Intent data = new Intent();
//                data.putExtra(EXTRA_TITLE, "test");
//                data.putExtra(EXTRA_DESCRIPTION, "test");
//                data.putExtra(EXTRA_USERID, "null");
//                data.putExtra(EXTRA_LAT, 126.44);
//                data.putExtra(EXTRA_LNG, 36.55);
//                data.putExtra(EXTRA_COUNT, 5);
//
//
//                //   data.putExtra(EXTRA_PRIORITY, priority);
//
////                int id = getIntent().getIntExtra(EXTRA_ID,-1);
////                if (id != -1){
////                    data.putExtra(EXTRA_ID,id);
////                }
//
//                setResult(RESULT_OK, data);
//                finish ();
            }
        });
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        edit_lat = findViewById(R.id.edit_lat);
        edit_lng = findViewById(R.id.edit_lng);
        //numberPickerPriority = findViewById(R.id.number_picker_priority);

        //numberPickerPriority.setMinValue(1);
        // numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        if (intent.hasExtra(EXTRA_ID)){
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            edit_lat.setText(intent.getStringExtra(EXTRA_LAT));
//            edit_lng.setText(intent.getStringExtra(EXTRA_LNG));

            //      numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }else {

        }


    }
    public double clickShowMap(View view) {
        String title = editTextTitle.getText().toString();

        if (title.trim().isEmpty() ) {
            Toast.makeText(this, "주소 확인을 클릭하세요", Toast.LENGTH_SHORT).show();

        }
        // 주소 -> 좌표 (지오코딩)
        String addr= editTextTitle.getText().toString();

        //지오 코딩 작업을 수행하는 객체 생성
        Geocoder geocoder= new Geocoder(this, Locale.KOREA);

        //지오코더에게 지오코딩작업 요청
        try {
            List<Address> addresses=geocoder.getFromLocationName(addr,3); //최대 3개까지 받는데, 0~3개까지 있으면 받는다.

            StringBuffer buffer= new StringBuffer();
            for(Address t : addresses){
                buffer.append(t.getLatitude()+", "+t.getLongitude()+"\n");
            }

            lat1= addresses.get(0).getLatitude ();
            lng1= addresses.get(0).getLongitude ();
            //다이얼로그로 좌표들 보여주기
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


