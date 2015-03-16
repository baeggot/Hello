package com.baeflower.hello.challenge.challenge_04;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baeflower.hello.R;

public class LoginActivityTargetActivity extends ActionBarActivity {

    public static final String TAG = LoginActivityTargetActivity.class.getSimpleName();
    public static final int REQUEST_CODE_LOGIN = 0;

    private Button mBtnCustom;
    private Button mBtnMoney;
    private Button mBtnGoods;
    private Button mBtnBackToLogin;

    private View.OnClickListener listener;
    private View.OnClickListener dialogListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity_target);
        Log.d(TAG, "onCreate");

        mBtnCustom = (Button) findViewById(R.id.btn_custom);
        mBtnMoney = (Button) findViewById(R.id.btn_money);
        mBtnGoods = (Button) findViewById(R.id.btn_goods);
        mBtnBackToLogin = (Button) findViewById(R.id.btn_backTo_login);


        /*
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginExamActivity.class);
                if (v.getId() == mBtnCustom.getId()) {
                    intent.putExtra("btnName", mBtnCustom.getText());
                } else if (v.getId() == mBtnMoney.getId()){
                    intent.putExtra("btnName", mBtnMoney.getText());
                } else if (v.getId() == mBtnGoods.getId()){
                    intent.putExtra("btnName", mBtnGoods.getText());
                }
                setResult(RESULT_OK, intent); // 나를 startActivityForResult로 부른 놈한테만 데이터를 넘겨줌
                finish(); // 이거는 현재 activity를 detroy하고 이전 activity로 돌아가는 것
                // startActivity(intent); 를 하면 새로운 activity를 위에 쌓는것이기 때문에 데이터 전달이 안됨
            }
        };
        mBtnCustom.setOnClickListener(listener);
        mBtnMoney.setOnClickListener(listener);
        mBtnGoods.setOnClickListener(listener);
        */

        dialogListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivityTargetActivity.this);
                if (v.getId() == mBtnCustom.getId()){
                    dialog.setTitle(mBtnCustom.getText());
                } else if (v.getId() == mBtnMoney.getId()){
                    dialog.setTitle(mBtnMoney.getText());
                } else if (v.getId() == mBtnGoods.getId()){
                    dialog.setTitle(mBtnGoods.getText());
                }
                dialog.setNegativeButton("닫기", null);
                dialog.create(); // 만들고
                dialog.show(); // 보여주기
            }
        };

        mBtnCustom.setOnClickListener(dialogListener);
        mBtnMoney.setOnClickListener(dialogListener);
        mBtnGoods.setOnClickListener(dialogListener);

        // 로그인 화면으로 돌아가기
        mBtnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        });

    }

}
