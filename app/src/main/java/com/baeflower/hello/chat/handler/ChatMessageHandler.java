
package com.baeflower.hello.chat.handler;

import android.os.Handler;
import android.os.Message;

import com.baeflower.hello.chat.ChatMessageActivity;
import com.baeflower.hello.chat.adapter.ChatMessageAdapter;
import com.baeflower.hello.chat.util.ChatConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sol on 2015-04-20.
 */
public class ChatMessageHandler extends Handler {

    private ChatMessageActivity mChatMessageActivity;
    private String mReceiveMessage;
    private ArrayList<String> mMessageList;
    private ChatMessageAdapter mChatMessageAdapter;

    public ChatMessageHandler() {
    }

    public ChatMessageHandler(ChatMessageActivity chatMessageActivity) {
        this.mChatMessageActivity = chatMessageActivity;
    }

    public ChatMessageHandler(ArrayList<String> mMessageList, ChatMessageAdapter mChatMessageAdapter) {
        this.mMessageList = mMessageList;
        this.mChatMessageAdapter = mChatMessageAdapter;
    }

    public List<String> getmMessageList() {
        return mMessageList;
    }
    public void setmMessageList(ArrayList<String> mMessageList) {
        this.mMessageList = mMessageList;
    }

    public ChatMessageAdapter getmChatMessageAdapter() {
        return mChatMessageAdapter;
    }

    public void setmChatMessageAdapter(ChatMessageAdapter mChatMessageAdapter) {
        this.mChatMessageAdapter = mChatMessageAdapter;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case ChatConstants.GET_CONNECTED_CLIENT:

                break;
            case ChatConstants.GET_MESSAGE:
                mReceiveMessage = (String) msg.obj;
                mChatMessageActivity.getmMessageList().add(mReceiveMessage);
                mChatMessageActivity.getmChatMessageAdapter().notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

}
