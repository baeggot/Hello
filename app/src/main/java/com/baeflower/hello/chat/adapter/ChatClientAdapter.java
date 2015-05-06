package com.baeflower.hello.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.baeflower.hello.R;

import java.util.ArrayList;

/**
 * Created by sol on 2015-04-17.
 */
public class ChatClientAdapter extends BaseAdapter {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<String> mClientList;
    private boolean[] isCheckedConfirm;

    class ViewHolder {
        private CheckBox cBox;
    }

    public ChatClientAdapter (Context c , ArrayList<String> mList){
        inflater = LayoutInflater.from(c);
        mClientList = mList;

        // ArrayList Size 만큼의 boolean 배열을 만든다. CheckBox의 true/false를 구별 하기 위해
        isCheckedConfirm = new boolean[mClientList.size()];
    }

    // CheckBox를 모두 선택하는 메서드
    public void setAllChecked(boolean isCheked) {
        int tempSize = isCheckedConfirm.length;
        for(int i = 0; i < tempSize ; i++){
            isCheckedConfirm[i] = isCheked;
        }
    }

    public void setChecked(int position) {
        isCheckedConfirm[position] = !isCheckedConfirm[position];
    }

    // check 된 index 리스트를 리턴
    public ArrayList<Integer> getChecked(){
        int tempSize = isCheckedConfirm.length;
        ArrayList<Integer> mArrayList = new ArrayList<>();
        for(int i = 0; i < tempSize ; i++){
            if(isCheckedConfirm[i]){
                mArrayList.add(i);
            }
        }
        return mArrayList;
    }

    @Override
    public int getCount() {
        return mClientList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ConvertView가 null 일 경우
        View view = convertView;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.chat_client_item, null);
            viewHolder.cBox = (CheckBox) view.findViewById(R.id.cb_chat_check);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // CheckBox는 기본적으로 이벤트를 가지고 있기 때문에 ListView의 아이템 클릭리스너를 사용하기 위해서는 CheckBox의 이벤트를 없애 주어야 한다.
        viewHolder.cBox.setClickable(false);
        viewHolder.cBox.setFocusable(false);

        viewHolder.cBox.setText(mClientList.get(position));
        // isCheckedConfirm 배열은 초기화시 모두 false로 초기화 되기때문에 기본적으로 false로 초기화 시킬 수 있다.
        viewHolder.cBox.setChecked(isCheckedConfirm[position]);

        return view;
    }
}
