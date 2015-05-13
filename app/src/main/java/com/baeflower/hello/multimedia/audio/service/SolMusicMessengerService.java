package com.baeflower.hello.multimedia.audio.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import com.baeflower.hello.R;
import com.baeflower.hello.multimedia.audio.SolMusicPlayer02Activity;
import com.baeflower.hello.multimedia.audio.SolMusicSongListActivity;
import com.baeflower.hello.multimedia.audio._dump.SolMusicPlayerActivity;

import java.io.IOException;

/**
 * Created by sol on 2015-05-08.
 */
public class SolMusicMessengerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = SolMusicMessengerService.class.getSimpleName();

    // Command to the service to display a message
    public static final int MSG_GET_MP = 1;
    public static final int MSG_NEXT_MP = 2;
    public static final int MSG_GET_MP_IN_LIST = 3;

    private static final int NOTIFICATION_ID = 0;

    private MediaPlayer mMediaPlayer;
    private NotificationManager mNM;
    private int NOTIFICATION = R.string.local_service_started;

    private Uri mMusicUri;
    private boolean mIsCompleted;


    // Target we publish for clients to send messages to IncomingHandler
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    final Messenger mMPMessenger = new Messenger(new SolMusicPlayer02Activity.MusicHandler());
    final Messenger mMPMessengerToList = new Messenger(new SolMusicSongListActivity.MusicHandler());
    private int mCurrentPosition;

    // Handler of incoming messages from clients
    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_MP:
                    Log.d(TAG, "media player 재요청");
                    sendMessageToPlayerActivity();
                    break;
                case MSG_NEXT_MP:
                    Log.d(TAG, "다음곡");
                case MSG_GET_MP_IN_LIST:
                    Log.d(TAG, "리스트에서 요청");
                    sendMessageToSongListActivity();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // showNotification();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

    }

    private boolean doRestart = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (mMediaPlayer.isPlaying() || isPaused) {
            Uri tmpUri = intent.getParcelableExtra("musicUri");

            if (mMusicUri.equals(tmpUri)) { // 재생되고 있었던 거랑 같음
                doRestart = true;
                sendMessageToPlayerActivity();
                return super.onStartCommand(intent, flags, startId);
            } else {
                mMusicUri = tmpUri;
            }
        } else {
            mMusicUri = intent.getParcelableExtra("musicUri");
        }

        doRestart = false;
        mCurrentPosition = intent.getIntExtra("currentPosition", -1);
        Log.d(TAG, "현재 위치 : " + String.valueOf(mCurrentPosition));

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK); //
            mMediaPlayer.setDataSource(getApplicationContext(), mMusicUri); //

            mMediaPlayer.prepare();
            // mMediaPlayer.prepareAsync();
            // mMediaPlayer.setOnErrorListener(this);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared");

        if (doRestart == false) {
            mp.start();
            if (mp.isPlaying()) { // ...
                Log.d(TAG, "재생위치 : " + String.valueOf(mp.getCurrentPosition()));
                sendMessageToPlayerActivity();
            }
        }
    }

    // When binding to the service,
    // we return an interface to our messenger for sending messages to the service
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");

        if (intent == null) {
            return mMessenger.getBinder();
        }

        // bind 하는 대상이 SolMusicSongListActivity 일때 전달해야되는데
//        String activityName = intent.getStringExtra("activity");
//        if (!TextUtils.isEmpty(activityName)) {
//            sendMessageToSongListActivity();
//        }

        return mMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) { // new client 가 접속했을 때 불린다고?
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 다음곡 Uri 요청해야됨

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }

        // stopForeground(true);
    }

    private void sendMessageToPlayerActivity() {
        Message msg = Message.obtain(null, SolMusicMessengerService.MSG_GET_MP, 0, 0);
        msg.obj = mMediaPlayer;
        try {
            mMPMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToSongListActivity() {
        Message msg = Message.obtain(null, SolMusicMessengerService.MSG_GET_MP_IN_LIST, 0, 0);
        msg.obj = mMediaPlayer;
        msg.arg1 = mCurrentPosition; //
        Log.d(TAG, "리스트로 넘김 : " + String.valueOf(mCurrentPosition));
        try {
            mMPMessengerToList.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPaused = false;

    public static void pause(MediaPlayer mp) {
        mp.pause();
        isPaused = true;
    }

    public static void restart(MediaPlayer mp) {
        mp.start();
        isPaused = false;
    }




    private void showNotification() {
        CharSequence text = getText(R.string.local_service_started);

        Notification notification = new Notification(android.R.drawable.ic_media_pause, text, System.currentTimeMillis());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SolMusicPlayerActivity.class), 0);
        notification.setLatestEventInfo(this, getText(R.string.local_service_started), text, contentIntent);

        mNM.notify(NOTIFICATION, notification);
    }

    private void playForegroundNotification() {
        String songName = "test song name";
        // assign the song name to songName
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SolMusicPlayerActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification();
        notification.tickerText = "tickerText";
        notification.icon = R.drawable.ic_plusone_standard_off_client;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample",
                "Playing: " + songName, pi);
        startForeground(NOTIFICATION_ID, notification);

    }

}
