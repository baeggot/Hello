package com.baeflower.hello.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.baeflower.hello.R;
import com.baeflower.hello.chat.adapter.ChatClientAdapter;
import com.baeflower.hello.chat.client.ChatClient;
import com.baeflower.hello.chat.handler.ChatMessageHandler;

import java.util.ArrayList;

public class ChatClientActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = ChatClientActivity.class.getSimpleName();

    // component
    // private Button mBtnChatConnect;
    private Button mBtnChatStart;

    private CheckBox mAllCheckBox = null;
    private Button mCountBt = null;


    // class
    private ChatClient mChatClient;


    // data
    private ArrayList<String> mChatClientList;
    // adapter
    private ChatClientAdapter mChatClientAdapter;

    // view
    private ListView mListView;

    private ChatMessageHandler mChatMessageHandler;



    private void init() {
        // mBtnChatConnect = (Button) findViewById(R.id.btn_connect);
        mBtnChatStart = (Button) findViewById(R.id.btn_chat_start);

        mListView = (ListView) findViewById(R.id.lv_chat_client_list);
        mChatClientList = new ArrayList<>();

        // 로컬에서 실행해 보려고

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);

        init();
        mBtnChatStart.setOnClickListener(this);

        // 서버 연결
        mChatMessageHandler = new ChatMessageHandler();
        connectServer();

        // 레이아웃 구성
        setLayout();
        mCountBt.setText("현재 체크된 인원은 0 명 입니다.");

        // data
        mChatClientList.add("첫번째");
        mChatClientList.add("두번째");
        mChatClientList.add("세번째");

        // adapter
        mChatClientAdapter = new ChatClientAdapter(ChatClientActivity.this , mChatClientList);

        // view
        mListView.setAdapter(mChatClientAdapter);
        mListView.setOnItemClickListener(mItemClickListener);

        // handler
        mChatMessageHandler = new ChatMessageHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chat_start:
                Intent intent = new Intent(getApplicationContext(), ChatMessageActivity.class);
                startActivity(intent);
                break;
        }
    }

    // ListView 안에 Item을 클릭시에 호출되는 Listener
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mChatClientAdapter.setChecked(position);
            mChatClientAdapter.notifyDataSetChanged();
        }

        /*
            @Override
            public void onItemClick(AdapterView<!--?--> arg0, View arg1, int position, long arg3) {
                Toast.makeText(getApplicationContext(), "" + (position + 1),
                        Toast.LENGTH_SHORT).show();

                mCustomAdapter.setChecked(position);
                // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                mCustomAdapter.notifyDataSetChanged();
            }
        */
    };


    private void connectServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mChatClient = new ChatClient(mChatMessageHandler);
                mChatClient.connect();
            }
        }).start();
    }

    /*
     * Layout
     */
    private void setLayout(){
        mListView = (ListView) findViewById(R.id.lv_chat_client_list);
        mCountBt = (Button) findViewById(R.id.btn_chat_client_count);
        mCountBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountBt.setText("현재 체크된 숫자는 = " + mChatClientAdapter.getChecked().size() + " 개 입니다.");
                for (int i = 0; i < mChatClientAdapter.getChecked().size(); i++) { // 체크되 있는 CheckBox의 Position을 얻어 온다.
                    Log.d(TAG, "체크되 있는 Position = " + mChatClientAdapter.getChecked().get(i));
                }
            }
        });

        mAllCheckBox = (CheckBox) findViewById(R.id.cb_all_chat);
        mAllCheckBox.setOnClickListener(new View.OnClickListener() {// 전체 체크 버튼 클릭시 Listener
            @Override
            public void onClick(View v) {
                mChatClientAdapter.setAllChecked(mAllCheckBox.isChecked());
                mChatClientAdapter.notifyDataSetChanged();
            }
        });
    }


}
