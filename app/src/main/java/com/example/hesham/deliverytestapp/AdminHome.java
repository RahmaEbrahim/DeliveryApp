package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.NotificationMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class AdminHome extends Activity {

    TextView balanceTime, customerPhone;
    Button callDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(AdminHome.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        balanceTime = (EditText) findViewById(R.id.balanceTime);
        customerPhone = (EditText) findViewById(R.id.customerPhone);
        callDriver = (Button) findViewById(R.id.callDriver);

        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GetText();
                  //  if(GetText()
                    

                } catch (Exception ex) {
                }
            }
        });
    }


        public void GetText () throws UnsupportedEncodingException {
            new AdminHome.HttpRequestTask().execute();
        }


        private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {

                    NotificationMessage notificationMessage = new NotificationMessage();
                    notificationMessage.setCustomerNumber(customerPhone.getText().toString());
                    notificationMessage.setTimeBalance(balanceTime.getText().toString());
                    final String url = "https://mysterious-forest-44790.herokuapp.com/delivery/message";
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                    HttpEntity<NotificationMessage> request = new HttpEntity<>(notificationMessage);
                    ResponseEntity<String> response = restTemplate
                            .exchange(url, HttpMethod.POST, request, String.class);

                    if (response.getStatusCode() == HttpStatus.CREATED) {
                        // Intent intent = new Intent(AdminHome.this, DriverHome.class);
                        //  startActivity(intent)
                        return true;
                    } else {
                        return false;
                    }

                }catch (Exception e){
                    Log.e("MainActivity", e.getMessage(), e);
                }
                return false;
            }
        }

    }