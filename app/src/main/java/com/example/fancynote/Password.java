package com.example.fancynote;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Password {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String pw;

    public Password(String pw) {
        this.pw = pw;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}