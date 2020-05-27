package com.sweetmay.weatherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    public static String cityKey;
    public static String pressureKey;
    public static String windKey;

    ListView cities;
    EditText cityInput;
    Button forecastBtn;
    CheckBox pressure;
    CheckBox wind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choose);
        initViews();
        initKeys();
        cities.setVisibility(View.GONE);
        setListView();
        showCitiesList();
        chooseCityFromList();
        setOnClickListenerForecastBtn();


    }

    private void setOnClickListenerForecastBtn() {
        forecastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPresenter.getInstance().setCity(cityInput.getText().toString());

                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra(cityKey, MainPresenter.getInstance().getCity());
                intent.putExtra(pressureKey, pressure.isChecked());
                intent.putExtra(windKey, wind.isChecked());
                startActivity(intent);
            }
        });
    }

    private void chooseCityFromList() {
        cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] str = getResources().getStringArray(R.array.cities);
                cityInput.setText(str[position]);
            }
        });
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

    private void setListView() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cities,
                android.R.layout.simple_list_item_1);
        cities.setAdapter(adapter);
    }


    private void initViews(){
        cities = findViewById((R.id.citySelect));
        cityInput = findViewById((R.id.editTextCity));
        forecastBtn = findViewById(R.id.forecast);
        pressure = findViewById((R.id.checkBoxPressure));
        wind = findViewById((R.id.checkBoxWind));

    }

}
