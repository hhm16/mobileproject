package com.example.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
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
import com.example.method.HttpUtils;
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

        /*else
        {
            //HttpThread httpThread = new HttpThread();
            //httpThread.start();
            Bundle bundle = new Bundle();
            EditText searchTitle = findViewById(R.id.searchTitle);
            String title = searchTitle.getText().toString();
            bundle.putString("data",title);
            boxFragment = new boxFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(tagFragment).show(boxFragment).commit();
        }*/
    }

    public void startInput(View view)
    {
        if(boxFragment!=null&&tagFragment!=null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            tagFragment = new TabFragment();
            fragmentTransaction.replace(R.id.resultLayout,tagFragment).commit();
        }
    }
}
