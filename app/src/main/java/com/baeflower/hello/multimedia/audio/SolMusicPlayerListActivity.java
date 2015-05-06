
package com.baeflower.hello.multimedia.audio;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baeflower.hello.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class SolMusicPlayerListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static final String MEDIA_PATH = new String("/sdcard/");
    private static final String TAG = SolMusicPlayerListActivity.class.getSimpleName();
    private String[][] mSongArr;
    private List<String> mSongList = new ArrayList<>();
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    private int mCurrentPosition = 0;


    private ListView mLvMusicList;
    private MusicListAdapter mMusicListAdapter;
    private Cursor mCursor;

    private void assignViews() {
        mLvMusicList = (ListView) findViewById(R.id.lv_sol_musicPlayer_music_list);

        mSongList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sol_music_player_list);

        assignViews();

        //updateSongList();
        // mSongList = getArtists();

        songSetting();

        mMusicListAdapter = new MusicListAdapter(getApplicationContext(), mSongArr);
        mLvMusicList.setAdapter(mMusicListAdapter);

        mLvMusicList.setOnItemClickListener(this);

    }

    /*
        리스트 아이템 클릭
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentPosition = position;

        long contentId = Long.parseLong(mSongArr[position][0]);
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentId);

        Intent intent = new Intent(getApplicationContext(), SolMusicPlayerActivity.class);
        intent.putExtra("uri", contentUri); // 선택된 노래의 Uri
        intent.putExtra("currentPosition", mCurrentPosition); // 현재 노래의 위치

        for(int i = 0; i < mSongArr.length; i++) {
            String key = "song_" + i;
            intent.putExtra(key, mSongArr[i]);
        }
        intent.putExtra("totalSize", mSongArr.length);

        startActivity(intent);

        /*
            long id = // 어디선가 아이디를 가져와서 설정
            Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
         */
    }



    class Mp3Filter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3"));
        }
    }

    public List<String> getArtists() {
        List<String> list = new ArrayList<>();
        String[] cursorColumns = new String[] {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST
        };
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Artists. EXTERNAL_CONTENT_URI, cursorColumns, null, null, null);

        if (cursor == null) {
            return list;
        }

        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            do
            {
                String artist = cursor.getString(artistColumn);
                list.add(artist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
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


}
