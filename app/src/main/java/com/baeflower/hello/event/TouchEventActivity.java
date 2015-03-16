package com.baeflower.hello.event;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baeflower.hello.R;

public class TouchEventActivity extends ActionBarActivity {

    private TextView mTextView;
    private Button mEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);

        mTextView = (TextView) findViewById(R.id.textView);
        mEventBtn = (Button) findViewById(R.id.eventBtn);

        mEventBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // MotionEvent 에 다양한 거 많음
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(Color.CYAN);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.GREEN);
                }

                // 버튼위에서 일어난 이벤트를 받음
                mTextView.setText(event.toString());
                // 이벤트가 정상적으로 종료되었으면 true(다른 이벤트리스너 무시), 그게 아니면 false
                // return true;

                // onClick든 머든 제일 먼저 먹는건 touch 이벤트이다
                // true일 경우에는 할 일 끝났다! 라고 하고 종료해버림
                // false일 경우에는 아직 안끝났다! 이벤트를 계속 흘려줌(?)
                return false;
            }
        });
        mEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "클릭", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        mTextView.setText(event.toString());

        return super.onTouchEvent(event);
    }
}
