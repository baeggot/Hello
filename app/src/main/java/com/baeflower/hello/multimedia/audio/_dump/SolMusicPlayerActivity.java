
package com.baeflower.hello.multimedia.audio._dump;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.baeflower.hello.location.SolMusicMPHandler;
import com.baeflower.hello.multimedia.audio.service.SolMusicBindService;
import com.baeflower.hello.multimedia.audio.service.SolMusicMessengerService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SolMusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SolMusicPlayerActivity.class.getSimpleName();
    private static final int SONG_COLUMN_SIZE = 8;

    private static final int MSG_HELLO_TEST = 0;
    private static final int MSG_GET_MP = 1;

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
    public static int oneTimeOnly = 0;

    private int mCurrentPosition = 0;
    private String[][] mSongArr;
    private String[] mPlaySongInfo;

    private SolMusicBindService mSolMusicBindService;
    private boolean mBound = false;
    private Uri mMusicUri;

    private SolMusicMPHandler mSolMusicMPHandler;


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

        mSolMusicMPHandler = new SolMusicMPHandler();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Log.d(TAG, String.valueOf(mCurrentPosition)); // 리스트에서의 노래 위치
        Log.d(TAG, mMusicUri.toString()); // song uri
        Log.d(TAG, mPlaySongInfo[2]); // title


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

        /*
            // 방법 1. activity 내에서 재생
            playSong(mMusicUri);
         */

        /*
            // 방법2. 서비스에서 재생 (startService 사용)

        Intent service = new Intent(getApplicationContext(), SolMusicService.class);
        service.putExtra("musicUri", mMusicUri);
        startService(service);
        */
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        /*
            // 방법 3. 서비스 binding (LPC : Local...)

            Intent service = new Intent(getApplicationContext(), SolMusicBindService.class);
            service.putExtra("musicUri", mMusicUri);
            bindService(service, mConnectionLocal, Context.BIND_AUTO_CREATE);
        */

        /*
            // 방법 4. 서비스 binding (IPC???????)
        */

        Intent serviceIPC = new Intent(getApplicationContext(), SolMusicMessengerService.class);
        serviceIPC.putExtra("musicUri", mMusicUri);
        startService(serviceIPC);
        bindService(serviceIPC, mConnectionMessenger, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection mConnectionLocal = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SolMusicBindService.LocalBinder binder = (SolMusicBindService.LocalBinder) service;
            mSolMusicBindService = binder.getService();
            mBound = true;
            setMusicLocalUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    // Messenger for communicating with the service
    private Messenger mServiceMessenger;
    // Flag indicating whether we have called bind on the service
    private boolean mBoundMessenger;


    private ServiceConnection mConnectionMessenger = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");

            mServiceMessenger = new Messenger(service);
            mBoundMessenger = true;
            setMusicIPCUI();


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mServiceMessenger = null;
            mBoundMessenger = false;
        }
    };

//    public void sayHello() {
//        if (mBoundMessenger == false) return;
//
//        // Create and send a message to the service, using a supported 'what' value
//        Message msg = Message.obtain(null, SolMusicMessengerService.MSG_HELLO_TEST, 0, 0);
//        try {
//            msg.obj = "hello";
//            mServiceMessenger.send(msg);
//        } catch (RemoteException e) {
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void setMusicIPCUI() {

        mTvMusicPlayerSongName.setText(mPlaySongInfo[2]); // 노래 제목

        Bitmap albumArt = getArtworkQuick(getApplicationContext(), Integer.parseInt(mPlaySongInfo[6]), 50, 50);
        if (albumArt != null) {
            mIvMusicPlayerPhoto.setImageBitmap(albumArt);
        }

        if (mMediaPlayer != null) {

            finalTime = mMediaPlayer.getDuration();
            startTime = mMediaPlayer.getCurrentPosition();

            if (oneTimeOnly == 0) {
                mSbMusicTimeBar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }

            mTvStartTime.setText(String.format("%2d : %2d",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            mTvEndTime.setText(String.format("%2d : %2d",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) finalTime)))
            );

            mSbMusicTimeBar.setProgress((int) startTime);
            /*
                @param r The Runnable that will be executed.
                @param delayMillis The delay (in milliseconds) until the Runnable will be executed.
             */
            mHandler.postDelayed(musicRunnable, 100);
        }
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

        /*
        if (mBound) {
            unbindService(mConnectionLocal);
            mBound = false;
        }
        */

        // unbind from the IPC service
        if (mBoundMessenger) {
            unbindService(mConnectionMessenger);
            mBoundMessenger = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_music_player_play:
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mIbMusicPlayerPlay.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    // 다시 재생
                    restart();
                    mIbMusicPlayerPlay.setImageResource(android.R.drawable.ic_media_pause);
                }
                break;
            case R.id.ib_music_player_forward:
                // forward();
                break;
            case R.id.ib_music_player_back:
                // rewind();
                break;
            case R.id.ib_music_player_back_to_list:
                break;
        }
    }

    // 앨범 이미지 가져오는 메서드 (이해가 안가네?)
    private Bitmap getArtworkQuick(Context context, int album_id, int w, int h) {

        /*
           // 예제1. 안되는데?
           final Uri ArtworkUri = Uri.parse("content://media/external/audio/albumart");
           Uri uri = ContentUris.withAppendedId(ArtworkUri, album_id); // Long.parseLong(ref.getMediaId())
           return uri;
         */

        /*
         * 예제 2. 여기서는 bitmap을 넘김
         */

        BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
        Uri artworkUri = Uri.parse("content://media/external/audio/albumart");

        w -= 2;
        h -= 2;
        Uri uri = ContentUris.withAppendedId(artworkUri, album_id);
        if (uri != null) {
            ParcelFileDescriptor fd = null;
            try {
                fd = context.getContentResolver().openFileDescriptor(uri, "r");
                int sampleSize = 1;

                // Compute the closest power-of-two scale factor
                // and pass that to sBitmapOptionsCache.inSampleSize, which will
                // result in faster decoding and better quality
                sBitmapOptionsCache.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null,
                        sBitmapOptionsCache);
                int nextWidth = sBitmapOptionsCache.outWidth >> 1;
                int nextHeight = sBitmapOptionsCache.outHeight >> 1;
                while (nextWidth > w && nextHeight > h) {
                    sampleSize <<= 1;
                    nextWidth >>= 1;
                    nextHeight >>= 1;
                }

                sBitmapOptionsCache.inSampleSize = sampleSize;
                sBitmapOptionsCache.inJustDecodeBounds = false;
                Bitmap b = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null,
                        sBitmapOptionsCache);

                if (b != null) {
                    // finally rescale to exactly the size we need
                    if (sBitmapOptionsCache.outWidth != w || sBitmapOptionsCache.outHeight != h) {
                        Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
                        b.recycle();
                        b = tmp;
                    }
                }
                return b;
            } catch (FileNotFoundException e) {
            } finally {
                try {
                    if (fd != null)
                        fd.close();
                } catch (IOException e) {
                }
            }
        }
        return null;

    }



    // 노래 재생
    private void playSong(Uri uri) {
        mTvMusicPlayerSongName.setText(mPlaySongInfo[2]); // 노래 제목

        Bitmap albumArt = getArtworkQuick(getApplicationContext(),Integer.parseInt(mPlaySongInfo[6]), 50, 50);
        if (albumArt != null) {
            mIvMusicPlayerPhoto.setImageBitmap(albumArt);
        }

        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(SolMusicPlayerActivity.this, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


        finalTime = mMediaPlayer.getDuration();
        startTime = mMediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            mSbMusicTimeBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        mTvStartTime.setText(String.format("%2d : %2d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
        );
        mTvEndTime.setText(String.format("%2d : %2d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) finalTime)))
        );


        mSbMusicTimeBar.setProgress((int) startTime);
        mHandler.postDelayed(musicRunnable, 100);
    }


    private void setMusicLocalUI() {

        mTvMusicPlayerSongName.setText(mPlaySongInfo[2]); // 노래 제목

        Bitmap albumArt = getArtworkQuick(getApplicationContext(),Integer.parseInt(mPlaySongInfo[6]), 50, 50);
        if (albumArt != null) {
            mIvMusicPlayerPhoto.setImageBitmap(albumArt);
        }

        if (mSolMusicBindService != null) {
            mMediaPlayer = mSolMusicBindService.getmMediaPlayer();

            if (mMediaPlayer != null) {

                finalTime = mSolMusicBindService.getmMediaPlayer().getDuration();
                startTime = mSolMusicBindService.getmMediaPlayer().getCurrentPosition();

                if (oneTimeOnly == 0) {
                    mSbMusicTimeBar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                mTvStartTime.setText(String.format("%2d : %2d",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) startTime)))
                        );
                mTvEndTime.setText(String.format("%2d : %2d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) finalTime)))
                        );

                mSbMusicTimeBar.setProgress((int) startTime);
                /*
                    @param r The Runnable that will be executed.
                    @param delayMillis The delay (in milliseconds) until the Runnable will be executed.
                 */
                mHandler.postDelayed(musicRunnable, 100);
            }
        }
    }

    private final MusicHandler mHandler = new MusicHandler();

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
            Log.d(TAG, msg.obj.toString());

            switch (msg.what) {
                case MSG_GET_MP:
                    mMediaPlayer = (MediaPlayer) msg.obj;
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private boolean mIsCompleted;

    // static final 이었는데 static 뺌
    private final Runnable musicRunnable = new Runnable() {
        @Override
        public void run() {

//            mIsCompleted = mSolMusicBindService.ismIsCompleted();
//
//            if (mIsCompleted == true) { // 다음 노래 재생
//
//            }

            try {
                if (mMediaPlayer.isPlaying()) {
                    startTime = mMediaPlayer.getCurrentPosition();
                    mTvStartTime.setText(String.format(
                                    "%2d : %2d"
                                    , TimeUnit.MILLISECONDS.toMinutes((long) startTime)
                                    , TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );
                    mSbMusicTimeBar.setProgress((int) startTime);
                    mHandler.postDelayed(musicRunnable, 100);
                }
            } catch (IllegalStateException e) {
            }

        }
    };

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mMediaPlayer.getCurrentPosition();
            mTvStartTime.setText(String.format(
                    "%2d : %2d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime)
                    ,
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                                    .toMinutes((long) startTime)))
                    );
            mSbMusicTimeBar.setProgress((int) startTime);
            mHandler.postDelayed(this, 100);
        }
    };

    private void nextSong() {
        mCurrentPosition++;
        Log.d(TAG, "다음곡 위치 : " + String.valueOf(mCurrentPosition));

        if (mCurrentPosition >= mSongArr.length) {
            mCurrentPosition = 0;
        } else {// 다음 곡 재생
            mPlaySongInfo = mSongArr[mCurrentPosition];

            long contentId = Long.parseLong(mPlaySongInfo[0]);
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentId);



            Toast.makeText(getApplicationContext(), "다음 곡 재생", Toast.LENGTH_SHORT).show();
        }
    }

    private void restart() {
        Toast.makeText(getApplicationContext(), "다시시작", Toast.LENGTH_SHORT).show();
        mMediaPlayer.start();
    }

    private void pause() {
        Toast.makeText(getApplicationContext(), "일시정지", Toast.LENGTH_SHORT).show();
        mMediaPlayer.pause();
    }

    private void stop() {
        mMediaPlayer.stop();

        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mMediaPlayer.seekTo(0);
        mSbMusicTimeBar.setProgress(0);
    }

    private void Thread() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // while 문을 돌려서 음악이 실행중일 때 계속 돌아가게 한다
                while (mMediaPlayer.isPlaying()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                mSbMusicTimeBar.setProgress(mMediaPlayer.getCurrentPosition()); // 현재재생되고있는위치
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void forward() {
        int temp = (int) startTime;
        if ((temp + forwardTime) <= finalTime) {
            startTime = startTime + forwardTime;
            mMediaPlayer.seekTo((int) startTime);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void rewind() {
        int temp = (int) startTime;
        if ((temp - backwardTime) > 0) {
            startTime = startTime - backwardTime;
            mMediaPlayer.seekTo((int) startTime);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
