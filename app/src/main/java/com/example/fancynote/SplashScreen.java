package com.example.fancynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    PwDatabase database;
    pwDAO pwDao;
    private TextView appname;
    private LottieAnimationView lottie;
    private final int REQUEST_PW_verify = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        database = Room.databaseBuilder(getApplicationContext(), PwDatabase.class, "RoomDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        pwDao = database.pwDao();


        appname = findViewById(R.id.appname);
        lottie = findViewById(R.id.lottie);

        appname.animate().translationY(-1700).setDuration(2700).setStartDelay(0);
        lottie.animate().setDuration(2700).setStartDelay(2900);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pwDao.getPwAll().isEmpty()) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Intent intent = new Intent(SplashScreen.this, PwVerification.class);
                    startActivityForResult(intent, REQUEST_PW_verify);
                }
            }
        }, 5000);

        Intent intent = getIntent();
        if (intent.getStringExtra("settingPw") != null) {
            String a = intent.getStringExtra("settingPw");
            Password pw = new Password(a);
            pwDao.setInsert(pw);
            setResult(RESULT_OK);
            finish();
        } else if (intent.getStringExtra("removeRequest") != null) {
            String b = intent.getStringExtra("removeRequest");
            Password pw = new Password(b);
            pwDao.setDelete(pw);
            setResult(RESULT_OK);
            finish();
        }



        if (intent.getStringExtra("request") != null) {
            if (pwDao.getPwAll().get(0).getPw() == null) {
                setResult(RESULT_CANCELED);
            } else {
                setResult(RESULT_OK);
            }
            finish();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PW_verify && resultCode == RESULT_OK && data != null) {
            // Thread 에서 명시한 비밀번호 검증 Class 로 이동, 만약에 데이터베이스 내에 있는 패스워드랑
            // 입력받은 패스워드가 같을 경우 MainActivity로 이동.
            String pw = data.getStringExtra("result");
            if (pwDao.getPwAll().get(0).getPw().equals(pw)) {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finishAffinity();
            }
        }
    }
}
