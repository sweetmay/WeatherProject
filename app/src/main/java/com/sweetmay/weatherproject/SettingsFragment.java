package com.sweetmay.weatherproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class SettingsFragment extends Fragment {

    private RadioGroup tempGroup;
    private SharedPreferences preferences;
    private RadioButton checkedButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getContext().getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);
        tempGroup = getView().findViewById(R.id.settings_radio_group);
        savedSettings();
        onChangeCheck();
    }

    private void savedSettings() {
        for (int i = 0; i < tempGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) tempGroup.getChildAt(i);
            if(radioButton.getText().equals(preferences.getString(getString(R.string.temperature_key_preferences), "celsius"))){
                radioButton.toggle();
            }
        }
    }

    private void onChangeCheck() {
        tempGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkedButton =  getView().findViewById(radioGroup.getCheckedRadioButtonId());
                savePreferences((String) checkedButton.getText());
            }
        });
    }


    private void savePreferences(String selectedTemp){
        preferences.edit().putString(getString(R.string.temperature_key_preferences), selectedTemp).apply();
    }
}
