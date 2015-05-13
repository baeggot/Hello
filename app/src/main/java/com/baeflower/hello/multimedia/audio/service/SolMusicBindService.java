package com.baeflower.hello.multimedia.audio.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.baeflower.hello.R;
import com.baeflower.hello.multimedia.audio._dump.SolMusicPlayerActivity;

import java.io.IOException;

/**
 * Created by sol on 2015-05-06.
 */
public class SolMusicBindService extends Service implements MediaPlayer.OnCompletionListener {
    private static final String TAG = SolMusicBindService.class.getSimpleName();

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private Uri mMusicUri;
    private MediaPlayer mMediaPlayer;

    private NotificationManager mNM;
    private int NOTIFICATION = R.string.local_service_started;

    private boolean mIsCompleted;


    public class LocalBinder extends Binder {
        public SolMusicBindService getService() {
            // return this instance of LocalService so clients can call public methods
            return SolMusicBindService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);


    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
        }

        if (intent == null) {
            return mBinder;
        }

        mMusicUri = intent.getParcelableExtra("musicUri");
        new Thread(mRun).start();
        mIsCompleted = false;

        return mBinder;
    }

    Runnable mRun = new Runnable() {
        @Override
        public void run() {
            // prepareAsyc() 는 에러나는데? 부적절한 곳에서 호출했다고.. 스레드에서 불러야되나?
            try {

                mMediaPlayer.setDataSource(SolMusicBindService.this, mMusicUri);
                mMediaPlayer.prepare();
                mMediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        mIsCompleted = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        mNM.cancel(NOTIFICATION);
    }

    private void showNotification() {
        CharSequence text = getText(R.string.local_service_started);

        Notification notification = new Notification(android.R.drawable.ic_media_pause, text, System.currentTimeMillis());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SolMusicPlayerActivity.class), 0);
        notification.setLatestEventInfo(this, getText(R.string.local_service_started), text, contentIntent);

        mNM.notify(NOTIFICATION, notification);
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public boolean ismIsCompleted() {
        return mIsCompleted;
    }
}
