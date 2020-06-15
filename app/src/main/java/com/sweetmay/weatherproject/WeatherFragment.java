package com.sweetmay.weatherproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class WeatherFragment extends Fragment {
    private final Handler handler = new Handler();
    private TextView city;
    private TextView wind;
    private TextView pressure;
    private Button cityInfo;
    private TextView temperature;
    private ContentLoadingProgressBar loading;
    private DecimalFormat format = new DecimalFormat("#.##");
    private ImageView weatherEmoji;

    public WeatherFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
        openCityInfo();
    }

    @Override
    public void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        showWeatherOnCreate();
    }

    private void showWeatherOnCreate() {
        if(MainPresenter.getInstance()!=null){
            UpdateWeather(MainPresenter.getInstance().getCity());
            if(MainPresenter.getInstance().isPressure()){
                pressure.setVisibility(View.VISIBLE);
            }
            if(MainPresenter.getInstance().isWind()){
                wind.setVisibility(View.VISIBLE);
            }
            city.setText(MainPresenter.getInstance().getCity());
        }
    }

    private void initViews() {
        loading = getView().findViewById(R.id.loading);
        loading.hide();
        weatherEmoji = getView().findViewById(R.id.weatherEmoji);
        city = getView().findViewById(R.id.cityViewFragment);
        wind = getView().findViewById(R.id.windView);
        temperature = getView().findViewById(R.id.temperature);
        pressure = getView().findViewById(R.id.pressureView);
        cityInfo = getView().findViewById(R.id.goToWikiBtn);
        wind.setVisibility(View.GONE);
        pressure.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_fragment, container, false);
    }

    private void openCityInfo(){
        cityInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse(createLink());
                Intent cityInfo = new Intent(Intent.ACTION_VIEW, address);
                startActivity(cityInfo);
            }
        });
    }

    private String createLink() {
        String url = city.getText().toString();
        if(url.contains(" ") || url.contains("-")){
            url = url.replaceAll(" ", "_");
            url = url.replaceAll("-", "_");
        }
        return "https://en.wikipedia.org/wiki/" + url;
    }

    @Subscribe
    public void onForecastEvent(ForecastEvent event) throws JSONException {
        wind.setVisibility(View.GONE);
        pressure.setVisibility(View.GONE);
        if(event.isPressure()){
            pressure.setVisibility(View.VISIBLE);
        }
        if(event.isWind()){
            wind.setVisibility(View.VISIBLE);
        }
        city.setText(event.getCity());
        UpdateWeather(event.getCity());
}

    private void UpdateWeather(final String city) {
        loading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject jsonObject = GetWeatherData.getData(city);
                if(jsonObject == null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    R.string.place_not_found, Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showWeather(jsonObject);
                        }
                    });
                }
            }
        }).start();
    }

    private void showWeather(JSONObject jsonObject) {
        try {
            JSONArray weather = jsonObject.getJSONArray("weather");
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject JSONwind = jsonObject.getJSONObject("wind");

            String[] strArr = getData(main, JSONwind, weather);
            getPNG(weather);

            temperature.setText(strArr[0]);
            pressure.setText(strArr[1]);
            wind.setText(strArr[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loading.hide();
    }

    private void getPNG(JSONArray weather) throws JSONException {
        switch (weather.getJSONObject(0).getString("icon")){

            case "01d":
            case "01n":
                weatherEmoji.setImageResource(R.drawable.d1);
                break;
            case "02d" :
            case "02n" :
                weatherEmoji.setImageResource(R.drawable.d2);
                break;
            case "03d":
            case "03n":
                weatherEmoji.setImageResource(R.drawable.d3);
                break;
            case "04d":
            case "04n":
                weatherEmoji.setImageResource(R.drawable.d4);
                break;
            case "09d":
            case "09n":
                weatherEmoji.setImageResource(R.drawable.d9);
                break;
            case "10d":
            case "10n":
                weatherEmoji.setImageResource(R.drawable.d10);
                break;
            case "11d":
            case "11n":
                weatherEmoji.setImageResource(R.drawable.d11);
                break;
            case "13d":
            case "13n":
                weatherEmoji.setImageResource(R.drawable.d13);
                break;
            case "50d":
            case "50n":
                weatherEmoji.setImageResource(R.drawable.d50);
                break;
        }
    }

    private String[] getData(JSONObject main, JSONObject wind, JSONArray weather) throws JSONException {
        String[] strArr = new String[3];
        float temp = Float.parseFloat(main.getString("temp")) - 273.15f;
        strArr[0] = format.format(temp) + "\u2103";
        strArr[1] = main.getString("pressure") + " hpa";
        strArr[2] = wind.getString("speed") + " m/s";
        return strArr;
    }
}