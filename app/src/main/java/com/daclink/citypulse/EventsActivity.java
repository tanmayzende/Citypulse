package com.daclink.citypulse;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.citypulse.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String city = getIntent().getStringExtra("city");
        fetchMockEvents(city);
    }

    private void fetchMockEvents(String city) {
        progressBar.setVisibility(View.VISIBLE);

        // Mock data
        List<Event> mockEvents = new ArrayList<>();
        mockEvents.add(new Event("Concert in " + city, "Music Hall", "2025-05-01"));
        mockEvents.add(new Event("Art Exhibition", "City Gallery", "2025-05-03"));
        mockEvents.add(new Event("Food Festival", "Downtown Plaza", "2025-05-05"));

        progressBar.setVisibility(View.GONE);

        if (!mockEvents.isEmpty()) {
            adapter = new EventAdapter(mockEvents);
            recyclerView.setAdapter(adapter);
        } else {
            showError("No events found for " + city);
        }
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }
}