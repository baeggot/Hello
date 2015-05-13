package com.baeflower.hello.multimedia.audio.util;

import android.media.MediaPlayer;

/**
 * Created by sol on 2015-05-13.
 */
public class MediaPlayerControlUtil {
    private MediaPlayer mMediaPlayer;

    public MediaPlayerControlUtil(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }


    private void previousSong() {

    }

    private void nextSong() {

    }

    private void pause() {
        // Toast.makeText(getApplicationContext(), "일시정지", Toast.LENGTH_SHORT).show();
        mMediaPlayer.pause();
    }

    private void restart() {
        // Toast.makeText(getApplicationContext(), "다시시작", Toast.LENGTH_SHORT).show();
        mMediaPlayer.start();
    }

}
