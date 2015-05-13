
package com.baeflower.hello.multimedia.audio.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.baeflower.hello.multimedia.audio._dump.SolMusicPlayerActivity;

import java.io.IOException;

/**
 * Created by sol on 2015-05-01.
 */
public class SolMusicService extends Service implements MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener{
    private static final String TAG = SolMusicService.class.getSimpleName();

    private MediaPlayer mMediaPlayer;
    private Uri mMusicUri;

    private NotificationManager mNM;
    private int NOTIFICATION = R.string.local_service_started;


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    private void showNotification() {
        CharSequence text = getText(R.string.local_service_started);

        Notification notification = new Notification(android.R.drawable.ic_media_pause, text, System.currentTimeMillis());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SolMusicPlayerActivity.class), 0);
        notification.setLatestEventInfo(this, getText(R.string.local_service_started), text, contentIntent);

        mNM.notify(NOTIFICATION, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
        }

        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        mMusicUri = intent.getParcelableExtra("musicUri");
        new Thread(mRun).start();

        return super.onStartCommand(intent, flags, startId);
    }

    Runnable mRun = new Runnable() {
        @Override
        public void run() {
            // prepareAsyc() 는 에러나는데? 부적절한 곳에서 호출했다고.. 스레드에서 불러야되나?
            try {

                mMediaPlayer.setDataSource(SolMusicService.this, mMusicUri);
                mMediaPlayer.prepare();
                mMediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        mNM.cancel(NOTIFICATION);

        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");
        stopSelf(); //서비스 종료

        // 다음곡 재생은?
    }


    /*
        머하는 놈이지?
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.d(TAG, "AUDIOFOCUS_GAIN");
                // resume playback
                if (mMediaPlayer == null){
                    // initMediaPlayer();
                }
                else if (!mMediaPlayer.isPlaying())
                    mMediaPlayer.start();
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.d(TAG, "AUDIOFOCUS_LOSS");
                // Lost focus for an unbounded amount of time: stop playback and
                // release media player
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.setVolume(0.1f, 0.1f);
                }
                break;
        }
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        mediaPlayer.reset();
        return false;
    }

}
