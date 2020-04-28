package com.example.weatherviewer;

public class Weather {
    int lat;
    int ion;
    int temprature;
    int cloudy;
    String city;

    public void setLat(int lat){ this.lat = lat;}
    public void setIon(int ion){ this.ion = ion;}
    public void setTemprature(int t){ this.temprature = t;}
    public void setCloudy(int cloudy){ this.cloudy = cloudy;}
    public void setCity(String city){ this.city = city;}

    public int getLat(){ return lat;}
    public int getIon() { return ion;}
    public int getTemprature() { return temprature;}
    public int getCloudy() { return cloudy; }
    public String getCity() { return city; }
}

