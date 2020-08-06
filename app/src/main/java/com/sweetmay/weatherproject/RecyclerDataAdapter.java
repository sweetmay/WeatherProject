package com.sweetmay.weatherproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.AppOpsManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ViewHolder> {
    private String[] cities;
    private RVOnItemClick onItemClick;

    public RecyclerDataAdapter(String[] cities, RVOnItemClick onItemClick){
        this.cities = cities;
        this.onItemClick = onItemClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cities_item_rv,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String str = cities[position];
        holder.city.setText(str);
        setOnItemClick(holder, str);
    }

    private void setOnItemClick(@NonNull ViewHolder holder, final String str) {
        holder.city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(str);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities == null ? 0 : cities.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView city;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.cityItem);
        }
    }
}
