package com.baeflower.hello.multimedia.audio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baeflower.hello.R;

import java.util.List;

/**
 * Created by sol on 2015-04-27.
 */
public class MusicListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ViewHolder viewHolder;

    private List<String> mMusicList;
    private String[][] mSongArr;

    static class ViewHolder {
        TextView music;
    }

    public MusicListAdapter(Context context, List<String> musicList) {
        mContext = context;
        mMusicList = musicList;
    }

    public MusicListAdapter(Context context, String[][] songArr) {
        mContext = context;
        mSongArr = songArr;
    }

    @Override
    public int getCount() {
        return mSongArr.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            // 내가 만든 레이아웃을 참조하려면 R을 참조해야된다
            // R을 참조하기 위해서는 Context가 필요
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_item_music_player, null);
            TextView music = (TextView) view.findViewById(R.id.tv_music_player_music_item);

            viewHolder = new ViewHolder();
            viewHolder.music = music;

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // viewHolder.music.setText(mMusicList.get(position));
        viewHolder.music.setText(mSongArr[position][2]);

        return view;
    }
}
