package com.baeflower.hello.challenge_s.challenge_04;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baeflower.hello.R;

public class MainActivity extends ActionBarActivity {

    private EditText mId;
    private EditText mPassword;

    private Button mLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mId = (EditText) findViewById(R.id.idEditText);
        mPassword = (EditText) findViewById(R.id.pwEditText);

        /*
            한번쓰고 안쓸 버튼들(재사용 필요없는것들)
         */
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mId.getText()) || TextUtils.isEmpty(mPassword.getText())){
                    Toast.makeText(getApplicationContext(), "ID, Password를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getApplicationContext(), SubActivity.class));
                }
            }
        });




    }

}
