package com.android.gpspro.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.android.gpspro.ContactService;
import com.android.gpspro.MainActivity;
import com.android.gpspro.MainMapActivity;
import com.android.gpspro.PhotoViewActivity;
import com.android.gpspro.PlaceDatabase;
import com.android.gpspro.PlaceViewModel;
import com.android.gpspro.R;

public class FragmentPage1 extends Fragment {

    private PlaceViewModel placeViewModel;
    private TextView qwer, tv_mainpicture;
    MainActivity activity;
    private Button buttonViewv, buttonView,buttonViewvv,btn_mainback;
    private CardView cv_frag2,cv_frag3;
    private CardView cv_frag1;
    Dialog dialog01;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //현재 소속된 액티비티를 메인 액티비티로 한다.
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        String extitle = bundle.getString("extitle");
        String userid = bundle.getString("extitle");
        getActivity ().setTitle ("나의 "+extitle+" 여행");

        ViewGroup rootView = (ViewGroup)inflater.inflate (R.layout.fragment_page_1, container, false);
        buttonViewv = rootView.findViewById (R.id.buttonViewv);
        buttonView = rootView.findViewById (R.id.buttonView);
        buttonViewvv= rootView.findViewById (R.id.buttonViewvv);
        btn_mainback=rootView.findViewById (R.id.btn_mainback);
        cv_frag3=rootView.findViewById (R.id.cv_frag3);
        buttonView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String email = bundle.getString("extitle");
                Intent intent=new Intent (getActivity (), MainMapActivity.class);
                intent.putExtra("extitle", email);
                intent.putExtra("extitle", extitle);
                startActivity (intent);
            }
        });
        dialog01= new Dialog (getContext ());
        dialog01.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog01.setContentView (R.layout.dialog_accountinfo);
        buttonViewvv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showDialog01();

            }

            private void showDialog01() {
                dialog01.show(); // 다이얼로그 띄우기

                /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */

                // 위젯 연결 방식은 각자 취향대로~
                // '아래 아니오 버튼'처럼 일반적인 방법대로 연결하면 재사용에 용이하고,
                // '아래 네 버튼'처럼 바로 연결하면 일회성으로 사용하기 편함.
                // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.

                // 아니오 버튼
                Button noBtn = dialog01.findViewById(R.id.noBtn);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 원하는 기능 구현
                        dialog01.dismiss(); // 다이얼로그 닫기
                    }
                });
                // 네 버튼
                dialog01.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog01.dismiss();
                    }
                });

            }
        });
        buttonViewv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String email = bundle.getString("extitle");
                Intent intent=new Intent (getActivity (), PhotoViewActivity.class);
                intent.putExtra("extitle", email);
                startActivity (intent);
            }
        });
        cv_frag1 = rootView.findViewById (R.id.cv_frag1);
        cv_frag1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String email = bundle.getString("extitle");
                Intent intent=new Intent (getActivity (), MainMapActivity.class);
                intent.putExtra("extitle", email);
//                intent.putExtra("extitle", extitle);
                startActivity (intent);
            }
        });
        cv_frag2 = rootView.findViewById (R.id.cv_frag2);
        cv_frag2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String email = bundle.getString("extitle");
                Intent intent=new Intent (getActivity (), PhotoViewActivity.class);
                intent.putExtra("extitle", email);
                startActivity (intent);
            }
        });
        cv_frag3.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showDialog01();
            }

            private void showDialog01() {
                    dialog01.show(); // 다이얼로그 띄우기

                    /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */

                    // 위젯 연결 방식은 각자 취향대로~
                    // '아래 아니오 버튼'처럼 일반적인 방법대로 연결하면 재사용에 용이하고,
                    // '아래 네 버튼'처럼 바로 연결하면 일회성으로 사용하기 편함.
                    // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.

                    // 아니오 버튼
                    Button noBtn = dialog01.findViewById(R.id.noBtn);
                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // 원하는 기능 구현
                            dialog01.dismiss(); // 다이얼로그 닫기
                        }
                    });
                    // 네 버튼
                    dialog01.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog01.dismiss();
                        }
                    });

                
            }
        });
        btn_mainback.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        tv_mainpicture = rootView.findViewById (R.id.tv_mainpicture);
        qwer = rootView.findViewById (R.id.qwer);
        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getRowCount(userid).observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer>0){
                    qwer.setText(String.valueOf(integer)+"개의 여행지를 방문했습니다.");
                }
            }
        });


        return rootView;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}