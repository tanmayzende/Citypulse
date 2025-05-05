package com.daclink.citypulse;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.citypulse.database.Activities;
import com.daclink.citypulse.database.ActivitiesAdapter;

import java.util.List;

public class Wishlist extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivitiesAdapter adapter;
    private Button btnDate;
    private boolean mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = findViewById(R.id.recyclerView);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Activities> activities = db.activitiesDAO().getAll();
            runOnUiThread(() -> {
                adapter = new ActivitiesAdapter(activities);
                recyclerView.setAdapter(adapter);
            });
        });


        btnDate = findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(v.getContext());
                    List<Activities> activities;
                    if (mode){
                        activities = db.activitiesDAO().sortDateAsc();
                    }
                    else{
                        activities = db.activitiesDAO().sortDateDesc();
                    }
                    mode = !mode;
                    runOnUiThread(() -> {
                        adapter = new ActivitiesAdapter(activities);
                        recyclerView.setAdapter(adapter);
                    });
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}