package com.baeflower.hello.menus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.baeflower.hello.listview.adapter.ActivityNameAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends ActionBarActivity {
    private List<String> mActivityList;
    private ListView mActivityListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mActivityListView = (ListView) findViewById(R.id.listView_menu);

        // init
        mActivityList = new ArrayList<>();


        // Data
        Intent intent = getIntent();
        final List<ActivityInfo> activityInfoList = (ArrayList<ActivityInfo>)intent.getSerializableExtra("activities");


        // adapter
        final ActivityNameAdapter<ActivityInfo> adapter = new ActivityNameAdapter<ActivityInfo>(getApplicationContext(),
                0, activityInfoList);


        // View에 붙이기
        mActivityListView.setAdapter(adapter);

        mActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ActivityInfo activityInfo = activityInfoList.get(position);
                    Log.d("activityinfo.name : ", activityInfo.name);
                    Class c = Class.forName(activityInfo.name);
                    Intent intent = new Intent(MenuActivity.this, c);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    Toast.makeText(MenuActivity.this, "activity 없음", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }// onCreate


}
