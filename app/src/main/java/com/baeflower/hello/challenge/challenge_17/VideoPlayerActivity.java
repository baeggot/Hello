package com.baeflower.hello.challenge.challenge_17;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baeflower.hello.R;
import com.baeflower.hello.challenge.challenge_17.adapter.VideoPlayerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int mVideoCount;
    private List<VideoData> mVideoList;

    private VideoPlayerAdapter mVideoPlayerAdapter;

    private ListView mLvVideoList;



    private void init() {
        mVideoList = new ArrayList<>();

        mLvVideoList = (ListView) findViewById(R.id.lv_video_play_list);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        init();

        videoSetting();

        mVideoPlayerAdapter = new VideoPlayerAdapter(getApplicationContext(), mVideoList);
        mLvVideoList.setAdapter(mVideoPlayerAdapter);

        mLvVideoList.setOnItemClickListener(this);

    }

    private void videoSetting() {
        Cursor videoCursor;

        String[] pro = {

                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.VideoColumns.ALBUM
        };

        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " ASC";

        videoCursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, pro, null, null, sortOrder);

        mVideoCount = videoCursor.getCount();

        if (videoCursor != null) {
            videoCursor.moveToFirst();

            VideoData videoData;
            for (int i = 0; i < mVideoCount; i++) {
                videoCursor.moveToPosition(i);

                videoData = new VideoData();

                long id = videoCursor.getLong(0);
                videoData.setId(id);

                videoData.setData(videoCursor.getString(1));
                videoData.setDisplayName(videoCursor.getString(2));
                videoData.setSize(videoCursor.getLong(3));
                videoData.setTitle(videoCursor.getString(4));
                videoData.setDuration(videoCursor.getLong(5));

                long dateAdded = videoCursor.getLong(6);
                dateAdded = dateAdded * 1000; // second -> ms 환산
                Date date = new Date(dateAdded);

                videoData.setDateAdded(date.toString());
                videoData.setResolution(videoCursor.getString(7));

                // videoData.setThumbnail(getVideoThumbnail(id));

                int n_album = videoCursor.getColumnIndex(MediaStore.Video.VideoColumns.ALBUM);
                videoData.setAlbum(videoCursor.getString(n_album));

                mVideoList.add(videoData);
            }
        }

        videoCursor.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mVideoList.get(position).getId());

        Intent intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
        intent.putExtra("videoUri", videoUri);
        startActivity(intent);

    }


}
