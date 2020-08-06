package com.sweetmay.weatherproject;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.sweetmay.weatherproject.requestweather.RequestWeather;

import java.text.DecimalFormat;

import retrofit2.Response;


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
    private AlertDialog alertWeatherDataIncorrect;
    private SunriseView sunriseView;
    private UpdaterService.ServiceBinder updaterServiceBinder;
    private UpdaterService updaterService;
    private boolean isBound = false;
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
        Intent intent = new Intent(getContext(), UpdaterService.class);
        getActivity().bindService(intent, updaterServiceConnection, Context.BIND_AUTO_CREATE);
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
        sunriseView = getView().findViewById(R.id.sunriseView);
        loading.hide();
        weatherEmoji = getView().findViewById(R.id.weatherEmoji);
        city = getView().findViewById(R.id.cityViewFragment);
        wind = getView().findViewById(R.id.windView);
        temperature = getView().findViewById(R.id.temperature);
        pressure = getView().findViewById(R.id.pressureView);
        cityInfo = getView().findViewById(R.id.goToWikiBtn);
        wind.setVisibility(View.GONE);
        pressure.setVisibility(View.GONE);
        initAlertDialog();
    }

    private void initAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.place_not_found)
                .setPositiveButton(R.string.alert_button_change_city, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EventBus.getBus().post(new OnErrorEvent(true));
                    }
                });
        alertWeatherDataIncorrect = builder.create();
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
    public void onForecastEvent(ForecastEvent event){
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
                while (updaterService == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                final Response<RequestWeather> response = updaterService.getWeather(city);
                if (response == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            alertWeatherDataIncorrect.show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            WeatherFragment.this.showWeather(response);
                        }
                    });
                }
            }
        }).start();
    }

    private void showWeather(Response<RequestWeather> response) {
        String[] strArr = getData(response);
        getPNG(response.body().getWeather().get(0).getIcon());

        sunriseView.setSunriseSunset(response.body().getSys().getSunrise(), response.body().getSys().getSunset(), response.body().getDt());
        temperature.setText(strArr[0]);
        pressure.setText(strArr[1]);
        wind.setText(strArr[2]);
        loading.hide();
    }

    private void getPNG(String string){
        switch (string){

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

    private String[] getData(Response<RequestWeather> response){

        String temperatureStr = format.format(response.body().getMain().getTemp() - 275.15) + "\u2103";
        String pressureStr = response.body().getMain().getPressure() + "hpa";
        String windStr = response.body().getWind().getSpeed() + "m/s";

        return new String[]{temperatureStr, pressureStr, windStr};
    }

    private ServiceConnection updaterServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            updaterServiceBinder = (UpdaterService.ServiceBinder) iBinder;
            if (updaterServiceBinder != null){
                isBound = true;
                updaterService = updaterServiceBinder.getService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            updaterServiceBinder = null;
        }
    };
}