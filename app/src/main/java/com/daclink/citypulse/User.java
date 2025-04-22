package com.daclink.citypulse;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    private String username;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "is_admin")
    private boolean isAdmin;

    // Constructor
    public User(@NonNull String username, @NonNull String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}