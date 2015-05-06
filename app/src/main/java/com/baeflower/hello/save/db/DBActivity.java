
package com.baeflower.hello.save.db;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.baeflower.hello.R;

public class DBActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private EditText mEtDbName;
    private EditText mEtDbEmail;
    private Button mBtnDbSubmit;
    private ListView mLvPersonList;

    private PersonHelper mPersonDbHelper;

    private SimpleCursorAdapter mAdapter;

    private void assignViews() {
        mEtDbName = (EditText) findViewById(R.id.et_db_name);
        mEtDbEmail = (EditText) findViewById(R.id.et_db_email);
        mBtnDbSubmit = (Button) findViewById(R.id.btn_db_submit);
        mLvPersonList = (ListView) findViewById(R.id.lv_db_person_info);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        assignViews();

        // getWritableDatabase()를 할 때 DB가 없으면 DB 생성 - PersonHelper의 onCreate
        // 동작해서 table이 생길 것이여
        mPersonDbHelper = new PersonHelper((getApplicationContext()));
        // SQLiteDatabase db = dbHelper.getWritableDatabase();
        // dbHelper.getReadableDatabase();

        // 1.
        // db.execSQL("INSERT INTO Person (name, email) VALUES ('test', 'test@test.com');");

        // 2.
        // Create a new map of values, where column names are the keys
        // ContentValues values = new ContentValues();
        // values.put("name", "test");
        // values.put("email", "test@test.com");
        // long rowId = db.insert("Person", null, values);

        // Test
        // long rowId = dbHelper.insert(new Person("이름", "이메일"));
        // Toast.makeText(getApplicationContext(), "inserted Id : " + String.valueOf(rowId), Toast.LENGTH_SHORT).show();

        mBtnDbSubmit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 모든 정보를 cursor로 받음
        Cursor cursor = mPersonDbHelper.selectAll();

        // id는 int로 저장되어있음
        mAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, cursor, new String[] {
                        "name", "email"}, new int[] {android.R.id.text1, android.R.id.text2}, 0);

        mLvPersonList.setAdapter(mAdapter);

        // long click
        mLvPersonList.setOnItemLongClickListener(this);

        // 롱 클릭 시 context 메뉴가 호출 되도록! long click 리스너랑 같이 붙이면 들어오지 않음.
        registerForContextMenu(mLvPersonList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_db_submit:
                String name = mEtDbName.getText().toString();
                String email = mEtDbEmail.getText().toString();

                Person person = new Person(name, email);
                // DB에 person 정보를 삽입
                long rowId = mPersonDbHelper.insert(person);

                break;
        }
    }

    int mSelectedPosition = -1;

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(), "position : " + position, Toast.LENGTH_SHORT).show();

        mSelectedPosition = position;

        // return true : 현재 이벤트까지 먹고 이벤트가 종료됨
        // 이벤트 처리 계속 시킴 ( 다음으로 이벤트를 흘림. context menu까지 호출 하겠지)
        return false;
    }

    private int deleteItem(int position) {
        // 모든 데이터를 들고 옴
        Cursor cursor = mPersonDbHelper.selectAll();
        // cursor.moveToFirst(); // 맨 앞을 가리키도록 이동
        // list 순서랑 커서의 순서랑 같음

        // 내가 클릭한 데이터로 커서를 이동
        cursor.moveToPosition(position);
        // column 명으로 index 찾아서 id 가져오기 (예를 들어 _id : 0, name : 1, email : 2 번째 컬럼이 되겠다)
        int clickedId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));

        // 삭제
        mPersonDbHelper.delete(clickedId);

        // 어댑터 갱신
        cursor = mPersonDbHelper.selectAll();
        mAdapter.swapCursor(cursor);

        return clickedId;
    }


    /*
        long click 했을 때 보여줄 menu 파일 inflate
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Toast.makeText(getApplicationContext(), "컨텍스트 메뉴 호출", Toast.LENGTH_SHORT).show();

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_db, menu);


    }

    /*
        컨텍스트 메뉴 이벤트 처리
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteItem(mSelectedPosition);
                return true;
            case R.id.action_update:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private int updateItem(long id) {
        // 현재 선택 된 위치로 cursor 이동
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(mSelectedPosition);

        Person person = new Person();
        person.setName(mEtDbName.getText().toString());
        person.setEmail(mEtDbEmail.getText().toString());

        int updatedCnt = mPersonDbHelper.update(id, person);
        return updatedCnt;
    }



}
