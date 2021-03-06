package com.example.fancynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordInsert extends AppCompatActivity {

    private EditText et_first,et_second,et_third,et_fourth;
    private TextView tv_info;
    private Button btn_setting;
    private String a,b;
    private final int REQUEST_SETTING_PW = 789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_insert);

        et_first = findViewById(R.id.et_first);
        et_second = findViewById(R.id.et_second);
        et_third = findViewById(R.id.et_third);
        et_fourth = findViewById(R.id.et_fourth);
        btn_setting = findViewById(R.id.btn_setting);
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        tv_info = findViewById(R.id.tv_info);




        et_first.addTextChangedListener(new CustomTextWatcher(et_first,et_first,et_second));
        et_second.addTextChangedListener(new CustomTextWatcher(et_first,et_second,et_third));
        et_third.addTextChangedListener(new CustomTextWatcher(et_second,et_third,et_fourth));
        et_fourth.addTextChangedListener(new CustomTextWatcher(et_third,et_fourth, et_fourth));

        btn_setting.setOnClickListener((v)->{
            if (et_first.getText().toString() != null && et_second.getText().toString() != null && et_third.getText().toString() != null && et_fourth.getText().toString() != null) {
                a = et_first.getText().toString() + et_second.getText().toString() + et_third.getText().toString() + et_fourth.getText().toString();
                vi.vibrate(200);
                tv_info.setText("??????????????? ?????? ??????????????????!");
                et_first.setText(null);
                et_second.setText(null);
                et_third.setText(null);
                et_fourth.setText(null);
                et_first.requestFocus();
                btn_setting.setOnClickListener((v1) -> {
                    if (et_first.getText().toString() != null && et_second.getText().toString() != null && et_third.getText().toString() != null && et_fourth.getText().toString() != null) {
                        b = et_first.getText().toString() + et_second.getText().toString() + et_third.getText().toString() + et_fourth.getText().toString();
                        if (a.equals(b)) {
                            Intent intent = new Intent(PasswordInsert.this, PwVerification.class);
                            intent.putExtra("settingPw", b);
                            startActivityForResult(intent, REQUEST_SETTING_PW);
                        }
                    }
                });
            } else {
                vi.vibrate(200);
                tv_info.setText("??????????????? ??????????????????");
                et_first.setText(null);et_second.setText(null);et_third.setText(null);et_fourth.setText(null);
                et_first.requestFocus();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PasswordInsert.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING_PW && resultCode == RESULT_OK) {
            Toast.makeText(this, "???????????? ????????? ??????????????? ?????????????????????!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PasswordInsert.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
}

class CustomTextWatcher implements TextWatcher {
    private View v,v1,v2;

    public CustomTextWatcher(View v, View v1,View v2) {
        this.v = v;
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        if (v.getId() == R.id.et_first) {
            if (text.length() == 1) {
                v1.clearFocus();
                v2.requestFocus();
            } else {
                v1.clearFocus();
                v.requestFocus();
            }
        }else if (v.getId() == R.id.et_second) {
            if (text.length() == 1) {
                v1.clearFocus();
                v2.requestFocus();
            } else {
                v1.clearFocus();
                v.requestFocus();
            }
        } else if (v.getId() == R.id.et_third) {
            if (text.length() == 1) {
                v1.clearFocus();
                v2.requestFocus();
            } else {
                v1.clearFocus();
                v.requestFocus();
            }
        } else if (v.getId() == R.id.et_fourth) {
            if (text.length() == 1) {
                v1.clearFocus();
                v2.requestFocus();
            } else {
                v1.clearFocus();
                v.requestFocus();
            }
        }

    }

}