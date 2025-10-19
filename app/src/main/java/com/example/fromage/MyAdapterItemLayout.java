package com.example.fromage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterItemLayout extends RecyclerView.Adapter<MyAdapterItemLayout.ViewHolder> {

    private List<ItemLayout> itemList;

    public MyAdapterItemLayout(List<ItemLayout> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView subtitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.CheeseIcon);
            titleView = itemView.findViewById(R.id.TitreCheese);
            subtitleView = itemView.findViewById(R.id.ActionCheese);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemLayout item = itemList.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.titleView.setText(item.getTitle());
        holder.subtitleView.setText(item.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}