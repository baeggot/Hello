package com.baeflower.hello.parsing;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baeflower.hello.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ParsingJsonActivity extends ActionBarActivity {

    private Button mBtnJsonParsing;
    private TextView mTvJsonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing_json);

        mBtnJsonParsing = (Button) findViewById(R.id.btn_json_parse);
        mTvJsonData = (TextView) findViewById(R.id.tv_parsed_json_data);


        mBtnJsonParsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTvJsonData();
            }
        });

    }

    private void setTvJsonData() {

        //JSON 데이터
        String jsonInfo = "{\"books\":[{\"genre\":\"소설\",\"price\":\"100\",\"name\":\"사람은 무엇으로 사는가?\",\"writer\":\"톨스토이\",\"publisher\":\"톨스토이 출판사\"},{\"genre\":\"소설\",\"price\":\"300\",\"name\":\"홍길동전\",\"writer\":\"허균\",\"publisher\":\"허균 출판사\"},{\"genre\":\"소설\",\"price\":\"900\",\"name\":\"레미제라블\",\"writer\":\"빅토르 위고\",\"publisher\":\"빅토르 위고 출판사\"}],\"persons\":[{\"nickname\":\"남궁민수\",\"age\":\"25\",\"name\":\"송강호\",\"gender\":\"남자\"},{\"nickname\":\"예니콜\",\"age\":\"21\",\"name\":\"전지현\",\"gender\":\"여자\"}]}";

        /*
        {
            "books": [
                {
                    "genre": "소설",
                    "price": "100",
                    "name": "사람은 무엇으로 사는가?",
                    "writer": "톨스토이",
                    "publisher": "톨스토이 출판사"
                },
                {
                    "genre": "소설",
                    "price": "300",
                    "name": "홍길동전",
                    "writer": "허균",
                    "publisher": "허균 출판사"
                },
                {
                    "genre": "소설",
                    "price": "900",
                    "name": "레미제라블",
                    "writer": "빅토르 위고",
                    "publisher": "빅토르 위고 출판사"
                }
            ],
            "persons": [
                {
                    "nickname": "남궁민수",
                    "age": "25",
                    "name": "송강호",
                    "gender": "남자"
                },
                {
                    "nickname": "예니콜",
                    "age": "21",
                    "name": "전지현",
                    "gender": "여자"
                }
            ]
        }
         */

        String strData = "";
        try {
            JSONObject jAr = new JSONObject(jsonInfo);

            // JSONArray는 1차원(계층 1) 배열만 받을 수 있음
            // 계층이 2 이상이면 JSONObject

//            for(int i=0; i < jAr.length(); i++) {
//                // 개별 객체를 하나씩 추출
//                JSONObject books = jAr.getJSONObject(i);
//                // 객체에서 데이터를 추출
//                strData += books.getString("genre") + " - " + books.getInt("price") + "\n";
//            }

        } catch (JSONException e) {
            Log.d("tag", "Parse Error");
        }

        mTvJsonData.setText(strData);
    }



}
