package com.baeflower.hello.multimedia;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.baeflower.hello.R;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int VIDEO_REQUEST_CODE = 0;
    private static final int AUDIO_REQUEST_CODE = 1;
    private Button mBtnFilePickAudio;
    private Button mBtnFilePickVideo;

    private Button mBtnPlay;
    private Button mBtnPause;
    private Button mBtnRestart;

    private VideoView mVideoView;

    // 플에이어
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        mBtnFilePickAudio = (Button) findViewById(R.id.btn_file_pick_audio);
        mBtnFilePickVideo = (Button) findViewById(R.id.btn_file_pick_video);

        mBtnPlay = (Button) findViewById(R.id.btn_media_play);
        mBtnPause = (Button) findViewById(R.id.btn_media_pause);
        mBtnRestart = (Button) findViewById(R.id.btn_media_restart);

        mVideoView = (VideoView) findViewById(R.id.vv_media_player);

        mBtnFilePickAudio.setOnClickListener(this);
        mBtnFilePickVideo.setOnClickListener(this);

        mBtnPlay.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnRestart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_file_pick_video:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*"); // MIME type 검색해바
                startActivityForResult(Intent.createChooser(intent, "파일선택..."), VIDEO_REQUEST_CODE);
                break;
            case R.id.btn_file_pick_audio:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*"); // MIME type 검색해바
                // 예) 영화를 눌렀는데 기본 플레이어로 할건지, 다른 앱으로 할건지 선택하려고 나오는 것
                // 타입을 지원하는 앱과 연결해줌
                startActivityForResult(Intent.createChooser(intent, "파일선택..."), AUDIO_REQUEST_CODE);
                break;
            case R.id.btn_media_pause:
                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                    mBtnPause.setEnabled(false); // 안눌러지게
                    mBtnRestart.setEnabled(true);
                }
                break;
            case R.id.btn_media_restart:
                if (mMediaPlayer != null) {
                    mMediaPlayer.start();

                    mBtnRestart.setEnabled(false);
                    mBtnPause.setEnabled(true);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 오디오
        if (requestCode == AUDIO_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri fileUri = data.getData();

            // activity 대신에 service를 넣는다
            Intent intent = new Intent(getApplicationContext(), MusicService.class);
            intent.putExtra("fileUri", fileUri);
            startService(intent);

            mBtnPause.setEnabled(true);
            mBtnRestart.setEnabled(true);
            mBtnFilePickAudio.setText(fileUri.getPath());

            Toast.makeText(getApplicationContext(), "Uri : " + fileUri, Toast.LENGTH_SHORT).show();

        } else if (requestCode == VIDEO_REQUEST_CODE && resultCode == RESULT_OK) { // 비디오

            Uri fileUri = data.getData();

            MediaController controller = new MediaController(this);
            mVideoView.setMediaController(controller);

            mVideoView.setVideoURI(fileUri);
            mVideoView.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) { // 폰 옆에 붙은 볼륨다운 버튼

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

        }



        return super.onKeyDown(keyCode, event);
    }
}
