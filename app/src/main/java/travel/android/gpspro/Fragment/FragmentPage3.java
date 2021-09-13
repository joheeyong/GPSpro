package travel.android.gpspro.Fragment;

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
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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

import java.util.List;

import travel.android.gpspro.DB.Entity.Place;
import travel.android.gpspro.DB.ViewModel.PlaceViewModel;

public class FragmentPage3 extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {



    private FragmentActivity mContext;
    private PlaceViewModel placeViewModel;
    private GoogleMap mMap;
    private MapView mapView = null;
    private Marker currentMarker = null;
    Marker addedMarker = null;
    Double[] latt = new Double[1000];
    Double[] lngg = new Double[1000];
    String[] title = new String[1000];
    String[] description = new String[1000];
    String[] userid = new String[1000];
    int[] id = new int[100];
    int[] count= new int[100];
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location mCurrentLocatiion;
    private TextView tv_conte;
    private TextView qwer;
    LatLng previousPosition = null;
    private final LatLng mDefaultLocation = new LatLng (37.56, 126.97);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

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
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        int idd = bundle.getInt ("idd");
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
        tv_conte=layout.findViewById (R.id.tv_conte);
        qwer = layout.findViewById (R.id.qwer);
        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getRowCount(idd).observe(getViewLifecycleOwner(), new Observer<Integer>() {
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
        super.onActivityCreated (savedInstanceState);

        MapsInitializer.initialize (mContext);

        locationRequest = new LocationRequest ()
                .setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval (UPDATE_INTERVAL_MS)
                .setFastestInterval (FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder ();

        builder.addLocationRequest (locationRequest);

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
        String useridd = bundle.getString("userid");
        getActivity ().setTitle ("나의 "+useridd+" 여행");
        int idd= bundle.getInt ("idd");
        mMap = googleMap;
        placeViewModel = new ViewModelProvider (this).get (PlaceViewModel.class);
        placeViewModel.getAllPlaces (idd).observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
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
                    String u=userid[i];
                    title[i] = userProfile.getTitle ();
                    description[i] = userProfile.getDescription ();
                    latt[i] = userProfile.getLat ();
                    lngg[i] = userProfile.getLng ();
                    id[i] = userProfile.getId ();
                    markerOptions.position (new LatLng (latt[i], lngg[i]))
                            .title (title[i])
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

//        getDeviceLocation ();
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
        }
    }

    private void setDefaultLocation() {
        if (currentMarker != null) currentMarker.remove ();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom (mDefaultLocation, 7);
        mMap.moveCamera (cameraUpdate);
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

                double radius = 100;
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
                            Bitmap mLargeIconForNoti = BitmapFactory.decodeResource (getResources (),R.drawable.gpsmia3);
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

                            NotificationManager notificationManager = (NotificationManager) getActivity ().getSystemService (Context.NOTIFICATION_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                notificationManager.createNotificationChannel (new NotificationChannel ("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                            }
                            notificationManager.notify (1, builder.build ());
                        }
                    }else if ((distance[i] > 150) && (!previousPosition.equals (currentPosition)))
                        istrue[i]=true;
                }
            }
        }
    };

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
    public void onStart() {
        super.onStart ();
        mapView.onStart ();
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
