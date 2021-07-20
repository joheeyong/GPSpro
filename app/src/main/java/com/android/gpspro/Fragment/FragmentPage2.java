package com.android.gpspro.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.gpspro.Place;
import com.android.gpspro.PlaceViewModel;
import com.android.gpspro.R;
import com.android.gpspro.Test3Activity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class FragmentPage2 extends Fragment
        implements OnMapReadyCallback {
    private PlaceViewModel model;

    private MapView mapView = null;
    private GoogleMap mMap;//!

    private Button testdb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate (R.layout.fragment_page_2, container, false);
        ViewGroup rootView = (ViewGroup)inflater.inflate (R.layout.fragment_page_1, container, false);
        mapView = (MapView) layout.findViewById (R.id.map);
        mapView.getMapAsync (this);

        testdb = (Button) layout.findViewById (R.id.testdb);

        testdb.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity (), Test3Activity.class);
            startActivity (intent);
            }
        });



        return layout;
    }



    @Override
    public void onStart() {
        super.onStart ();
        mapView.onStart ();
    }

    @Override
    public void onStop() {
        super.onStop ();
        mapView.onStop ();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState (outState);
        mapView.onSaveInstanceState (outState);
    }

    @Override
    public void onResume() {
        super.onResume ();
        mapView.onResume ();


    }

    @Override
    public void onPause() {
        super.onPause ();
        mapView.onPause ();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory ();
        mapView.onLowMemory ();
    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        mapView.onLowMemory ();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수

        if (mapView != null) {
            mapView.onCreate (savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
                    userListText += title + lat + lng + "\n";
                    makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                            .position (new LatLng (lat, lng))
                            .title ("마커" + title); // 타이틀.
                    mMap.addMarker (makerOptions);

                    LatLng startingPoint = new LatLng(lat, lng);

                    mMap.moveCamera (CameraUpdateFactory.newLatLngZoom(startingPoint,16));


                }

            }
        });

    }




}

