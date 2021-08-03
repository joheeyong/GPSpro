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

import com.android.gpspro.Activity.MainActivity;
import com.android.gpspro.Activity.MainMapActivity;
import com.android.gpspro.DB.ViewModel.AccountViewModel;
import com.android.gpspro.Activity.PhotoViewActivity;
import com.android.gpspro.DB.ViewModel.PlaceViewModel;
import com.android.gpspro.R;

import java.text.DecimalFormat;

public class FragmentPage1 extends Fragment {

    private PlaceViewModel placeViewModel;
    private AccountViewModel accountViewModel;
    private TextView tv_countplace, tv_mainpicture, tv_mainmoney;
    MainActivity activity;
    private Button btn_mainpicture, btn_countplace,btn_mainback;
    private CardView cv_frag1,cv_frag2,cv_frag3;
    String favSumm;
    Dialog dialog01;
    String resilt_int;
    String favSum = "0";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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

        String userid = bundle.getString("userid");
        int idd = bundle.getInt ("idd");
        getActivity ().setTitle ("나의 "+userid+" 여행");

        ViewGroup rootView = (ViewGroup)inflater.inflate (R.layout.fragment_page_1, container, false);

        dialog01= new Dialog (getContext ());
        dialog01.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog01.setContentView (R.layout.dialog_accountinfo);

        btn_countplace = rootView.findViewById (R.id.btn_countplace);
        btn_mainpicture = rootView.findViewById (R.id.btn_mainpicture);
        btn_mainback=rootView.findViewById (R.id.btn_mainback);

        cv_frag1 = rootView.findViewById (R.id.cv_frag1);
        cv_frag2 = rootView.findViewById (R.id.cv_frag2);
        cv_frag3=rootView.findViewById (R.id.cv_frag3);
        tv_countplace = rootView.findViewById (R.id.tv_countplace);
        tv_mainmoney = rootView.findViewById (R.id.tv_mainmoney);


        cv_frag1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String userid = bundle.getString("userid");
                int idd = bundle.getInt ("idd");
                Intent intent=new Intent (getActivity (), MainMapActivity.class);
                intent.putExtra("userid", userid);
                intent.putExtra ("idd",idd);
                startActivity (intent);
            }
        });
        btn_countplace.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String userid = bundle.getString("userid");
                int idd = bundle.getInt ("idd");
                Intent intent=new Intent (getActivity (), MainMapActivity.class);
                intent.putExtra("userid", userid);
                intent.putExtra ("idd",idd);
                startActivity (intent);
            }
        });
        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getRowCount(idd).observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer>0){
                    tv_countplace.setText(String.valueOf(integer)+"개의 여행지를 방문했습니다.");
                }
            }
        });

        cv_frag2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                int idd = bundle.getInt ("idd");
                Intent intent=new Intent (getActivity (), PhotoViewActivity.class);
                intent.putExtra("idd", idd);
                startActivity (intent);
            }
        });
        btn_mainpicture.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                int idd = bundle.getInt ("idd");
                Intent intent=new Intent (getActivity (), PhotoViewActivity.class);
                intent.putExtra("idd", idd);
                startActivity (intent);
            }
        });

        cv_frag3.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showDialog01();
            }
            private void showDialog01() {
                dialog01.show();
                Button noBtn = dialog01.findViewById(R.id.btn_close);
                TextView tv_allcount =dialog01.findViewById (R.id.tv_allcount);
                TextView tv_1count = dialog01.findViewById (R.id.tv_1count);
                TextView tv_2count = dialog01.findViewById (R.id.tv_2count);
                TextView tv_3count = dialog01.findViewById (R.id.tv_3count);
                TextView tv_4count = dialog01.findViewById (R.id.tv_4count);
                TextView tv_5count = dialog01.findViewById (R.id.tv_5count);
                TextView tv_6count = dialog01.findViewById (R.id.tv_6count);

                accountViewModel = ViewModelProviders.of(getActivity ()).get(AccountViewModel.class);;
                accountViewModel.getmTotal(idd).observe(getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSum = "0";
                            tv_allcount.setText("아직 사용한 금액이 없습니다.");
                        } else {

                            long value = Long.parseLong(string);
                            DecimalFormat format = new DecimalFormat("###,###");
                            format.format(value);
                            String resilt_int =format.format (value);
                            favSum = string;
                            tv_allcount.setText("이번 여행에서 총 "+resilt_int+" 원을 사용했습니다.");
                        }
                    }
                });

                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "교통").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_1count.setText ("교통 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_1count.setText ("교통 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "숙박").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_2count.setText ("숙박 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_2count.setText ("숙박 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "식비").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_3count.setText ("식비 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_3count.setText ("식비 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "쇼핑").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_4count.setText ("쇼핑 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_4count.setText ("쇼핑 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "관광").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_5count.setText ("관광 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_5count.setText ("관광 : " + resilt_int + " 원");
                        }
                    }
                });
                accountViewModel = ViewModelProviders.of (getActivity ()).get (AccountViewModel.class);
                accountViewModel.getmTotall (idd, "기타").observe (getActivity (), new Observer<String> () {
                    @Override
                    public void onChanged(String string) {
                        if (string == null) {
                            favSumm = "0";
                            tv_6count.setText ("기타 : 0 원");
                        } else {
                            long value = Long.parseLong (string);
                            DecimalFormat format = new DecimalFormat ("###,###");
                            format.format (value);
                            resilt_int = format.format (value);
                            tv_6count.setText ("기타 : " + resilt_int + " 원");
                        }
                    }
                });



                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog01.dismiss();
                    }
                });
            }
        });

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        accountViewModel.getmTotal(idd).observe(getViewLifecycleOwner(), new Observer<String> () {
            @Override
            public void onChanged(String string) {
                if (string == null) {
                    favSum = "0";
                    tv_mainmoney.setText("여행 경비을 등록해주세요.");
                } else {
                    long value = Long.parseLong(string);
                    DecimalFormat format = new DecimalFormat("###,###");
                    format.format(value);
                    String resilt_int =format.format (value);
                    favSum = string;
                    tv_mainmoney.setText(resilt_int+" 원을 사용했습니다.");
                }
            }
        });

        btn_mainback.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootView;
    }

}