package com.sweetmay.weatherproject;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sweetmay.weatherproject.bus.EventBus;
import com.sweetmay.weatherproject.bus.ForecastEvent;

import java.util.Objects;

public class CityChooseFragment extends Fragment implements RVOnItemClick{
    public static String cityKey;
    public static String pressureKey;
    public static String windKey;
    private RecyclerDataAdapterCity adapter;
    private TextInputLayout cityInputLayout;
    private TextInputEditText cityInput;
    private RecyclerView cities;

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
        onEnterClick();
    }

    private void onEnterClick() {
        cityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEND){
                    onForecast(Objects.requireNonNull(cityInput.getText()).toString());
                    return true;
                }
                return false;
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


    private void initViews(){
        cities = getView().findViewById((R.id.cityRecyclerView));
        cityInputLayout = getView().findViewById((R.id.editTextCity));
        cityInput = cityInputLayout.findViewById(R.id.inputCity);
        setUpRV();
    }

    private void setUpRV() {
        String[] strings = getResources().getStringArray(R.array.cities);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        adapter = new RecyclerDataAdapterCity(strings, this);
        cities.setLayoutManager(layoutManager);
        cities.setAdapter(adapter);
    }


    @Override
    public void onItemClick(String text) {
        onForecast(text);
    }

    private void onForecast(String text) {
        MainPresenter.getInstance().setCity(text);
        cityInput.setText(MainPresenter.getInstance().getCity());
        EventBus.getBus().post(new ForecastEvent(cityInput.getText().toString(),
                true, true));
    }
}
