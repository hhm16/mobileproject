package com.example.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my.R;

import java.util.ArrayList;
import java.util.List;

public class boxFragment extends Fragment {
    OrderListAdapter adapter;
    List<OrderBoxStructure> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        return inflater.inflate(R.layout.res_fragment, container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new OrderListAdapter(getActivity());
        RecyclerView recyclerView = getView().findViewById(R.id.res_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Button button = getView().findViewById(R.id.addActivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivity();
            }
        });
        list = new ArrayList<>();
    }

    public void addActivity()
    {
        list.add(new OrderBoxStructure(String.valueOf(list.size())));
        adapter.setItem(list);
    }
}
