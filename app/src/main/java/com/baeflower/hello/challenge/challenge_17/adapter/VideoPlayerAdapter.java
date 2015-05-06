
package com.baeflower.hello.challenge.challenge_17.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baeflower.hello.R;
import com.baeflower.hello.challenge.challenge_17.VideoData;

import java.util.List;

/**
 * Created by sol on 2015-04-28.
 */
public class VideoPlayerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ViewHolder viewHolder;

    private List<VideoData> mVideoList;

    static class ViewHolder {
        ImageView image;
        TextView title;
        TextView date;
    }

    public VideoPlayerAdapter(Context context, List<VideoData> videoDataList) {
        mContext = context;
        mVideoList = videoDataList;
    }

    @Override
    public int getCount() {
        return mVideoList.size();
    }

    @Override
    public VideoData getItem(int position) {
        return mVideoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mVideoList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_item_video_player, null);
            ImageView image = (ImageView) view.findViewById(R.id.iv_video_player_thumbnail);
            TextView title = (TextView) view.findViewById(R.id.tv_video_player_title);
            TextView date = (TextView) view.findViewById(R.id.tv_video_player_date);

            viewHolder = new ViewHolder();
            viewHolder.image = image;
            viewHolder.title = title;
            viewHolder.date = date;

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        /*
         * // 비트맵 이미지 가져오는걸 getView에서... 스레드 돌려서 해야될듯 if(p != null) { bmThumb =
         * ThumbnailUtils.createVideoThumbnail(p.getFilePath(),
         * MediaStore.Video.Thumbnails.MINI_KIND); Log.d("adapter",
         * p.getFilePath()); img.setImageBitmap(bmThumb);
         * txt.setText(p.getFileName()); }
         */
        viewHolder.image.setImageBitmap(getVideoThumbnail(getItemId(position)));

        viewHolder.title.setText(getItem(position).getTitle());
        viewHolder.date.setText(getItem(position).getDateAdded());

        return view;
    }

    private Bitmap getVideoThumbnail(long id) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        // MICRO_KIND : 작은이미지(정사각형)
        // MINI_KIND :중간 이미지

        Bitmap videoThumbnail = MediaStore.Video.Thumbnails.getThumbnail(
                mContext.getContentResolver(), id,
                MediaStore.Video.Thumbnails.MICRO_KIND, options);

        options = null;

        if (videoThumbnail != null) {
            return videoThumbnail;
        } else {
            // Bitmap noImage = BitmapFactory.decodeResource(getResources(),
            // R.drawable.giraffe_1);
            return null;
        }
    }
}
