package com.example.fancynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Remove_Password extends AppCompatActivity {

    EditText et_first,et_second,et_third, et_fourth;
    Button btn_setting;
    TextView tv_info;
    private final int REQUEST_REMOVE_PASSWORD = 16435;
    String a,b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_password);

        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        et_first = findViewById(R.id.et_first);
        et_second = findViewById(R.id.et_second);
        et_third = findViewById(R.id.et_third);
        et_fourth = findViewById(R.id.et_fourth);
        tv_info = findViewById(R.id.tv_info);
        btn_setting = findViewById(R.id.btn_setting);

        et_first.addTextChangedListener(new CustomTextWatcher(et_second, et_first));
        et_second.addTextChangedListener(new CustomTextWatcher(et_third, et_first));
        et_third.addTextChangedListener(new CustomTextWatcher(et_fourth, et_second));

        btn_setting.setOnClickListener((v)->{

            if (et_first.getText().toString() != null && et_second.getText().toString() != null && et_third.getText().toString() != null && et_fourth.getText().toString() != null) {
                a = et_first.getText().toString() + et_second.getText().toString() + et_third.getText().toString() + et_fourth.getText().toString();
                vi.vibrate(200);
                tv_info.setText("비밀번호를 다시 한번 입력해주세요");
                et_first.setText(null); et_second.setText(null); et_third.setText(null); et_fourth.setText(null);
                et_first.requestFocus();
                btn_setting.setOnClickListener((v1)->{
                    if (et_first.getText().toString() != null && et_second.getText().toString() != null && et_third.getText().toString() != null && et_fourth.getText().toString() != null) {
                        b = et_first.getText().toString() + et_second.getText().toString() + et_third.getText().toString() + et_fourth.getText().toString();
                        if (a.equals(b)) {
                            Intent intent = new Intent(Remove_Password.this, SplashScreen.class);
                            intent.putExtra("removeRequest", b);
                            startActivityForResult(intent, REQUEST_REMOVE_PASSWORD);
                        } else {
                            Toast.makeText(this, "비밀번호를 다르게 입력하셨습니다. 처음부터 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                            vi.vibrate(200);
                            Intent intent = new Intent(Remove_Password.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }
                });

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_REMOVE_PASSWORD && resultCode == RESULT_OK) {
            Toast.makeText(this, "비밀번호가 정상적으로 해제되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Remove_Password.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

    }
}