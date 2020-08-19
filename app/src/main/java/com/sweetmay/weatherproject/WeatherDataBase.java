package com.sweetmay.weatherproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DBWeatherEntity.class}, version = 1)
public abstract class WeatherDataBase  extends RoomDatabase {
    public abstract WeatherDAO getWeatherDAO();
}
