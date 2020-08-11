package com.sweetmay.weatherproject.requestweather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMap {
    @GET("data/2.5/weather")
    Call<RequestWeather> loadWeather(@Query("q") String city, @Query("appid") String ApiKey);
}
