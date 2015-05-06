
package com.baeflower.hello.save.file;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baeflower.hello.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = FileActivity.class.getSimpleName();
    private EditText mFileNameEditText;
    private Button mBtnInternal;
    private Button mBtnExternal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        mFileNameEditText = (EditText) findViewById(R.id.et_filename);
        mBtnInternal = (Button) findViewById(R.id.btn_internal_save);
        mBtnExternal = (Button) findViewById(R.id.btn_external_save);

        mBtnInternal.setOnClickListener(this);
        mBtnExternal.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_internal_save:
                saveInternal();

                break;
            case R.id.btn_external_save:
                saveExternal();
                break;
        }
    }

    //
    private void saveInternal() {

        String fileName = "internalTest.txt";

        // 1. 생성
        File file = new File(getFilesDir(), fileName);

        try {
            if (!file.exists()) {
                // 2. 저장
                file.createNewFile();

                // 3. 데이터 밀어넣기
            /*
                // byte 배열로 저장
                FileOutputStream outputStream;

                outputStream = openFileOutput(file.getAbsolutePath(), Context.MODE_PRIVATE);
                outputStream.write(str.getBytes());
                outputStream.close();
            */

            /*
                텍스트 넣기 위해서
             */
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.println("The first line");
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // mnt / shell / emulated / 0 / Android / data / 패키지명 / files / 여기에 파일 생성
    private void saveExternal() {
        if (isExternalStorageWritable()) {
            Toast.makeText(getApplicationContext(), "path : " + getExternalFilesDir(null), Toast.LENGTH_SHORT).show();
            File file = new File(getExternalFilesDir(null), mFileNameEditText.getText().toString());

            FileWriter fileWriter = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                // true = append
                fileWriter = new FileWriter(file, true); // 뒤에 true로 하면 file이 덮어씌워지지 않고 append 된다... 새로생성?
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("append data");
                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
