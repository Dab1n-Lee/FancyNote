package com.example.fancynote;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Password.class},exportSchema = true,version = 1)
public abstract class PwDatabase extends RoomDatabase {


    public abstract pwDAO pwDao();

}
