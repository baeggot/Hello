package com.baeflower.hello.thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baeflower.hello.R;

/**
 * Created by sol on 2015-03-26.
 */
public class ThreadTimerExam02Activity extends ActionBarActivity{

    public static final String TAG = ThreadTimerExam02Activity.class.getSimpleName();

    private Button mBtnStart;
    private Button mBtnPause;
    private Button mBtnReset;
    private TextView mTvTime;

    private SolTimerTask solTimerTask;

    private long mAppRunTime;
    private long mStartTime;
    private long mStopTime;

    private void initData() {
        mBtnStart = (Button) findViewById(R.id.btn_timer_start);
        mBtnPause = (Button) findViewById(R.id.btn_timer_pause);
        mBtnReset = (Button) findViewById(R.id.btn_timer_reset);
        mTvTime = (TextView) findViewById(R.id.tv_time);

        mAppRunTime = SystemClock.elapsedRealtime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_timer_exam01);

        initData();


        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solTimerTask = new SolTimerTask();
                solTimerTask.execute();
            }
        });

    }

    // AsyncTask클래스는 항상 Subclassing 해서 사용 해야 함. (상속받아서 쓴다는 말인듯?!)
    // 사용 자료형은
    // background 작업에 사용할 data의 자료형
    // background 작업 진행 표시를 위해 사용할 인자
    // background 작업의 결과를 표현할 자료형
    // 인자를 사용하지 않은 경우 Void Type 으로 지정.
    class SolTimerTask extends AsyncTask<Integer, Integer, Integer> {

        // Background 작업 시작전에 UI 작업을 진행 한다.
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
        }

        //
        @Override
        protected Integer doInBackground(Integer... params) {
            Log.d(TAG, "doInBackground");

            mStartTime = SystemClock.elapsedRealtime();
            Log.d("mStartTime", Long.toString(mStartTime));
            Log.d("mAppRunTime", Long.toString(mAppRunTime));


            publishProgress(); // onProgressUpdate 메서드 호출

            return null;
        }

        // Background 작업을 진행 한다.
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG, "onProgressUpdate");
        }

        // Background 작업이 끝난 후 UI 작업을 진행 한다.
        @Override
        protected void onPostExecute(Integer integer) {
            Log.d(TAG, "onPostExecute");
        }

        // 스레드 취소
        @Override
        protected void onCancelled(Integer integer) {
            Log.d(TAG, "onCancelled");
        }
    }



}
