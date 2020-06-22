package com.example.my;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class tabOnClickListener implements View.OnClickListener {
    Context mContext;
    public tabOnClickListener(Context mContext){
        this.mContext=mContext;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
