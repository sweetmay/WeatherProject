package com.sweetmay.weatherproject;

public class MainPresenter {
    private static MainPresenter instance = null;

    private static final Object synchObj = new Object();

    private String city;
    private boolean wind;
    private boolean pressure;

    private MainPresenter(){
        city = " ";
        wind = false;
        pressure = false;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isWind() {
        return wind;
    }

    public void setWind(boolean wind) {
        this.wind = wind;
    }

    public boolean isPressure() {
        return pressure;
    }

    public void setPressure(boolean pressure) {
        this.pressure = pressure;
    }

    public static MainPresenter getInstance(){
        synchronized (synchObj){
            if (instance == null){
                instance = new MainPresenter();
            }
            return instance;
        }
    }
}
