package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.NotificationMessage;
import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

public class DriverDeliveryState extends Activity {
    Button recieve,delivery;
    String msgType,msgBody;
    TextView orderDetails;
    NotificationMessage notificationMessage = new NotificationMessage();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_delivry_state);
        Session session = Session.getIntsance();
        recieve = (Button)findViewById(R.id.recieve);
        delivery = (Button) findViewById(R.id.delivery);
        orderDetails = (TextView)findViewById(R.id.orderDetails2) ;

        orderDetails.setText( "TIME BALANCE IS " + session.getTimeBalance() + "MINUTE", TextView.BufferType.EDITABLE);
        recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    msgBody ="Order has been recieved";
                    msgType = "DriverRecievedOrder";
                    sendNotification();
                    recieve.setEnabled(false);
                }
                catch(Exception ex){}
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    msgType = "DriverDeliveredOrder";
                    msgBody = "Order has beed delivered";
                    sendNotification();
                }
                catch(Exception ex){}
            }
        });
        }

        public void sendNotification () throws UnsupportedEncodingException {
            new DriverDeliveryState.HttpRequestTask().execute();
        }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                notificationMessage.setTopicName("admin");
                notificationMessage.setMessageBody(msgBody);
                notificationMessage.setMessageType(msgType);
                final String url = "https://mysterious-forest-44790.herokuapp.com/delivery/message";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                HttpEntity<NotificationMessage> request = new HttpEntity<>(notificationMessage);
                ResponseEntity<String> response = restTemplate
                        .exchange(url, HttpMethod.POST, request, String.class);

                if (response.getStatusCode() == HttpStatus.CREATED) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return false;
        }
    }
}
