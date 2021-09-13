package travel.android.gpspro.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.android.gpspro.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import travel.android.gpspro.DB.Entity.Place;
import travel.android.gpspro.DB.ViewModel.PlaceViewModel;

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
