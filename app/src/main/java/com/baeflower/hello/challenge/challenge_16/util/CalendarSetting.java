package com.baeflower.hello.challenge.challenge_16.util;

import java.util.GregorianCalendar;

/**
 * Created by sol on 2015-04-16.
 */
public class CalendarSetting {

    // Calendar 출력에 사용하는 변수들
    private GregorianCalendar calendar;
    private int currentYear, currentMonth, currentDay;
    private int mSelectedYear, mSelectedMonth, mSelectedDay;
    private int lastDay, yoilOfFirstDay, lastDayOfLastMonth;

    private int idxOfLastDay;
    private int idxOfFirstDay;

    private Integer[] mDateArr;

    private int mSelectedPosition;
    private String mSelectedDate;

    private final int CURRENT = 0;
    private final int BEFORE = 1;
    private final int AFTER = 2;

    public CalendarSetting(int size) {
        calendar = new GregorianCalendar();
        mDateArr = new Integer[size];
    }

    // 달력 데이터 세팅
    public void setMonthData(int flag) {

        // 현재 달을 세팅할 때 오늘 데이터를 세팅(안건드릴거임)
        if (flag == CURRENT) {
            currentYear = calendar.get(calendar.YEAR);
            currentMonth = calendar.get(calendar.MONTH);
            currentDay = calendar.get(calendar.DAY_OF_MONTH); // 오늘 날짜
            mSelectedYear = currentYear;
            mSelectedMonth = currentMonth;
            mSelectedDay = currentDay;
        } else {
            if (flag == BEFORE) {// 이전 달
                calendar.add(calendar.MONTH, -1);
            } else if (flag == AFTER) {// 다음 달
                calendar.add(calendar.MONTH, +1);
            }
            mSelectedYear = calendar.get(calendar.YEAR);
            mSelectedMonth = calendar.get(calendar.MONTH);
            mSelectedDay = calendar.get(calendar.DAY_OF_MONTH);
        }

        lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH); // 선택된 달의 마지막 날짜

        // 이번달 1일의 요일 구하기
        calendar.set(calendar.DATE, 1);
        yoilOfFirstDay = calendar.get(calendar.DAY_OF_WEEK);

        // 이전 달의 마지막 날짜 구하기
        calendar.add(calendar.MONTH, -1);
        lastDayOfLastMonth = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
        // 선택된 달로 다시 돌려놓기
        calendar.add(calendar.MONTH, +1);

        // 1행에 1일에 해당하는 index 구하기
        idxOfFirstDay = yoilOfFirstDay - 1;

        // 현재 달의 1일 이전의 날짜 대입
        int remainedLastMonthDays = idxOfFirstDay - 1;
        for (int i = remainedLastMonthDays; i > -1; i--) {
            mDateArr[i] = lastDayOfLastMonth;
            lastDayOfLastMonth--;
        }

        // 달 날짜 대입
        idxOfLastDay = -1;
        int dayCnt = 1;
        for (int i = idxOfFirstDay; i < mDateArr.length; i++) {

            if (flag == CURRENT) {
                if (currentDay == dayCnt) {
                    mSelectedPosition = i; // 오늘 날짜의 idx (다른 달로 넘어가면 ... 해결못함)
                }
            } else { // 1일을 기본으로 함
                if (i == idxOfFirstDay) {
                    mSelectedPosition = idxOfFirstDay;
                }
            }

            if (dayCnt > lastDay) {
                idxOfLastDay = i - 1;
                dayCnt = 1;
            }

            mDateArr[i] = dayCnt;
            dayCnt++;
        }

        // 년,월 출력 정보 수정
        // mCurrentYearMonthTextView.setText(mSelectedYear + "년 " + (mSelectedMonth + 1) + "월");

        // selectedDate 세팅
        setmSelectedDate();
    }

    public void setmSelectedDate() {
        if (idxOfFirstDay > mSelectedPosition) { // 이전달 날짜
            if ((mSelectedMonth + 1) == 1) { // 현재 달이 1월이면 이전달은 12월
                mSelectedDate = "" + mSelectedYear + "/" + 12 + "/" + mSelectedDay;
            } else {
                mSelectedDate = "" + mSelectedYear + "/" + mSelectedMonth + "/" + mSelectedDay;
            }
        } else if (idxOfLastDay < mSelectedPosition) { // 다음달 날짜
            if ((mSelectedMonth + 1) == 12) { // 현재 달이 12월이면 다음달은 1월
                mSelectedDate = "" + mSelectedYear + "/" + 1 + "/" + mSelectedDay;
            } else {
                mSelectedDate = "" + mSelectedYear + "/" + (mSelectedMonth + 2) + "/" + mSelectedDay;
            }
        } else {
            mSelectedDate = "" + mSelectedYear + "/" + (mSelectedMonth + 1) + "/" + mSelectedDay;
        }
    }

    public int getmSelectedYear() {
        return mSelectedYear;
    }

    public int getmSelectedMonth() {
        return mSelectedMonth;
    }

    public void setmSelectedDay(int mSelectedDay) {
        this.mSelectedDay = mSelectedDay;
    }

    public int getmSelectedDay() {
        return mSelectedDay;
    }

    public String getmSelectedDate() {
        return mSelectedDate;
    }

    public Integer[] getmDateArr() {
        return mDateArr;
    }

    public int getmSelectedPosition() {
        return mSelectedPosition;
    }
}
