package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.ResponseModel;
import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.UserModel;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

public class LoginAdmin extends AppCompatActivity {

    EditText adminUserName, adminPassword;
    String adminUserNameS, adminPasswordS;
    Button adminLogin;
    private static final String TAG = "LoginAdmin";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_admin);
        //   String adminUserName, adminPassword;
        adminUserName = (EditText) findViewById(R.id.adminUserName);
        adminPassword = (EditText) findViewById(R.id.adminPassword);
        adminLogin = (Button) findViewById(R.id.adminLogin);


        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GetText();
                    FirebaseMessaging.getInstance().subscribeToTopic("admin")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.msg_subscribed);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.msg_subscribe_failed);
                                    }
                                       Log.d(TAG, msg);
                                    Toast.makeText(LoginAdmin.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (Exception ex) {
                }
            }
        });

    }

    public void GetText() throws UnsupportedEncodingException {
        new HttpRequestTask().execute();


    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                adminUserNameS = adminUserName.getText().toString();
                adminPasswordS = adminPassword.getText().toString();
                final String url = "https://mysterious-forest-44790.herokuapp.com/delivery/admin/" + adminUserNameS + "/" + adminPasswordS;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    Intent intent = new Intent(LoginAdmin.this, AdminHome.class);
                    startActivity(intent);
                    return true;

                } else {
                    return false;
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }
    }
}
