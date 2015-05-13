package com.baeflower.hello.multimedia.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baeflower.hello.R;
import com.baeflower.hello.multimedia.audio.service.SolMusicMessengerService;
import com.baeflower.hello.multimedia.audio.util.Mp3AlbumImageUtil;

import java.util.concurrent.TimeUnit;

public class SolMusicPlayer02Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SolMusicPlayer02Activity.class.getSimpleName();
    private static final int SONG_COLUMN_SIZE = 8;

    private static final int MSG_GET_MP = 1;
    private static final int MSG_NEXT_MP = 2;

    private TextView mTvMusicPlayerNowPlaying;
    private TextView mTvMusicPlayerSongName;
    private ImageView mIvMusicPlayerPhoto;
    private SeekBar mSbMusicTimeBar;
    private TextView mTvEndTime;
    private TextView mTvStartTime;

    private ImageButton mIbMusicPlayerPlay;
    private ImageButton mIbMusicPlayerPause;
    private ImageButton mIbMusicPlayerForward;
    private ImageButton mIbMusicPlayerBack;
    private ImageButton mIbMusicPlayerBackToList;

    private static MediaPlayer mMediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;

    private int mCurrentPosition = 0;
    private String[][] mSongArr;
    private String[] mPlaySongInfo;

    private Uri mMusicUri;
    private int mAlbumId;


    private void assignViews() {
        mTvMusicPlayerNowPlaying = (TextView) findViewById(R.id.tv_music_player_now_playing);
        mTvMusicPlayerSongName = (TextView) findViewById(R.id.tv_music_player_song_name);
        mIvMusicPlayerPhoto = (ImageView) findViewById(R.id.iv_music_player_photo);
        mSbMusicTimeBar = (SeekBar) findViewById(R.id.sb_music_player);
        mTvEndTime = (TextView) findViewById(R.id.tv_music_player_init_time_right);
        mTvStartTime = (TextView) findViewById(R.id.tv_music_player_init_time_left);

        mIbMusicPlayerPlay = (ImageButton) findViewById(R.id.ib_music_player_play);
        mIbMusicPlayerForward = (ImageButton) findViewById(R.id.ib_music_player_forward);
        mIbMusicPlayerBack = (ImageButton) findViewById(R.id.ib_music_player_back);
        mIbMusicPlayerBackToList = (ImageButton) findViewById(R.id.ib_music_player_back_to_list);
        // mIbMusicPlayerPause = (ImageButton)
        // findViewById(R.id.ib_music_player_pause);

        mIbMusicPlayerPlay.setOnClickListener(this);
        mIbMusicPlayerBack.setOnClickListener(this);
        mIbMusicPlayerForward.setOnClickListener(this);
        mIbMusicPlayerBackToList.setOnClickListener(this);
        // mIbMusicPlayerPause.setOnClickListener(this);

        mMediaPlayer = new MediaPlayer();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_sol_music_player);

        assignViews();

        Intent intent = getIntent();
        mMusicUri = intent.getParcelableExtra("uri");
        mCurrentPosition = intent.getIntExtra("currentPosition", 0);

        int totalSize = intent.getIntExtra("totalSize", 0);
        mSongArr = new String[totalSize][SONG_COLUMN_SIZE];

        for (int i = 0; i < totalSize; i++) {
            String key = "song_" + i;
            mSongArr[i] = intent.getStringArrayExtra(key);
        }
        mPlaySongInfo = mSongArr[mCurrentPosition];
        mAlbumId = Integer.parseInt(mPlaySongInfo[6]);

        // seekbar에 이벤트 핸들러 연결
        mSbMusicTimeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    mMediaPlayer.seekTo(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        Intent serviceIPC = new Intent(getApplicationContext(), SolMusicMessengerService.class);

        serviceIPC.putExtra("currentPosition", mCurrentPosition); // 리스트에서 현재 노래의 인덱스
        serviceIPC.putExtra("musicUri", mMusicUri);
        startService(serviceIPC);
        bindService(serviceIPC, mConnectionMessenger, Context.BIND_AUTO_CREATE);

        if (SolMusicMessengerService.isPaused == true) {
            mIbMusicPlayerPlay.setImageResource(android.R.drawable.ic_media_play);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        if (mBoundMessenger) {
            unbindService(mConnectionMessenger);
            mBoundMessenger = false;
        }

        if (musicThread != null && musicThread.isAlive()) {
            musicThread.interrupt();
        }
    }

    // Messenger for communicating with the service
    private static Messenger mServiceMessenger;
    // Flag indicating whether we have called bind on the service
    private boolean mBoundMessenger;

    private ServiceConnection mConnectionMessenger = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");

            mServiceMessenger = new Messenger(service);
            mBoundMessenger = true;
            setMusicUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");

            mServiceMessenger = null;
            mBoundMessenger = false;
        }
    };

    private void setMusicUI() {
        Log.d(TAG, "setMusicUI");

        mTvMusicPlayerSongName.setText(mPlaySongInfo[2]); // 노래 제목

        // Context, album id, width, height
        Bitmap albumArt = Mp3AlbumImageUtil.getArtworkQuick(getApplicationContext(), Integer.parseInt(mPlaySongInfo[6]), 50, 50); // 앨범아트
        if (albumArt != null) {
            mIvMusicPlayerPhoto.setImageBitmap(albumArt);
        }

        if (mMediaPlayer != null) {
            mMusicHandler.postDelayed(musicThread, 100);
        }
    }

    private final MusicHandler mMusicHandler = new MusicHandler();

    public static class MusicHandler extends Handler {
//        private final WeakReference<SolMusicPlayerActivity> mActivity;

        public MusicHandler() {
        }

//        public MusicHandler(SolMusicPlayerActivity activity) {
//            mActivity = new WeakReference<>(activity);
//        }

        @Override
        public void handleMessage(Message msg) {
//            SolMusicPlayerActivity activity = mActivity.get();
//            if (activity != null) {
//            }

            switch (msg.what) {
                case MSG_GET_MP: // service 에서 재생중인 media player 정보 받기
                    mMediaPlayer = (MediaPlayer) msg.obj;
                    Log.d(TAG, "MediaPlayer_MSG_받음");
                    Log.d(TAG, String.valueOf(mMediaPlayer.getCurrentPosition()));
//                    if (mMediaPlayer.getCurrentPosition() <= 0) { // 0이 들어올 때가 있는데 이러면 노래 재생이 안됨.. 뭐지?
//                        Message msgToService = Message.obtain(null, SolMusicMessengerService.MSG_GET_MP, 0, 0);
//                        try {
//                            mServiceMessenger.send(msgToService);
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    break;
                case MSG_NEXT_MP: // 한곡 끝났을 때 다음곡으로 바로 재생해주기
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private boolean mIsCompleted;
    private boolean firstSetting = true;

    private final Thread musicThread = new Thread(new Runnable() {
        @Override
        public void run() {
//            synchronized (mMediaPlayer) {}

            try {
                if (mMediaPlayer != null) {
                    startTime = mMediaPlayer.getCurrentPosition(); // startTime 이 0 일 때 노래 재생이 안됨

                    mTvStartTime.setText(String.format(
                                    "%2d : %2d"
                                    , TimeUnit.MILLISECONDS.toMinutes((long) startTime)
                                    , TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    if (firstSetting == true) { // 더 좋은 방법이 없을까? static 핸들러에서 media player 정보를 받고 그 다음에 실행되어야 하기 때문에..
                        Log.d(TAG, String.valueOf(startTime));

                        finalTime = mMediaPlayer.getDuration(); // 노래 종료시간
                        mSbMusicTimeBar.setMax((int) finalTime); // progress bar 최대값 설정

                        mTvEndTime.setText(String.format("%2d : %2d",
                                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                        toMinutes((long) finalTime)))
                        );
                        firstSetting = false;
                    }

                    mSbMusicTimeBar.setProgress((int) startTime);
                    mMusicHandler.postDelayed(musicThread, 100);
                }
            } catch (IllegalStateException e) {
                Log.d(TAG, "IllegalStateException");
            }
        }
    });


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_music_player_play:
                if (mMediaPlayer.isPlaying()) {
                    pause();
                } else { // 다시 재생
                    restart();
                }
                break;
            case R.id.ib_music_player_forward: // 이전 노래
                // forward();
                break;
            case R.id.ib_music_player_back: // 다음 노래
                // rewind();
                break;
            case R.id.ib_music_player_back_to_list: // 리스트로 돌아가기
                backToList();
                break;
        }
    }

    private void pause() {
        SolMusicMessengerService.pause(mMediaPlayer);
        mIbMusicPlayerPlay.setImageResource(android.R.drawable.ic_media_play);
    }

    private void restart() {
        SolMusicMessengerService.restart(mMediaPlayer);
        mIbMusicPlayerPlay.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void backToList() {
        Intent intent = new Intent();
        // intent.putExtra("currentPosition", mCurrentPosition);

        // 현재 activity가 죽고 그 전의 activity가 보임(이전 activity가 없으면 앱이 끝나나?)
        // RESULT_OK, RESULT_CANCEL, ...
        setResult(RESULT_OK, intent);
        finish();
    }

}
