
package com.baeflower.hello;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baeflower.hello.menus.MenuActivity;

import java.util.ArrayList;

public class StartActivity extends ActionBarActivity {

    private ArrayList<String> mPackageList;
    private ListView mStartListView;
//    private HashMap<String, List<ActivityInfo>> mActivityMap;

    private ArrayList<ActivityInfo> activityInfoList;
    private ArrayList<ActivityInfo> challengeInfoList;
    private ArrayList<ActivityInfo> challengeSamInfoList;
    private ArrayList<ActivityInfo> eventInfoList;
    private ArrayList<ActivityInfo> listViewInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // init
        mStartListView = (ListView) findViewById(R.id.listView_start);

        // Data
        mPackageList = new ArrayList<>();
        mPackageList.add("activity");
        mPackageList.add("challenge");
        mPackageList.add("challenge(sam)");
        mPackageList.add("event");
        mPackageList.add("list view");

//        List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
//        for (int i = 0; i < packageInfoList.size(); i++){
//            PackageInfo pi = packageInfoList.get(i);
//            mPackageList.add(pi.packageName);
//        }

//        String version = "";
//        PackageInfo i = null;
//        try {
//            i = getPackageManager().getPackageInfo(getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        version = i.packageName;
//        Log.d("패키지명 : ", version);


        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        for (ActivityInfo ai : pi.activities){
//            Log.d("activity 명 : ", ai.name); // manifest 밑에 있는 activity 다가져옴
//        }

        // 패키지명이 같은 것 끼리 묶어줘야되는데...
        activityInfoList = new ArrayList<>();
        challengeInfoList = new ArrayList<>();
        challengeSamInfoList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        listViewInfoList = new ArrayList<>();

        for (ActivityInfo activityInfo : packageInfo.activities) {
            String name = activityInfo.name;

            Log.d("액티비티명", name);

            if (name.contains("activity")){
                activityInfoList.add(activityInfo);
            } else if (name.contains("challenge")) {
                challengeInfoList.add(activityInfo);
            } else if (name.contains("challenge_s")) {
                challengeSamInfoList.add(activityInfo);
            } else if (name.contains("event")) {
                eventInfoList.add(activityInfo);
            } else if (name.contains("listview")) {
                listViewInfoList.add(activityInfo);
            }
        }
//        mActivityMap = new HashMap<>();
//        mActivityMap.put("activity", activityInfoList);
//        mActivityMap.put("challenge", challengeInfoList);
//        mActivityMap.put("challenge_s", challengeSamInfoList);
//        mActivityMap.put("event", eventInfoList);
//        mActivityMap.put("listview", listViewInfoList);

        // Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, mPackageList);

        // View에 붙이기
        mStartListView.setAdapter(adapter);

        // 이벤트
        mStartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = null;

                switch (position) {
                    case 0:
                        intent = new Intent(StartActivity.this, MenuActivity.class);
                        intent.putExtra("activities", activityInfoList);
                        break;
                    case 1:
                        intent = new Intent(StartActivity.this, MenuActivity.class);
                        intent.putExtra("activities", challengeInfoList);
                        break;
                    case 2:
                        intent = new Intent(StartActivity.this, MenuActivity.class);
                        intent.putExtra("activities", challengeSamInfoList);
                        break;
                    case 3:
                        intent = new Intent(StartActivity.this, MenuActivity.class);
                        intent.putExtra("activities", eventInfoList);
                        break;
                    case 4:
                        intent = new Intent(StartActivity.this, MenuActivity.class);
                        intent.putExtra("activities", listViewInfoList);
                        break;
                    default:
                }

                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(StartActivity.this, "activity 없음", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
