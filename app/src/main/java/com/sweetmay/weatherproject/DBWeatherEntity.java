package com.sweetmay.weatherproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "weatherDB")
public class DBWeatherEntity {

    public DBWeatherEntity(
            long date,
            String city,
            double temp,
            String icon,
            int pressure,
            double wind){

        this.date = date;
        this.city = city;
        this.temp = temp;
        this.icon = icon;
        this.pressure = pressure;
        this.wind = wind;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(index = true)
    private long date;

    @ColumnInfo(index = true)
    private String city;

    private double temp;

    private String icon;

    private int pressure;

    private double wind;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }
}
