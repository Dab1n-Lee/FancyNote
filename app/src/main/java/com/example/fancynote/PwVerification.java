package com.example.fancynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class PwVerification extends AppCompatActivity {

    EditText et_first,et_second,et_third, et_fourth;
    Button btn_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_verification);



        et_first = findViewById(R.id.et_first);
        et_second = findViewById(R.id.et_second);
        et_third = findViewById(R.id.et_third);
        et_fourth = findViewById(R.id.et_fourth);
        btn_setting = findViewById(R.id.btn_setting);

        et_first.addTextChangedListener(new CustomTextWatcher(et_second, et_first));
        et_second.addTextChangedListener(new CustomTextWatcher(et_third, et_first));
        et_third.addTextChangedListener(new CustomTextWatcher(et_fourth,et_second));

        btn_setting.setOnClickListener((v)->{

            if (et_first.getText().toString() != null && et_second.getText().toString() != null && et_third.getText().toString() != null && et_fourth.getText().toString() != null) {
                String a = et_first.getText().toString()+et_second.getText().toString()+et_third.getText().toString()+et_fourth.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("result", a);
                setResult(RESULT_OK, intent);
                finish();
            }

        });


    }
}