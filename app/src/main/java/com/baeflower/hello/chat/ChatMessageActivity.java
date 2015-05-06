package com.baeflower.hello.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.baeflower.hello.R;
import com.baeflower.hello.chat.adapter.ChatMessageAdapter;
import com.baeflower.hello.chat.client.ChatClient;
import com.baeflower.hello.chat.handler.ChatMessageHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ChatMessageActivity extends ActionBarActivity implements View.OnClickListener {

    private Button mBtnMessageSend;
    private EditText mMessageEditText;
    private ListView mLvMessageList;

    private ChatClient mChatClient;

    private ChatMessageAdapter mChatMessageAdapter;
        
    private ArrayList<String> mMessageList;
    private ChatMessageHandler mChatMessageHandler;

    private MessageHandler mMessageHandler;

    private void init() {
        mMessageEditText = (EditText)findViewById(R.id.et_message);
        mBtnMessageSend = (Button) findViewById(R.id.btn_send);
        mLvMessageList = (ListView) findViewById(R.id.lv_chat_message_list);

        mMessageList = new ArrayList<>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        init();
        mBtnMessageSend.setOnClickListener(this);

        // handler
        mMessageHandler = new MessageHandler(this);
        connectServer();

        // adapter
        mChatMessageAdapter = new ChatMessageAdapter(getApplicationContext(), mMessageList);

        // view
        mLvMessageList.setAdapter(mChatMessageAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();

        mChatClient.stopChat();
    }

    private void connectServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mChatClient = new ChatClient(mMessageHandler);
                mChatClient.connect();
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String message = mMessageEditText.getText().toString();
                mChatClient.sendMessage(message);
                mMessageEditText.setText(null);
                break;
        }
    }

    public ArrayList<String> getmMessageList() {
        return mMessageList;
    }
    public ChatMessageAdapter getmChatMessageAdapter() {
        return mChatMessageAdapter;
    }

    public static class MessageHandler extends Handler {

        private static final int GET_CONNECTED_CLIENT = 0;
        private static final int GET_MESSAGE = 1;
        private final WeakReference<ChatMessageActivity> mWeakRefeerence;
        private ChatMessageActivity mChatMessageActivity;
        private String mReceiveMessage;
        // private ArrayList<String> mMessageList;

        public MessageHandler(ChatMessageActivity activity) {
            mWeakRefeerence = new WeakReference<>(activity);

            // mMessageList = new ArrayList<>();
            mChatMessageActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case GET_CONNECTED_CLIENT:

                    break;
                case GET_MESSAGE:
                    mReceiveMessage = (String) msg.obj;
                    mChatMessageActivity.getmMessageList().add(mReceiveMessage);
                    mChatMessageActivity.getmChatMessageAdapter().notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

}
