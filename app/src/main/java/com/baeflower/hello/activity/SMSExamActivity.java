package com.baeflower.hello.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baeflower.hello.R;


public class SMSExamActivity extends ActionBarActivity {

    private static final String TAG = EditTextActivity.class.getSimpleName();

    private EditText mInputText;
    private TextView mBytesCountView;
    private Button mSmsTransferBtn;
    private Button mSmsExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsexam);

        mInputText = (EditText) findViewById(R.id.sms_input);
        mBytesCountView = (TextView) findViewById(R.id.bytes_count);
        mSmsTransferBtn = (Button) findViewById(R.id.sms_transfer);
        mSmsExit = (Button) findViewById(R.id.sms_exit);

        // byte 구하기 -> String에서 바이트 구하는 메소드 getBytes().length
        // 현재는 영어와 한글 바이트수가 같다(영어 80자 들어가면 한글도 80자 들어감)
        mInputText.addTextChangedListener(new TextWatcher() {

            // 변경되기 전에 들어오는 메서드
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int inputLength = s.toString().getBytes().length;


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int inputLength = s.toString().getBytes().length;

                if (inputLength > 80){
                    s.delete(s.length() - 2, s.length() - 1);
                    Toast.makeText(getApplicationContext(), "80바이트를 넘을 수 없어요ㅠㅠ", Toast.LENGTH_SHORT).show();
//                    mInputText.setText(s.toString().substring(0, s.toString().length()));
//                    문제(TextChanged 안에서 해당 EditText를 또 setText 하면 꼬일 수 있어서 문제 생기는듯?!
                } else {
                    changeBytesCountView(inputLength);
                }
            }
        });

        /*
        전송 버튼 눌렀을 때 inputEdit 내용 띄우기
         */
        mSmsTransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), mInputText.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        닫기 버튼 눌렀을 때 어플  종료시키기
         */
        mSmsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

    } // onCreate end

    private void changeBytesCountView(int inputBytesLength){
        mBytesCountView.setText(inputBytesLength + " / 80 바이트");
    }
}
