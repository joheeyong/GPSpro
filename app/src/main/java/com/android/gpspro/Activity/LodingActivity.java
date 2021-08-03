package com.android.gpspro.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.gpspro.R;

public class LodingActivity extends Activity {
    Animation anim_FadeIn;
    Animation anim_ball;
    ConstraintLayout constraintLayout;
    ImageView lcklockImageView;
    ImageView oImageView;
    ImageView faceRecgnitionImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);
        constraintLayout=findViewById(R.id.constraintLayout);
        lcklockImageView=findViewById(R.id.lock_lck);
        oImageView=findViewById(R.id.lock_o);
        faceRecgnitionImageView=findViewById(R.id.faceReconition);

        anim_FadeIn= AnimationUtils.loadAnimation(this,R.anim.ani_fadein);
        anim_ball=AnimationUtils.loadAnimation(this,R.anim.ani_ball);

        lcklockImageView.startAnimation(anim_FadeIn);
        faceRecgnitionImageView.startAnimation(anim_FadeIn);
        oImageView.startAnimation(anim_ball);
        startLoading();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), TravelActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

}