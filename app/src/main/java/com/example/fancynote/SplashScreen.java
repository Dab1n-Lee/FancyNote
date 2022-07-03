package com.example.fancynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    PwDatabase database;
    pwDAO pwDao;
    private TextView appname;
    private LottieAnimationView lottie;
    private final int REQUEST_PW_SETTING = 123;



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
                    startActivity(intent);
                    finishAffinity();
                }
            }
        }, 5000);

    }

}