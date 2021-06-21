package com.android.gpspro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentPage1 extends Fragment {
    ViewFlipper v_fllipper;
    private ImageButton btn_main;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int images[] = {
                R.drawable.bhcevent,
                R.drawable.pizzahut,
                R.drawable.onegrand
        };


        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup)inflater.inflate (R.layout.fragment_page_1, container, false);
        btn_main = (ImageButton) rootView.findViewById (R.id.one);

        btn_main.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent (getActivity (),MygpsActivity.class);
                startActivity (intent);
            }
        });

        v_fllipper = rootView.findViewById(R.id.image_slide);
        for(int image : images) {
            fllipperImages(image);
        }




        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(getContext ());
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(3000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(getContext (),android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(getContext (),android.R.anim.slide_out_right);
    }

}