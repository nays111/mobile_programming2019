package com.example.musicplayer3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgview;
    private MediaPlayer mp;
    private SimpleDateFormat df;
    private SeekBar seekbar;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView infotext;

    private TextView likecounttext;
    private ImageButton goodbutton;
    private ImageButton playbutton;
    private ImageButton stopbutton;

    int playcount = 0;
    int likecount = 0;
    int checkplay = 0;
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Handler handler = new Handler();
        SharedPreferences sf = getSharedPreferences("sf", MODE_PRIVATE);

        imgview = (ImageView) findViewById(R.id.imageview);
        tvLeft = (TextView) findViewById(R.id.tvleft);
        tvRight = (TextView) findViewById(R.id.tvright);

        likecounttext = (TextView) findViewById(R.id.likecounttext);
        infotext = (TextView) findViewById(R.id.infotext);
        goodbutton = (ImageButton) findViewById(R.id.goodbutton);
        playbutton = (ImageButton) findViewById(R.id.playbutton);
        stopbutton = (ImageButton) findViewById(R.id.stopbutton);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

        Bundle extras = getIntent().getExtras();
        df = new SimpleDateFormat("mm:ss");

        goodbutton.setOnClickListener(this);
        playbutton.setOnClickListener(this);
        stopbutton.setOnClickListener(this);

        tvLeft.setText(df.format(new Date(0)));

        if (extras != null) {
            i = getIntent().getIntExtra("i", 0);

            if (i == 0) {

                imgview.setImageResource(R.drawable.badguy);
                infotext.setText("가수: Billie Eilish\n제목: Badguy");
                likecount = extras.getInt("like1");
                playcount = extras.getInt("play1");
                likecounttext.setText("Likes  :" + likecount);

                mp = MediaPlayer.create(getApplicationContext(), R.raw.badguy);
                seekbar.setMax(mp.getDuration());

                mp.seekTo(sf.getInt("time1", 0));
                seekbar.setProgress(mp.getCurrentPosition());
                tvLeft.setText(df.format(seekbar.getProgress()));
                tvRight.setText(df.format(mp.getDuration()));
            }

            if (i == 1) {

                imgview.setImageResource(R.drawable.whateverittakes);
                infotext.setText("Artist: Imagine Dragons\nTitle: Whatever it takes");
                likecount = extras.getInt("like2");
                playcount = extras.getInt("play2");
                likecounttext.setText("Likes  :" + likecount);

                mp = MediaPlayer.create(getApplicationContext(), R.raw.whateverittakes);
                seekbar.setMax(mp.getDuration());

                mp.seekTo(sf.getInt("time2", 0));
                seekbar.setProgress(mp.getCurrentPosition());
                tvLeft.setText(df.format(seekbar.getProgress()));
                tvRight.setText(df.format(mp.getDuration()));
            }


            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mp != null) {
                        mp.seekTo(progress);
                        tvLeft.setText(df.format(new Date(progress)));
                    }
                }

                @Override
                public void onStartTrackingTouch(android.widget.SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(android.widget.SeekBar seekBar) {

                }
            });

            DetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mp != null && mp.isPlaying()) {
                        seekbar.setProgress(mp.getCurrentPosition());
                        tvLeft.setText(df.format(new Date(seekbar.getProgress())));
                        tvRight.setText(df.format(new Date(mp.getDuration() - seekbar.getProgress())));
                    }
                    handler.postDelayed(this, 100);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playbutton:
                if (mp == null)
                    return;
                if (mp.isPlaying()) {
                    mp.pause();

                } else {
                    mp.start();

                    checkplay++;
                    if (checkplay == 1) {
                        playcount += 1;
                    }
                }
                break;

            case R.id.stopbutton:
                if (mp == null)
                    return;

                mp.pause();
                mp.seekTo(0);
                seekbar.setProgress(0);
                tvLeft.setText(df.format(new Date(0)));

                break;

            case R.id.goodbutton:
                likecount += 1;
                likecounttext.setText("Likes  :" + likecount);
                break;
        }

    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = getIntent();
        SharedPreferences sf = getSharedPreferences("sf", MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();

        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        returnIntent.putExtra("i", i);
        returnIntent.putExtra("play", playcount);
        returnIntent.putExtra("like", likecount);


        if (i == 0) {
            edit.putInt("play1", playcount);
            edit.putInt("like1", likecount);
            edit.putInt("time1", seekbar.getProgress());
        }
        if (i == 1) {
            edit.putInt("play2", playcount);
            edit.putInt("like2", likecount);
            edit.putInt("time2", seekbar.getProgress());
        }
        edit.apply();

        setResult(0, returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SharedPreferences sf = getSharedPreferences("sf", MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();

        if (i == 0) {
            edit.putInt("gplay1", playcount);
            edit.putInt("glike1", likecount);
            edit.putInt("gtime1", seekbar.getProgress());
        }
        if (i == 1) {
            edit.putInt("play2", playcount);
            edit.putInt("like2", likecount);
            edit.putInt("time2", seekbar.getProgress());
        }
        edit.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}



