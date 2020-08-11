package com.sweetmay.weatherproject.requestweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sweetmay.weatherproject.DBWeatherEntity;
import com.sweetmay.weatherproject.R;
import com.sweetmay.weatherproject.WeatherIcon;


import java.sql.Date;
import java.text.DecimalFormat;
import java.util.List;

public class RecyclerDataAdapterHistory extends RecyclerView.Adapter<RecyclerDataAdapterHistory.ViewHolder>{

    private List<DBWeatherEntity> weatherEntityList;
    private WeatherIcon weatherIcon;
    private DecimalFormat format = new DecimalFormat("#.##");
    private Date date;
    public RecyclerDataAdapterHistory(Context context){
        weatherIcon = new WeatherIcon(context);
    }

    @NonNull
    @Override
    public RecyclerDataAdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerDataAdapterHistory.ViewHolder holder, int position) {
        DBWeatherEntity entity = weatherEntityList.get(position);
        date = new Date(entity.getDate()*1000);
        String temp = format.format((entity.getTemp() - 273.15)) +"\u2103";
        holder.temperature.setText(temp);
        holder.weatherIcon.setImageDrawable(weatherIcon.getPNG(entity.getIcon()));
        holder.city.setText(entity.getCity());
        holder.date.setText(date.toString());
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
    }
}
