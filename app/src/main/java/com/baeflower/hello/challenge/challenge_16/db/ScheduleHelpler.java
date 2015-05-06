package com.baeflower.hello.challenge.challenge_16.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

/**
 * Created by sol on 2015-04-15.
 */
public class ScheduleHelpler extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Schedule.db"; // DB 이름
    private static final String TABLE_NAME = "Schedule";
    private static final int DATABASE_VERSION = 1; // DB 버전

    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TODO = "todo";
    private static final String COLUMN_WEATHER = "weather";
    private static final String COLUMN_TIME = "time";

    private final Context mContext;

    public ScheduleHelpler(Context context) {
        // 부모의 생성자 호출
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /*
        DB가 없을 때 DB 생성하는 부분
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE PERSON (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT);

        // 1. sql 문법을 바로 처리
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, todo TEXT, weather TEXT, time TEXT);");
    }

    /*
        DB 구조 변경 시 처리하는 곳
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(Schedule schedule) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, schedule.getDate());
        values.put(COLUMN_TODO, schedule.getTodo());
        values.put(COLUMN_TIME, schedule.getTime());
        values.put(COLUMN_WEATHER, schedule.getWeather());
        long rowId = db.insert(TABLE_NAME, null, values);
        return rowId;
    }

    public Cursor selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from Schedule", null); // 앞에 ? 로 두고 뒤에 대입되는 것을 쓰면 됨
        return c;
    }

    public Cursor selectColumn(String columnName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select " + columnName + " from Schedule", null);
        return c;
    }

    public Cursor selectClickedTodo(String date) {
        String sql = "select * from " + TABLE_NAME + " where date = '" + date +"';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor selectTodoExist(String yearAndMonth) {
        String sql = "select * from " + TABLE_NAME + " where date like '%" + yearAndMonth +"%';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public int delete(int pk) {
        // DELETE FROM Person WHERE _id = pk;
        SQLiteDatabase db = getWritableDatabase();
        // 몇 줄이 삭제됐는지 리턴한다
        int deletedRowCnt = db.delete(TABLE_NAME, BaseColumns._ID + " = " + pk, null);
        Toast.makeText(mContext, "deletedRowCnt : " + deletedRowCnt, Toast.LENGTH_SHORT).show();
        return deletedRowCnt;
    }

    public int update(long id, Schedule schedule) {
        // UPDATE Person SET field = value;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, schedule.getDate());
        values.put(COLUMN_TODO, schedule.getTodo());
        values.put(COLUMN_TIME, schedule.getTime());
        values.put(COLUMN_WEATHER, schedule.getWeather());
        int updatedCnt = db.update(TABLE_NAME, values, BaseColumns._ID + " = " + id, null);
        return updatedCnt;
    }

}
