package com.example.data;

import android.app.Application;

public class GlobalData extends Application {
    private int sessionID;
    public String headImagePath;
    public int getSessionID()
    {
        return sessionID;
    }
    public void setSessionID(int ID)
    {
        sessionID = ID;
    }
    public  String getHeadImagePath()
    {
        return headImagePath;
    }
    @Override
    public void onCreate(){
        sessionID = -1;
        headImagePath = getExternalCacheDir().getPath()+"/head";
        super.onCreate();
    }
}
