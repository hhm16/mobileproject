package com.example.login;

import android.content.Context;
import android.content.Intent;
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

import com.example.data.GlobalData;
import com.example.my.MainActivity;
import com.example.my.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        return inflater.inflate(R.layout.log_fragment, container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btn = getView().findViewById(R.id.loginbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect(view);
            }
        });
    }

    public void connect(View view)
    {
        String account;
        String password;
        String url;
        EditText accountEditText,passwordEditText;
        accountEditText = getView().findViewById(R.id.accounttext);
        accountEditText.setText("hhm16");
        passwordEditText = getView().findViewById(R.id.passwordtext);
        passwordEditText.setText("hhm16");
        account = accountEditText.getText().toString();
        password = passwordEditText.getText().toString();
        url = "http://106.54.118.148:8000/user/login?name="+account+"&"+"password="+password;
        LoginTask loginTask =new LoginTask(getActivity(),url,account,password);
        loginTask.execute();


        //Intent mainIntent = new Intent(LogActivity.this, MainActivity.class);
        //startActivity(mainIntent);


    }

    class LoginTask extends AsyncTask<Integer,Integer,Integer> {

        private Context context;
        private URL url;
        private String account;
        private String password;

        public LoginTask(Context context, String string, String account, String password){
            this.context = context;
            this.account = account;
            this.password = password;
            try {
                this.url = new URL(string);
            }
            catch (MalformedURLException e)
            {

            }
        }

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(Integer i) {
            if(i==1) {
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                //downloadIntent.setData(Uri.parse(fileUrl));
                startActivity(mainIntent);
            }
        }
        @Override
        protected Integer doInBackground(Integer... integers) {
            try {

                String url;
                EditText accountEditText,passwordEditText;
                url = "http://106.54.118.148:8080/user/login?name="+account+"&"+"password="+password;
                URL mUrl = new URL(url);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
                mHttpURLConnection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        mHttpURLConnection.getInputStream()));
                String str = null;
                while ((str = reader.readLine()) != null) {
                    JSONObject json = new JSONObject(str);
                    if((int)json.get("code")==403)
                    {
                        mHttpURLConnection.disconnect();
                        return 0;
                    }
                    else if((int)json.get("code")==200)
                    {
                        int id = json.getInt("uid");
                        GlobalData globalData = (GlobalData)getActivity().getApplication();
                        globalData.setID(id);
                        String cookie = mHttpURLConnection.getHeaderField("Set-Cookie");
                        //JSESSIONID=********;
                        String pattern = "JSESSIONID=(\\w*);";
                        Pattern p = Pattern.compile(pattern);
                        Matcher m =  p.matcher(cookie);
                        String sessionID = "";
                        if(m.find())
                        {
                            sessionID = m.group(1);
                        }
                        globalData.sessionID = (String) sessionID;
                        mHttpURLConnection.disconnect();
                        return 1;
                    }
                    return 0;
                }
                /*if((code=mHttpURLConnection.getResponseCode())==200)
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            mHttpURLConnection.getInputStream()));
                    final StringBuffer buffer = new StringBuffer();
                    String str = null;
                    while ((str = reader.readLine()) != null) {
                        JSONObject json = new JSONObject(str);
                        if((int)json.get("code")==403)
                        {
                            mHttpURLConnection.disconnect();
                            return 0;
                        }
                    }
                    mHttpURLConnection.disconnect();
                    return 1;
                    //Looper.loop();
                }
                else
                {
                    Looper.prepare();
                    Toast.makeText(context,"服务器端错误",Toast.LENGTH_SHORT).show();
                    //Looper.loop();
                    return 1;
                }*/

            }
            catch (MalformedURLException e)
            {
                Looper.prepare();
                Toast toast=Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT);
                toast.show();
                //Looper.loop();
            }
            catch (IOException e)
            {
                Looper.prepare();
                Toast toast=Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT);
                toast.show();
                //Looper.loop();
            }
            catch (Exception e)
            {
                Looper.prepare();
                Toast toast=Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT);
                toast.show();
                //Looper.loop();
            }
            return 0;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 运行在ui线程中，在doInBackground()执行完毕后执行,传入的参数为doInBackground()返回的结果
         */


        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         * 这里的Intege参数对应AsyncTask中的第二个参数
         * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
         * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }

    }
}
