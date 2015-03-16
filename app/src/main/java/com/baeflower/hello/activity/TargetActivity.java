package com.baeflower.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baeflower.hello.R;

public class TargetActivity extends ActionBarActivity {

    private static final String TAG = TargetActivity.class.getSimpleName();

    private Button mMoveBeforeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        Log.d(TAG, "onCreate");

        mMoveBeforeBtn = (Button) findViewById(R.id.move_before_activity);

        // 나를 호출한 놈의 intent
        Intent intent = getIntent();

        // 위젯으로 들어가면 getIntent가 null 이다
        if (intent != null){
            String value = getIntent().getStringExtra("key");
            // value가 null 이거나 길이가 0인 경우 까지 다 체크해주는 것
            if (!TextUtils.isEmpty(value)){ // TextUtils.isEmpty(value) == false : 잘보이도록 이렇게 쓰기도
                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
            }
        }

        mMoveBeforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityExamActivity.class);
                // intent.addFlags를 통해서 FLAG or 연산도 가능
                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        // 끝내기 버튼
        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Target 없이 그냥 생성
                Intent intent = new Intent();
                intent.putExtra("data", "smartapp");

                // 현재 activity가 죽고 그 전의 activity가 보임(이전 activity가 없으면 앱이 끝나나?)
                // RESULT_OK, RESULT_CANCEL, ...
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }






    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    /*
    위젯에서 실행되는건 또 이쪽으로 오고...
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
