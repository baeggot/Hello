
package com.baeflower.hello.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baeflower.hello.R;

public class ActivityExamActivity extends ActionBarActivity {

    private static final String TAG = ActivityExamActivity.class.getSimpleName();
    public static final int REQUEST_CODE_A = 0;

    private Button mMoveBtn;
    private Button mDataMoveBtn;
    private EditText mDataEditText;

    private Button mBtnPhone;
    private EditText mEtPhoneNm;

    /*

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_exam);

        Log.d(TAG, "onCreate");

        mMoveBtn = (Button) findViewById(R.id.move_activity_btn);
        mDataMoveBtn = (Button) findViewById(R.id.move_data_btn);
        mDataEditText = (EditText) findViewById(R.id.dataEditText);

        // 전화걸기
        mBtnPhone = (Button) findViewById(R.id.btn_phone);
        mEtPhoneNm = (EditText) findViewById(R.id.editText_phone_number);


        mMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent : 안드로이드에서 어떤 정보를 담고 있는 객체 중에 하나(책, 어디?)
                // 점프 뛸 액티비티의 정보를 Intent에 담는다
                // Context : 앱 전체에 대한 정보(프로젝트), 시스템 전체적인 자원을 처리하는 것들은 context를 꼭
                // 받는다
                // Intent는 내가 담을 만한 정보를 정해서 준다(모든 소스를 던질 수 있는데...?)
                // activity가 context를 상속을 받았기 때문에 getAppliactionContext 대신 this를
                // 넘겨도 되는데 여긴 안되네?
                Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        /*
        데이터 실어서 보내기
         */
        mDataMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
                String value = mDataEditText.getText().toString();
                intent.putExtra("key", value);
                startActivity(intent);
            }
        });

        findViewById(R.id.get_result_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
                // requestCode : A의 입장에서 B가 나를 불렀는지 C가 나를 불렀는지를 requestCode를 통해서 알 수있다
                startActivityForResult(intent, REQUEST_CODE_A);
            }
        });

        mBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String phoneNm = mEtPhoneNm.getText().toString();
//                startActivity(intent.ACTION_DIAL, Uri.parse(phoneNm));
            }
        });


        // 다이얼로그 여는 버튼에 클릭 이벤트 리스터 연결
        // 다이얼로그를 만드는 방법은 많은데 이 방법이 제일 좋다고 하심
        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    new AleretDialog.Builder(getApplicationContext()) 넘겼는데 에러나네
                    일단 기본적으로 저거 쓰면 되긴 하는데
                    ActivityExamActivity.this 넘겨도 동작함(이게 context를 상속받고 있으니깐...)
                    근데 또 이렇게 this를 넘기면 안되는 경우가 있음
                    Context에 대해서 공부하면 아려나?

                    interface안에 onClick안에 있는거라서 그냥 this를 쓰면 new View.OnClickListener를 가리키기 때문에
                    ActivityExamActivity.this로 쓴다

                    dialog에 context 넘겼을 때 문제있으면 this로 넘겨봐라
                    왜 그런지는 인터넷 찾으면..
                 */
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityExamActivity.this);
                dialog.setMessage("이것은 dialog다!!!");
                dialog.setTitle("이것은 title이다!!!");
                // 버튼 추가
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setNegativeButton("취소", null);
                dialog.setNeutralButton("중간", null); // 버튼 세개까지 가능(네개 부터는 구현), 이쁘게 하고 싶다 -> 구현
                dialog.create(); // 만들고
                dialog.show(); // 보여주기
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_A && resultCode == RESULT_OK){
            mDataEditText.setText(data.getStringExtra("data"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }






    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

}
