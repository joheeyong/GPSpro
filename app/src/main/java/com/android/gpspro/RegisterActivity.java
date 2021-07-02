package com.android.gpspro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {

    private EditText et_id, et_pass, et_name, et_age;
    private Button btn_register;
    private Button btn_login, btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_register);

        et_age=findViewById (R.id.et_age);
        et_id=findViewById (R.id.et_id);
        et_pass=findViewById (R.id.et_pass);
        et_name=findViewById (R.id.et_name);

        btn_back=findViewById (R.id.btn_back);
        btn_back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (RegisterActivity.this, GoogleloginActivity.class);
                startActivity (intent);
            }
        });

        btn_login=findViewById (R.id.btn_login);
        btn_login.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (RegisterActivity.this, LoginActivity.class);
                startActivity (intent);
            }
        });

        btn_register=findViewById (R.id.btn_register);
        btn_register.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String userID = et_id.getText ().toString ();
                String userPass = et_pass.getText ().toString ();
                String userName = et_name.getText ().toString ();
                int userAge = Integer.parseInt (et_age.getText().toString ());

                Response.Listener<String> responseListener= new Response.Listener<String> () {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean ("success");
                            if(success) {
                                Toast.makeText (getApplicationContext (), "회원에 등록했습니다.", Toast.LENGTH_SHORT).show ();
                                Intent intent = new Intent (RegisterActivity.this, GoogleloginActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText (getApplicationContext (), "회원 등록에 실패했습니다.", Toast.LENGTH_SHORT).show ();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest (userID,userPass,userName,userAge,responseListener);
                RequestQueue queue = Volley.newRequestQueue (RegisterActivity.this);
                queue.add (registerRequest);

            }
        });

    }
}