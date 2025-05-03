package com.daclink.citypulse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Wishlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            db.activitiesDAO().getWishlistedEvent(true);
        });
        setContentView(R.layout.activity_wishlist);
    }
}