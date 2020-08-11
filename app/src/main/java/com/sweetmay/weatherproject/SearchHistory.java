package com.sweetmay.weatherproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.sweetmay.weatherproject.requestweather.RecyclerDataAdapterHistory;

import java.util.Collections;
import java.util.List;

public class SearchHistory extends Fragment{

    private List<DBWeatherEntity> historyList;
    private RecyclerView rv;
    private TextInputEditText searchInput;
    private WeatherDAO weatherDAO;
    private RecyclerDataAdapterHistory adapterHistory;
    private Handler handler;
    private Handler uiHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherDAO = App.getInstance().getWeatherDataBase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_history_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDBThread();
        initViews();
    }

    private void initViews() {
        uiHandler = new Handler();
        searchInput = getView().findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getFilteredHistory(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rv = getView().findViewById(R.id.history_RV);
        adapterHistory = new RecyclerDataAdapterHistory(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        this.rv.setLayoutManager(layoutManager);
        this.rv.setAdapter(adapterHistory);
        getAllHistory();

    }

    private void initDBThread() {
        final HandlerThread dbThreadHandler = new HandlerThread("DBThread");
        dbThreadHandler.start();
        handler = new Handler(dbThreadHandler.getLooper());
    }


    private void getAllHistory(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                historyList = weatherDAO.getHistory();
                setHistory();
            }
        });
    }

    private void getFilteredHistory(String city){
        handler.post(new Runnable() {
            @Override
            public void run() {
                historyList = weatherDAO.getAllWithCityLike("%" + city + "%");
                setHistory();
            }
        });

    }

    public void setHistory(){
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Collections.reverse(historyList);
                adapterHistory.invalidateRV(historyList);
            }
        });
    }

}
