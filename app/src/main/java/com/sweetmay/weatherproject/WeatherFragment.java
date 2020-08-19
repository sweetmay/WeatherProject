package com.sweetmay.weatherproject;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
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

import com.sweetmay.weatherproject.bus.EventBus;
import com.sweetmay.weatherproject.bus.OnErrorEvent;
import com.sweetmay.weatherproject.requestWeather.RequestWeather;

import java.text.DecimalFormat;


public class WeatherFragment extends Fragment{
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
    private WeatherDAO db;
    private WeatherIcon weatherIcon;
    private Settings settings;

    public WeatherFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
        openCityInfo();
        city.setText(MainPresenter.getInstance().getCity());
    }

    @Override
    public void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindWeatherService();
    }

    private void bindWeatherService() {
        Intent intent = new Intent(getContext(), UpdaterService.class);
        getActivity().bindService(intent, updaterServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
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
        openCityInfo();
        initAlertDialog();

        db = App.getInstance().getWeatherDataBase();
        weatherIcon = new WeatherIcon(getContext());
        settings = App.getInstance().getSettingsInstance();
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

    private void UpdateWeather(final String city) {
        loading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RequestWeather body = updaterService.getWeather(city);
                if (body == null) {
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
                            WeatherFragment.this.showWeather(body);
                        }
                    });
                    addToDataBase(body, city);
                }
            }
        }).start();
    }

    private void addToDataBase(RequestWeather body, String city) {
        DBWeatherEntity dbWeatherEntity = new DBWeatherEntity(
                body.getDt(),
                city, body.getMain().getTemp(),
                body.getWeather().get(0).getIcon(),
                body.getMain().getPressure(),
                body.getWind().getSpeed());
        db.insertWeatherData(dbWeatherEntity);
    }

    private void showWeather(RequestWeather body) {

        String[] strArr = getData(body);
        weatherEmoji.setImageDrawable(getPNG(body.getWeather().get(0).getIcon()));

        sunriseView.setSunriseSunset(
                body.getSys().getSunrise(),
                body.getSys().getSunset(),
                body.getDt());

        temperature.setText(strArr[0]);
        pressure.setText(strArr[1]);
        wind.setText(strArr[2]);
        loading.hide();
    }

    private Drawable getPNG(String string){
         return weatherIcon.getPNG(string);
    }

    private String[] getData(RequestWeather response){

        String temperatureStr = settings.getTemp(response.getMain().getTemp());
        String pressureStr = response.getMain().getPressure() + "hpa";
        String windStr = response.getWind().getSpeed() + "m/s";

        return new String[]{temperatureStr, pressureStr, windStr};
    }

    private ServiceConnection updaterServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            updaterServiceBinder = (UpdaterService.ServiceBinder) iBinder;
            if (updaterServiceBinder != null){
                isBound = true;
                updaterService = updaterServiceBinder.getService();
                UpdateWeather((String) city.getText());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            updaterServiceBinder = null;
        }
    };
}