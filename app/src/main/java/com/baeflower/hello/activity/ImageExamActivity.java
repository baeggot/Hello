package com.baeflower.hello.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baeflower.hello.R;


public class ImageExamActivity extends ActionBarActivity {

    private ImageView mImage1;
    private ImageView mImage2;

    private Button mImgDownBtn;
    private Button mImgUpBtn;

    // View 안에 선언된 인터페이스 (OnClickListener)... 인터페이스 메서드?!
    // 인터페이스 안에 있는 메소드는 추상메소드(구현부, 즉 바디는 없고 선언만 있는 인터페이스 메서드 존재)
    private View.OnClickListener listener;

    // 내가 한 방식대로 하려면 상수로 지정해 두는 것이 좋다
//    final int IMAGE_UP = 1;
//    final int IMAGE_DOWN = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_exam);

        mImgDownBtn = (Button) findViewById(R.id.btn_img_down);
        mImgUpBtn = (Button) findViewById(R.id.btn_img_up);

        // 리소스에서 그림 파일을 가져와서 BitmapDrawable 객체로 생성
        // 메모리에 bitmap을 하나만 가지고 있다가 위에다 연결/아래에 연결했다가 하는 것
        // visible을 쓰면 이미지를 2장을 올려놓고 보였다가 하는 것
        // 그래서 이런 경우 bitmap을 사용하는 것이 ... 메모리 사용에 좋다?!
        // 우리가 자원으로 쓸 이미지를 bitmap으로 가지고 있음
        // 그래서 같은 이미지로 비트맵 만들고 이미지 올리고 하는건 메모리 사용이 같은 듯
        // 다른 이미지로 하면 여튼 이런식으로 하는게 메모리 사용에 좋다
        mBitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.giraffe_1);

        // 이미지를 비트맵으로 디코딩 하는 건 생각보다 빠름
        // 비트맵에서 다시 jpg 같은 사진파일로 인코딩 하는건 좀 오래걸림

        // 그림파일은 비트맵이 순수 압축하지 않은 데이터(순수 그림 데이터)
        // jpg, png는 인코딩을 통해 (어떤 알고리즘들에 의해서) 압축한 것

        mImage1 = (ImageView) findViewById(R.id.img_1);
        mImage2 = (ImageView) findViewById(R.id.img_2);

//        mImgDownBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeImage(1);
//            }
//        });
//
//        mImgUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeImage(2);
//            }
//        });

        // 변수로 뺄 (리팩토할) 부분을 블록 지정한 후 오른쪽 버튼 클릭 - refactor -> extract -> field(전역변수), variable(지역변수)
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(v.getId());
            }
        };
        mImgDownBtn.setOnClickListener(listener);
        mImgUpBtn.setOnClickListener(listener);
    }


    private BitmapDrawable mBitmap;
    private void changeImage(int viewId){
        if (viewId == R.id.btn_img_down){
            // ImageView 에 BitmapDrawable 객체를 Set
            mImage2.setImageDrawable(mBitmap);
            mImage1.setImageDrawable(null);
        } else if (viewId == R.id.btn_img_up) {
            // ImageView 에 BitmapDrawable 객체를 Set
            mImage1.setImageDrawable(mBitmap);
            mImage2.setImageDrawable(null);
        }
    }

//    private void changeImage(int imageNum){
//        // 리소스에서 그림 파일을 가져와서 BitmapDrawable 객체로 생성
//        mBitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.giraffe_1);
//        mImage1 = (ImageView) findViewById(R.id.img_1);
//        mImage2 = (ImageView) findViewById(R.id.img_2);
//
//        if (imageNum == 1){
//            // ImageView 에 BitmapDrawable 객체를 Set
//            mImage2.setImageDrawable(mBitmap);
//            mImage1.setImageDrawable(null);
//        } else {
//            // ImageView 에 BitmapDrawable 객체를 Set
//            mImage1.setImageDrawable(mBitmap);
//            mImage2.setImageDrawable(null);
//        }
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_exam, menu);
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
