package com.baeflower.hello.save.db;

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
public class PersonHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Person.db"; // DB 이름
    private static final String TABLE_NAME = "Person";
    private static final int DATABASE_VERSION = 1; // DB 버전

    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private final Context mContext;


    public PersonHelper(Context context) {
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
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT);");



    }

    /*
        DB 구조 변경 시 처리하는 곳
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(Person person) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, person.getName());
        values.put(COLUMN_EMAIL, person.getEmail());
        long rowId = db.insert(TABLE_NAME, null, values);
        return rowId;
    }

    public Cursor selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from Person", null); // 앞에 ? 로 두고 뒤에 대입되는 것을 쓰면 됨
        return c;
    }

    public Cursor select(String columnName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select " + columnName + " from Person", null);
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

    public int update(long id, Person person) {
        // UPDATE Person SET field = value;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, person.getName());
        values.put(COLUMN_EMAIL, person.getEmail());
        int updatedCnt = db.update(TABLE_NAME, values, BaseColumns._ID + " = " + id, null);
        return updatedCnt;
    }

}
