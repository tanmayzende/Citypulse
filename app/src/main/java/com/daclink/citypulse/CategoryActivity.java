package com.daclink.citypulse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.citypulse.AppDatabase;
import com.daclink.citypulse.model.CachedEvent;
import com.daclink.citypulse.model.CachedEventDao;
import com.daclink.citypulse.model.EventItem;
import com.daclink.citypulse.model.TicketmasterResponse;
import com.daclink.citypulse.network.ApiService;
import com.daclink.citypulse.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private EventItemAdapter adapter;

    private static final String API_KEY = "A1igDbj4DxNkXX0VGnIb8YXKhEROppl7";
    private CachedEventDao cachedEventDao;
    private ExecutorService executorService;

    private String city;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        cachedEventDao = db.cachedEventDao();
        executorService = AppDatabase.databaseWriteExecutor;

        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        category = intent.getStringExtra("category");

        loadEvents(city, category);
    }

    private void loadEvents(String city, String category) {
        progressBar.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            List<CachedEvent> cached = cachedEventDao.getEventsByCityAndCategory(city, category);
            if (cached != null && !cached.isEmpty()) {
                runOnUiThread(() -> {
                    List<EventItem> items = new ArrayList<>();
                    for (CachedEvent e : cached) {
                        items.add(new EventItem(e.getTitle(), e.getVenue(), e.getDate()));
                    }
                    adapter = new EventItemAdapter(items);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                });
            } else {
                fetchEventsFromApi(city, category);
            }
        });
    }

    private void fetchEventsFromApi(String city, String category) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<TicketmasterResponse> call = apiService.getEventsByCategory(API_KEY, city, category, 10);

        call.enqueue(new Callback<TicketmasterResponse>() {
            @Override
            public void onResponse(Call<TicketmasterResponse> call, Response<TicketmasterResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().getEvents() != null) {
                    List<EventItem> events = response.body().getEvents();
                    adapter = new EventItemAdapter(events);
                    recyclerView.setAdapter(adapter);

                    List<CachedEvent> toCache = new ArrayList<>();
                    for (EventItem e : events) {
                        toCache.add(new CachedEvent(city, category, e.getName(), e.getVenueName(), e.getLocalDate()));
                    }

                    executorService.execute(() -> {
                        cachedEventDao.deleteEventsForCityAndCategory(city, category);
                        cachedEventDao.insertAll(toCache);
                    });
                } else {
                    showError("No events found for " + category);
                }
            }

            @Override
            public void onFailure(Call<TicketmasterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Failed to load events: " + t.getMessage());
                Log.e("CategoryActivity", "API call failed", t);
            }
        });
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }
}