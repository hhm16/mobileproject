package com.example.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.my.R;

public class TabFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        return inflater.inflate(R.layout.tag_fragment, container,false);
    }

    @Override
    public void onStart() {
        Button btn;
        btn =getView().findViewById(R.id.tag0);
        btn.setTag(false);
        btn =getView().findViewById(R.id.tag1);
        btn.setTag(false);
        btn =getView().findViewById(R.id.tag2);
        btn.setTag(false);
        super.onStart();
    }
}
