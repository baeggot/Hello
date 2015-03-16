package com.baeflower.hello.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.baeflower.hello.R;


public class EditTextActivity extends ActionBarActivity {

    private static final String TAG = EditTextActivity.class.getSimpleName();
    private EditText mInputEditText;
    private EditText mOutputEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        mInputEditText = (EditText) findViewById(R.id.input_edit_text);
        mOutputEditText = (EditText) findViewById(R.id.output_edit_text);

        // mInputEditText.setVerticalScrollBarEnabled(true);

        /*

        mInputEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // event.getAction () : ??????? ????? ?´?, ???? action?? ??
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    // ?????? ?? ???? enter???
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        textChange();
                        return true;
                    }
                }
                return false;
            }
        });
         */


        // EditText
        // inputText(191p)
        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged");
//                String inputString = mInputEditText.getText().toString();
//                String inputString = s.toString();
//                mOutputEditText.setText(s.toString());
                  mOutputEditText.setText(s);
            }
        });

    }

    /**
     * mInputEditText ?? ?????? mOutputEditText ?? set
     */
    //
    private void textChange() {
        // mInputEditText.getText()는 Editable을 리턴
        // setText는 CharacterSequence 인터페이스를 받는데 Editable이 CharacterSequence 인터페이스를 상속받고 있기 때문에
        // setText에서 Editable을 받을 수 있음

        mOutputEditText.setText(mInputEditText.getText());
    }


}
