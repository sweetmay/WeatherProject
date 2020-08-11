package com.sweetmay.weatherproject;


import com.sweetmay.weatherproject.requestweather.OpenWeatherMap;
import com.sweetmay.weatherproject.requestweather.RequestWeather;


import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetWeatherData {
    private static final String API_KEY = "9a8f4f630b7856915df4ec913e6715ed";
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private OpenWeatherMap openWeatherMap;

    public GetWeatherData(){
        initRetrofit();
    }

    private void initRetrofit(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherMap = retrofit.create(OpenWeatherMap.class);
    }

    public Response<RequestWeather> requestWeather(String city){
        try {
            Response<RequestWeather> response = openWeatherMap.loadWeather(city, API_KEY).execute();
            if (response.body() != null && response.body().getCod() == 200){
                return response;
            }else throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
