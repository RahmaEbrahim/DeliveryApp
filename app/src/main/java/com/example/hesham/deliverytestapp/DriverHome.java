package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;
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
                FirebaseMessaging.getInstance().subscribeToTopic("news")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = getString(R.string.msg_subscribed);

                                if (!task.isSuccessful()) {
                                    msg = getString(R.string.msg_subscribe_failed);
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(DriverHome.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

            } catch (Exception ex) {
            }
        }
        driverHostry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    Intent intent = new Intent(DriverHome.this,DriverNotification.class);
                    startActivity(intent);

                }

                catch (Exception ex){}
            }
        });
        driverLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session session = Session.getIntsance();
                session.logout();
                Intent intent = new Intent(DriverHome.this,MainActivity.class);
                startActivity(intent);
            }
        });
        driverBusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Session session = Session.getIntsance();
               // session.setAdmin(true);
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
            }
        });

    }
}
