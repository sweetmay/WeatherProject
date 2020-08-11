package com.sweetmay.weatherproject;

import android.content.Context;
import android.graphics.drawable.Drawable;


public class WeatherIcon {

    private Context context;

    public WeatherIcon(Context context){
        this.context = context;
    }

    public Drawable getPNG(String string){
        switch (string){
            case "01d":
            case "01n":
                return context.getDrawable(R.drawable.d1);
            case "02d" :
            case "02n" :
                return context.getDrawable(R.drawable.d2);
            case "03d":
            case "03n":
                return context.getDrawable(R.drawable.d3);
            case "04d":
            case "04n":
                return context.getDrawable(R.drawable.d4);
            case "09d":
            case "09n":
                return context.getDrawable(R.drawable.d9);
            case "10d":
            case "10n":
                return context.getDrawable(R.drawable.d10);
            case "11d":
            case "11n":
                return context.getDrawable(R.drawable.d11);
            case "13d":
            case "13n":
                return context.getDrawable(R.drawable.d13);
            case "50d":
            case "50n":
                return context.getDrawable(R.drawable.d50);
        }
        return null;
}
}
