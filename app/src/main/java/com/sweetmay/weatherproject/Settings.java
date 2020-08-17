package com.sweetmay.weatherproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DecimalFormat;


public class Settings {

    private Context context;
    private DecimalFormat format;

    public Settings(Context context){
        this.context = context;
        format = new DecimalFormat("#.##");
    }

    public String getTemp(Double initialTempData){
        SharedPreferences preferences = context.getSharedPreferences
                (context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
        String tempType = preferences.getString(context.getString(R.string.temperature_key_preferences), "celsius");
        switch (tempType){
            case "celsius":
                return format.format(initialTempData - 273.15) + "\u2103";
            case "fahrenheit":
                return format.format((initialTempData - 273.15) * 9/5 +32) + "\u2109";
            case "kelvin":
                return format.format(initialTempData) + "\u212a";
        }
        return format.format(initialTempData);
    }
}
