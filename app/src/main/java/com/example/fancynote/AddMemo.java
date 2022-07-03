package com.example.fancynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMemo extends AppCompatActivity {

    EditText et_title, et_content;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference(); // 데이터베이스 접근
    private DatabaseReference note = database.child("note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        note.child(et_title.getText().toString());
        note.child(et_title.getText().toString()).setValue(et_content.getText().toString());
        database.notifyAll();
        startActivity(new Intent(AddMemo.this, MainActivity.class));
    }
}