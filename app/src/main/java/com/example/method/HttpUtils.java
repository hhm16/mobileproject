package com.example.method;

import com.example.data.GlobalData;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static InputStream getImageViewInputStream(String urlString, String jID)
    {
        try {
            InputStream inputStream = null;
            URL url = new URL(urlString);                    //服务器地址
            //打开连接
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestProperty("Cookie", jID);
            httpURLConnection.setConnectTimeout(3000);//设置网络连接超时的时间为3秒
            httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
            httpURLConnection.setDoInput(true);                //打开输入流
            int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
            if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
                inputStream = httpURLConnection.getInputStream();        //获取输入流
            }
            return inputStream;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
