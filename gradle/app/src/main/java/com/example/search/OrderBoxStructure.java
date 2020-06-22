package com.example.search;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.example.my.OrderBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderBoxStructure {
    String title;
    String content;
    String endPos;
    int issuerId;
    int oid;
    int price;
    String startPos;
    String status;
    long updateTime;
    ArrayList<Bitmap>imageList;
    OrderBoxStructure(String title)
    {
        this.title = title;
    }
    OrderBoxStructure()
    {
        imageList = new ArrayList<>();
    }
    public void initWithJson(JSONObject json){
        try {
            title = json.getString("title");
            content = json.getString("content");
            endPos = json.getString("endPos");
            issuerId = json.getInt("issuerId");
            oid = json.getInt("oid");
            price = json.getInt("price");
            startPos = json.getString("startPos");
            status = json.getString("status");
            updateTime = json.getLong("updateTime");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
