
package com.baeflower.hello.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baeflower.hello.R;

public class FrameLayoutActivity extends ActionBarActivity {

    private Button mImgChangeBtn; // m :
    private ImageView mImage1;
    private ImageView mImage2;

    /*
     * frame layout activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_layout);

        mImgChangeBtn = (Button) findViewById(R.id.img_change_btn);
        mImage1 = (ImageView) findViewById(R.id.img_giraffe_1);
        mImage2 = (ImageView) findViewById(R.id.img_giraffe_2);

        //
        mImgChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mImage1.getVisibility() : int
                // View.VISIBLE :
                if (mImage1.getVisibility() == View.VISIBLE){
                    mImage1.setVisibility(View.INVISIBLE);
                    mImage2.setVisibility(View.VISIBLE);
                } else {
                    mImage1.setVisibility(View.VISIBLE);
                    mImage2.setVisibility(View.INVISIBLE);
                }
            }
        });

    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frame_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
