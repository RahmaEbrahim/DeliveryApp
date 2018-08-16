package com.example.hesham.deliverytestapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model.Session;

import java.util.ArrayList;
import java.util.Date;

public class AdminHistory extends AppCompatActivity {

    ListView mainListView;
    private static ArrayAdapter<String> listAdapter ;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_history);
        Session session = Session.getIntsance();
        mainListView = (ListView) findViewById( R.id.mainListView );
        if (listAdapter == null){
            initList();
        }
        if (session.getMessage()!=null && session.getMessage()!= ""){
        listAdapter.add(session.getMessage() + ", Time Balance: " + session.getTimeBalance() +", "+ new Date());
        mainListView.setAdapter( listAdapter );
        }
    }

    private void initList(){
        ArrayList<String> planetList = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, R.layout.admin_history_row, planetList);
    }
    }
