
package com.baeflower.hello.challenge.challenge_17;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.baeflower.hello.R;

public class VideoPlayActivity extends AppCompatActivity{

    private VideoView mVideoView;
    private MediaPlayer mMediaPlayer;
    private boolean mFirst;

    private Uri mVideoUri;

    private void init() {
        mVideoView = (VideoView) findViewById(R.id.vv_video_play);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        init();

        Intent intent = getIntent();

        mVideoUri = intent.getParcelableExtra("videoUri");

        //
        MediaController controller = new MediaController(this);
        mVideoView.setMediaController(controller);

        mVideoView.setVideoURI(mVideoUri);
        mVideoView.start();

    }

    public void deletePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
