package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

public class DriverHome extends Activity {
    Button driverBusy, driverHostry, driverLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_home);
        driverBusy = (Button)findViewById(R.id.driverBusy);
        driverHostry = (Button)findViewById(R.id.driverHostry);
        driverLogout = (Button) findViewById(R.id.driverLogout);

        Session session = Session.getIntsance();
        if (session.getAdmin()==false) {
            try {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
                FirebaseMessaging.getInstance().subscribeToTopic("news");
            } catch (Exception ex) {
            }
        }
        driverLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverLogout.setBackgroundColor(Color.parseColor("#DCDCDC"));
                Session session = Session.getIntsance();
                session.logout();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                Intent intent = new Intent(DriverHome.this,MainActivity.class);
                startActivity(intent);
            }
        });
        driverBusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverBusy.setBackgroundColor(Color.parseColor("#DCDCDC"));
                if (driverBusy.getText() =="Busy"){
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                driverBusy.setText("Available");}
                if (driverBusy.getText() =="Available"){
                    FirebaseMessaging.getInstance().subscribeToTopic("news");
                    driverBusy.setText("Available");
                }
            }
        });

        driverHostry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverHostry.setBackgroundColor(Color.parseColor("#DCDCDC"));
                Intent intent = new Intent(DriverHome.this,DriverHistory.class);
                startActivity(intent);
            }
        });
    }
}
