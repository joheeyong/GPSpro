package com.android.gpspro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.gpspro.Fragment.FragmentPage1;
import com.android.gpspro.Fragment.FragmentPage2;
import com.android.gpspro.Fragment.FragmentPage3;
import com.android.gpspro.Fragment.FragmentPage4;
import com.android.gpspro.Fragment.FragmentPage5;
import com.android.gpspro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int idd = intent.getIntExtra ("idd",100);
        setTitle ("나의 "+idd+" 여행");

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());

                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_1);
    }

    public void BottomNavigate(int id) {

        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        Intent intent = getIntent();
        String userid = intent.getStringExtra("extitle");
        int idd = intent.getIntExtra ("idd",100);
        if (fragment == null) {
            if (id == R.id.navigation_1) {
                fragment = new FragmentPage1 ();
                Bundle bundle = new Bundle(1);
                bundle.putString("userid",userid);
                bundle.putInt("idd", idd);
                fragment.setArguments (bundle);

            } else if (id == R.id.navigation_2){
                fragment = new FragmentPage2 ();
                Bundle bundle = new Bundle(1);
                bundle.putString("userid",userid);
                bundle.putInt("idd", idd);
                fragment.setArguments (bundle);

            }else if (id == R.id.navigation_3){
                fragment=new FragmentPage3 ();
                Bundle bundle = new Bundle(1);
                bundle.putString("userid",userid);
                bundle.putInt("idd", idd);
                fragment.setArguments (bundle);

            } else if (id == R.id.navigation_4){
                fragment=new FragmentPage4 ();
                Bundle bundle = new Bundle(1);
                bundle.putString("userid",userid);
                bundle.putInt("idd", idd);
                fragment.setArguments (bundle);

            } else if(id == R.id.navigation_5){
                fragment=new FragmentPage5 ();
                Bundle bundle = new Bundle(1);
                bundle.putString("userid",userid);
                bundle.putInt("idd", idd);
                fragment.setArguments (bundle);
            }


            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();


    }
}