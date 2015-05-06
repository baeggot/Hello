
package com.baeflower.hello.multimedia;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service {

    // 플레이어
    private MediaPlayer mMediaPlayer;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        try {
            if (intent == null) {
                return super.onStartCommand(intent, flags, startId);
            }
            Uri fileUri = intent.getParcelableExtra("fileUri");

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(MusicService.this, fileUri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    // activity와 서비스를 연결 시킬 때 쓰는 것 (붙였다가 뗐다가?)
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
