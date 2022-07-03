package com.example.fancynote;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface pwDAO {

    @Insert
    void setInsert(Password pw);

    @Update
    void setUpdate(Password pw);

    @Delete
    void setDelete(Password pw);

    @Query("SELECT * FROM Password")
    List<Password> getPwAll();

}
