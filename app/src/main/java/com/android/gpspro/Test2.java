package com.android.gpspro;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.gpspro.Fragment.FragmentPage1;
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

public class Test2 extends FragmentActivity implements OnMapReadyCallback {
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
    private PlaceViewModel model;
    //GoogleMap 객체
    GoogleMap googleMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    RelativeLayout boxMap;
    //나의 위도 경도 고도
    double mLatitude;  //위도
    double mLongitude; //경도

    private AlertDialog dialog;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin, btn_clickback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);



        boxMap = (RelativeLayout)findViewById(R.id.boxMap);

        //LocationManager
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        btn_clickback = (Button) findViewById (R.id.btn_clickback);

        btn_clickback.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext (), FragmentPage1.class);
                String userID = intent.getStringExtra("userID");

                startActivity (intent);

            }
        });

        //GPS가 켜져있는지 체크
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            finish();
        }

        //마시멜로 이상이면 권한 요청하기
        if(Build.VERSION.SDK_INT >= 23){
            //권한이 없는 경우
            if(ContextCompat.checkSelfPermission(Test2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(Test2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Test2.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION} , 1);
            }
            //권한이 있는 경우
            else{
                requestMyLocation();
            }
        }
        //마시멜로 아래
        else{
            requestMyLocation();
        }
    }



    //권한 요청후 응답 콜백
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //ACCESS_COARSE_LOCATION 권한
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == 1) {
            //권한받음
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestMyLocation ();
            }
            //권한못받음
            else {
                Toast.makeText (this, "권한없음", Toast.LENGTH_SHORT).show ();
                finish ();
            }
        }
    }

    //나의 위치 요청
    public void requestMyLocation(){
        if(ContextCompat.checkSelfPermission(Test2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Test2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        //요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, locationListener);
    }

    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(ContextCompat.checkSelfPermission(Test2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(Test2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            //나의 위치를 한번만 가져오기 위해
            locationManager.removeUpdates(locationListener);

            //위도 경도
            mLatitude = location.getLatitude();   //위도
            mLongitude = location.getLongitude(); //경도

            //맵생성
            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            //콜백클래스 설정
            mapFragment.getMapAsync(Test2.this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { Log.d("gps", "onStatusChanged"); }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    //구글맵 생성 콜백
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        model = new ViewModelProvider (this).get (PlaceViewModel.class);
        model.getAllPlaces ().observe (this, new Observer<List<Place>> () {
            @Override
            public void onChanged(List<Place> places) {
                updateUserProfileList (places);
            }

            private void updateUserProfileList(List<Place> userProfileList) {
                String userListText = "";
                String title;
                Double lat, lng;

                for (Place userProfile : userProfileList) {
                    MarkerOptions makerOptions = new MarkerOptions ();
                    title = userProfile.getTitle ();
                    lat = userProfile.getLat ();
                    lng = userProfile.getLng ();
                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.gpsmia2);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                    userListText += title + lat + lng + "\n";
                    makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                            .position (new LatLng (lat, lng))
                            .title ("마커" + title) // 타이틀.
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    googleMap.addMarker (makerOptions);

                    LatLng startingPoint = new LatLng(lat, lng);

                    //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    googleMap.moveCamera (CameraUpdateFactory.newLatLngZoom(startingPoint,16));


                }

            }
        });
        final Geocoder geocoder = new Geocoder(this);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                Dialog dialog;

                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.dialog_gpsinput, null);
                final Button save = (Button) loginLayout.findViewById(R.id.btn_save);
                final Button cancle = (Button) loginLayout.findViewById(R.id.btn_cancle);
                final EditText id = (EditText)loginLayout.findViewById(R.id.id);
                final EditText pw = (EditText)loginLayout.findViewById(R.id.pw);
                final TextView clicklng = (TextView) loginLayout.findViewById(R.id.clicklng);
                final TextView clicklat = (TextView) loginLayout.findViewById(R.id.clicklat);
                TextView txt_redzone = (TextView) loginLayout.findViewById(R.id.txt_redzone);
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                clicklat.setText (String.valueOf(latitude));
                clicklng.setText (String.valueOf(longitude));
                List<Address> list = null;
                try {
                    double d1 = point.latitude;
                    double d2 = point.longitude;

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
                        id.setText ("해당되는 주소 정보는 없습니다");
                    } else {
                        id.setText (list.get (0).getAdminArea () +" "+ list.get (0).getThoroughfare ()+" " + list.get (0).getPostalCode ());
                    }

                }

                dialog=new AlertDialog.Builder(Test2.this)
                        .setView(loginLayout)
                        .show();
                save.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        String userID = intent.getStringExtra("userID");

                        String title = id.getText().toString();
                        String description = pw.getText().toString();
                        String userid = userID;
                        String lat = clicklat.getText().toString();
                        String lng = clicklng.getText().toString();


                        if (title.trim().isEmpty()) {
                            txt_redzone.setVisibility (View.VISIBLE);
                            return;
                        }

                        Intent data = new Intent();
                        data.putExtra(EXTRA_TITLE, title);
                        data.putExtra(EXTRA_DESCRIPTION, description);
                        data.putExtra(EXTRA_USERID, userid);
                        data.putExtra(EXTRA_LAT, lat);
                        data.putExtra(EXTRA_LNG, lng);
                        data.putExtra(EXTRA_COUNT, 5);
                        //   data.putExtra(EXTRA_PRIORITY, priority);

                        int id = getIntent().getIntExtra(EXTRA_ID,-1);
                        if (id != -1){
                            data.putExtra(EXTRA_ID,id);
                        }

                        setResult(RESULT_OK, data);
                        Double latt, lngt;
                        latt= Double.valueOf (clicklat.getText().toString());
                        lngt= Double.valueOf (clicklng.getText().toString());
                        MarkerOptions makerOptions = new MarkerOptions ();

                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.gpsmia2);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

                        makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                                .position (new LatLng (latt, lngt))
                                .title ("마커" + title)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));// 타이틀.

                        googleMap.addMarker (makerOptions);
                        dialog.dismiss ();
                    }
                });
                cancle.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss ();
                    }
                });
                // 마커 타이틀
                //  mOptions.title("마커 좌표");
                //   Double latitude = point.latitude; // 위도
                //   Double longitude = point.longitude; // 경도
                //  // 마커의 스니펫(간단한 텍스트) 설정
                //   mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                //   mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                //   googleMap.addMarker(mOptions);
            }
        });
        //지도타입 - 일반
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //나의 위치 설정
        LatLng position = new LatLng(mLatitude , mLongitude);

        //화면중앙의 위치와 카메라 줌비율
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        //지도 보여주기
        boxMap.setVisibility(View.VISIBLE);


    }
}
