package com.example.musicplayer5;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.Date;

public class Second extends AppCompatActivity implements View.OnClickListener{

    private ImageView ImageView;
    private Button sBt, paBt, lbt;
    private TextView mText, nText, tText, lText;
    private SeekBar SeekBar;
    private SimpleDateFormat df;
    private MyService Bservice;

    boolean isService = false;
    int pcount= 0;
    int lcount = 0;
    int i = 0;
    int playcheck = 0;
    Handler mHand = new Handler();

    public static Second activity = null;



    public void StartBindService(){
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        SharedPreferences sp =getSharedPreferences("sp", MODE_PRIVATE);
        intent.putExtra("i",i);
        intent.putExtra("gtime",sp.getInt("gtime",0));
        intent.putExtra("ptime",sp.getInt("ptime",0));
        startService(intent);
        bindService(intent,Con,BIND_AUTO_CREATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        activity = this;

        ImageView = findViewById(R.id.imageView);
        sBt = findViewById(R.id.Sbt);
        paBt = findViewById(R.id.Pabt);
        lbt = findViewById(R.id.Lbt);
        mText = findViewById(R.id.mtext);
        nText = findViewById(R.id.nText);
        tText = findViewById(R.id.tText);
        lText = findViewById(R.id.Ltext);
        SeekBar = findViewById(R.id.seekBar);

        Bundle extras = getIntent().getExtras();
        df = new SimpleDateFormat("mm:ss");

        nText.setText(df.format(new Date(0)));

        sBt.setOnClickListener(this);
        paBt.setOnClickListener(this);
        lbt.setOnClickListener(this);

        SharedPreferences sp =getSharedPreferences("sp", MODE_PRIVATE);
        i = getIntent().getIntExtra("i",0);
        StartBindService();

        if(extras != null){
            if(i == 0){
                ImageView.setImageResource(R.drawable.badguy);
                mText.setText("가수: Billie Ellish \n제목 : bad guy");
                lcount = extras.getInt("like1");
                pcount = extras.getInt("play1");
                lText.setText("Likes  :"+lcount);
            }
            if(i == 1){
                ImageView.setImageResource(R.drawable.whateverittakes);
                mText.setText("가수 : Imagine Dragons \n제목 : whatever it takes");
                lcount = extras.getInt("like2");
                pcount = extras.getInt("play2");
                lText.setText("Likes  :"+lcount);

            }
        }
    }
    private ServiceConnection Con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Intent intent = getIntent();
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            Bservice = binder.getService();

            if(Bservice.whatplay() == i && Bservice.playing()){
                sBt.setBackgroundResource(R.drawable.pause1);
            }
            if(Bservice.whatplay() == i){
                seekBarOnProgress();
            }
            if(Bservice.whatplay()  != i&& !Bservice.playing()){
                Bservice.ServiceStop();
                stopService(intent);
                unbindService(Con);
                StartBindService();
            }
            if(Bservice.whatplay()  != i){
                Bservice.ServiceStop();
                stopService(intent);
                unbindService(Con);
                StartBindService();
            }
            isService = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isService = false;
        }
    };
    private void seekBarOnProgress(){
        nText.setText(df.format(new Date(0)));
        SeekBar.setMax(Bservice.getDuration());
        SeekBar.setProgress(Bservice.getCurrentPosition());
        nText.setText(df.format(SeekBar.getProgress()));
        tText.setText(df.format(Bservice.getDuration()));

        SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && Bservice.mp != null) {
                    Bservice.move(progress);
                    nText.setText(df.format(new Date(progress)));
                }
            }

            @Override
            public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
            }
        });

        Second.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Bservice.mp != null && Bservice.playing()) {
                    SeekBar.setProgress(Bservice.getCurrentPosition());
                    nText.setText(df.format(new Date(SeekBar.getProgress())));
                    tText.setText(df.format(new Date(Bservice.getDuration() - SeekBar.getProgress())));
                }
                assert Bservice.mp != null;
                Bservice.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        createNotification();

                    }
                });
                mHand.postDelayed(this, 100);
            }
        });
    }

    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        Intent intent = getIntent();
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        if(i == 0){
            builder.setContentText("bad guy");
        }
        else
            builder.setContentText("whatever it takes");

        builder.setContentIntent(pi);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert notificationManager != null;
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        assert notificationManager != null;
        notificationManager.notify(1, builder.build());

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Sbt:
                if(Bservice.mp == null){

                    return;
                }
                if(Bservice.whatplay() == i && Bservice.playing()){

                    sBt.setBackgroundResource(R.drawable.start1);
                    Bservice.stop();
                }
                else{
                    if(Bservice.whatplay() != i ){
                        Bservice.mp.reset();
                        Bservice.ServiceStop();

                        Intent intent = getIntent();
                        stopService(intent);
                        unbindService(Con);
                        StartBindService();
                    }
                    sBt.setBackgroundResource(R.drawable.pause1);
                    playcheck++;
                    if(playcheck == 1){
                        pcount +=1;
                    }
                    Bservice.start();
                }
                break;
            case R.id.Pabt:
                if(Bservice.isRunning == false)
                    return;
                Bservice.pause();
                sBt.setBackgroundResource(R.drawable.start1);
                Bservice.move(0);
                SeekBar.setProgress(0);
                nText.setText(df.format(new Date(0)));
                break;
            case R.id.Lbt:
                lcount += 1;
                lText.setText("Likes  :"+lcount);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        Intent returnIntent = getIntent();

        returnIntent.putExtra("i",i);
        returnIntent.putExtra("play",pcount);
        returnIntent.putExtra("like",lcount);

        SharedPreferences sp =getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor edit  = sp.edit();
        if(i == 0){
            edit.putInt("play1",pcount);
            edit.putInt("like1",lcount);
            edit.putInt("gtime",SeekBar.getProgress());
        }
        if(i == 1){
            edit.putInt("play2",pcount);
            edit.putInt("like2",lcount);
            edit.putInt("ptime",SeekBar.getProgress());
        }
        edit.apply();
        setResult(0,returnIntent);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SharedPreferences sp =getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor edit  = sp.edit();

        if(i == 0){
            edit.putInt("play1",pcount);
            edit.putInt("like1",lcount);
            edit.putInt("gtime",SeekBar.getProgress());
        }
        if(i == 1){
            edit.putInt("play2",pcount);
            edit.putInt("like2",lcount);
            edit.putInt("ptime",SeekBar.getProgress());
        }
        edit.apply();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbindService(Con);
        super.onDestroy();
    }


}
