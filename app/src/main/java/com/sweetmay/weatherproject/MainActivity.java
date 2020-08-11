package com.sweetmay.weatherproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.squareup.otto.Subscribe;
import com.sweetmay.weatherproject.bus.EventBus;
import com.sweetmay.weatherproject.bus.ForecastEvent;
import com.sweetmay.weatherproject.bus.OnErrorEvent;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private AlertDialog requiresLocationAlert;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        checkPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requiresLocationAlert.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();

            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());
                        String city = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getLocality();
                        forecast(city);
                    } catch (IOException e) {

                        navController.navigate(R.id.nav_city);
                    }
                }
            }
        });
    }

    private void forecast(String city) {
        MainPresenter.getInstance().setCity(city);
        MainPresenter.getInstance().setPressure(true);
        MainPresenter.getInstance().setWind(true);
        EventBus.getBus().post(new ForecastEvent(city,
                true, true));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        initAlertDialog();
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_permission_title);
        builder.setMessage(R.string.alert_permission_message);
        builder.setIcon(R.drawable.location_icon);
        builder.setPositiveButton(R.string.alert_button_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.requestLocationPermission();
            }
        });
        builder.setNegativeButton(R.string.alert_button_dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        requiresLocationAlert = builder.create();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Subscribe
    public void onForecastEvent(ForecastEvent event){
        navController.navigate(R.id.nav_weather);
    }

    @Subscribe
    public void onErrorEvent(OnErrorEvent event){
        navController.navigate(R.id.nav_city);
    }
}

