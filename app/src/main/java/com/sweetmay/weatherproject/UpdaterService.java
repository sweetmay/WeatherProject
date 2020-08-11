package com.sweetmay.weatherproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.sweetmay.weatherproject.requestweather.RequestWeather;


public class UpdaterService extends Service {
    private IBinder binder = new ServiceBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public RequestWeather getWeather(String city){
        GetWeatherData getWeatherData = new GetWeatherData();
        if(getWeatherData.requestWeather(city) == null){
            return null;
        }else {
        return getWeatherData.requestWeather(city).body();}
    }

    class ServiceBinder extends Binder{
        UpdaterService getService(){
            return UpdaterService.this;
        }
        RequestWeather getWeather(String city){
            return getService().getWeather(city);
        }
    }
}

