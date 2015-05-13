package com.baeflower.hello.location;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by sol on 2015-05-11.
 */
public class SolMusicMPHandler extends Handler {
    private static final String TAG = SolMusicMPHandler.class.getSimpleName();
    private static final int MSG_GET_MP = 1;

    private MediaPlayer mMediaPlayer;

    @Override
    public void handleMessage(Message msg) {
        Log.d(TAG, msg.obj.toString());

        switch (msg.what) {
            case MSG_GET_MP:
                mMediaPlayer = (MediaPlayer) msg.obj;
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
