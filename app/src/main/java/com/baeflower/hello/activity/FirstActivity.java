package com.baeflower.hello.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baeflower.hello.R;


public class FirstActivity extends ActionBarActivity {

    /*
        onCreate가 메인 메서드라고 보면 된다
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // 버튼 객체 가지고 온다
        Button testButton = (Button) findViewById(R.id.test_button1);

        // 버튼 객체에 onClick 이벤트를 연결
        // activity.xml의 Button view에 android:onClick="onButton1Clicked" 속성을 추가해서 연결할 수도 있음
        // 하지만 이렇게 할 경우 어떻게 연결되는지 알기가 쉽지 않고 단발적이다. 확장이 안됨
        // 그래서 아래의 방법처럼 쓴다
        testButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 버튼이 클릭 되었을 때 수행
                onButton1Clicked(v);
            }
        });
    }

    public void onButton1Clicked(View v){
        Toast.makeText(getApplicationContext(), "버튼 클릭", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
