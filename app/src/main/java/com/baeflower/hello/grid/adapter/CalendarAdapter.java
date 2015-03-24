package com.baeflower.hello.grid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baeflower.hello.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sol on 2015-03-23.
 */
public class CalendarAdapter extends BaseAdapter{

    public static final String TAG = CalendarAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private Integer[] dayArr;
    private ViewHolder viewHolder;
    private Context mContext;

    private int mSelectedPosition;
    private boolean selectedFlag;


    private HashMap<String, ArrayList<String>> mMapTodoData;
    private String[] mKeyArr;


    static class ViewHolder {
        TextView date;
        TextView todo;
    }

    public CalendarAdapter(Context c , Integer[] dayArr){
        this.mContext = c;
        // this.inflater = LayoutInflater.from(c);
        this.dayArr = dayArr;
        selectedFlag = false;
        mSelectedPosition = -1;
        mMapTodoData = new HashMap<>();
        mKeyArr = new String[35];
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
            // 내가 만든 레이아웃을 참조하려면 R을 참조해야된다
            // R을 참조하기 위해서는 Context가 필요
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
        if (getmKeyArr(position) != null){
            String key = getmKeyArr(position);
            ArrayList<String> dataList = mMapTodoData.get(key);
            if (dataList != null && dataList.size() != 0) {
                viewHolder.todo.setVisibility(View.VISIBLE);
            } else {
                viewHolder.todo.setVisibility(View.GONE);
            }
        } else {
            viewHolder.todo.setVisibility(View.GONE);
        }

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



    // mMapTodoData set 메서드 (리스트에 데이터만  추가)
    public void setmMapTodoData(String key, String inputText) {
        mMapTodoData.get(key).add(inputText);
    }
    // mMapTodoData set 메서드 (맵에 리스트 추가)
    public void setmMapTodoData(String key, ArrayList<String> inputList) {
        mMapTodoData.put(key, inputList);
    }
    // mMapTodoData get 메서드
    public HashMap<String, ArrayList<String>> getmMapDataDialog() {
        return mMapTodoData;
    }



    // mKeyArr set 메서드
    public void setmKeyArr(int position, String key) {
        mKeyArr[position] = key;
    }
    // 달 바뀔때 마다 생성된 keyArr 데이터 세팅
    public void setmKeyArr(String[] keyArr){
        this.mKeyArr = keyArr;
    }
    // mKeyArr get 메서드
    public String getmKeyArr(int position) {
        if (mKeyArr.length != 0){
            return mKeyArr[position];
        } else {
            return null;
        }
    }

    // 날짜에 해당하는 데이터 리스트
    public ArrayList<String> getTodoList(int position){
        return mMapTodoData.get(mKeyArr[position]);
    }

}
