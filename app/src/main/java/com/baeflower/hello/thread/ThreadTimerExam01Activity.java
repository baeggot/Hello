package com.baeflower.hello.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.baeflower.hello.R;

public class ThreadTimerExam01Activity extends ActionBarActivity {

    public static final String TAG = ThreadTimerExam01Activity.class.getSimpleName();

    private Button mBtnStart;
    private Button mBtnPause;
    private Button mBtnReset;
    private Button mBtnChronStart;
    private Button mBtnChronPause;
    private TextView mTvTime;


    private Chronometer chronometer;

    private TimerHandler mTimerHandler;
    private long mTime = 0; //시작버튼을 누른 시점 저장하는데 사용
    private long mStop = 0; //정지버튼을 누른 시점 저장하는데 사용
    private int status = 0; //시작 상태인지 정지 상태인지 상태 확인용.


    private void initData() {
        mBtnStart = (Button) findViewById(R.id.btn_timer_start);
        mBtnPause = (Button) findViewById(R.id.btn_timer_pause);
        mBtnReset = (Button) findViewById(R.id.btn_timer_reset);
        mTvTime = (TextView) findViewById(R.id.tv_time);

        chronometer = (Chronometer) findViewById(R.id.chron);
        mBtnChronStart = (Button) findViewById(R.id.btn_chron_timer_start);
        mBtnChronPause = (Button) findViewById(R.id.btn_chron_timer_pause);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_timer_exam01);

        // Data init
        initData();

        /*
            Chronmeter 이용했을 때 (안드로이드 내부 타이머)
            mm:ss 로 출력됨. 밀리세크는 출력 못하는듯?
         */
        mBtnChronStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime()); // 실행 후 시간
                chronometer.start();
                mBtnChronStart.setEnabled(false);
                mBtnChronPause.setEnabled(true);
            }
        });
        mBtnChronPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                mBtnChronStart.setEnabled(true);
                mBtnChronPause.setEnabled(false);
            }
        });


        //리스너 구현
        TimerClickListener click = new TimerClickListener();
        mBtnStart.setOnClickListener(click);
        mBtnPause.setOnClickListener(click);
        mBtnReset.setOnClickListener(click);
        mTimerHandler = new TimerHandler();


    }

    /*
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){ //받은 메시지의 ID를 통해 구현을 선택
            if(msg.what==0){
                mTvTime.setText(getTime()); //메인 스레드의 텍스트뷰에 접근하여 수정.
                mHandler.sendEmptyMessage(0); //재귀 함수 처럼 자기 자신의 핸들러를 호출
            }
        }
    };
    */

    //메인 스레드와 작업 스레드간에 통신을 위한 핸들러 생성
    class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "TimerHandler > handleMessage");
            if (msg.what == 0) { // 이게 뭐지?
                mTvTime.setText(getTime());
                mTimerHandler.sendEmptyMessage(0);
            }
        }
    }


    private String getTime() {
        long now = SystemClock.elapsedRealtime(); // Returns milliseconds since boot, including time spent in sleep.
        long eTime = now - mTime; // 프로그램이 진행된 시간 - 내가 버튼을 누른 시점 = 버튼을 누른 경과 시간.
        String setTime = String.format("%02d:%02d:%03d", eTime / 1000 / 60, (eTime / 1000) % 60, eTime % 1000);
        return setTime;
    }


    class TimerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_timer_start:
                    Log.d(TAG, "TimerClickListener > start click");
                    switch (status) {
                        case 0:
                            mTime = SystemClock.elapsedRealtime(); //버튼을 누른 시간 저장 SystemClock.elapsedRealtime()메소드는 앱이 켜진 시간을 알려주는 메소드이다.
                            //sleep을 했어도 계속해서 시간을 재고있는 좋은 메소드이다. (시간 경과알기에 딱좋은 메소드)

                            mTimerHandler.sendEmptyMessage(0); //핸들러에게 메시지 전달.(어떻게 보면 동작하라는 뜻)
                            mBtnStart.setEnabled(false); //setEnable을 계속해서 설정하는 이유: 스탑버튼이나 스타트버튼을 연속해서 두번을 누르면 오류 발생..
                            mBtnPause.setEnabled(true);
                            break;
                        case 2: //참고로 이부분을 이해하는데 가장 어려웠다... 정지한 시점에서 다시 시작하는 식이 생각보다 어려워서 종이에 그려가며 이해했다..
                            long nowTime = SystemClock.elapsedRealtime(); //재시작 버튼을 누른 시점
                            mTime += (nowTime - mStop); //재시작 버튼을 누른 시점과 멈추었던 시점을 빼서 애초에 시작했던 시간과 더하면 경과시간이 나옴.
                            mTimerHandler.sendEmptyMessage(0);
                            mBtnStart.setEnabled(false);
                            mBtnPause.setEnabled(true);
                            break;
                    }
                    break;
                case R.id.btn_timer_pause: //정지 버튼
                    Log.d(TAG, "TimerClickListener > pause click");
                    mTimerHandler.removeMessages(0);
                    mStop = SystemClock.elapsedRealtime(); //멈춘 시점 저장
                    mBtnStart.setEnabled(true);
                    mBtnPause.setEnabled(false);
                    status = 2; // 정지한 시간부터 재시작 하기위해
                    break;
                case R.id.btn_timer_reset: //초기화 버튼
                    Log.d(TAG, "TimerClickListener > reset click");
                    mTimerHandler.removeMessages(0); //다 초기화
                    mBtnStart.setEnabled(true);
                    mTvTime.setText("00:00:000");
                    status = 0;
                    break;
            }//버튼 스위치 문 끝
        }
    }



}
