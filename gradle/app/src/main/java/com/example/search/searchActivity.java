package com.example.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.ChatListRecycleView;
import com.example.data.GlobalData;
import com.example.login.LogFragment;
import com.example.my.R;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class searchActivity extends AppCompatActivity {
    ArrayList<String>strings;
    ArrayList<Integer>btnStatus;
    ArrayList<OrderBoxStructure>resultList;
    private FragmentManager supportFragmentManager;
    private FragmentTransaction fragmentTransaction;
    private  TabFragment tagFragment;
    private  boxFragment boxFragment;
    OrderListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        supportFragmentManager = getSupportFragmentManager();
        fragmentTransaction = supportFragmentManager.beginTransaction();
        tagFragment = new TabFragment();
        fragmentTransaction.add(R.id.resultLayout,tagFragment).commit();
        strings = new ArrayList<>();
        resultList = new ArrayList<>();
    }

    public void addCondition(View view)
    {
        Button btn = (Button) view;
        if((boolean)btn.getTag())
        {
            btn.setTag(false);
            btn.setTextColor(getResources().getColor(R.color.black));
        }
        else
        {
            btn.setTag(true);
            btn.setTextColor(getResources().getColor(R.color.google_blue));
        }
        if(!(boolean)btn.getTag()) {
            for (int i = 0; i < strings.size(); i++) {
                String str = strings.get(i);
                if (str.equals(btn.getText().toString())) {
                    strings.remove(i);
                    i = i - 1;
                }
            }
        }
        else {
            strings.add(btn.getText().toString());
        }
    }

    public void startSearch(View view)
    {
        resultList.clear();
        if(boxFragment==null) {
            //HttpThread httpThread = new HttpThread();
            //httpThread.start();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            EditText searchTitle = findViewById(R.id.searchTitle);
            String title = searchTitle.getText().toString();
            bundle.putString("data",title);
            boxFragment = new boxFragment();
            boxFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.resultLayout,boxFragment).commit();
            //fragmentTransaction.hide(tagFragment).add(R.id.resultLayout, boxFragment).commit();
        }
        else
        {
            //HttpThread httpThread = new HttpThread();
            //httpThread.start();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(tagFragment).show(boxFragment).commit();
        }
    }

    public void startInput(View view)
    {
        if(boxFragment!=null&&tagFragment!=null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(boxFragment).show(tagFragment).commit();
        }
    }

    public void searchImage()
    {
        try {
            for(int i=0;i<resultList.size();i++) {
                String url;
                EditText searchTitle = findViewById(R.id.searchTitle);
                String title = searchTitle.getText().toString();
                url = "http://106.54.118.148:8080/orderImage/getImages?oid=" + resultList.get(i).oid;
                URL mUrl = new URL(url);
                GlobalData globalData = (GlobalData) getApplication();
                String jID = "JSESSIONID=" + globalData.sessionID;
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
                mHttpURLConnection.setRequestProperty("Cookie", jID);
                mHttpURLConnection.setRequestMethod("GET");
                //mHttpURLConnection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        mHttpURLConnection.getInputStream()));
                final StringBuffer buffer = new StringBuffer();
                String str = null;
                while ((str = reader.readLine()) != null) {
                    JSONObject json = new JSONObject(str);
                    String listString = (String) json.get("msg");
                    String[] arr = listString.split("\\}");
                    for(int j=0;j<arr.length-1;j++)
                    {
                        String tempStr = arr[j].substring(1,arr[j].length());
                        tempStr+="}";
                        int k  = 0;
                        JSONObject js = new JSONObject(tempStr);
                        int iid = js.getInt("iid");
                        downloadImage(i,iid);
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void downloadImage(int resultPos,int iid)
    {
        try {

                String url;
                EditText searchTitle = findViewById(R.id.searchTitle);
                String title = searchTitle.getText().toString();
                url = "http://106.54.118.148:8080/orderImage/download/" + iid;
                URL mUrl = new URL(url);
                GlobalData globalData = (GlobalData) getApplication();
                String jID = "JSESSIONID=" + globalData.sessionID;
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
                mHttpURLConnection.setRequestProperty("Cookie", jID);
                mHttpURLConnection.setRequestMethod("GET");
                //mHttpURLConnection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        mHttpURLConnection.getInputStream()));
                final StringBuffer buffer = new StringBuffer();
                String str = null;
                String allString = "";
                while ((str = reader.readLine()) != null) {
                    allString = allString+str;
                }
                byte[]bytes = allString.getBytes();
                YuvImage yuvimage=new YuvImage(bytes, ImageFormat.NV21, 250,250, null);//20、20分别是图的宽度与高度
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                yuvimage.compressToJpeg(new Rect(0, 0,250, 250), 100, baos);//80--JPG图片的质量[0-100],100最高
                byte[] jdata = baos.toByteArray();

                Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
                resultList.get(resultPos).imageList.add(bmp);


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private class HttpThread extends Thread
    {
        @Override
        public void run() {
            try {
                String url;
                EditText searchTitle = findViewById(R.id.searchTitle);
                String title = searchTitle.getText().toString();
                url = "http://106.54.118.148:8080/order/getOrders/title?keyword="+title;
                URL mUrl = new URL(url);
                GlobalData globalData = (GlobalData) getApplication();
                String jID = "JSESSIONID="+globalData.sessionID;
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
                mHttpURLConnection.setRequestProperty("Cookie",jID);
                mHttpURLConnection.setRequestMethod("GET");
                //mHttpURLConnection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        mHttpURLConnection.getInputStream()));
                final StringBuffer buffer = new StringBuffer();
                String str = null;
                while ((str = reader.readLine()) != null) {
                    JSONObject json = new JSONObject(str);
                    String listString = (String) json.get("msg");
                    String[] arr = listString.split("\\}");
                    for(int i=0;i<arr.length-1;i++)
                    {
                        String tempStr = arr[i].substring(1,arr[i].length());
                        tempStr+="}";
                        JSONObject js = new JSONObject(tempStr);
                        OrderBoxStructure orderBoxStructure = new OrderBoxStructure();
                        orderBoxStructure.initWithJson(js);
                        resultList.add(orderBoxStructure);
                    }
                }
                reader.close();
                searchImage();
            }
            catch (Exception e)
            {

            }
        }
    }
}
