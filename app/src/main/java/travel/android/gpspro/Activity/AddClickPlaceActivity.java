package travel.android.gpspro.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.io.IOException;
import java.util.List;

import travel.android.gpspro.DB.Entity.Place;
import travel.android.gpspro.DB.ViewModel.PlaceViewModel;

public class AddClickPlaceActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    public static final String EXTRA_ID =
            "EXTRA_ID";
    public static final String EXTRA_TITLE =
            "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "EXTRA_DESCRIPTION";
    public static final String EXTRA_IDD=
            "EXTRA_IDD";
    public static final String EXTRA_LAT =
            "EXTRA_LAT";
    public static final String EXTRA_LNG =
            "EXTRA_LNG";

    private PlaceViewModel placeViewModel;
    private GoogleMap mMap;
    Marker  addedMarker = null;
    Double[] latt = new Double[1000];
    Double[] lngg = new Double[1000];
    String[] title = new String[1000];
    private TextView qwer;
    private Button btn_backpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle ("나의 여행 지도");
        setContentView(R.layout.activity_click_place);
        Intent intent = getIntent();
        int idd = intent.getIntExtra ("idd",1000);
        qwer = findViewById (R.id.qwer);
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
                    qwer.setText(String.valueOf(integer)+"개의 여행지를 방문했습니다.");
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
        setDefaultLocation();
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
                            .title (title[i]) // 타이틀.
                            .icon (BitmapDescriptorFactory.fromBitmap (smallMarker));
                    addedMarker = mMap.addMarker (markerOptions);
                    LatLng startingPoint = new LatLng (latt[i], lngg[i]);
                    mMap.moveCamera (CameraUpdateFactory.newLatLngZoom (startingPoint, 16));
                }
            }
        });

        final Geocoder geocoder = new Geocoder(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
                Double latitude = point.latitude;
                Double longitude = point.longitude;
                clicklat.setText (String.valueOf(latitude));
                clicklng.setText (String.valueOf(longitude));
                List<Address> list = null;
                try {
                    double d1 = point.latitude;
                    double d2 = point.longitude;
                    list = geocoder.getFromLocation(
                            d1,
                            d2,
                            10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (list != null) {
                    if (list.size () == 0) {
                        id.setText ("해당되는 주소 정보는 없습니다");
                    } else {
                        id.setText (list.get (0).getAdminArea () +" "+ list.get (0).getThoroughfare ()+" " + list.get (0).getPostalCode ());
                    }
                }
                dialog=new AlertDialog.Builder(AddClickPlaceActivity.this)
                        .setView(loginLayout)
                        .show();
                save.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        String title = id.getText().toString();
                        String description = pw.getText().toString();
                        String lat = clicklat.getText().toString();
                        String lng = clicklng.getText().toString();
                        if (title.trim().isEmpty()) {
                            txt_redzone.setVisibility (View.VISIBLE);
                            return;
                        }
                        String idd = String.valueOf (intent.getIntExtra("idd",1000));
                        Intent data = new Intent();
                        data.putExtra(EXTRA_TITLE, title);
                        data.putExtra(EXTRA_DESCRIPTION, description);
                        data.putExtra(EXTRA_IDD, idd);
                        data.putExtra(EXTRA_LAT, lat);
                        data.putExtra(EXTRA_LNG, lng);
                        int id = getIntent().getIntExtra(EXTRA_ID,-1);
                        if (id != -1){
                            data.putExtra(EXTRA_ID,id);
                        }
                        setResult(RESULT_OK, data);
                        Double latt, lngt;
                        latt= Double.valueOf (clicklat.getText().toString());
                        lngt= Double.valueOf (clicklng.getText().toString());
                        MarkerOptions makerOptions = new MarkerOptions ();

                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.gpsmia3);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

                        makerOptions
                                .position (new LatLng (latt, lngt))
                                .title ("마커" + title)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));// 타이틀.

                        googleMap.addMarker (makerOptions);
                        dialog.dismiss ();
                        overridePendingTransition(R.anim.stay, R.anim.slide_down);
                        finish ();
                    }
                });
                cancle.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss ();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setDefaultLocation() {
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 9);
        mMap.moveCamera(cameraUpdate);
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
