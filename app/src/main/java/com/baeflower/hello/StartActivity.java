
package com.baeflower.hello;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

    private ArrayList<ActivityInfo> activityInfoList;
    private ArrayList<ActivityInfo> challengeInfoList;
    private ArrayList<ActivityInfo> eventInfoList;
    private ArrayList<ActivityInfo> listViewInfoList;
    private ArrayList<ActivityInfo> spinnerInfoList;
    private ArrayList<ActivityInfo> gridViewInfoList;
    private ArrayList<ActivityInfo> threadInfoList;
    private ArrayList<ActivityInfo> parsingInfoList;
    private ArrayList<ActivityInfo> mapsInfoList;
    private ArrayList<ActivityInfo> bitmapInfoList;
    private ArrayList<ActivityInfo> styleInfoList;
    private ArrayList<ActivityInfo> canvasInfoList;
    private ArrayList<ActivityInfo> saveInfoList;
    private ArrayList<ActivityInfo> chatInfoList;
    private ArrayList<ActivityInfo> cameraInfoList;
    private ArrayList<ActivityInfo> multimediaInfoList;
    private ArrayList<ActivityInfo> contentProviderInfoList;
    private ArrayList<ActivityInfo> locationInfoList;




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
        mPackageList.add("event");
        mPackageList.add("list view");
        mPackageList.add("spinner");
        mPackageList.add("grid view");
        mPackageList.add("thread");
        mPackageList.add("parsing");
        mPackageList.add("maps");
        mPackageList.add("bitmap");
        mPackageList.add("style");
        mPackageList.add("canvas");
        mPackageList.add("save");
        mPackageList.add("chat");
        mPackageList.add("camera");
        mPackageList.add("multimedia");
        mPackageList.add("content provider");
        mPackageList.add("location");

        // List<PackageInfo> packageInfoList =
        // getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
        // for (int i = 0; i < packageInfoList.size(); i++){
        // PackageInfo pi = packageInfoList.get(i);
        // mPackageList.add(pi.packageName);
        // }

        // String version = "";
        // PackageInfo i = null;
        // try {
        // i = getPackageManager().getPackageInfo(getPackageName(), 0);
        // } catch (PackageManager.NameNotFoundException e) {
        // e.printStackTrace();
        // }
        // version = i.packageName;
        // Log.d("패키지명 : ", version);

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 패키지명이 같은 것 끼리 묶어줘야되는데...
        activityInfoList = new ArrayList<>();
        challengeInfoList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        listViewInfoList = new ArrayList<>();
        spinnerInfoList = new ArrayList<>();
        gridViewInfoList = new ArrayList<>();
        threadInfoList = new ArrayList<>();
        parsingInfoList = new ArrayList<>();
        mapsInfoList = new ArrayList<>();
        bitmapInfoList = new ArrayList<>();
        styleInfoList = new ArrayList<>();
        canvasInfoList = new ArrayList<>();
        saveInfoList = new ArrayList<>();
        chatInfoList = new ArrayList<>();
        cameraInfoList = new ArrayList<>();
        multimediaInfoList = new ArrayList<>();
        contentProviderInfoList = new ArrayList<>();
        locationInfoList = new ArrayList<>();


        for (ActivityInfo activityInfo : packageInfo.activities) {
            String name = activityInfo.name;

            if (name.contains("activity")) {
                activityInfoList.add(activityInfo);
            } else if (name.contains("challenge")) {
                challengeInfoList.add(activityInfo);
            } else if (name.contains("event")) {
                eventInfoList.add(activityInfo);
            } else if (name.contains("listview")) {
                listViewInfoList.add(activityInfo);
            } else if (name.contains("spinner")) {
                spinnerInfoList.add(activityInfo);
            } else if (name.contains("grid")) {
                gridViewInfoList.add(activityInfo);
            } else if (name.contains("thread")) {
                threadInfoList.add(activityInfo);
            } else if (name.contains("parsing")) {
                parsingInfoList.add(activityInfo);
            } else if (name.contains("maps")) {
                mapsInfoList.add(activityInfo);
            } else if (name.contains("bitmap")) {
                bitmapInfoList.add(activityInfo);
            } else if (name.contains("style")) {
                styleInfoList.add(activityInfo);
            } else if (name.contains("canvas")) {
                canvasInfoList.add(activityInfo);
            } else if (name.contains("save")) {
                saveInfoList.add(activityInfo);
            } else if (name.contains("chat")) {
                chatInfoList.add(activityInfo);
            } else if (name.contains("camera")) {
                cameraInfoList.add(activityInfo);
            } else if (name.contains("multimedia")) {
                multimediaInfoList.add(activityInfo);
            } else if (name.contains("contentProvider")) {
                contentProviderInfoList.add(activityInfo);
            } else if (name.contains("location")) {
                locationInfoList.add(activityInfo);
            }
        }

        // Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, mPackageList);

        // View에 붙이기
        mStartListView.setAdapter(adapter);

        // 이벤트
        mStartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(StartActivity.this, MenuActivity.class);
                switch (position) {
                    case 0:
                        intent.putExtra("activities", activityInfoList);
                        break;
                    case 1:
                        intent.putExtra("activities", challengeInfoList);
                        break;
                    case 2:
                        intent.putExtra("activities", eventInfoList);
                        break;
                    case 3:
                        intent.putExtra("activities", listViewInfoList);
                        break;
                    case 4:
                        intent.putExtra("activities", spinnerInfoList);
                        break;
                    case 5:
                        intent.putExtra("activities", gridViewInfoList);
                        break;
                    case 6:
                        intent.putExtra("activities", threadInfoList);
                        break;
                    case 7:
                        intent.putExtra("activities", parsingInfoList);
                        break;
                    case 8:
                        intent.putExtra("activities", mapsInfoList);
                        break;
                    case 9:
                        intent.putExtra("activities", bitmapInfoList);
                        break;
                    case 10:
                        intent.putExtra("activities", styleInfoList);
                        break;
                    case 11:
                        intent.putExtra("activities", canvasInfoList);
                        break;
                    case 12:
                        intent.putExtra("activities", saveInfoList);
                        break;
                    case 13:
                        intent.putExtra("activities", chatInfoList);
                        break;
                    case 14:
                        intent.putExtra("activities", cameraInfoList);
                        break;
                    case 15:
                        intent.putExtra("activities", multimediaInfoList);
                        break;
                    case 16:
                        intent.putExtra("activities", contentProviderInfoList);
                        break;
                    case 17:
                        intent.putExtra("activities", locationInfoList);
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
