package com.sweetmay.weatherproject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetWeatherData {
    private static final String API_KEY = "&appid=9a8f4f630b7856915df4ec913e6715ed";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static String cityName;
    private static JSONObject json;

    public static JSONObject getJson(String city) {
        getData(city);
        return json;
    }


    public static void getData(String city) {
        cityName = city;
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(BASE_URL + cityName + API_KEY);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                StringBuilder SB = new StringBuilder();
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()));
                    String tempStr = reader.readLine();
                    while (tempStr != null) {
                        SB.append(tempStr);
                        tempStr = reader.readLine();
                    }
                    json = new JSONObject(SB.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
