package com.baeflower.hello.challenge.challenge_04;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baeflower.hello.R;

public class LoginExamActivity extends ActionBarActivity {

    public static final String TAG = LoginExamActivity.class.getSimpleName();
    public static final int REQUEST_CODE_TARGET = 1;

    private Button mBtnLogin;
    private EditText mEtId;
    private EditText mEtPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_exam);
        Log.d(TAG, "onCreate");

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mEtId = (EditText) findViewById(R.id.et_id);
        mEtPw = (EditText) findViewById(R.id.et_pw);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mEtId.getText()) && !TextUtils.isEmpty(mEtPw.getText())){
                    Intent intent = new Intent(getApplicationContext(), LoginActivityTargetActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_TARGET);
                } else {
                    Toast.makeText(getApplicationContext(), "입력해라", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TARGET && resultCode == RESULT_OK){
            String btnName = data.getStringExtra("btnName");
            Toast.makeText(getApplicationContext(), btnName, Toast.LENGTH_SHORT).show();
        }
    }


}
