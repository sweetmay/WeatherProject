package com.sweetmay.weatherproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;


import java.sql.Date;
import java.text.DecimalFormat;
import java.util.List;

public class RecyclerDataAdapterHistory extends RecyclerView.Adapter<RecyclerDataAdapterHistory.ViewHolder>{

    private List<DBWeatherEntity> weatherEntityList;
    private WeatherIcon weatherIcon;
    private DecimalFormat format = new DecimalFormat("#.##");
    private Settings settings;

    public RecyclerDataAdapterHistory(Context context){
        weatherIcon = new WeatherIcon(context);
        settings = App.getInstance().getSettingsInstance();
    }

    @NonNull
    @Override
    public RecyclerDataAdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerDataAdapterHistory.ViewHolder holder, int position) {
        holder.setData(position, weatherIcon);
    }

    @Override
    public int getItemCount() {
        if(weatherEntityList == null || weatherEntityList.size() == 0){
            return 0;
        }else {
            return weatherEntityList.size();
        }
    }

    public void invalidateRV(List<DBWeatherEntity> list){
        weatherEntityList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView weatherIcon;
        TextView city;
        TextView temperature;
        TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.history_card);
            weatherIcon = itemView.findViewById(R.id.weather_icon);
            city = itemView.findViewById(R.id.city_history);
            temperature = itemView.findViewById(R.id.temperature_history);
            date = itemView.findViewById(R.id.date_history);
        }
        private void setData(int position, WeatherIcon weatherPng){
            DBWeatherEntity entity = weatherEntityList.get(position);
            Date convertedDate = new Date(entity.getDate() * 1000);
            String temp = settings.getTemp(entity.getTemp());
            temperature.setText(temp);
            weatherIcon.setImageDrawable(weatherPng.getPNG(entity.getIcon()));
            city.setText(entity.getCity());
            date.setText(convertedDate.toString());
        }
    }
}
