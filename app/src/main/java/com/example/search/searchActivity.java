package com.example.search;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.ChatListRecycleView;
import com.example.login.LogFragment;
import com.example.my.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class searchActivity extends AppCompatActivity {
    ArrayList<String>strings;
    ArrayList<Integer>btnStatus;
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
        if(boxFragment==null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            boxFragment = new boxFragment();
            fragmentTransaction.hide(tagFragment).add(R.id.resultLayout, boxFragment).commit();
        }
        else
        {
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
}
