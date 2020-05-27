package com.sweetmay.weatherproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WeatherActivity extends AppCompatActivity {
    TextView city;
    TextView pressure;
    TextView wind;
    Button cityInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_weather);
        initViews();
        showReceivedData();
        openCityInfo();
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

    private void showReceivedData() {
        Intent intent = getIntent();
        boolean isPressure = intent.getBooleanExtra(MainActivity.pressureKey, false);
        boolean isWind = intent.getBooleanExtra(MainActivity.windKey, false);
        System.out.println(isPressure);
        System.out.println(isWind);

        if(isPressure){
            pressure.setVisibility(View.VISIBLE);
        }else {
            pressure.setVisibility(View.GONE);
        }

        if(isWind){
            wind.setVisibility(View.VISIBLE);
        }else {
            wind.setVisibility(View.GONE);
        }

        city.setText(intent.getStringExtra(MainActivity.cityKey));


    }

    private void initViews() {
        city = findViewById(R.id.cityView);
        pressure = findViewById(R.id.pressureView);
        wind = findViewById(R.id.windView);
        cityInfo = findViewById(R.id.goToWikiBtn);
    }
}
