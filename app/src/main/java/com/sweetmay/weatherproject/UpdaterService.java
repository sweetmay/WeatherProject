package com.sweetmay.weatherproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.sweetmay.weatherproject.requestweather.RequestWeather;

import retrofit2.Response;

public class UpdaterService extends Service {
    private IBinder binder = new ServiceBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public Response<RequestWeather> getWeather(String city){
        GetWeatherData getWeatherData = new GetWeatherData();
        return getWeatherData.requestWeather(city);
    }

    class ServiceBinder extends Binder{
        UpdaterService getService(){
            return UpdaterService.this;
        }
        Response<RequestWeather> getWeather(String city){
            return getService().getWeather(city);
        }
    }
}

