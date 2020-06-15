package com.sweetmay.weatherproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CityChooseFragment extends Fragment implements RVOnItemClick{
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

    public CityChooseFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_choose_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initKeys();
        cities.setVisibility(View.GONE);
        showCitiesList();
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
        cities = getView().findViewById((R.id.cityRecyclerView));
        cityInputLayout = getView().findViewById((R.id.editTextCity));
        cityInput = cityInputLayout.findViewById(R.id.inputCity);
        pressure = getView().findViewById((R.id.checkBoxPressure));
        wind = getView().findViewById((R.id.checkBoxWind));
        setUpRV();
    }

    private void setUpRV() {
        String[] strings = getResources().getStringArray(R.array.cities);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        adapter = new RecyclerDataAdapter(strings, this);
        cities.setLayoutManager(layoutManager);
        cities.setAdapter(adapter);
    }


    @Override
    public void onItemClick(String text) {
        MainPresenter.getInstance().setCity(text);
        MainPresenter.getInstance().setPressure(pressure.isChecked());
        MainPresenter.getInstance().setWind(wind.isChecked());
        cityInput.setText(MainPresenter.getInstance().getCity());
        EventBus.getBus().post(new ForecastEvent(cityInput.getText().toString(),
                wind.isChecked(), pressure.isChecked()));
    }
}
