package com.sweetmay.weatherproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;


public class WeatherActivityFragment extends Fragment {
    private TextView city;
    private TextView wind;
    private TextView pressure;
    private Button cityInfo;


    public WeatherActivityFragment() {
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
        city = getView().findViewById(R.id.cityViewFragment);
        wind = getView().findViewById(R.id.windView);
        pressure = getView().findViewById(R.id.pressureView);
        cityInfo = getView().findViewById(R.id.goToWikiBtn);
        wind.setVisibility(View.GONE);
        pressure.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_weather_activity, container, false);

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
    public void onForecastEvent(ForecastEvent event){
        wind.setVisibility(View.GONE);
        pressure.setVisibility(View.GONE);
        city.setText(event.getCity());
        if(event.isPressure()){
            pressure.setVisibility(View.VISIBLE);
        }
        if(event.isWind()){
            wind.setVisibility(View.VISIBLE);
        }
    }
}