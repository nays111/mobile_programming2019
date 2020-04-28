package com.example.musicplayer3;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;
import java.lang.InterruptedException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyService extends IntentService {
    private MediaPlayer mp;
    public MyService() {
        super("MyService");
    }
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method


    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int i = intent.getIntExtra("i", 0);
            if (i == 0) {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.badguy);
            } else {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.whateverittakes);
            }
            mp.setLooping(false);
            mp.start();
            try {
                Thread.sleep(mp.getDuration()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!mp.isPlaying())
                stopSelf();
        }
    }


    @Override
    public void onDestroy() {
        if(!mp.isPlaying())
        super.onDestroy();
    }
}
