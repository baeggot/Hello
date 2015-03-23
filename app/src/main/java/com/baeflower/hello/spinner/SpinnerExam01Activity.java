package com.baeflower.hello.spinner;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.baeflower.hello.R;

public class SpinnerExam01Activity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private String[] items = {"배꽃", "그리고", "솔"};

    private Spinner spinner;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_exam01);

        textView = (TextView) findViewById(R.id.textView_in_spinnerExam);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        // getApplicationContext로 쓰다가 안되면 this로 쓰는걸로!
        // 뭐... 이게 더 좋다고 하시넹
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        textView.setText(items[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textView.setText("");
    }
}
