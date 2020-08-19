package com.sweetmay.weatherproject;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    private static final Object synchObj = new Object();

    private static App instance;

    private WeatherDataBase weatherDataBase;

    private WeatherDAO weatherDAO;

    private Settings settings;

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

        settings = new Settings(getApplicationContext());
        weatherDAO = weatherDataBase.getWeatherDAO();
    }

    public Settings getSettingsInstance(){
        return settings;
    }

    public WeatherDAO getWeatherDataBase(){
        synchronized (synchObj){
            return weatherDAO;
        }
    }
}
