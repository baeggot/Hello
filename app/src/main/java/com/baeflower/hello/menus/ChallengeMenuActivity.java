package com.baeflower.hello.menus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.baeflower.hello.R;
import com.baeflower.hello.listview.adapter.ActivityNameAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChallengeMenuActivity extends ActionBarActivity {


    private List<String> mActivityList;
    private ListView mActivityListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_menu);

        mActivityListView = (ListView) findViewById(R.id.listView_activityMenu);

        // init
        mActivityList = new ArrayList<>();

        // Data
        Intent intent = getIntent();
        List<ActivityInfo> activityInfoList = (ArrayList<ActivityInfo>)intent.getSerializableExtra("activities");

        // adapter
        final ActivityNameAdapter<ActivityInfo> adapter = new ActivityNameAdapter<ActivityInfo>(getApplicationContext(),
                0, activityInfoList);

        // View에 붙이기
        mActivityListView.setAdapter(adapter);


    }

}
