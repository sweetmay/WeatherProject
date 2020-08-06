package com.sweetmay.weatherproject;

public class ForecastEvent {
    private static String city;
    private static boolean isWind;
    private static boolean isPressure;

    public ForecastEvent(String city, boolean isWind, boolean isPressure){
        ForecastEvent.city = city;
        ForecastEvent.isWind = isWind;
        ForecastEvent.isPressure = isPressure;
    }


    public ForecastEvent(String city){
        ForecastEvent.city = city;
        isWind = MainPresenter.getInstance().isWind();
        isPressure = MainPresenter.getInstance().isPressure();
    }


    public String getCity() {
        return city;
    }

    public boolean isWind() {
        return isWind;
    }

    public boolean isPressure() {
        return isPressure;
    }
}
