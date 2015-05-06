package com.baeflower.hello.contentProvider;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;

/**
 * 연락처 정보 가져와서 뿌려주기
 * ListActivity : XML 없이 List에 데이터만 넣으면 됨
 *
 * <uses-permission android:name="ANDROID.PERMISSION.READ_CONTACTS" />
 * <uses-permission android:name="ANDROID.PERMISSION.WRITE_CONTACTS" />
 *
 */
public class ContactActivity extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        // support 버전에 있는거 쓰세요
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, cursor
                , new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER} // 컬럼명
                , new int[]{android.R.id.text1, android.R.id.text2}  // 레이아웃 위치
                , 0); // flag : 뭐하는건지 몰라. 일단 그냥 0

        cursor.moveToPosition(0);
        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        // 번호를 가려오려면 쿼리를 한번더 따로 날려야 됩니다

        setListAdapter(adapter);

    }



}
