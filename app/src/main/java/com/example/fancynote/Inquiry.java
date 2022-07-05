package com.example.fancynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

// 문의하기 기능
public class Inquiry extends AppCompatActivity {

    TextView btn_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        btn_email = findViewById(R.id.btn_email);

        btn_email.setOnClickListener((v)->{
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("plain/text");
            String[] adress = {"dda2816@naver.com"};
            email.putExtra(Intent.EXTRA_EMAIL, adress);
            email.putExtra(Intent.EXTRA_SUBJECT,"");
            email.putExtra(Intent.EXTRA_TEXT,"문의 내용을 입력해주세요");
            startActivity(email);
        });
    }
}