package com.baeflower.hello.challenge_s.challenge_04;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baeflower.hello.R;

public class SubActivity extends ActionBarActivity {

    private Button mCustomerBtn;
    private Button mSalesBtn;
    private Button mMerchantBtn;

    /*
        onCreate가 길어지면 안됨(뭘하는앤지 파악하기 쉽게)
        제일 윗단은 명료해야 한다! (하지만 하다보면 이게 쉽지 않다는...)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub); // layout 붙이기

        init(); // 초기화
    }

    private void init() {
        mCustomerBtn = (Button) findViewById(R.id.customerBtn);
        mSalesBtn = (Button) findViewById(R.id.salesBtn);
        mMerchantBtn = (Button) findViewById(R.id.merchantBtn);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog((Button) v);
            }
        };
        mCustomerBtn.setOnClickListener(onClickListener);
        mSalesBtn.setOnClickListener(onClickListener);
        mMerchantBtn.setOnClickListener(onClickListener);
    }

    private void showDialog(Button v) {
        // Context를 던져야하는데 SubActivity가 Context를 상속받은 것이기 때문에 SubActivity를 던진다
        // 근데 무명메서드(?) 에서 getApplicationContext()를 던지면 ... 어떤걸 던저야할지 못찾음
        // 그래서 명시적으로 Context를 상속받은 class로 넘김

        final String title = v.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(SubActivity.this);
        builder.setTitle(title);
        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            /*
                inner class에서 밖에 있는 변수를 참조할 때, title을 final로 하라고 하는데
                그 이유는 이 변수가 불리기 전에 값이 변할 수 있기 때문에...?!
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SubActivity.this, title + "에서 닫혔음", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

}
