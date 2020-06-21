package com.example.search;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.login.LogFragment;
import com.example.my.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class searchActivity extends AppCompatActivity {
    ArrayList<String>strings;
    ArrayList<Integer>btnStatus;
    private FragmentManager supportFragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        supportFragmentManager = getSupportFragmentManager();
        fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.resultLayout,new TabFragment()).commit();
        strings = new ArrayList<>();
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
        //fragmentTransaction = supportFragmentManager.beginTransaction();
        //fragmentTransaction.add(R.id.resultLayout,new boxFragment()).commit();
    }
}
