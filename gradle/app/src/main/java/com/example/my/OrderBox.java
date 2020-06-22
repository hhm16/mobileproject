package com.example.my;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class OrderBox extends FrameLayout {
    private Context context;
    private TextView title;
    private TextView info;
    private ImageView title_image;
    private View mView;
    public OrderBox(Context context)
    {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mView = inflater.inflate(R.layout.box_fragment, this, true);
        init();
    }
    private void init()
    {
        title_image = (ImageView) mView.findViewById(R.id.order_cover);
        title_image.setImageResource(R.drawable.title);
        title = mView.findViewById(R.id.order_title);
        title.setText("有一说一");
        info = mView.findViewById(R.id.order_info);
        info.setText("确实!");
    }

}
