
package com.baeflower.hello.challenge.challenge_18;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.baeflower.hello.R;

import java.io.IOException;
import java.util.List;

public class CameraFrameActivity extends ActionBarActivity implements SurfaceHolder.Callback {

    private SurfaceView mPreview;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // status bar(맨위에...)
        setContentView(R.layout.activity_camera_frame);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        mPreview = (SurfaceView) findViewById(R.id.sv_challenge18_camera);

        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera_frame, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();

        try {
            /*
                mCamera.setDisplayOrientation(0);

                mCamera.setPreviewDisplay(holder);

                int m_resWidth = mCamera.getParameters().getPictureSize().width;
                int m_resHeight = mCamera.getParameters().getPictureSize().height;
                Camera.Parameters parameters = mCamera.getParameters();

                // 아래 숫자를 변경하여 자신이 원하는 해상도로 변경한다
                // m_resWidth = 1280;
                // m_resHeight = 720;

                // 1280x720,800x480,720x480,640x480,576x432,480x320
                // parameter.setPreviewSize(1280, 720)


                parameters.setPictureSize(m_resWidth, m_resHeight);
                mCamera.setParameters(parameters);

                Camera.Parameters params = mCamera.getParameters();

                mCamera.setParameters(params);
            */

            mCamera.setPreviewDisplay(holder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.setDisplayOrientation(90);

        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> arSize = params.getSupportedPreviewSizes();

        if (arSize == null) {
            params.setPreviewSize(width, height);
        } else {
            int diff = 10000;

            Camera.Size opti = null;

            for (Camera.Size s : arSize) {
                if (Math.abs(s.height - height) < diff) {
                    diff = Math.abs(s.height - height);
                    opti = s;
                }
            }

            params.setPreviewSize(opti.width, opti.height);
        }

        params.setRotation(0);

        mCamera.setParameters(params);
        mCamera.startPreview(); // 실행
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.release(); // 메모리에서 해제
        mCamera = null;
    }



}
