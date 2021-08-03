package com.android.gpspro.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.gpspro.Activity.AddClickPlaceActivity;
import com.android.gpspro.Activity.AddEditPlaceActivity;
import com.android.gpspro.Activity.MainActivity;
import com.android.gpspro.DB.Entity.Place;
import com.android.gpspro.Adapter.PlaceAdapter;
import com.android.gpspro.DB.Repository.PlaceRepository;
import com.android.gpspro.DB.ViewModel.PlaceViewModel;
import com.android.gpspro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentPage2 extends Fragment {
    public static final int ADD_PLACE_REQUEST = 1;
    public static final int EDIT_PLACE_REQUEST = 2;
    private static final int RESULT_OK = -1;
    private PlaceViewModel placeViewModel;
    private TextView emptyView, emptyView2;
    private ImageView emptyImage;
    private RecyclerView recyclerView;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;
    PlaceAdapter adapter = new PlaceAdapter();
    LinearLayout asdf;
    MainActivity activity;
    Dialog dialog01;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater .inflate (R.layout.fragment_page_2, container, false);
        emptyImage= rootView.findViewById (R.id.empty_image);
        emptyView = rootView.findViewById (R.id.empty_view);
        emptyView2 = rootView.findViewById (R.id.empty_view2);
        Bundle bundle = getArguments();
        String userid = bundle.getString("userid");
        int idd = bundle.getInt("idd");
        getActivity ().setTitle ("나의 "+userid+" 여행");
        fab_open = AnimationUtils.loadAnimation(getActivity (), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity (), R.anim.fab_close);
        FloatingActionButton fab_main = rootView.findViewById(R.id.fab_main);
        FloatingActionButton  fab_sub1 = rootView.findViewById(R.id.fab_sub1);
        FloatingActionButton  fab_sub2= rootView.findViewById(R.id.fab_sub2);
        asdf = rootView.findViewById (R.id.asdf);
        dialog01= new Dialog (getContext ());
        dialog01.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog01.setContentView (R.layout.dialog_placechoice);

        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity (), AddEditPlaceActivity.class);
                intent.putExtra ("userid",userid);
                intent.putExtra ("idd",idd);
                startActivityForResult (intent,ADD_PLACE_REQUEST);
            }
        });
        fab_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity (), AddClickPlaceActivity.class);
                intent.putExtra ("userid",userid);
                intent.putExtra ("idd",idd);
                startActivityForResult (intent,ADD_PLACE_REQUEST);
            }
        });
        fab_main.setOnClickListener (new View.OnClickListener () {
                                         @Override
                                         public void onClick(View v) {

                                             switch (v.getId ()) {
                                                 case R.id.fab_main:
                                                     toggleFab();
                                                     break;
                                                 case R.id.fab_sub1:
                                                     toggleFab();
                                                     break;
                                                 case R.id.fab_sub2:
                                                     toggleFab();
                                                     break;
                                             }
                                         }

                                         private void toggleFab() {
                                             if (isFabOpen) {
                                                 fab_main.setImageResource(R.drawable.ic_add);
                                                 fab_sub1.startAnimation(fab_close);
                                                 fab_sub2.startAnimation(fab_close);
                                                 fab_sub1.setClickable(false);
                                                 fab_sub2.setClickable(false);
                                                 isFabOpen = false;
                                             } else {
                                                 fab_main.setImageResource(R.drawable.ic_close);
                                                 fab_sub1.startAnimation(fab_open);
                                                 fab_sub2.startAnimation(fab_open);
                                                 fab_sub1.setClickable(true);
                                                 fab_sub2.setClickable(true);
                                                 isFabOpen = true;
                                             }
                                         }
                                     }
        );

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager (getContext ()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);



        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getAllPlaces (idd).observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                updateTaskList();
            }
        });

        adapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Place place) {

                dialog01.show();
                Button noBtn = dialog01.findViewById(R.id.noBtn);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent (getContext (), AddEditPlaceActivity.class);
                        intent.putExtra (AddEditPlaceActivity.EXTRA_ID, place.getId ());
                        intent.putExtra (AddEditPlaceActivity.EXTRA_TITLE, place.getTitle ());
                        intent.putExtra (AddEditPlaceActivity.EXTRA_DESCRIPTION, place.getDescription ());
                        intent.putExtra (AddEditPlaceActivity.EXTRA_LAT, place.getLat ());
                        intent.putExtra (AddEditPlaceActivity.EXTRA_LNG, place.getLng ());
                        intent.putExtra ("userid", userid);
                        intent.putExtra (AddEditPlaceActivity.EXTRA_IDD, place.getIdd ());
                        intent.putExtra ("idd", idd);
                        startActivityForResult (intent, EDIT_PLACE_REQUEST);
                        dialog01.dismiss();
                    }
                });
                dialog01.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        placeViewModel.delete (place);
                        dialog01.dismiss();
                    }
                });
            }
        });
        updateTaskList();
        return rootView;
    }

    private void updateTaskList() {
        Bundle bundle = getArguments();
        int idd = bundle.getInt ("idd");
        placeViewModel = new ViewModelProvider (this).get (PlaceViewModel.class);
        placeViewModel.getAllPlaces (idd).observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                adapter.submitList(places);
                if(places.size() > 0) {
                    emptyImage.setVisibility (View.GONE);
                    emptyView.setVisibility (View.GONE);
                    emptyView2.setVisibility (View.GONE); }
                else {
                    emptyView.setVisibility (View.VISIBLE);
                    emptyView2.setVisibility (View.VISIBLE);
                    emptyImage.setVisibility (View.VISIBLE); }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ADD_PLACE_REQUEST) {
            String title = data.getStringExtra(AddEditPlaceActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION);
            Double lat = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LAT));
            Double lng = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LNG));
            int idd= Integer.parseInt (data.getStringExtra(AddEditPlaceActivity.EXTRA_IDD));
            Place place = new Place(idd, title, description, lat, lng);
            placeViewModel.insert(place);

        } else if (requestCode == EDIT_PLACE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditPlaceActivity.EXTRA_ID, -1);

            if (id == -1){
                return;
            }

            String title = data.getStringExtra(AddEditPlaceActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION);
            Double lat = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LAT));
            Double lng = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LNG));
            int idd= Integer.parseInt (data.getStringExtra(AddEditPlaceActivity.EXTRA_IDD));
            Place place = new Place(idd, title, description, lat, lng);
            place.setId(id);
            placeViewModel.update(place);

        } else {
        }
        updateTaskList();
    }


}