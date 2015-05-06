
package com.baeflower.hello.challenge.challenge_16;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.baeflower.hello.challenge.challenge_16.adapter.CalendarUsingDBAdapter;
import com.baeflower.hello.challenge.challenge_16.db.Schedule;
import com.baeflower.hello.challenge.challenge_16.db.ScheduleHelpler;
import com.baeflower.hello.challenge.challenge_16.util.CalendarSetting;

public class CalendarUsingDBActivity extends ActionBarActivity implements
        AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String TAG = CalendarUsingDBActivity.class.getSimpleName();
    public static final int CALENDAR_DATE_SIZE = 42;

    private Integer[] mDateArr;

    private CalendarUsingDBAdapter calendarUsingDBAdapter;

    private GridView mCalendarGridView;
    private TextView mCurrentYearMonthTextView;
    private Button mBtnLastMonth;
    private Button mBtnAfterMonth;

    // 일정 리스트 출력하는 예제
    private EditText mEtTodo;
    private EditText mEtHour;
    private EditText mEtMinute;
    private RadioButton mRbSunny;
    private RadioButton mRbCloudy;
    private RadioButton mRbRainy;
    private RadioButton mRbSnow;

    private int mSelectedPosition;
    private SimpleCursorAdapter mListViewArrAdapter;
    private ListView mListViewTodo;

    // Calendar
    private CalendarSetting mCalendarSetting;

    private final int CURRENT = 0;
    private final int BEFORE = 1;
    private final int AFTER = 2;

    // for DB
    private ScheduleHelpler mScheduleHelpler;
    private String mSelectedDate;


    private void initData() {
        mDateArr = new Integer[CALENDAR_DATE_SIZE];
        mCalendarGridView = (GridView) findViewById(R.id.gridView_calendar);
        mCurrentYearMonthTextView = (TextView) findViewById(R.id.textView_currentYearMonth);
        mBtnLastMonth = (Button) findViewById(R.id.btn_lastMonth);
        mBtnAfterMonth = (Button) findViewById(R.id.btn_afterMonth);

        mCalendarSetting = new CalendarSetting(CALENDAR_DATE_SIZE);

        // dialog로 editText 출력하는 예제
        mListViewTodo = (ListView) findViewById(R.id.listView_todo_gridView);
        mListViewTodo = (ListView) findViewById(R.id.listView_todo_gridView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_calendar);

        // init
        this.initData();

        // Data
        mCalendarSetting.setMonthData(CURRENT);
        mDateArr = mCalendarSetting.getmDateArr();

        // Adapter
        calendarUsingDBAdapter = new CalendarUsingDBAdapter(getApplicationContext(), mDateArr, CALENDAR_DATE_SIZE);
        calendarUsingDBAdapter.setmSelectedPosition(mCalendarSetting.getmSelectedPosition()); // 처음에는 현재 날짜의 위치가 들어감

        // View
        mCalendarGridView.setAdapter(calendarUsingDBAdapter);
        mCalendarGridView.setOnItemClickListener(this);

        // 이전달, 다음달 버튼에 대한 Event Handler 연결
        mBtnLastMonth.setOnClickListener(this);
        mBtnAfterMonth.setOnClickListener(this);

        // DB Helper 클래스 생성
        mScheduleHelpler = new ScheduleHelpler((getApplicationContext()));

    }// onCreate

    @Override
    protected void onResume() {
        super.onResume();
        // Log.d(TAG, "onResume()");
    }

    // 메뉴버튼
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "일정추가").setIcon(android.R.drawable.ic_menu_revert);
        return true;
    }

    // 메뉴버튼 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mCalendarSetting.getmSelectedDay() == -1) {
            Toast.makeText(getApplicationContext(), "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
        } else {
            switch (item.getItemId()) {
                case 0:
                    showAlertCustomDialog(); // dialog 부르기
                    break;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lastMonth: // 이전 달 버튼
                mCalendarSetting.setMonthData(BEFORE);
                mDateArr = mCalendarSetting.getmDateArr();
                calendarUsingDBAdapter.setmSelectedPosition(mCalendarSetting.getmSelectedPosition());
                calendarUsingDBAdapter.notifyDataSetChanged();
                setTodoListView();
                break;
            case R.id.btn_afterMonth: // 다음 달 버튼
                mCalendarSetting.setMonthData(AFTER);
                mDateArr = mCalendarSetting.getmDateArr();
                calendarUsingDBAdapter.setmSelectedPosition(mCalendarSetting.getmSelectedPosition());
                calendarUsingDBAdapter.notifyDataSetChanged();
                setTodoListView();
                break;
        }
    }

    // grid view 클릭 이벤트
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mCalendarSetting.setmSelectedDay(mDateArr[position]);
        calendarUsingDBAdapter.setmSelectedPosition(position);
        calendarUsingDBAdapter.notifyDataSetChanged();

        mSelectedPosition = position;

        // DB 저장하기 위한 0000/00/00 스트링 생성
        mCalendarSetting.setmSelectedDate();

        // 날짜 선택할 때 listview 붙이기
        setTodoListView();
    }

    private String mSelectedWeather = "";

    // 메뉴버튼 눌렀을 때 dialog 띄우기
    private void showAlertCustomDialog() {
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarUsingDBActivity.this);

        final View scheduleDialog = inflater.inflate(R.layout.dialog_schedule_input, null);

        builder.setTitle("일정 추가");
        builder.setView(scheduleDialog);

        mEtTodo = (EditText) scheduleDialog.findViewById(R.id.et_dialog_schedule);

        mEtHour = (EditText) scheduleDialog.findViewById(R.id.et_dialog_time_hour);
        mEtMinute = (EditText) scheduleDialog.findViewById(R.id.et_dialog_time_minute);

        mRbSunny = (RadioButton) scheduleDialog.findViewById(R.id.rb_dialog_weather_sunny);
        mRbCloudy = (RadioButton) scheduleDialog.findViewById(R.id.rb_dialog_weather_cloudy);
        mRbRainy = (RadioButton) scheduleDialog.findViewById(R.id.rb_dialog_weather_rainy);
        mRbSnow = (RadioButton) scheduleDialog.findViewById(R.id.rb_dialog_weather_snow);

        mRbSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedWeather = "sunny";
            }
        });
        mRbCloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedWeather = "cloudy";
            }
        });
        mRbRainy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedWeather = "rainy";
            }
        });
        mRbSnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedWeather = "snowy";
            }
        });

        // 저장 버튼
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String todo = mEtTodo.getText().toString();
                int hour = -1;
                int minute = -1;

                try {
                    hour = Integer.parseInt(mEtHour.getText().toString());
                    minute = Integer.parseInt(mEtMinute.getText().toString());

                    if (!TextUtils.isEmpty(todo) && !TextUtils.isEmpty(mSelectedWeather)) {
                        Schedule schedule = new Schedule(mSelectedDate, todo, String.valueOf(hour) + "시 "
                                + String.valueOf(minute) + "분", mSelectedWeather);

                        long insertedId = mScheduleHelpler.insert(schedule);

                        if (insertedId != -1) {
                            Toast.makeText(getApplicationContext(), "일정 [" + insertedId + ". " + todo + "] 저장", Toast.LENGTH_SHORT).show();
                            setTodoListView();
                            calendarUsingDBAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "저장실패", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(getApplicationContext(), "입력을 제대로 완료해 주세요", Toast.LENGTH_SHORT).show();
                    }
                } catch(NumberFormatException exception) {
                    Toast.makeText(getApplicationContext(), "시간을 제대로 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("닫기", null);

        builder.show();
    }

    // 선택된 날짜에 데이터 리스트가 있을 때 listview 붙이기
    private void setTodoListView() {
        Cursor cursor = mScheduleHelpler.selectClickedTodo(mSelectedDate);
        mListViewArrAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, cursor
                , new String[] {"todo"}, new int[] {android.R.id.text1}, 0);
        mListViewTodo.setAdapter(mListViewArrAdapter);
    }

    // 일단 현재 선택된 달에 대해서만 출력해 보자
    private void setTodoListIsExist() {
        String[] dates = mSelectedDate.split("/");
        Log.d(TAG, dates.toString());

        Cursor cursor = mScheduleHelpler.selectTodoExist(dates[0] + "/" + dates[1]);

        // id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));

    }


}
