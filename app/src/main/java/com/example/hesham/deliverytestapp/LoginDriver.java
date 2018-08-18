package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

public class LoginDriver extends Activity {
    EditText driverUserName, driverPassword;
    String driverUserNameS, driverPasswordS;
    Button driverLogin;

    private static final String TAG = "LoginDriver";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_driver);
        driverUserName = (EditText) findViewById(R.id.driverUserName);
        driverPassword = (EditText) findViewById(R.id.driverPassword);
        driverLogin = (Button) findViewById(R.id.driverLogin);
        driverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (driverUserName.getText().toString().matches("")){
                        driverUserName.setError("You did not enter your UserName"); }
                    if (driverPassword.getText().toString().matches("")){
                        driverPassword.setError("You did not enter your UserName");
                    }
                    //driverLogin.setVisibility(Button.GONE);
                    //ProgressDialog.show(LoginDriver.this, "Loading", "Wait while Logging in...");
                    else{
                        driverLogin.setBackgroundColor(Color.parseColor("#DCDCDC"));
                        GetText();}
                } catch (Exception ex) {
                }
            }
        });
    }
    public void GetText() throws UnsupportedEncodingException {
        new LoginDriver.HttpRequestTask().execute();
    }
    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                Session session  = Session.getIntsance();
                driverUserNameS = driverUserName.getText().toString();
                driverPasswordS = driverPassword.getText().toString();
                session.setUserName(driverUserNameS);
                session.setPassword(driverPasswordS);
                session.setAdmin(false);
                final String url = "https://mysterious-forest-44790.herokuapp.com/delivery/driver/" + driverUserNameS +"/" + driverPasswordS;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);
                if (response.getStatusCode() == HttpStatus.OK)
                {

                    Intent intent = new Intent(LoginDriver.this, DriverHome.class);
                    startActivity(intent);
                    return true;
                }
                else
                {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //dialog.dismiss();
                            Toast.makeText(LoginDriver.this, "please check your UserName or Password", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                    return false;                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }
    }
}
