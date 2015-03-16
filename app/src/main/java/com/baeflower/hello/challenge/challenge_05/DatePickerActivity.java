package com.baeflower.hello.challenge.challenge_05;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baeflower.hello.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerActivity extends ActionBarActivity {

    private EditText mNameEditText;
    private EditText mAgeEditText;
    private Button mSaveBtn;
    private Button mBirthdayBtn;
    private Button mBirthdayTimeBtn;

    private int year, month, day, hour, minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        mNameEditText = (EditText) findViewById(R.id.textView_name);
        mAgeEditText = (EditText) findViewById(R.id.textView_age);
        mBirthdayBtn = (Button) findViewById(R.id.btn_birthday);
        mBirthdayTimeBtn = (Button) findViewById(R.id.btn_birthday_time);
        mSaveBtn = (Button) findViewById(R.id.btn_save);

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        mBirthdayBtn.setText(year + "년 " + (month + 1) + "월 " + day + "일");
        mBirthdayTimeBtn.setText(hour + " 시 " + minute + " 분");


        /*
            생년월일 버튼
         */
        mBirthdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DatePickerActivity.this, dateSetListener, year, month, day).show();
            }
        });

        /*
            시간 넣는 버튼
         */
        mBirthdayTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    onCreate에서 처음 세팅된 시간으로 계속 들어감... click 할때마다 들어가게 하려면
                    이 안에서 계속 변수에 새로 값을 넣어야겠네
                 */

                // false 하니깐 오전, 오후 안뜨네
                new TimePickerDialog(DatePickerActivity.this, timeSetListener, hour, minute, false).show();
            }
        });


        /*
            저장 버튼
         */
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "이름 : " + mNameEditText.getText().toString() + ", 나이 : " + mAgeEditText.getText().toString();
                msg += ", " + mBirthdayBtn.getText().toString();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // String msg = String.format("%d 년 %d 월 %d 일", year, monthOfYear + 1, dayOfMonth);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy년 MM월 dd일");
            GregorianCalendar tmpCalendar = new GregorianCalendar();
            tmpCalendar.set(year, monthOfYear, dayOfMonth);

            mBirthdayBtn.setText(sf.format(tmpCalendar.getTime()));
            Toast.makeText(DatePickerActivity.this, sf.format(tmpCalendar.getTime()), Toast.LENGTH_SHORT).show();
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String am_pm = "";

            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";

            String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
            strHrsToShow += " 시 " + datetime.get(Calendar.MINUTE) + " 분 " + am_pm;

            mBirthdayTimeBtn.setText(strHrsToShow);
            //Toast.makeText(DatePickerActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };
}
