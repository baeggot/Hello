package com.baeflower.hello.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baeflower.hello.R;

import java.util.ArrayList;

/**
 * Created by sol on 2015-04-17.
 */
public class ChatMessageAdapter extends BaseAdapter {

    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private Context mContext;

    private ArrayList<String> mMessageList;


    class ViewHolder {
        private TextView nickname;
        private TextView message;
    }

    public ChatMessageAdapter (Context c , ArrayList<String> messageList){
        mContext = c;
        //inflater = LayoutInflater.from(c);
        mMessageList = messageList;
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            viewHolder = new ViewHolder();
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_message_item, null);

            viewHolder.nickname = (TextView) view.findViewById(R.id.tv_nickname_message);
            viewHolder.message = (TextView) view.findViewById(R.id.tv_message);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 닉네임은 어케 하지?

        // 메세지
        viewHolder.message.setText(mMessageList.get(position));

        return view;
    }
}
