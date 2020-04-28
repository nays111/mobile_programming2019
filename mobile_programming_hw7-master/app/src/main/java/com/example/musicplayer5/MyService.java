package com.example.musicplayer5;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MyService extends Service {
    MediaPlayer mp;
    int Duration = 0;
    int i = 0;

    private final IBinder mBinder = new LocalBinder();
    boolean isRunning = false;

    public class  LocalBinder extends Binder {
        public MyService getService(){
            isRunning = true;
            return MyService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        if(isRunning == false) {
            i = intent.getIntExtra("i", 0);
            int ptime = intent.getIntExtra("ptime", 0);
            int gtime = intent.getIntExtra("gtime",0);
            if (i == 0) {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.badguy);
                mp.seekTo(gtime);

            } else{
                mp = MediaPlayer.create(getApplicationContext(), R.raw.whateverittakes);
                mp.seekTo(ptime);
            }

            Duration = mp.getDuration();
        }
        return mBinder;
    }
    public int whatplay(){
        return i;
    }
    public void start(){
        mp.start();
    }
    public void pause(){
        mp.pause();
        mp.seekTo(0);
    }
    public void stop(){
        mp.pause();
    }
    public int getDuration(){
        return Duration;
    }
    public int getCurrentPosition(){
        return mp.getCurrentPosition();
    }
    public void move(int i){
        mp.seekTo(i);
    }
    public boolean playing(){
        return mp.isPlaying();
    }
    public void ServiceStop(){
        mp.stop();
        mp.reset();
        stopSelf();
        onDestroy();
    }

    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);

        registerReceiver(battery, filter);
    }

    BroadcastReceiver battery = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                Toast.makeText(context, "배터리가 부족합니다", Toast.LENGTH_LONG).show();
                if (mp != null) {
                    mp.pause();
                }
            }
            if (action.equals(Intent.ACTION_BATTERY_OKAY)) {
                Toast.makeText(context, "배터리가 양호합니다", Toast.LENGTH_LONG).show();
                if (mp != null) {
                    mp.start();
                }
            }

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
