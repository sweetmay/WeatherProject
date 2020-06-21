package com.sweetmay.weatherproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onStart() {
        EventBus.getBus().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
        setSupportActionBar(toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_city, R.id.nav_weather, R.id.nav_settings).
                setDrawerLayout(drawerLayout).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Subscribe
    public void onForecastEvent(ForecastEvent event){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //обработать горизонтальную ориентацию
        }else {
            navController.navigate(R.id.nav_weather);
            }
    }

    @Subscribe
    public void onErrorEvent(OnErrorEvent event){
        navController.navigate(R.id.nav_city);
    }
}

