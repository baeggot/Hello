package com.baeflower.hello.parsing.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baeflower.hello.R;
import com.baeflower.hello.parsing.model.GoogleNews;

import java.util.List;

public class GoogleNewsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private Context mContext;
    private List<GoogleNews> googleNewsList;

    static class ViewHolder {
        TextView title;
        TextView link;
    }

    public GoogleNewsAdapter(Context c , List<GoogleNews> googleNewsList) {
        this.googleNewsList = googleNewsList;
        mContext = c;
    }

    @Override
    public int getCount() {
        return googleNewsList.size();
    }

    @Override
    public GoogleNews getItem(int position) {
        return googleNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null){

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.google_news_item, null);
            TextView titleView = (TextView) view.findViewById(R.id.tv_googlenews_title);

            viewHolder = new ViewHolder();
            viewHolder.title = titleView;

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        GoogleNews googleNews = getItem(position);
        viewHolder.title.setText(googleNews.getTitle());

        return view;
    }
}
