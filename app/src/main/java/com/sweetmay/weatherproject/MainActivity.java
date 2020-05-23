package com.sweetmay.weatherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView cities;
    CheckBox wind;
    CheckBox pressure;
    EditText cityInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choose);
        initViews();
        cities.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cities,
                android.R.layout.simple_list_item_1);
        cities.setAdapter(adapter);

        cityInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cities.setVisibility(View.VISIBLE);
            }
        });
        cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] str = getResources().getStringArray(R.array.cities);
                cityInput.setText(str[position]);
            }
        });


    }



    private void initViews(){
        cities = findViewById((R.id.citySelect));
        wind = findViewById((R.id.checkBoxPressure));
        pressure = findViewById((R.id.checkBoxPressure));
        cityInput = findViewById((R.id.editTextCity));
    }

}
