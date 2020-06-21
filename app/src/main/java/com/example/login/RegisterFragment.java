package com.example.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.my.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    private EditText emailEdit;
    private TextView emailInfo;
    public String account;
    public String password;
    public String email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        return inflater.inflate(R.layout.register_fragment, container,false);
    }
    @Override
    public void onStart() {
        super.onStart();
        emailEdit = getView().findViewById(R.id.emailEdit);
        emailInfo = getView().findViewById(R.id.emailInfo);
        emailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {

                    //String str3 = "今天<font color='#FF0000'><big>天气不错</big></font>";
                    //tv3.setText(Html.fromHtml(str3));
                }
                else
                {
                    String str = emailEdit.getText().toString();
                    String regex = "@mails.tsinghua.edu.cn$";
                    Pattern r = Pattern.compile(regex);
                    Matcher m = r.matcher(str);
                    if(!m.find())
                    {
                        emailInfo.setText("(格式错误)");
                    }
                    else
                    {
                        emailInfo.setText("");
                    }
                }
            }
        }
        );
        Button btn = getView().findViewById(R.id.registerButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    public void register()
    {
        EditText accountEdit,passwordEdit,passwordAdmitEdit;
        accountEdit = getView().findViewById(R.id.registerUser);
        passwordEdit = getView().findViewById(R.id.registerPassword);
        accountEdit.setText("hhm16");
        passwordEdit.setText("hhm16");
        emailEdit.setText("hhm16@mails.tsinghua.edu.cn");
        account = accountEdit.getText().toString();
        password = passwordEdit.getText().toString();
        email = emailEdit.getText().toString();
        HttpThread sendData = new HttpThread();
        sendData.start();
    }



    private class HttpThread extends Thread
    {
        @Override
        public void run() {
            try {
                String url;
                url = "http://106.54.118.148:8080/user/register";
                URL mUrl = new URL(url);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
                mHttpURLConnection.setRequestMethod("PUT");
                OutputStream output = mHttpURLConnection.getOutputStream();
                String content = "name="+account +"&email="+email+"&password="+password;
                output.write(content.getBytes());
                output.flush();
                output.close();
                //mHttpURLConnection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        mHttpURLConnection.getInputStream()));
                final StringBuffer buffer = new StringBuffer();
                String str = null;
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                reader.close();
                int code = mHttpURLConnection.getResponseCode();
                if(code==200)
                {

                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public boolean emailEditFocusChanged()
    {
        return false;
    }
}
