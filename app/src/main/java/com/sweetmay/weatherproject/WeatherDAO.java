package com.sweetmay.weatherproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeatherData(DBWeatherEntity dbWeatherEntity);

    @Delete
    void deleteWeatherData(DBWeatherEntity dbWeatherEntity);

    @Query("DELETE FROM weatherDB WHERE date = :date")
    void deleteWeatherDataByDate(long date);

    @Query("SELECT * FROM weatherDB")
    List<DBWeatherEntity> getHistory();

    @Query("SELECT * FROM weatherDB WHERE date = :date")
    DBWeatherEntity getWeatherByDate(long date);

    @Query("DELETE FROM weatherDB WHERE city = :city")
    void deleteWeatherDataByCity(String city);

    @Query("SELECT * FROM weatherDB WHERE city = :city")
    DBWeatherEntity getWeatherDataByCity(String city);

    @Query("SELECT COUNT() FROM weatherDB")
    long getCount();

    @Query("SELECT * FROM weatherDB WHERE city LIKE :search")
    List<DBWeatherEntity> getAllWithCityLike(String search);
}
