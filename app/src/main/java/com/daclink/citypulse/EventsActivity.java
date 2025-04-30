package com.daclink.citypulse;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    // Temporarily using a generic adapter to avoid build errors
    private RecyclerView.Adapter<?> adapter;

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

        // Mock data until real API and adapter are added
        List<String> mockEvents = new ArrayList<>();
        mockEvents.add("Concert in " + city + " at Music Hall on 2025-05-01");
        mockEvents.add("Art Exhibition at City Gallery on 2025-05-03");
        mockEvents.add("Food Festival at Downtown Plaza on 2025-05-05");

        progressBar.setVisibility(View.GONE);

        if (!mockEvents.isEmpty()) {
            adapter = new RecyclerView.Adapter<>() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
                    TextView tv = new TextView(parent.getContext());
                    return new RecyclerView.ViewHolder(tv) {
                    };
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    ((TextView) holder.itemView).setText(mockEvents.get(position));
                }

                @Override
                public int getItemCount() {
                    return mockEvents.size();
                }
            };
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