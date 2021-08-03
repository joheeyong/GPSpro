package com.android.gpspro.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.android.gpspro.DB.Entity.Place;
import com.android.gpspro.DB.Repository.PlaceRepository;
import com.android.gpspro.R;
import com.android.gpspro.DB.ViewModel.PlaceViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private PlaceViewModel placeViewModel;
    private GoogleMap mMap;
    Marker  addedMarker = null;
    Double[] latt = new Double[1000];
    Double[] lngg = new Double[1000];
    String[] title = new String[1000];
    private TextView tv_placecount;
    private Button btn_backpage;
    private View mLayout;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle ("나의 여행 지도");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_mainmap);
        mLayout = findViewById(R.id.layout_main);

        Intent intent = getIntent();
        int idd = intent.getIntExtra ("idd",1000);
        tv_placecount = findViewById (R.id.qwer);
        btn_backpage=findViewById (R.id.btn_backpage);
        btn_backpage.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getRowCount(idd).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer>0){
                    tv_placecount.setText(String.valueOf(integer)+"개의 여행지를 방문했습니다.");
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener (this);
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 9);
        mMap.moveCamera(cameraUpdate);

        Intent intent = getIntent();
        int idd = intent.getIntExtra ("idd",1000);
        mMap = googleMap;
        placeViewModel = new ViewModelProvider (this).get (PlaceViewModel.class);
        placeViewModel.getAllPlaces (idd).observe(this, new Observer<List<Place>> () {
            @Override
            public void onChanged(List<Place> places) {
                updateUserProfileList (places);
            }
            private void updateUserProfileList(List<Place> userProfileList) {
                int i;
                for (Place userProfile : userProfileList) {
                    i=0;
                    MarkerOptions markerOptions = new MarkerOptions();
                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.gpsmia3);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                    i++;
                    title[i] = userProfile.getTitle ();
                    latt[i] = userProfile.getLat ();
                    lngg[i] = userProfile.getLng ();
                    markerOptions.position (new LatLng (latt[i], lngg[i]))
                            .title (title[i])
                            .icon (BitmapDescriptorFactory.fromBitmap (smallMarker));
                    addedMarker = mMap.addMarker (markerOptions);
                    LatLng startingPoint = new LatLng (latt[i], lngg[i]);
                    mMap.moveCamera (CameraUpdateFactory.newLatLngZoom (startingPoint, 14));
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Intent intent = new Intent();
        intent.setAction (Intent.ACTION_VIEW);
        LatLng location = marker.getPosition();
        Uri uri = Uri.parse ("geo:"+location.latitude+""+location.longitude+"?z=16"+"&q="+location.latitude+","+location.longitude+"(aaa)");
        intent.setData (uri);
        startActivity (intent);
        return false;
    }
}
