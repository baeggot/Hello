
package com.baeflower.hello.multimedia.audio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by sol on 2015-05-01.
 */
public class SolMusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener{

    private static final String TAG = SolMusicService.class.getSimpleName();
    private MediaPlayer mMediaPlayer;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SolMusicService getService() {
            // return this instance of LocalService so clients can call public methods
            return SolMusicService.this;
        }
    }

    private void initMediaPlayer() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
        } else {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        }
    }

    /*
        bindService로 했을때만 타는 것
     */
    @Override
    public IBinder onBind(Intent intent) {

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        try {
//            if (intent == null) {
//                return mBinder;
//            }

            initMediaPlayer();

            Uri musicUri = intent.getParcelableExtra("musicUri");
            mMediaPlayer.setDataSource(SolMusicService.this, musicUri);
            // prepareAsyc() 는 에러나는데? 부적절한 곳에서 호출했다고.. 스레드에서 불러야되나?
            mMediaPlayer.prepare();
            // mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mBinder;
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }


    /*
        startService로 호출했을 때
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        try {
            if (intent == null) {
                return super.onStartCommand(intent, flags, startId);
            }

            initMediaPlayer();

            Uri musicUri = intent.getParcelableExtra("musicUri");
            mMediaPlayer.setDataSource(SolMusicService.this, musicUri);
            // prepareAsyc() 는 에러나는데? 부적절한 곳에서 호출했다고.. 스레드에서 불러야되나?
            mMediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");

        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");
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
                if (mMediaPlayer == null)
                    initMediaPlayer();
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
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        mediaPlayer.reset();
        return false;
    }

    /*
    final RemoteCallbackList<IMusicInterface> mCallbacks = new RemoteCallbackList<>();

    IMusicInterface.Stub mBinder = new IMusicInterface.Stub(){
        @Override
        public int getDuration() throws RemoteException {
            return mMediaPlayer.getDuration();
        }
        @Override
        public int getCurrentPosition() throws RemoteException {
            return mMediaPlayer.getCurrentPosition();
        }

        public void registerCallback(IMusicInterface cb) {
            if (cb != null) mCallbacks.register(cb);
        }

        public void unregisterCallback(IMusicInterface cb) {
            if (cb != null) mCallbacks.unregister(cb);
        }
    };
*/


}
