package com.baeflower.hello.save.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.baeflower.hello.R;

/**
 * 저장 위치 :data/data/패키지명/Shared_prefs/어쩌구저쩌구.xml
 */
public class SharedPreferenceExam01Activity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox mCheckBox;

    //
    private static final String KEY_BACKUP = "backup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preference_exam01);

        mCheckBox = (CheckBox) findViewById(R.id.cb_backup);
        mCheckBox.setOnCheckedChangeListener(this);

    }

    /*
        onCreate 다음에 onResume
     */
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean backup = sharedPref.getBoolean(KEY_BACKUP, false); // key, default value
        mCheckBox.setChecked(backup);
    }

    private void setBackup(boolean set){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_BACKUP, set);
        editor.apply(); // commit 보다 apply를 쓰는 게 좋다. 이렇게 하면 저장!
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(getApplicationContext(), "check : " + isChecked, Toast.LENGTH_SHORT).show();

        setBackup(isChecked);


    }



}
