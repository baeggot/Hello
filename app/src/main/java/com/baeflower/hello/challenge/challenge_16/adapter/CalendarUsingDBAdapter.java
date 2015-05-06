package com.baeflower.hello.challenge.challenge_16.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baeflower.hello.R;

/**
 * Created by sol on 2015-03-23.
 */
public class CalendarUsingDBAdapter extends BaseAdapter{

    public static final String TAG = CalendarUsingDBAdapter.class.getSimpleName();

    private LayoutInflater inflater;
    private Integer[] dayArr;
    private ViewHolder viewHolder;
    private Context mContext;

    private int mSelectedPosition;

    private boolean[] mDataExistChkArr;

    static class ViewHolder {
        TextView date;
        TextView todo;
    }

    public CalendarUsingDBAdapter(Context c, Integer[] dayArr, int dataExistChkArrSize){
        this.mContext = c;
        this.dayArr = dayArr;
        mSelectedPosition = -1;
        mDataExistChkArr = new boolean[dataExistChkArrSize];
    }


    @Override
    public int getCount() {
        return dayArr.length;
    }

    @Override
    public Integer getItem(int position) {
        return dayArr[position];
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

            view = inflater.inflate(R.layout.calendar_view_item, null);
            TextView dateView = (TextView) view.findViewById(R.id.calendar_textView_item);
            TextView todoView = (TextView) view.findViewById(R.id.calendar_textView_todo);

            viewHolder = new ViewHolder();
            viewHolder.date = dateView;
            viewHolder.todo = todoView;

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Integer day = getItem(position);
        String dayStr = day.toString();
        viewHolder.date.setText(dayStr);

        // 선택된 position은 yellow
        if (getmSelectedPosition() == position) {
            viewHolder.date.setBackgroundColor(Color.YELLOW);
        } else {
            viewHolder.date.setBackgroundColor(Color.WHITE);
        }

        // 현재 포지션의 view에 데이터가 있으면
        // viewHolder.todo.setVisibility(View.VISIBLE);
        // viewHolder.todo.setVisibility(View.GONE);

        return view;
    }

    // mSelectedPosition set 메서드
    public void setmSelectedPosition(int position){
        this.mSelectedPosition = position;
        notifyDataSetChanged();
    }
    // mSelectedPosition get 메서드
    public int getmSelectedPosition(){
        return this.mSelectedPosition;
    }

    public void setmDataExistChkArr(int index, boolean data) {
        mDataExistChkArr[index] = data;
    }


}
