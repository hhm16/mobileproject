package com.example.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.ChatListRecycleView;
import com.example.chat.Word;
import com.example.data.GlobalData;
import com.example.my.OrderBox;
import com.example.my.R;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderBoxHolder> {
    private final LayoutInflater mInflater;
    private List<OrderBoxStructure>orderList;
    OrderListAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderBoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.box_fragment, parent, false);
        return new OrderListAdapter.OrderBoxHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderBoxHolder holder, int position) {
        if (orderList != null) {
            OrderBoxStructure current = orderList.get(position);
            holder.titleItemView.setText(current.title);
            holder.coverItemView.setImageBitmap(current.imageList.get(0));
            holder.infoItemView.setText(current.content);
        } else {
            // Covers the case of data not being ready yet.
            holder.titleItemView.setText("No Word");
        }
    }

    @Override
    public int getItemCount() {
        if (orderList != null)
            return orderList.size();
        else return 0;
    }

    void setItem(List<OrderBoxStructure>list)
    {
        orderList = list;
        notifyDataSetChanged();
    }

    static class OrderBoxHolder extends RecyclerView.ViewHolder {
        private final TextView titleItemView;
        private final ImageView coverItemView;
        private final TextView infoItemView;
        private OrderBoxHolder(View itemView) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.order_title);
            coverItemView = itemView.findViewById(R.id.order_cover);
            infoItemView = itemView.findViewById(R.id.order_info);
        }
    }
}
