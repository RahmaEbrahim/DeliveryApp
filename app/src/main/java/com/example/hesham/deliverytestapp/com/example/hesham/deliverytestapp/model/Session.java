package com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

   private String userName,password,timeBalance;
   private boolean admin;
   private static Session session = null;


    private Session() {
    }

    public static Session getIntsance(){
        if(session == null){
            session = new Session();
        }
        return session;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void logout() {
        session = new Session();
    }

    public String getTimeBalance() {
        return timeBalance;
    }

    public void setTimeBalance(String timeBalance) {
        this.timeBalance = timeBalance;
    }
}

