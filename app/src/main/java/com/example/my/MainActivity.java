package com.example.my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;

import com.example.chat.ChatActivity;
import com.example.login.LogActivity;
import com.example.map.MapActivity;
import com.example.person.PersonActivity;
import com.example.publish.addActivity;
import com.example.search.searchActivity;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.bottomnavigationbar.*;
public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] mImg;
    private int[] mImg_id;
    private String[] mDec;
    private LinearLayout container;
    private ArrayList<ImageView> mImgList;
    private Button btn;
    boolean isRunning = false;
    private LinearLayout ll_dots_container;
    private TextView loop_dec;
    private int previousSelectedPosition = 0;
    private LinearLayout tab_container;
    private ArrayList<ImageView> mTabList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        initLoopView();
        initTabBar();
        //OrderBox orderBox = new OrderBox(MainActivity.this);
        //container.addView(orderBox,4);
        //OrderBox orderBox1 = new OrderBox(MainActivity.this);
        //container.addView(orderBox1,5);

    }

    public void search(View view)
    {
        Intent intent = new Intent(MainActivity.this, searchActivity.class);
        startActivity(intent);
    }

    private void initTabBar()
    {
        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottomNavigationBar);
        List<BottomNavigationEntity> mEntities = new ArrayList<>();

        mEntities.add(new BottomNavigationEntity(
                "图片",
                R.drawable.info,
                R.drawable.info_selected));
        mEntities.add(new BottomNavigationEntity(
                "视频",
                R.drawable.info,
                R.drawable.info_selected));
        mEntities.add(new BottomNavigationEntity(
                "关注",
                R.drawable.info,
                R.drawable.info_selected));
        mEntities.add(new BottomNavigationEntity(
                "我的",
                R.drawable.info,
                R.drawable.info_selected, 10));
        bottomNavigationBar.setEntities(mEntities);
        bottomNavigationBar.setBnbItemSelectListener(new BottomNavigationBar.IBnbItemSelectListener() {

            @Override
            public void onBnbItemSelect(int position) {
                if(position==1)
                {
                    Intent addIntent = new Intent(MainActivity.this, addActivity.class);
                    startActivity(addIntent);
                }
                else if(position==2)
                {
                    Intent personIntent = new Intent(MainActivity.this, PersonActivity.class);
                    startActivity(personIntent);
                }
                else if(position==3)
                {
                    Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(chatIntent);
                }
            }
        });
        //重复点击
        bottomNavigationBar.setBnbItemDoubleClickListener(new BottomNavigationBar.IBnbItemDoubleClickListener() {
            @Override
            public void onBnbItemDoubleClick(int position) {
            }
        });
    }
    private void initLoopView() {
        viewPager = (ViewPager) findViewById(R.id.loopViewPager);
        ll_dots_container = (LinearLayout) findViewById(R.id.ll_dots_loop);
        loop_dec = (TextView) findViewById(R.id.loop_dec);

        // 图片资源id数组
        mImg = new int[]{
                R.drawable.test,
                R.drawable.test,
                R.drawable.test,
                R.drawable.test,
                R.drawable.test
        };

        // 文本描述
        mDec = new String[]{
                "Test1",
                "Test2",
                "Test3",
                "Test4",
                "Test5"
        };

        mImg_id = new int[]{
                R.id.pager_img1,
                R.id.pager_img2,
                R.id.pager_img3,
                R.id.pager_img4,
                R.id.pager_img5
        };
        // 初始化要展示的5个ImageView
        mImgList = new ArrayList<ImageView>();
        ImageView imageView;
        View dotView;
        LinearLayout.LayoutParams layoutParams;
        for(int i=0;i<mImg.length;i++){
            //初始化要显示的图片对象
            imageView = new ImageView(this);
            imageView.setBackgroundResource(mImg[i]);
            imageView.setId(mImg_id[i]);
            imageView.setOnClickListener(new pagerOnClickListener(getApplicationContext()));
            mImgList.add(imageView);
            //加引导点
            dotView = new View(this);
            dotView.setBackgroundResource(R.drawable.dot);
            layoutParams = new LinearLayout.LayoutParams(10,10);
            if(i!=0){
                layoutParams.leftMargin=10;
            }
            //设置默认所有都不可用
            dotView.setEnabled(false);
            ll_dots_container.addView(dotView,layoutParams);
        }

        ll_dots_container.getChildAt(0).setEnabled(true);
        loop_dec.setText(mDec[0]);
        previousSelectedPosition=0;
        //设置适配器
        viewPager.setAdapter(new LoopViewAdapter(mImgList));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) %mImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int newPosition = i % mImgList.size();
                loop_dec.setText(mDec[newPosition]);
                ll_dots_container.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_dots_container.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // 开启轮询
        new Thread(){
            public void run(){
                isRunning = true;
                while(isRunning){
                    try{
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //下一条
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
            }
        }.start();
    }
}
