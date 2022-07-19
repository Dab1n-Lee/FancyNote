package com.example.fancynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PwVerification extends AppCompatActivity {

    private PwDatabase database;
    private pwDAO pwDao;
    private EditText et_first,et_second,et_third, et_fourth;
    private Button btn_setting;
    private String a;


    @Override
    public void onBackPressed() {
        setResult(RESULT_FIRST_USER);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_verification);

        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        /** 데이터베이스 구현부 **/
        database = Room.databaseBuilder(getApplicationContext(), PwDatabase.class, "PwDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        pwDao = database.pwDao();
        /** 데이터베이스 구현부 **/


        /** Get View Reference **/
        et_first = findViewById(R.id.et_first);
        et_second = findViewById(R.id.et_second);
        et_third = findViewById(R.id.et_third);
        et_fourth = findViewById(R.id.et_fourth);
        btn_setting = findViewById(R.id.btn_setting);
        /** Get View Reference **/

        /** 비밀번호를 설정 및 해제를 하기 위한 get Intent 구현부 **/
        Intent intent = getIntent();
        if (intent.getStringExtra("request1") != null) {
            if (pwDao.getPwAll().isEmpty()) {
                setResult(RESULT_OK);
            }else {
                setResult(RESULT_CANCELED);
            }
            finish();
        } else if (intent.getStringExtra("settingPw") != null) {
            String a = intent.getStringExtra("settingPw");
            Password pw = new Password(a);
            pwDao.setInsert(pw);
            setResult(RESULT_OK);
            finish();
        } else if (intent.getStringExtra("request2")!=null) {
            if (pwDao.getPwAll().get(0).getPw() != null) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        } else if (intent.getStringExtra("requestRemove") != null) {
            String a = intent.getStringExtra("requestRemove");
            Password pw = new Password(a);
            pwDao.setDelete(pw);
            setResult(RESULT_OK);
            finish();
        }




        /** 비밀번호를 설정 및 해제를 하기 위한 get Intent 구현부 **/

        /** Text Watcher 구현부 **/
        et_first.addTextChangedListener(new CustomTextWatcher(et_first,et_first,et_second));
        et_second.addTextChangedListener(new CustomTextWatcher(et_first,et_second,et_third));
        et_third.addTextChangedListener(new CustomTextWatcher(et_second,et_third,et_fourth));
        et_fourth.addTextChangedListener(new CustomTextWatcher(et_third,et_fourth, et_fourth));
        /** Text Watcher 구현부 **/

        /** SplashScreen.class 에서 MainActivity.class로 넘어가기 이전 비밀번호 검증부 **/
        // 1차로 데이터베이스에 패스워드가 없을 경우 Canceld 반환
        if (pwDao.getPwAll().isEmpty()) {
            setResult(RESULT_CANCELED);
            finish();
        }

        // 패스워드가 있으므로 변화가 없음.
        btn_setting.setOnClickListener((v)->{
            if (et_first.getText().toString() != null && et_second.getText().toString() != null && et_third.getText().toString() != null && et_fourth.getText().toString() != null) {
                a = et_first.getText().toString()+et_second.getText().toString()+et_third.getText().toString()+et_fourth.getText().toString();
                if (pwDao.getPwAll().get(0).getPw().equals(a)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        /** SplashScreen.class 에서 MainActivity.class로 넘어가기 이전 비밀번호 검증부 **/

    }
}