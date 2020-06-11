package com.sweetmay.weatherproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity implements RVOnItemClick {
    public static String cityKey;
    public static String pressureKey;
    public static String windKey;
    RecyclerDataAdapter adapter;
    private Fragment weatherFragment;
    private TextInputLayout cityInputLayout;
    private TextInputEditText cityInput;
    private RecyclerView cities;
    private CheckBox pressure;
    private CheckBox wind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choose);
        initViews();
        initKeys();
        cities.setVisibility(View.GONE);
        showCitiesList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }
    

    private void showCitiesList() {
        cityInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cities.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initKeys() {
        pressureKey = getResources().getString(R.string.pressure_key);
        windKey = getResources().getString(R.string.wind_key);
        cityKey = getResources().getString(R.string.city_key);
    }


    private void initViews(){
        weatherFragment = getSupportFragmentManager().findFragmentById(R.id.weatherFragment);
        cities = findViewById((R.id.cityRecyclerView));
        cityInputLayout = findViewById((R.id.editTextCity));
        cityInput = cityInputLayout.findViewById(R.id.inputCity);
        pressure = findViewById((R.id.checkBoxPressure));
        wind = findViewById((R.id.checkBoxWind));
        setUpRV();
    }

    private void setUpRV() {
        String[] strings = getResources().getStringArray(R.array.cities);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        adapter = new RecyclerDataAdapter(strings, this);
        cities.setLayoutManager(layoutManager);
        cities.setAdapter(adapter);
    }


    @Override
    public void onItemClick(String text) {
        MainPresenter.getInstance().setCity(text);
        cityInput.setText(MainPresenter.getInstance().getCity());
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    EventBus.getBus().post(new ForecastEvent(cityInput.getText().toString(),
                            wind.isChecked(), pressure.isChecked()));
                }else{
                    MainPresenter.getInstance().setCity(cityInput.getText().toString());
                    Intent intent = new Intent(MainActivity.this,
                            WeatherActivity.class);
                    intent.putExtra(cityKey, MainPresenter.getInstance().getCity());
                    intent.putExtra(pressureKey, pressure.isChecked());
                    intent.putExtra(windKey, wind.isChecked());
                    startActivity(intent);
                }
            }
    }

