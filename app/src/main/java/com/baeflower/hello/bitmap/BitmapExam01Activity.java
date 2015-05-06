package com.baeflower.hello.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import com.baeflower.hello.R;

public class BitmapExam01Activity extends ActionBarActivity implements View.OnClickListener {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_exam01);

        mImageView = (ImageView) findViewById(R.id.iv_bitmap_img_exam);

        // 색 채우기
        findViewById(R.id.btn_bitmap_color_fill).setOnClickListener(this);

        // 이미지 로드
        findViewById(R.id.btn_bitmap_img_load).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bitmap_color_fill :
                fillColor();
                break;
            case R.id.btn_bitmap_img_load :
                imageLoad();
        }
    }

    private void fillColor() {
        Bitmap bitmap = Bitmap.createBitmap(500, 700, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                bitmap.setPixel(x, y, Color.YELLOW);
            }
        }
        mImageView.setImageBitmap(bitmap);
    }

    private void imageLoad() {
        // 리소스 로드
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.giraffe_1);

        // scale
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 300, 500, true);

        // 메모리 관리?

        // MediaStore ?

        mImageView.setImageBitmap(bitmap);
    }
}
