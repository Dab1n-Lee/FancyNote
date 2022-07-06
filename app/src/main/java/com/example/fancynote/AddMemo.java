package com.example.fancynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMemo extends AppCompatActivity {

    EditText et_title, et_content;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference(); // 데이터베이스 접근

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);


    }

    @Override
    public void onBackPressed() {
        if (et_title.getText().toString() != null && et_content.getText().toString() != null) {
            String title = et_title.getText().toString();
            String content = et_content.getText().toString();
            String id = database.push().getKey();

            MemoItem memoItem = new MemoItem(title, content,"");
            database.child("note").child(id).setValue(memoItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddMemo.this, "성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Intent intent = new Intent(AddMemo.this, MainActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
            finishAffinity();
        } else {
            Toast.makeText(this, "값을 저장해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddMemo.this, MainActivity.class);
            startActivity(intent);
        }
        
        super.onBackPressed();
    }
}