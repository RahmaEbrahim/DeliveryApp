package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

public class LoginAdmin extends Activity {

    private static final String TAG = "LoginAdmin";
    EditText adminUserName, adminPassword;
    String adminUserNameS, adminPasswordS;
    Button adminLogin;
    private ProgressDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_admin);
        adminUserName = (EditText) findViewById(R.id.adminUserName);
        adminPassword = (EditText) findViewById(R.id.adminPassword);
        adminLogin = (Button) findViewById(R.id.adminLogin);

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (adminUserName.getText().toString().matches("")) {
                        adminUserName.setError("You did not enter your UserName");
                    }
                    if (adminPassword.getText().toString().matches("")) {
                        adminPassword.setError("You did not enter your Password");
                    }
                    else
                   // adminLogin.setVisibility(Button.GONE);
                    // new MyProgressDialog().execute();
                    GetText();

                } catch (Exception e) {
                    Log.e("MainActivity", e.getMessage(), e);
                }
            }
        });
    }


    public void GetText() throws UnsupportedEncodingException {
      //   dialog.show(LoginAdmin.this, "Loading", "Wait while Logging in...");
        new HttpRequestTask().execute();
          ///dialog.dismiss();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                adminUserNameS = adminUserName.getText().toString();
                adminPasswordS = adminPassword.getText().toString();
                Session session = Session.getIntsance();
                session.setUserName(adminUserNameS);
                session.setPassword(adminPasswordS);
                session.setAdmin(Boolean.TRUE);
                Log.d("session", session.getUserName());
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

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                       //     dialog.dismiss();
                            Toast.makeText(LoginAdmin.this, "please check your UserName or Password", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                    return false;
                }
            } catch (Exception e) {

                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }
    }

}
