package com.example.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.data.GlobalData;
import com.example.my.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class boxFragment extends Fragment {
    OrderListAdapter adapter;
    ArrayList<OrderBoxStructure> resultList;
    EditText seachTitle;
    String searchKeyWord;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        searchKeyWord = bundle.getString("data");
        return inflater.inflate(R.layout.res_fragment, container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new OrderListAdapter(getActivity());
        RecyclerView recyclerView = getView().findViewById(R.id.res_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        resultList = new ArrayList<>();
        HttpThread httpThread = new HttpThread();
        httpThread.start();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,3000);
        for(int i=0;i<30000;i++)
        {

        }
        addActivity();
    }

    public void addActivity()
    {
        adapter.setItem(resultList);
    }

    public void searchImage()
    {
        try {
            for(int i=0;i<resultList.size();i++) {
                String url;
                url = "http://106.54.118.148:8080/orderImage/getImages?oid=" + resultList.get(i).oid;
                URL mUrl = new URL(url);
                GlobalData globalData = (GlobalData) getActivity().getApplication();
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
            url = "http://106.54.118.148:8080/orderImage/download/" + iid;
            URL mUrl = new URL(url);
            GlobalData globalData = (GlobalData) getActivity().getApplication();
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

            resultList.get(resultPos).imageList.add(globalData.bitmap);


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
                url = "http://106.54.118.148:8080/order/getOrders/title?keyword="+searchKeyWord;
                URL mUrl = new URL(url);
                GlobalData globalData = (GlobalData) getActivity().getApplication();
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
