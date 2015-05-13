
package com.baeflower.hello.multimedia.audio;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baeflower.hello.R;
import com.baeflower.hello.multimedia.audio.adapter.MusicListAdapter;
import com.baeflower.hello.multimedia.audio.service.SolMusicMessengerService;
import com.baeflower.hello.multimedia.audio.util.Mp3AlbumImageUtil;

import java.util.concurrent.TimeUnit;

public class SolMusicSongListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = SolMusicSongListActivity.class.getSimpleName();

    private static final String MEDIA_PATH = new String("/sdcard/");

    private static final int PLAY_MUSIC = 0;
    private String[][] mSongArr;

    private static int mCurrentPosition = -1;

    private ListView mLvMusicList;
    private MusicListAdapter mMusicListAdapter;
    private Cursor mCursor;

    private static MediaPlayer mMediaPlayer;
    private static final int MSG_GET_MP_IN_LIST = 3;

    private LinearLayout mLlMiniMiniPlayer;
    private ImageView mIvAlbum;
    private TextView mTvSongTitle;
    private TextView mTvMiniPlayerStartTime;
    private TextView mTvMiniPlayerFinalTime;

    private SeekBar mSbMiniPlayer;
    private Button mBtnPrevious;
    private Button mBtnPlay;
    private Button mBtnNext;


    private void assignViews() {

        mLvMusicList = (ListView) findViewById(R.id.lv_sol_musicPlayer_music_list);

        mLlMiniMiniPlayer = (LinearLayout) findViewById(R.id.ll_mini_music_player);
        mIvAlbum = (ImageView) findViewById(R.id.iv_album_in_list);
        mTvSongTitle = (TextView) findViewById(R.id.tv_mini_music_player_song_title);
        mTvMiniPlayerStartTime = (TextView) findViewById(R.id.tv_mini_music_player_init_time_left);
        mTvMiniPlayerFinalTime = (TextView) findViewById(R.id.tv_mini_music_player_init_time_right);

        mSbMiniPlayer = (SeekBar) findViewById(R.id.sb_mini_music_player);
        mBtnPrevious = (Button) findViewById(R.id.btn_previous_song_in_list);
        mBtnPlay = (Button) findViewById(R.id.btn_play_song_in_list);
        mBtnNext = (Button) findViewById(R.id.btn_next_song_in_list);

        mBtnPrevious.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sol_music_player_list);

        assignViews();

        //updateSongList();
        // mSongList = getArtists();

        songSetting();

        mMusicListAdapter = new MusicListAdapter(getApplicationContext(), mSongArr, mCurrentPosition);
        mLvMusicList.setAdapter(mMusicListAdapter);

        mLvMusicList.setOnItemClickListener(this);

        // seekbar 에 이벤트 핸들러 연결
        mSbMiniPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        Log.d(TAG, String.valueOf(isMp3PlayerServiceRunning()));
        if (isMp3PlayerServiceRunning()) {
            firstSetting = true;
            setMiniPlayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBoundMessenger) {
            unbindService(mServiceConnection);
            mBoundMessenger = false;
        }
    }

    /*
        리스트 아이템 클릭
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentPosition = position;
        refreshSongListView();

        long contentId = Long.parseLong(mSongArr[position][0]);
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentId);

        Intent intent = new Intent(getApplicationContext(), SolMusicPlayer02Activity.class);
        intent.putExtra("uri", contentUri); // 선택된 노래의 Uri
        intent.putExtra("currentPosition", mCurrentPosition); // 현재 노래의 위치

        for(int i = 0; i < mSongArr.length; i++) {
            String key = "song_" + i;
            intent.putExtra(key, mSongArr[i]);
        }
        intent.putExtra("totalSize", mSongArr.length);

        // startActivity(intent);
        startActivityForResult(intent, PLAY_MUSIC);

        /*
            long id = // 어디선가 아이디를 가져와서 설정
            Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        if (requestCode == PLAY_MUSIC && resultCode == RESULT_OK){
            // refreshSongListView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous_song_in_list: // 이전 곡
                break;
            case R.id.btn_next_song_in_list: // 다음 곡
                break;
            case R.id.btn_play_song_in_list: // 재생, 정지
                if (mMediaPlayer.isPlaying()) {
                    pause();
                } else {
                    restart();
                }
                break;
        }
    }

    private void songSetting() {
        // query는 동기 (느려질 수 있음)
        // loader는 비동기 처리

        String[] pro = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.MediaColumns.DATA
        };

        String selection = MediaStore.Audio.Media.DATA + " like ? OR " + MediaStore.Audio.Media.DATA + " like ? "; // where 조건
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , pro, selection, new String[] {"%mp3","%wav"}, sortOrder);

        Bitmap noImage = BitmapFactory.decodeResource(getResources(), R.drawable.giraffe_1);

        if (mCursor != null) {
            mSongArr = new String[mCursor.getCount()][8];
            mCursor.moveToFirst();

            for (int i = 0; i < mCursor.getCount(); i++) {
                mCursor.moveToPosition(i);
                mSongArr[i][0] = mCursor.getString(0);
                mSongArr[i][1] = mCursor.getString(1);
                mSongArr[i][2] = mCursor.getString(2);
                mSongArr[i][3] = mCursor.getString(3);
                mSongArr[i][4] = mCursor.getString(4);
                mSongArr[i][5] = mCursor.getString(5);
                mSongArr[i][6] = mCursor.getString(6);
                mSongArr[i][7] = mCursor.getString(7);

                /*
                PlayActivity.mTitleList.add(mSongList[i][2]);
                PlayActivity.mArtistList.add(mSongList[i][1]);

                getArtworkQuick(MainActivity.this, Integer.parseInt(MainActivity.mSongList[i][6]), 200, 200);

                if (mAlbumArtImageNo) {
                    mBitmapArray.add(noImage);
                } else {
                    mBitmapArray.add(b);
                }
                */
            }
        }
    }

    private void setMiniPlayer() {
        Intent service = new Intent(getApplicationContext(), SolMusicMessengerService.class);
        service.putExtra("activity", SolMusicSongListActivity.class.getSimpleName());
        bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);

        mMusicHandler.postDelayed(mMiniMusicRunnable, 100);
    }

    private Messenger mMsgOfListAndService;
    private boolean mBoundMessenger;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");

            mMsgOfListAndService = new Messenger(service);
            mBoundMessenger = true;

            // mp 정보 가져오기
            Message msg = Message.obtain(null, SolMusicMessengerService.MSG_GET_MP_IN_LIST, 0, 0);
            try {
                mMsgOfListAndService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");

            mMsgOfListAndService = null;
            mBoundMessenger = false;
        }
    };

    private boolean firstSetting = true;

    private int startTime;
    private int finalTime;

    // static final 이었는데 static 뺌
    private final Runnable mMiniMusicRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                if (mMediaPlayer != null) {

                    startTime = mMediaPlayer.getCurrentPosition();
                    mTvMiniPlayerStartTime.setText(String.format(
                                    "%2d : %2d"
                                    , TimeUnit.MILLISECONDS.toMinutes((long) startTime)
                                    , TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    if (firstSetting == true) {
                        setMiniMusicUI();

                        finalTime = mMediaPlayer.getDuration();
                        mSbMiniPlayer.setMax(finalTime); // progress bar 최대값 설정

                        mTvMiniPlayerFinalTime.setText(String.format("%2d : %2d",
                                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                        toMinutes((long) finalTime)))
                        );
                        firstSetting = false;
                    }

                    mSbMiniPlayer.setProgress(startTime);
                    mMusicHandler.postDelayed(mMiniMusicRunnable, 100);
                }
            } catch (IllegalStateException e) {
                Log.d(TAG, "IllegalStateException");
            }
        }
    };

    private final MusicHandler mMusicHandler = new MusicHandler();

    public static class MusicHandler extends Handler {

        public MusicHandler() {
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_GET_MP_IN_LIST: // service 에서 재생중인 media player 정보 받기
                    mMediaPlayer = (MediaPlayer) msg.obj;
                    mCurrentPosition = msg.arg1;
                    // mSongPositionInList = msg.arg2;
                    Log.d(TAG, "MediaPlayer_MSG_받음 in list");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    }

    /*
        MP3 player 서비스가 재생중인지 아닌지
     */
    private boolean isMp3PlayerServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SolMusicMessengerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setMiniMusicUI() {
        Log.d(TAG, "setMiniMusicUI : " + String.valueOf(mCurrentPosition));

        String[] currentSong = mSongArr[mCurrentPosition];

        // index 6 : album id
        Bitmap albumArt = Mp3AlbumImageUtil.getArtworkQuick(getApplicationContext(), Integer.parseInt(currentSong[6]), 50, 50); // 앨범아트
        if (albumArt != null) {
            mIvAlbum.setImageBitmap(albumArt);
        } else {
            mIvAlbum.setImageResource(R.drawable.giraffe_1);
        }

        mLlMiniMiniPlayer.setVisibility(View.VISIBLE);
        mTvSongTitle.setText(currentSong[2]); // index 2 : title

        if (!mMediaPlayer.isPlaying()) {
            mBtnPlay.setText("재생");
        }

    }

    private void refreshSongListView() {
        mMusicListAdapter.setmCurrentPosition(mCurrentPosition);
        mMusicListAdapter.notifyDataSetChanged();
    }


    private void pause() {
        SolMusicMessengerService.pause(mMediaPlayer);
        mBtnPlay.setText("재생");
    }

    private void restart() {
        SolMusicMessengerService.restart(mMediaPlayer);
        mBtnPlay.setText("중지");
    }

}
