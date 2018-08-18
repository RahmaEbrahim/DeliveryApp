package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.NotificationMessage;
import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

public class DriverNotification extends Activity {

    TextView timeBalance;
    private static final String TAG = "DriverNotification";
Button accept;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_notification);
        timeBalance = (TextView) findViewById(R.id.timeBalance);
        Session session = Session.getIntsance();
        Log.d(TAG, "session content is : " +session.toString());
        timeBalance.setText(session.getTimeBalance() +"Minute", TextView.BufferType.EDITABLE);
        accept = (Button)findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    accept.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    sendNotification();
                    Intent intent = new Intent(DriverNotification.this,DriverDeliveryState.class);
                    startActivity(intent);
                }
                catch(Exception ex){}
            }
        });


    }

    public void sendNotification () throws UnsupportedEncodingException {
        new DriverNotification.HttpRequestTask().execute();
    }


    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                NotificationMessage notificationMessage = new NotificationMessage();
                notificationMessage.setTopicName("admin");
                notificationMessage.setMessageBody("Order has been accepted");
                notificationMessage.setMessageType("DriverAcceptedOrder");
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
