package com.daclink.citypulse.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Activities")
public class Activities {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "Title")
    private String title;
}
