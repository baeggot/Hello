
package com.baeflower.hello.listview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.baeflower.hello.listview.adapter.CustomAdapter;

import java.util.ArrayList;

/*
    전체적인 레이아웃에 대한 정보는 여기서 수정
 */
public class ListViewExam02Activity extends ActionBarActivity {


    private ArrayList<String> mNameList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_exam01);

        mListView = (ListView) findViewById(R.id.listView);

        // Data
        mNameList = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            mNameList.add("배경수");
            mNameList.add("김경숙");
            mNameList.add("배꽃그리고솔");
            mNameList.add("배우리차돌");
            mNameList.add("배정용");
        }


        /*
         * Adapter 준비 T, E (아무형태나 다 들어온다), ? view 하나에 대한 xml 필요 그냥 R. 을 하면 내가 만든
         * 리소스를 쓰는거고 android.R. 을 하면 안드로이드가 만든 리소스를 쓰는 것
         *
         * layout 부르는 자리에 0을 넣었는데, 이유는 getView에서 커스터마이징한 layout을 부르기 때문에 여기서 부르는건 의미가 없어서 그냥 아무거나 넘김
         */

        final CustomAdapter adapter = new CustomAdapter<String>(getApplicationContext(), 0, mNameList);


        // View에 붙이기
        mListView.setAdapter(adapter);

        // 이벤트
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.getItem(position);

                Toast.makeText(
                        ListViewExam02Activity.this,
                        "position : " + position + ", id : " + id + ", text : "
                                + mNameList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        //줄 없애기
        mListView.setDivider(null);
    }

}
