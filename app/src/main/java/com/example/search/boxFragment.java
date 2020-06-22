package com.example.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.data.GlobalData;
import com.example.method.HttpUtils;
import com.example.my.MainActivity;
import com.example.my.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        SearchTask searchTask = new SearchTask(getActivity());
        searchTask.execute();
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
                        resultList.get(i).imageList.add(BitmapFactory.decodeStream(HttpUtils.getImageViewInputStream("http://106.54.118.148:8080/orderImage/download/"+iid,jID)));
                        //downloadImage(i,iid);
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 步骤1：创建AsyncTask子类
     * 注：
     *   a. 继承AsyncTask类
     *   b. 为3个泛型参数指定类型；若不使用，可用java.lang.Void类型代替
     *   c. 根据需求，在AsyncTask子类内实现核心方法
     */
    class SearchTask extends AsyncTask<Integer,Integer,Integer> {

        private Context context;


        public SearchTask(Context context){
            this.context = context;

        }
        // 方法1：onPreExecute（）
        // 作用：执行 线程任务前的操作
        // 注：根据需求复写
        @Override
        protected void onPreExecute() {

        }
        // 方法4：onPostExecute（）
        // 作用：接收线程任务执行结果、将执行结果显示到UI组件
        // 注：必须复写，从而自定义UI操作,参数为doInBackground的返回值，运行在UI线程中
        @Override
        protected void onPostExecute(Integer i) {
            addActivity();
        }

        // 方法2：doInBackground（）
        // 作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
        // 注：必须复写，从而自定义线程任务
        @Override
        protected Integer doInBackground(Integer... integers) {
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
            catch (MalformedURLException e)
            {

            }
            catch (IOException e)
            {

            }
            catch (Exception e)
            {

            }
            return 0;
        }



        // 方法3：onProgressUpdate（）
        // 作用：在主线程 显示线程任务执行的进度
        // 注：根据需求复写
        @Override
        protected void onProgressUpdate(Integer... values) {

        }

    }
}
