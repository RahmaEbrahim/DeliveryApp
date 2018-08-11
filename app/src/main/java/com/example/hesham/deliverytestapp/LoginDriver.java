package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                    GetText();

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
                driverUserNameS = driverUserName.getText().toString();
                driverPasswordS = driverPassword.getText().toString();
                final String url = "http://192.168.21.195:8080/delivery/driver/" + driverUserNameS +"/" + driverPasswordS;
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
                    return false;
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }
    }
}
