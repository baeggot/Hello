package com.baeflower.hello.grid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.baeflower.hello.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class GridViewCalendarActivity extends ActionBarActivity {



    private ArrayList<Integer> mDayList;
    private Integer[][] mDayArr;
    private Integer[] mDayArr2;

    private ArrayAdapter<Integer> arrayAdapter;

    private GridView mCalendarGridView;
    private TextView mCurrentYearMonthTextView;
    private Button mBtnLastMonth;
    private Button mBtnAfterMonth;

    private GregorianCalendar calendar;
    private int currentYear, currentMonth, day, yoil, weekNum, lastDay, yoilOfFirstDay, lastDayOfLastMonth;

    private final int CURRENT = 0;
    private final int BEFORE = 1;
    private final int AFTER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_calendar);

        // init
        this.initData();

        // Data
        this.setMonthData(CURRENT);

        // Adapter
        arrayAdapter = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_list_item_1, mDayArr2);

        // View
        mCalendarGridView.setAdapter(arrayAdapter);
        mCurrentYearMonthTextView.setText(currentYear + "년 " + (currentMonth + 1) + "월");


        // Event Handler
        mBtnLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthData(BEFORE);
                mCurrentYearMonthTextView.setText(currentYear + "년 " + (currentMonth + 1) + "월");
                arrayAdapter.notifyDataSetChanged();
            }
        });
        mBtnAfterMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthData(AFTER);
                mCurrentYearMonthTextView.setText(currentYear + "년 " + (currentMonth + 1) + "월");
                arrayAdapter.notifyDataSetChanged();
            }
        });


    }// onCreate

    private void initData(){
        mDayList = new ArrayList<>();
        // mDayArr = new Integer[5][7]; // 7칸 짜리 배열이 5줄
        mDayArr2 = new Integer[35]; // 35칸
        mCalendarGridView = (GridView) findViewById(R.id.gridView_calendar);
        mCurrentYearMonthTextView = (TextView) findViewById(R.id.textView_currentYearMonth);
        mBtnLastMonth = (Button) findViewById(R.id.btn_lastMonth);
        mBtnAfterMonth = (Button) findViewById(R.id.btn_afterMonth);
        calendar = new GregorianCalendar();
    }


    // 달력 데이터 세팅
    private void setMonthData(int flag){

        // View의 출력된 month를 기준으로 해야됨!
        if (flag == BEFORE) {// 이전 달
            calendar.set(calendar.MONTH, currentMonth);
            calendar.add(calendar.MONTH, -1);
        } else if (flag == AFTER) {// 다음 달
            calendar.set(calendar.MONTH, currentMonth);
            calendar.add(calendar.MONTH, +1);
        }

        currentYear = calendar.get(calendar.YEAR);
        currentMonth = calendar.get(calendar.MONTH);
        day = calendar.get(calendar.DAY_OF_MONTH);
        yoil = calendar.get(calendar.DAY_OF_WEEK); // 일요일(1), ~ , 토요일(7)
        weekNum = calendar.get(calendar.WEEK_OF_MONTH); // 이달의 몇째주
        lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH); // 이달의 마지막 날짜

        // 이번달 1일의 요일 구하기
        calendar.set(calendar.DATE, 1);
        yoilOfFirstDay = calendar.get(calendar.DAY_OF_WEEK);

        // 이전 달의 마지막 날짜 구하기
        calendar.add(calendar.MONTH, -1);
        lastDayOfLastMonth = calendar.getActualMaximum(calendar.DAY_OF_MONTH);

        // 1행에 1일에 해당하는 index 구하기
        int yoilOfFirstDayIdx = yoilOfFirstDay - 1;

        // 현재 달의 1일 이전의 날짜 대입
        int remainedLastMonthDays = yoilOfFirstDayIdx - 1;
        for (int i = remainedLastMonthDays; i > -1; i--) {
            mDayArr2[i] = lastDayOfLastMonth;
            lastDayOfLastMonth--;
        }

        // 달 날짜 대입
        int dayCnt = 1;
        for (int i = yoilOfFirstDayIdx; i < mDayArr2.length; i++) {
            if (dayCnt > lastDay) {
                dayCnt = 1;
            }
            mDayArr2[i] = dayCnt;
            dayCnt++;
        }

        // 현재 달로 다시 돌려놓기
        calendar.add(calendar.MONTH, +1);

        /*
        // 이전달 날짜 대입
        int remainedLastMonthDays = yoilOfFirstDayIdx - 1; // 남은 요일에서 - 1 (index 때문에)
        for (int i = remainedLastMonthDays; i > -1; i--) {
            mDayArr[0][remainedLastMonthDays] = lastDayOfLastMonth;
            lastDayOfLastMonth--;
        }
        // 이번달 날짜 대입
        int dayCnt = 1;
        for (int i = 0; i < 5; i++) {

            // 이번달 마지막 날짜보다 커졌으면 다시 1일 부터
            if (dayCnt > lastDay) {
                dayCnt = 1;
            }
            // 첫째주만 따로 대입입
           if (i == 0) {
                for (int j = yoilOfFirstDayIdx; j < 7; j++) {
                    mDayArr[0][yoilOfFirstDayIdx] = dayCnt;
                    dayCnt ++;
                }
            }
            for (int j = 1; j < 7; j++) {
                mDayArr[i][j] = dayCnt;
                dayCnt++;
            }
        }
        */
    }

}
