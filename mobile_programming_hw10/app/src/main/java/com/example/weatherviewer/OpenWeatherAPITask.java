package com.example.weatherviewer;

import android.os.AsyncTask;

public class OpenWeatherAPITask extends AsyncTask<Integer, Void, Weather> {

    @Override

    public Weather doInBackground(Integer... params) {

        com.example.weatherviewer.OpenWeatherAPIClient client = new com.example.weatherviewer.OpenWeatherAPIClient();



        int lat = params[0];

        int lon = params[1];

        // API 호출

        Weather w = client.getWeather(lat,lon);

        //System.out.println("Weather : "+w.getTemprature());

        // 작업 후 리
        return w;

    }

}


