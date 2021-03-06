package com.example.data;

import android.app.Application;
import android.graphics.Bitmap;

public class GlobalData extends Application {
    public String sessionID;
    private int ID;
    public Bitmap bitmap;
    public String headImagePath;
    public int getSessionID()
    {
        return ID;
    }
    public void setID(int ID)
    {
        this.ID = ID;
    }
    public  String getHeadImagePath()
    {
        return headImagePath;
    }
    @Override
    public void onCreate(){
        ID = -1;
        headImagePath = getExternalCacheDir().getPath()+"/head";
        super.onCreate();
    }
}
