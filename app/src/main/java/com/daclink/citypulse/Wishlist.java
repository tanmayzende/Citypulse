package com.daclink.citypulse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.citypulse.database.Activities;
import com.daclink.citypulse.database.ActivitiesAdapter;

import java.util.List;

public class Wishlist extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Activities> activities = db.activitiesDAO().getAll();
            adapter = new ActivitiesAdapter(activities);
            recyclerView.setAdapter(adapter);
        });
    }
}