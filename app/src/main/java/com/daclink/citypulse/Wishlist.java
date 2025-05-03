package com.daclink.citypulse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.citypulse.database.Activities;
import com.daclink.citypulse.database.ActivitiesDAO;
import com.daclink.citypulse.model.EventItem;

import java.util.List;

public class Wishlist extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        //bug?
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Activities> activities = db.activitiesDAO().getAll();
            List<EventItem> events = null;
            for (Activities activity : activities){
                // convert Activities to EventItem GLHF
                // put event item in events variable
            }
            adapter = new EventItemAdapter(events);
            recyclerView.setAdapter(adapter);
        });
    }
}