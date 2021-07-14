package com.android.gpspro;

import android.content.Intent;
import android.net.Uri;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentPage1 extends Fragment {
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    int count =1;
    public static final int ADD_PLACE_REQUEST = 1;
    public static final int EDIT_PLACE_REQUEST = 2;

    private static final int RESULT_OK = -1;
    private PlaceRepository noteRepository;
    private PlaceViewModel placeViewModel;
    private PlaceViewModel model;
    private TextView emptyView;
    private ImageView emptyImage;
    private Button test;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;

    private Animation fab_open, fab_close;
    int i=0;
    private boolean isFabOpen = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup)inflater .inflate (R.layout.fragment_page_1, container, false);
        emptyImage= rootView.findViewById (R.id.empty_image);
        emptyView = rootView.findViewById (R.id.empty_view);
//        test = rootView.findViewById (R.id.test);
        Bundle bundle = getArguments();
        String userID = bundle.getString("userID");
        getActivity ().setTitle (userID+"님 환영합니다");
        fab_open = AnimationUtils.loadAnimation(getActivity (), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity (), R.anim.fab_close);
        noteRepository = new PlaceRepository (getContext ());
        FloatingActionButton fab_main = rootView.findViewById(R.id.fab_main);
        FloatingActionButton  fab_sub1 = rootView.findViewById(R.id.fab_sub1);
        FloatingActionButton  fab_sub2= rootView.findViewById(R.id.fab_sub2);

//        test.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//                String title = "ITLE);";
//                String description = "data.getStringExtra(TestActivity.EXTRA_DESCRIPTION)";
//                String userid = "data.getStringExtra(TestActivity.EXTRA_USERID)";
//                Double lat = 32.33;
//                Double lng =126.33;
//                int count = 6;
//                int priority = 5;
//
//                Place place = new Place(title, description, userid, lat, lng, count, priority);
//                place.setId(1);
//                placeViewModel.update (place);
//
//
//                Toast.makeText(getActivity (), "업데이트 성공", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity (), AddEditPlaceActivity.class);
                intent.putExtra ("userID",userID);
                startActivityForResult(intent, ADD_PLACE_REQUEST);
            }
        });
//        ImageView imageview = rootView.findViewById(R.id.imageView);
//        imageview.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, GET_GALLERY_IMAGE);
//            }
//        });

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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL));
//        recyclerView.setLayoutManager(new LinearLayoutManager (getActivity ())); //!!!!!
        recyclerView.setHasFixedSize(true);

        final PlaceAdapter adapter = new PlaceAdapter();
        recyclerView.setAdapter(adapter);

int id=2;
        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        noteRepository.getTasks(id).observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                adapter.submitList(places);
            }
        });

//        new ItemTouchHelper (new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                placeViewModel.delete(adapter.getPlaceAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(getActivity (), "장소 삭제", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(recyclerView);

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
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);

        }



        if (resultCode == RESULT_OK && requestCode == ADD_PLACE_REQUEST) {
            String title = data.getStringExtra(AddEditPlaceActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION);
            String userid = data.getStringExtra(AddEditPlaceActivity.EXTRA_USERID);
            Double lat = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LAT));
            Double lng = Double.valueOf (data.getStringExtra(AddEditPlaceActivity.EXTRA_LNG));
            int count = 1;
            int priority = data.getIntExtra(AddEditPlaceActivity.EXTRA_PRIORITY, 1);


            Place place = new Place(title, description, userid, lat, lng, count, priority);
            placeViewModel.insert(place);

            Toast.makeText(getActivity (), "플레이스 세이브", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_PLACE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditPlaceActivity.EXTRA_ID, -1);

            if (id == -1){
                Toast.makeText(getActivity (), "플레이스 업데이트 불가", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(TestActivity.EXTRA_TITLE);
            String description = data.getStringExtra(TestActivity.EXTRA_DESCRIPTION);
            String userid = data.getStringExtra(TestActivity.EXTRA_USERID);
            Double lat = Double.valueOf (data.getStringExtra(TestActivity.EXTRA_LAT));
            Double lng = Double.valueOf (data.getStringExtra(TestActivity.EXTRA_LNG));
            int count = data.getIntExtra(TestActivity.EXTRA_COUNT, 1);
            int priority = data.getIntExtra(TestActivity.EXTRA_PRIORITY, 1);

            Place place = new Place(title, description, userid, lat, lng, count, priority);
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
//        if (i!=0){


//            Bundle extra = this.getArguments();
//
//                extra = getArguments ();
//                String title = extra.getString ("title");
//                String description = extra.getString ("name");
//                String userid = extra.getString ("time");
//
//            Double lat=124.33;
//            Double lng=34.22;
//            int count = 2;
//            int priority=2;
////            String description = data.getStringExtra(Test3Activity.EXTRA_DESCRIPTION);
////            String userid = data.getStringExtra(Test3Activity.EXTRA_USERID);
////            Double lat = Double.valueOf (data.getStringExtra(Test3Activity.EXTRA_LAT));
////            Double lng = Double.valueOf (data.getStringExtra(Test3Activity.EXTRA_LNG));
////            int count = data.getIntExtra(Test3Activity.EXTRA_COUNT, 1);
////            int priority = data.getIntExtra(Test3Activity.EXTRA_PRIORITY, 1);
////        String title = "된거같아!";
////        String description = "data.getStringExtra(AddEditPlaceActivity.EXTRA_DESCRIPTION)";
////        String userid = "data.getStringExtra(AddEditPlaceActivity.EXTRA_USERID)";
////        Double lat = 12.33;
////        Double lng = 38.22;
////        int count = 5;
////        int priority = 6;
//
//
//        Place place = new Place(title, description, userid, lat, lng, count, priority);
//        place.setId(1);
//        placeViewModel.update (place);
//        } else{
//            i++;
//        }
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