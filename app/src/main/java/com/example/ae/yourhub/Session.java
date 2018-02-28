package com.example.ae.yourhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by A E on 2/28/2017.
 */

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = cntx.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
    }

    public void setcommunity(String community) {
        prefs.edit().putString("community", community).commit();
        //prefsCommit();
    }
    public String getcommunity() {
        String community = prefs.getString("community","");
        return community;
    }
    public void setcommid(String id) {
        prefs.edit().putString("comm_id", id).commit();
        //prefsCommit();
    }
    public String getcommid() {
        String usename = prefs.getString("comm_id","");
        return usename;
    }
    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
        //prefsCommit();
    }
    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }
    public String getid() {
        String usename = prefs.getString("user_id","");
        return usename;
    }
    public void setid(String id) {
        prefs.edit().putString("user_id", id).commit();
        //prefsCommit();
    }
    public void setemail(String e) {
        prefs.edit().putString("email", e).commit();
        //prefsCommit();
    }
    public String getemail(){
        String email = prefs.getString("email","");
        return email;
    }
    public void UserUnset()
    {
        prefs.edit().putString("user_id", "").commit();
        prefs.edit().putString("usename", "").commit();
        prefs.edit().putString("email", "").commit();
    }
}
