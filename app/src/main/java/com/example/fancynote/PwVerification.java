package com.example.fancynote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PwVerification extends AppCompatActivity {

    private PwDatabase database;
    private pwDAO pwDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_verification);



        // Splash Screen 에다가 로직을 잔뜩 짜게 될 경우 Animation 이 렉이 걸린다.
        // 그러므로 PwVerification.class 에다가 비밀번호가 있는지? 없는지? 에 관한 로직을 구현한다음
        // SplashScreen 에서는 그냥 StartActivityForResult 를 통해서 구현을 한 다음에
        // 반환을 하는 것으로 해서, MainActivity 또는, 잠금 해제 화면으로 갈 수 있도록 한다.
    }
}