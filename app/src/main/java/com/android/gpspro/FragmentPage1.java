package com.android.gpspro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentPage1 extends Fragment {


    public static final int ADD_PLACE_REQUEST = 1;
    public static final int EDIT_PLACE_REQUEST = 2;

    private static final int RESULT_OK = -1;

    private PlaceViewModel placeViewModel;

    private Button next;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup)inflater.inflate (R.layout.fragment_page_1, container, false);






        FloatingActionButton buttonAddPlace = rootView.findViewById(R.id.button_add_place);
        buttonAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity (), AddEditPlaceActivity.class);
                startActivityForResult(intent, ADD_PLACE_REQUEST);
            }
        });


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


        return rootView;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PLACE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditPlaceActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION);
            Double lat = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LAT));
            Double lng = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LNG));
            int priority = data.getIntExtra(AddEditPlaceActivity.EXTRA_PRIORITY, 1);

            Place place = new Place(title, description, lat, lng, priority);
            placeViewModel.insert(place);

            Toast.makeText(getActivity (), "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_PLACE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditPlaceActivity.EXTRA_ID, -1);

            if (id == -1){
                Toast.makeText(getActivity (), "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditPlaceActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION);
            Double lat = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LAT));
            Double lng = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LNG));
            int priority = data.getIntExtra(AddEditPlaceActivity.EXTRA_PRIORITY, 1);

            Place place = new Place(title, description, lat, lng, priority);
            place.setId(id);
            placeViewModel.update(place);


            Toast.makeText(getActivity (), "Note Updated", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(getActivity (), "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_places:
                placeViewModel.deleteAllPlaces();
                Toast.makeText(getActivity (), "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}