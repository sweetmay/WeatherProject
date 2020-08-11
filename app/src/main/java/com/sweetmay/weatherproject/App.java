package com.sweetmay.weatherproject;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    private static App instance;

    private WeatherDataBase weatherDataBase;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        weatherDataBase = Room.databaseBuilder(
                getApplicationContext(),
                WeatherDataBase.class,
                "weather_database").
                build();
    }

    public WeatherDAO getWeatherDataBase(){
        return weatherDataBase.getWeatherDAO();
    }
}
