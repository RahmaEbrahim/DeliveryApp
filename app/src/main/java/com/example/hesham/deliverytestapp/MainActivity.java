package com.example.hesham.deliverytestapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;
import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;
    Button loginAdmin, loginDriver;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();

        Session session = Session.getIntsance();
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
               if(key.equalsIgnoreCase("balanceTime")){
                   session.setTimeBalance(getIntent().getExtras().getString(key));
               }
                if(key.equalsIgnoreCase("messageType")){
                    session.setMessage(getIntent().getExtras().getString(key));
                }
            }
        }
        if(session.getUserName() != null && session.getUserName() != ""){
            if(session.getAdmin()){
                try {
                    Intent intent = new Intent(MainActivity.this, AdminHistory.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.d("adminerror", ex.getMessage());
                }
            }else{
                try {
                    Intent intent = new Intent(MainActivity.this, DriverNotification.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.d("adminerror", ex.getMessage());
                }
            }
        }
        else{
        loginAdmin = (Button)findViewById(R.id.loginAdmin);
        loginDriver = (Button) findViewById(R.id.loginDriver);
        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivity.this, LoginAdmin.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.d("adminerror", ex.getMessage());
                }
            }
        });
        loginDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, LoginDriver.class);
                    startActivity(intent);
                }
                catch (Exception ex){
                    Log.d("drivererror", ex.getMessage());

                }
            }
        });}
    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
