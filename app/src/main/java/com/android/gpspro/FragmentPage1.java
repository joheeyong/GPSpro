package com.android.gpspro;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentPage1 extends Fragment {


    public static final int ADD_PLACE_REQUEST = 1;
    public static final int EDIT_PLACE_REQUEST = 2;

    private static final int RESULT_OK = -1;

    private PlaceViewModel placeViewModel;
    private PlaceViewModel model;
    private TextView emptyView;
    private ImageView emptyImage;

    private FloatingActionButton fab_main, fab_sub1, fab_sub2;

    private Animation fab_open, fab_close;

    private boolean isFabOpen = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup)inflater.inflate (R.layout.fragment_page_1, container, false);
        emptyImage= rootView.findViewById (R.id.empty_image);
        emptyView = rootView.findViewById (R.id.empty_view);

        Bundle bundle = getArguments();
        String userID = bundle.getString("userID");
        getActivity ().setTitle (userID+"님 환영합니다");
        fab_open = AnimationUtils.loadAnimation(getActivity (), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity (), R.anim.fab_close);

        FloatingActionButton fab_main = rootView.findViewById(R.id.fab_main);
        FloatingActionButton  fab_sub1 = rootView.findViewById(R.id.fab_sub1);
        FloatingActionButton  fab_sub2= rootView.findViewById(R.id.fab_sub2);

        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity (), AddEditPlaceActivity.class);
                intent.putExtra ("userID",userID);
                startActivityForResult(intent, ADD_PLACE_REQUEST);
            }
        });

        fab_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity (), AddClickPlaceActivity.class);
                intent.putExtra ("userID",userID);
                startActivityForResult(intent, ADD_PLACE_REQUEST);
            }
        });
        fab_main.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                switch (v.getId ()) {
                    case R.id.fab_main:
                        toggleFab ();
                        break;
                    case R.id.fab_sub1:
                        toggleFab ();
                        break;
                    case R.id.fab_sub2:
                        toggleFab ();
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

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager (getActivity ())); //!!!!!
        recyclerView.setHasFixedSize(true);

        final PlaceAdapter adapter = new PlaceAdapter();
        recyclerView.setAdapter(adapter);


        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getAllPlaces().observe(getViewLifecycleOwner(), new Observer<List<Place>> () {
            @Override
            public void onChanged(List<Place> places) {
                adapter.submitList(places);
            }
        });

        new ItemTouchHelper (new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                placeViewModel.delete(adapter.getPlaceAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity (), "장소 삭제", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Place place) {

                Intent intent = new Intent(getContext (), AddEditPlaceActivity.class);
                intent.putExtra(AddEditPlaceActivity.EXTRA_ID, place.getId());
                intent.putExtra(AddEditPlaceActivity.EXTRA_TITLE, place.getTitle());
                intent.putExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION, place.getDescription());
                intent.putExtra(AddEditPlaceActivity.EXTRA_LAT, place.getLat());
                intent.putExtra(AddEditPlaceActivity.EXTRA_LNG, place.getLng ());
                intent.putExtra(AddEditPlaceActivity.EXTRA_PRIORITY, place.getPriority());
                startActivityForResult(intent, EDIT_PLACE_REQUEST);
            }
        });
        updateTaskList();

        return rootView;

    }



    private void updateTaskList() {
        model = new ViewModelProvider (this).get (PlaceViewModel.class);
        model.getAllPlaces ().observe (getViewLifecycleOwner(), new Observer<List<Place>> () {
            @Override
            public void onChanged(List<Place> places) {
                if(places.size() > 0) {
                    emptyImage.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);}
                    else {
                        emptyView.setVisibility (View.VISIBLE);
                        emptyImage.setVisibility (View.VISIBLE);
                    }

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ADD_PLACE_REQUEST) {
            String title = data.getStringExtra(AddEditPlaceActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION);
            String userid = data.getStringExtra(AddEditPlaceActivity.EXTRA_USERID);
            Double lat = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LAT));
            Double lng = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LNG));
            int priority = data.getIntExtra(AddEditPlaceActivity.EXTRA_PRIORITY, 1);


            Place place = new Place(title, description, userid, lat, lng, priority);
            placeViewModel.insert(place);

            Toast.makeText(getActivity (), "플레이스 세이브", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_PLACE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditPlaceActivity.EXTRA_ID, -1);

            if (id == -1){
                Toast.makeText(getActivity (), "플레이스 업데이트 불가", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditPlaceActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION);
            String userid = data.getStringExtra(AddEditPlaceActivity.EXTRA_USERID);
            Double lat = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LAT));
            Double lng = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LNG));
            int priority = data.getIntExtra(AddEditPlaceActivity.EXTRA_PRIORITY, 1);

            Place place = new Place(title, description, userid, lat, lng, priority);
            place.setId(id);
            placeViewModel.update(place);


            Toast.makeText(getActivity (), "업데이트 성공", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(getActivity (), "저장하지않음", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public void onResume() {
        super.onResume ();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_places:
                placeViewModel.deleteAllPlaces();
                Toast.makeText(getActivity (), "모든 데이터 삭제", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}