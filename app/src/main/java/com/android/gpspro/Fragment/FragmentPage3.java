package com.android.gpspro.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.android.gpspro.Place;
import com.android.gpspro.PlaceRepository;
import com.android.gpspro.PlaceViewModel;
import com.android.gpspro.R;
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
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentPage3 extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

//    public static final String EXTRA_ID =
//            "com.bdtask.architectureexample.EXTRA_ID";
//    public static final String EXTRA_TITLE =
//            "com.bdtask.architectureexample.EXTRA_TITLE";
//    public static final String EXTRA_DESCRIPTION =
//            "com.bdtask.architectureexample.EXTRA_DESCRIPTION";
//    public static final String EXTRA_USERID=
//            "com.bdtask.architectureexample.EXTRA_USERID";
//    public static final String EXTRA_LAT =
//            "com.bdtask.architectureexample.EXTRA_LAT";
//    public static final String EXTRA_LNG =
//            "com.bdtask.architectureexample.EXTRA_LNG";
//    public static final String EXTRA_COUNT =
//            "com.bdtask.architectureexample.EXTRA_COUNT";
//    public static final String EXTRA_PRIORITY =
//            "com.bdtask.architectureexample.EXTRA_PRIORITY";

    private FragmentActivity mContext;
    private PlaceViewModel model;
    private PlaceViewModel placeViewModel;
    private static final String TAG = FragmentPage3.class.getSimpleName ();
    private GoogleMap mMap;
    private MapView mapView = null;
    private Marker currentMarker = null;
    Marker  addedMarker = null;
    Double[] latt = new Double[100];
    Double[] lngg = new Double[100];
    String[] title = new String[100];
    String[] description = new String[100];
    String[] userid = new String[100];
    int[] id = new int[100];
    int[] count= new int[100];
    private PlaceRepository placeRepository;
    private FusedLocationProviderClient mFusedLocationProviderClient; // Deprecated된 FusedLocationApi를 대체
    private LocationRequest locationRequest;
    private Location mCurrentLocatiion;
    private TextView tv_conte;
    private TextView qwer;
     LatLng previousPosition = null;
    private final LatLng mDefaultLocation = new LatLng (37.56, 126.97);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000 * 60 * 1;  // 1분 단위 시간 갱신
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000 * 30; // 30초 단위로 화면 갱신

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    int tracking = 0;
    private Location location;
    LatLng currentPosition;
    Double[] distance = new Double[100];
    Boolean[] istrue = new Boolean[] {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
    public FragmentPage3() {
    }

    @Override
    public void onAttach(Activity activity) { // Fragment 가 Activity에 attach 될 때 호출된다.
        mContext = (FragmentActivity) activity;
        super.onAttach (activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Layout 을 inflate 하는 곳이다.
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        String userid = bundle.getString("extitle");
        if (savedInstanceState != null) {
            mCurrentLocatiion = savedInstanceState.getParcelable (KEY_LOCATION);
            CameraPosition mCameraPosition = savedInstanceState.getParcelable (KEY_CAMERA_POSITION);
        }
        View layout = inflater.inflate (R.layout.fragment_page_3, container, false);
        mapView = (MapView) layout.findViewById (R.id.map);
        if (mapView != null) {
            mapView.onCreate (savedInstanceState);
        }
        mapView.getMapAsync (this);
        tracking = 1 - tracking;
        placeRepository = new PlaceRepository (getContext ());
        tv_conte=layout.findViewById (R.id.tv_conte);
        qwer = layout.findViewById (R.id.qwer);
        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getRowCount(userid).observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer>0){
                    qwer.setText(String.valueOf(integer)+"개의 여행지를 방문했습니다.");
                }
            }
        });
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Fragement에서의 OnCreateView를 마치고, Activity에서 onCreate()가 호출되고 나서 호출되는 메소드이다.
        // Activity와 Fragment의 뷰가 모두 생성된 상태로, View를 변경하는 작업이 가능한 단계다.
        super.onActivityCreated (savedInstanceState);

        MapsInitializer.initialize (mContext);

        locationRequest = new LocationRequest ()
                .setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY) // 정확도를 최우선적으로 고려
                .setInterval (UPDATE_INTERVAL_MS) // 위치가 Update 되는 주기
                .setFastestInterval (FASTEST_UPDATE_INTERVAL_MS); // 위치 획득후 업데이트되는 주기

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder ();

        builder.addLocationRequest (locationRequest);

        // FusedLocationProviderClient 객체 생성
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient (mContext);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState (outState);
        mapView.onSaveInstanceState (outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundle = getArguments();
        String useridd = bundle.getString("extitle");
        getActivity ().setTitle ("나의 "+useridd+" 여행");
        mMap = googleMap;
        model = new ViewModelProvider (this).get (PlaceViewModel.class);
//        model.getAllPlaces ().observe (this, new Observer<List<Place>> () {
        model.getAllPlaces (useridd).observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                updateUserProfileList (places);
            }

            private void updateUserProfileList(List<Place> userProfileList) {
                Bundle bundle = getArguments();
                String extitle = bundle.getString("extitle");
                String userListText = "";
                int i;
                Double lat, lng;


                for (Place userProfile : userProfileList) {
                    i=0;
                    MarkerOptions markerOptions = new MarkerOptions();
                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.gpsmia2);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                    i++;
                        userid[i] =userProfile.getUserid ();
                        String u=userid[i];
                        title[i] = userProfile.getTitle ();
                        description[i] = userProfile.getDescription ();
                        latt[i] = userProfile.getLat ();
                        lngg[i] = userProfile.getLng ();
                        id[i] = userProfile.getId ();

                            markerOptions.position (new LatLng (latt[i], lngg[i]))
                                    .title (title[i]) // 타이틀.
                                    .icon (BitmapDescriptorFactory.fromBitmap (smallMarker));
                            addedMarker = mMap.addMarker (markerOptions);

                            LatLng startingPoint = new LatLng (latt[i], lngg[i]);

                            mMap.moveCamera (CameraUpdateFactory.newLatLngZoom (startingPoint, 16));
                }
            }
        });
        mMap.setOnMarkerClickListener (this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
            }
        });
        setDefaultLocation (); // GPS를 찾지 못하는 장소에 있을 경우 지도의 초기 위치가 필요함.

        getLocationPermission ();

        updateLocationUI ();

        getDeviceLocation ();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled (true);
                mMap.getUiSettings ().setMyLocationButtonEnabled (true);
            } else {
                mMap.setMyLocationEnabled (false);
                mMap.getUiSettings ().setMyLocationButtonEnabled (false);
                mCurrentLocatiion = null;
                getLocationPermission ();
            }
        } catch (SecurityException e) {
            Log.e ("Exception: %s", e.getMessage ());
        }
    }

    private void setDefaultLocation() {
        if (currentMarker != null) currentMarker.remove ();

//        MarkerOptions markerOptions = new MarkerOptions ();
//        markerOptions.position (mDefaultLocation);
//        markerOptions.title ("위치정보 가져올 수 없음");
//        markerOptions.snippet ("위치 퍼미션과 GPS 활성 여부 확인하세요");
//        markerOptions.draggable (true);
//        markerOptions.icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED));
//        currentMarker = mMap.addMarker (markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom (mDefaultLocation, 15);
        mMap.moveCamera (cameraUpdate);
    }

    String getCurrentAddress(LatLng latlng) {
        // 위치 정보와 지역으로부터 주소 문자열을 구한다.
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder (mContext, Locale.getDefault ());

        // 지오코더를 이용하여 주소 리스트를 구한다.
        try {
            addressList = geocoder.getFromLocation (latlng.latitude, latlng.longitude, 1);
        } catch (IOException e) {
            Toast.makeText (mContext, "위치로부터 주소를 인식할 수 없습니다. 네트워크가 연결되어 있는지 확인해 주세요.", Toast.LENGTH_SHORT).show ();
            e.printStackTrace ();
            return "주소 인식 불가";
        }

        if (addressList.size () < 1) { // 주소 리스트가 비어있는지 비어 있으면
            return "해당 위치에 주소 없음";
        }

        // 주소를 담는 문자열을 생성하고 리턴
        Address address = addressList.get (0);
        StringBuilder addressStringBuilder = new StringBuilder ();
        for (int i = 0; i <= address.getMaxAddressLineIndex (); i++) {
            addressStringBuilder.append (address.getAddressLine (i));
            if (i < address.getMaxAddressLineIndex ())
                addressStringBuilder.append ("\n");
        }

        return addressStringBuilder.toString ();
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            List<Location> locationList = locationResult.getLocations();
            int i=1;


            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);

                previousPosition = currentPosition;

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                if (previousPosition == null) previousPosition = currentPosition;


                    double radius = 100; // 500m distance.
                    for(i=1; latt[i]!=null ;i++){
                        distance[i] = SphericalUtil.computeDistanceBetween (currentPosition, new LatLng (latt[i],lngg[i]));

                        if ((distance[i] < radius) && (!previousPosition.equals (currentPosition))) {
                            if(istrue[i]==true) {

                                Intent push = new Intent();
                                push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                push.setClass(getActivity(), FragmentPage3.class);
                                PendingIntent mPendingIntent = PendingIntent.getActivity(
                                        getActivity (),0,
                                        push,
                                        PendingIntent.FLAG_CANCEL_CURRENT
                                );
                                Bitmap mLargeIconForNoti = BitmapFactory.decodeResource (getResources (),R.drawable.gpsmia2);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder (getActivity (),"default");
                                builder.setSmallIcon (R.mipmap.ic_launcher_foreground);
                                builder.setContentTitle ("GPS 위치도착 알림");
                                builder.setContentText (title[i] + "에 도착했습니다.");
                                builder.setContentText ("에 도착했습니다.");
                                builder.setAutoCancel (true);
                                builder.setColor(Color.BLUE);
                                builder.setLargeIcon (mLargeIconForNoti);
                                builder.setFullScreenIntent(mPendingIntent, true);
                                count[i]++;
//                                Intent intent = getIntent();
//                                Intent data = new Intent();
//                                data.putExtra(EXTRA_TITLE, title);
//                                data.putExtra(EXTRA_DESCRIPTION, description);
//                                data.putExtra(EXTRA_USERID, userid);
//                                data.putExtra(EXTRA_LAT, latt[i]);
//                                data.putExtra(EXTRA_LNG, lngg[i]);
//                                data.putExtra(EXTRA_COUNT, count[i]);
//                                //   data.putExtra(EXTRA_PRIORITY, priority);
//
//                                int id = getIntent().getIntExtra(EXTRA_ID,-1);
//                                if (id != -1){
//                                    data.putExtra(EXTRA_ID,id);
//                                }
//
//                                setResult(RESULT_OK, data);
//                                data.putExtra(EXTRA_COUNT, count);
//                                int id = getIntent().getIntExtra(EXTRA_ID,-1);
//                                if (id != -1){
//                                    data.putExtra(EXTRA_ID,id);
//                                }
//
//                                setResult(RESULT_OK, data);
                                NotificationManager notificationManager = (NotificationManager) getActivity ().getSystemService (Context.NOTIFICATION_SERVICE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    notificationManager.createNotificationChannel (new NotificationChannel ("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                                }

                                notificationManager.notify (1, builder.build ());

                            }


                        }else if ((distance[i] > 150) && (!previousPosition.equals (currentPosition)))
                            istrue[i]=true;
                    }
                    //else
                    //{
                    //  Toast.makeText (TestActivity.this, addedMarker.getTitle () + "까지" + (int) distance + "m 남음", Toast.LENGTH_LONG).show ();
                    // }

            }

        }

    };


    private String CurrentTime() {
        Date today = new Date ();
        SimpleDateFormat date = new SimpleDateFormat ("yyyy/MM/dd");
        SimpleDateFormat time = new SimpleDateFormat ("hh:mm:ss a");
        return time.format (today);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove ();

        LatLng currentLatLng = new LatLng (location.getLatitude (), location.getLongitude ());

        MarkerOptions markerOptions = new MarkerOptions ();
        markerOptions.position (currentLatLng);
        markerOptions.title (markerTitle);
        markerOptions.snippet (markerSnippet);
        markerOptions.draggable (true);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng (currentLatLng);
        mMap.moveCamera (cameraUpdate);
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.requestLocationUpdates (locationRequest, locationCallback, Looper.myLooper ());
            }
        } catch (SecurityException e) {
            Log.e ("Exception: %s", e.getMessage ());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission (mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions (mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI ();
    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity ().getSystemService (Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled (LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onStart() { // 유저에게 Fragment가 보이도록 해준다.
        super.onStart ();
        mapView.onStart ();
        Log.d (TAG, "onStart ");
    }

    @Override
    public void onStop() {
        super.onStop ();
        mapView.onStop ();
        if (mFusedLocationProviderClient != null) {
            Log.d (TAG, "onStop : removeLocationUpdates");
            mFusedLocationProviderClient.removeLocationUpdates (locationCallback);
        }
    }

    @Override
    public void onResume() { // 유저에게 Fragment가 보여지고, 유저와 상호작용이 가능하게 되는 부분
        super.onResume ();
        mapView.onResume ();
        if (mLocationPermissionGranted) {
            Log.d (TAG, "onResume : requestLocationUpdates");

        }
        if (ActivityCompat.checkSelfPermission (getContext (), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (getActivity (), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates (locationRequest, locationCallback, null);
        if (mMap!=null) mMap.setMyLocationEnabled(true);
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onDestroyView() { // 프래그먼트와 관련된 View 가 제거되는 단계
        super.onDestroyView();
        if (mFusedLocationProviderClient != null) {
            Log.d(TAG, "onDestroyView : removeLocationUpdates");
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
    @Override
    public void onDestroy() {
        // Destroy 할 때는, 반대로 OnDestroyView에서 View를 제거하고, OnDestroy()를 호출한다.
        super.onDestroy();
        mapView.onDestroy();
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
