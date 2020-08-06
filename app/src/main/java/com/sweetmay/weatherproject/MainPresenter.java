package com.sweetmay.weatherproject;

class MainPresenter {
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


    String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }

    boolean isWind() {
        return wind;
    }

    void setWind(boolean wind) {
        this.wind = wind;
    }

    boolean isPressure() {
        return pressure;
    }

    void setPressure(boolean pressure) {
        this.pressure = pressure;
    }

    static MainPresenter getInstance(){
        synchronized (synchObj){
            if (instance == null){
                instance = new MainPresenter();
            }
            return instance;
        }
    }
}
