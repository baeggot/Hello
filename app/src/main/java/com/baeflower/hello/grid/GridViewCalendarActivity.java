package com.baeflower.hello.grid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.baeflower.hello.grid.adapter.CalendarAdapter;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class GridViewCalendarActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = GridViewCalendarActivity.class.getSimpleName();


    private Integer[] mDayArr2;

    private CalendarAdapter calendarAdapter;

    private GridView mCalendarGridView;
    private TextView mCurrentYearMonthTextView;
    private Button mBtnLastMonth;
    private Button mBtnAfterMonth;


    // 일정 리스트 출력하는 예제
    private EditText mEtInputInDialog;
    private int selectedPosition;
    private ArrayAdapter<String> mListViewArrAdapter;
    private ListView mListViewTodo;
    private String[] mKeyArr;


    // Calendar 출력에 사용하는 변수들
    private GregorianCalendar calendar;
    private int currentYear, currentMonth, currentDay;
    private int selectedYear, selectedMonth, selectedDay;
    private int yoil, weekNum, lastDay, yoilOfFirstDay, lastDayOfLastMonth;

    private String mSelectedKey;


    private final int CURRENT = 0;
    private final int BEFORE = 1;
    private final int AFTER = 2;


    /*
        도전 7.
        private LinearLayout mLLayoutInput;
        private Button mBtnTodoSave;
        private EditText mEtTodo;
     */


    private void initData(){
        mDayArr2 = new Integer[35]; // 35칸 + 한줄 늘리기
        mCalendarGridView = (GridView) findViewById(R.id.gridView_calendar);
        mCurrentYearMonthTextView = (TextView) findViewById(R.id.textView_currentYearMonth);
        mBtnLastMonth = (Button) findViewById(R.id.btn_lastMonth);
        mBtnAfterMonth = (Button) findViewById(R.id.btn_afterMonth);
        calendar = new GregorianCalendar();

        // dialog로 editText 출력하는 예제
        mListViewTodo = (ListView) findViewById(R.id.listView_todo_gridView);
        mKeyArr = new String[35];
        mListViewTodo = (ListView) findViewById(R.id.listView_todo_gridView);

        /*
        도전 7
        // 달력 아래 editText 출력하는 예제
        //mLLayoutInput = (LinearLayout) findViewById(R.id.lLayout_todo_gridView);
        //mBtnTodoSave = (Button) findViewById(R.id.btnSave_inputTodo_grindView);
        //mEtTodo = (EditText) findViewById(R.id.et_inputTodo_gridView);
        */
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_calendar);

        // init
        this.initData();

        // Data
        this.setMonthData(CURRENT);

        // Adapter
        calendarAdapter = new CalendarAdapter(getApplicationContext(), mDayArr2);
        calendarAdapter.setmSelectedPosition(selectedPosition); //처음에는 현재 날짜의 위치가 들어감
        setmSelectedKey(selectedPosition);
        setKeyArrInAdapter(); // adapter의 keyArr 세팅

        // View
        mCalendarGridView.setAdapter(calendarAdapter);
        mCalendarGridView.setOnItemClickListener(this);


        // Event Handler
        mBtnLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthData(BEFORE);
                setKeyArrInAdapter();
                calendarAdapter.setmSelectedPosition(selectedPosition);
                setmSelectedKey(selectedPosition);
                setTodoListView(selectedPosition);
                calendarAdapter.notifyDataSetChanged();
            }
        });
        mBtnAfterMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthData(AFTER);
                setKeyArrInAdapter();
                calendarAdapter.setmSelectedPosition(selectedPosition);
                setmSelectedKey(selectedPosition);
                setTodoListView(selectedPosition);
                calendarAdapter.notifyDataSetChanged();
            }
        });



        /*
        // 도전7. 날짜 별 일정 저장 버튼 이벤트핸들러 연결
        mBtnTodoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTodoMap.put(getTodoEditTextKey(), mEtTodo.getText().toString());
                calendarAdapter.notifyDataSetChanged();
            }
        });
        */

    }// onCreate


    // 메뉴버튼
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "일정추가").setIcon(android.R.drawable.ic_menu_revert);
        return true;
    }

    // 메뉴버튼 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (selectedDay == -1) {
            Toast.makeText(getApplicationContext(), "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
        } else {
            switch(item.getItemId()) {
                case 0:
                    // setTodoEditTextKey(selectedPosition); // 선택된 날짜로 key 생성
                    // calendarAdapter.setmKeyArr(selectedPosition, getTodoEditTextKey()); // adapter의 keyArr에 key 추가
                    showAlertCustomDialog(); // dialog 부르기
                    break;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // grid view 클릭 이벤트
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectedDay = mDayArr2[position];
        calendarAdapter.setmSelectedPosition(position);
        calendarAdapter.notifyDataSetChanged();

        selectedPosition = position;

        // 날짜 선택할 때 listview 붙이기
        setTodoListView(position);

        // 현재 선택된 날짜의 key를 알고싶어서
        setmSelectedKey(position);

        /*
            도전 7.
            if (mLLayoutInput.getVisibility() == View.INVISIBLE) {
                mLLayoutInput.setVisibility(View.VISIBLE);
            } else if (mLLayoutInput.getVisibility() == View.VISIBLE) {
                mLLayoutInput.setVisibility(View.GONE);
            }
        */
    }

    // 달력 데이터 세팅
    private void setMonthData(int flag){

        // 현재 달을 세팅할 때 오늘 데이터를 세팅(안건드릴거임)
        if (flag == CURRENT) {
            currentYear = calendar.get(calendar.YEAR);
            currentMonth = calendar.get(calendar.MONTH);
            currentDay = calendar.get(calendar.DAY_OF_MONTH); // 오늘 날짜
            selectedYear = currentYear;
            selectedMonth = currentMonth;
            selectedDay = currentDay;
        } else {
            if (flag == BEFORE) {// 이전 달
                calendar.add(calendar.MONTH, -1);
            } else if (flag == AFTER) {// 다음 달
                calendar.add(calendar.MONTH, +1);
            }
            selectedYear = calendar.get(calendar.YEAR);
            selectedMonth = calendar.get(calendar.MONTH);
            selectedDay = calendar.get(calendar.DAY_OF_MONTH);
        }

        // yoil = calendar.get(calendar.DAY_OF_WEEK); // 일요일(1), ~ , 토요일(7)
        // weekNum = calendar.get(calendar.WEEK_OF_MONTH); // 이달의 몇째주
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
        int yoilOfFirstDayIdx = yoilOfFirstDay - 1;

        // 현재 달의 1일 이전의 날짜 대입
        int remainedLastMonthDays = yoilOfFirstDayIdx - 1;
        for (int i = remainedLastMonthDays; i > -1; i--) {
            mDayArr2[i] = lastDayOfLastMonth;
            mKeyArr[i] = getKeyArrWithDateInfo(selectedYear, selectedMonth, lastDayOfLastMonth, i); // 날짜에 대한 key 저장
            lastDayOfLastMonth--;
        }

        // 달 날짜 대입
        int dayCnt = 1;
        for (int i = yoilOfFirstDayIdx; i < mDayArr2.length; i++) {

            if (flag == CURRENT) {
                if (currentDay == dayCnt){
                    selectedPosition = i; // 오늘 날짜의 idx (다른 달로 넘어가면 ... 해결못함)
                    mSelectedKey = mKeyArr[i];
                }
            } else {
                if (i == yoilOfFirstDayIdx) {
                    selectedPosition = yoilOfFirstDayIdx;
                }
            }

            if (dayCnt > lastDay) {
                dayCnt = 1;
            }

            mKeyArr[i] = getKeyArrWithDateInfo(selectedYear, selectedMonth, dayCnt, i); // 날짜에 대한 key 저장
            mDayArr2[i] = dayCnt;
            dayCnt++;
        }

        // 년,월 출력 정보 수정
        mCurrentYearMonthTextView.setText(selectedYear + "년 " + (selectedMonth + 1) + "월");
    }

    // 메뉴버튼 눌렀을 때 dialog 띄우기
    private void showAlertCustomDialog(){
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(GridViewCalendarActivity.this);

        final View customDialog = inflater.inflate(R.layout.custom_alert_dialog, null);

        builder.setTitle("일정 추가");
        builder.setView(customDialog);

        // 저장 버튼
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEtInputInDialog = (EditText) customDialog.findViewById(R.id.et_todo_in_dialog);
                String inputText = mEtInputInDialog.getText().toString();

                if (!TextUtils.isEmpty(inputText)) {
                    String key = getmSelectedKey();
                    HashMap<String, ArrayList<String>> todoListMap = calendarAdapter.getmMapDataDialog();
                    ArrayList<String> inputList = todoListMap.get(key); // 해당하는 키에 데이터리스트를 가져옴

                    if (inputList == null || inputList.isEmpty()) { // 데이터리스트가 없으면 새로 만들어서 넣어줌
                        inputList = new ArrayList<>();
                        inputList.add(inputText);
                        calendarAdapter.setmMapTodoData(key, inputList);
                    } else {                                        // 데이터리스트가 있으면 리스트에 추가
                        calendarAdapter.setmMapTodoData(key, inputText);
                    }
                    Toast.makeText(getApplicationContext(), "일정 [" + inputText +  "] 저장", Toast.LENGTH_SHORT).show();
                    setTodoListView(selectedPosition);
                    calendarAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "저장할 일정이 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("최소", null);

        builder.show();
    }

    // 선택된 날짜에 데이터 리스트가 있을 때 listview 붙이기
    private void setTodoListView(int position) {
        ArrayList<String> toDoList = calendarAdapter.getTodoList(position);
        if (toDoList != null && toDoList.size() != 0) {
            mListViewArrAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, toDoList);
            mListViewTodo.setAdapter(mListViewArrAdapter);
        } else {
            mListViewTodo.setAdapter(null);
        }
    }

    // map에 저장할 key 세팅
    public void setmSelectedKey(int position){
//        mTodoMapKey = selectedYear + "" + (selectedMonth + 1) + "";
//        mTodoMapKey += mDayArr2[position] + "_" + position;
        mSelectedKey = mKeyArr[position];
    }

    // key 가져옴
    public String getmSelectedKey(){
        return mSelectedKey;
    }

    // 달력 만들 때 마다 keyArr 만들기
    public String getKeyArrWithDateInfo(int year, int month, int day, int position){
        System.out.println(year + "" + (month + 1) + "" + day + "_" + position);
        return year + "" + (month + 1) + "" + day + "_" + position;
    }

    // adapter의 keyArr에 데이터 세팅
    public void setKeyArrInAdapter(){
        calendarAdapter.setmKeyArr(mKeyArr);
    }

}
