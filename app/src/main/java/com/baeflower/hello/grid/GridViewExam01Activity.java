package com.baeflower.hello.grid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.baeflower.hello.R;

import java.util.ArrayList;

public class GridViewExam01Activity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private GridView mGridView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_exam01);

        // init
        mGridView = (GridView) findViewById(R.id.gridView);

        // Data
        arrayList = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            arrayList.add(String.valueOf(i));
        }

        // Adapter
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);

        // View
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(this);



    }// onCreate

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "position : " + position + ", data : " + arrayList.get(position), Toast.LENGTH_SHORT).show();

        // 이렇게 데이터 바꿔도 반영이 안됨
        // 반영하기 위해서는 adapter에 알려줘야된다!
        arrayList.set(position, Integer.parseInt(arrayList.get(position)) + 10 + "");

        // Data에 변화를 줬을 때 adapter에 알려주는 것
        // 이렇게 하면 adapter가 data가 수정됐다는 걸 알고 adapter가 view의 getView를 호출해서 데이터 바뀜
        adapter.notifyDataSetChanged();
    }
}
