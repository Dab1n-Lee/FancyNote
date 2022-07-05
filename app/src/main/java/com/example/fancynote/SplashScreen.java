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

    private TextView appname;
    private LottieAnimationView lottie;
    private final int REQUEST_PW_verify = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appname = findViewById(R.id.appname);
        lottie = findViewById(R.id.lottie);

        appname.animate().translationY(-1700).setDuration(2700).setStartDelay(0);
        lottie.animate().setDuration(2700).setStartDelay(2900);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, PwVerification.class);
                startActivityForResult(intent, REQUEST_PW_verify);
            }
        }, 5000);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PW_verify) {
            if (resultCode == RESULT_CANCELED) {
                // 패스워드가 없으므로 PwVerify 에서 그냥 바로 CANCEL 반환된 케이스
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            } else if (resultCode == RESULT_OK) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            } else {
                finishAffinity();
            }
        }
    }
}
