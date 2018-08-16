package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.NotificationMessage;
import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;
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

    TextView balanceTime, customerNumber;
    Button callDriver , logout,history;
   // ProgressDialog dialog = new ProgressDialog(AdminHome.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

      FirebaseMessaging.getInstance().subscribeToTopic("admin");
                /*.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                     //   Session session = new Session(getApplicationContext());
                        String msg = getString(R.string.msg_subscribed);
                       // String msg = session.getUserName();
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(AdminHome.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });*/
        balanceTime = (EditText) findViewById(R.id.balanceTime);
        customerNumber = (EditText) findViewById(R.id.customerPhone);
        callDriver = (Button) findViewById(R.id.callDriver);
        logout = (Button)  findViewById(R.id.logout);
        history = (Button) findViewById(R.id.hostry);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this,AdminHistory.class);
                startActivity(intent);
            }
        });
        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (balanceTime.getText().toString().matches("")){
                        balanceTime.setError("You did not enter your Balance Time"); }

                    if (customerNumber.getText().toString().matches("")){
                        customerNumber.setError("You did not enter your Custome Number"); }

                    else{
                        // callDriver.setVisibility(Button.GONE);
//                    dialog.setCancelable(true);
//                    dialog.setTitle("Wait");
//                    dialog.setMessage("Calling Driver.");
//                    dialog.show();
                        Session session =  Session.getIntsance();
                        session.setTimeBalance(balanceTime.getText().toString());
                        GetText();}
                }
                 catch (Exception ex) {
                }
        }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    Session session = Session.getIntsance();
                    session.logout();
                    Intent intent = new Intent(AdminHome.this,MainActivity.class);
                    startActivity(intent);
                }
                catch (Exception ex){}
            }
        });
    }


        public void GetText () throws UnsupportedEncodingException {
            new AdminHome.HttpRequestTask().execute();
            Toast.makeText(AdminHome.this, "Order has been sent",
                    Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());        }


        private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    NotificationMessage notificationMessage = new NotificationMessage();
                    notificationMessage.setCustomerNumber(customerNumber.getText().toString());
                    notificationMessage.setTimeBalance(balanceTime.getText().toString());
                    notificationMessage.setTopicName("news");
                    notificationMessage.setMessageBody("New order has been submitted");
                    notificationMessage.setMessageType("AdminOrderToDriver");
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

                }catch (Exception e){
                    Log.e("MainActivity", e.getMessage(), e);
                }
                return false;
            }
        }

    }
