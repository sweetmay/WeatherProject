package com.sweetmay.weatherproject;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetWeatherData {
    private static final String API_KEY = "9a8f4f630b7856915df4ec913e6715ed";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

    public static JSONObject getData(String city) {
        URL url = null;
        try {
            url = new URL(BASE_URL + city + "&appid=" + API_KEY);
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
            return new JSONObject(SB.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
