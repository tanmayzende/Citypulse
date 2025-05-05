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

import com.daclink.citypulse.database.Activities;
import com.daclink.citypulse.model.CachedEvent;
import com.daclink.citypulse.model.EventItem;
import com.daclink.citypulse.model.TicketmasterResponse;
import com.daclink.citypulse.network.ApiService;
import com.daclink.citypulse.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private EventItemAdapter adapter;

    private static final String API_KEY = "A1igDbj4DxNkXX0VGnIb8YXKhEROppl7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String category = intent.getStringExtra("category");

        fetchEvents(city, category);
    }

    private void fetchEvents(String city, String category) {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<TicketmasterResponse> call = apiService.getEventsByCategory(API_KEY, city, category, 10);

        call.enqueue(new Callback<TicketmasterResponse>() {
            @Override
            public void onResponse(Call<TicketmasterResponse> call, Response<TicketmasterResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().getEvents() != null) {
                    List<EventItem> events = response.body().getEvents();
                    Log.d("CategoryActivity", "API call succeeded");
                    Log.d("CategoryActivity", "Number of events: " + events.size());

                    adapter = new EventItemAdapter(events, city, category);
                    recyclerView.setAdapter(adapter);

                    List<CachedEvent> toCache = new ArrayList<>();
                    for (EventItem e : events) {
                        CachedEvent ce = fromEventItem(e, city, category);
                        //setWishlist(ce);
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            AppDatabase db = AppDatabase.getInstance(CategoryActivity.this);
                            List<Activities> l = db.activitiesDAO().getAll();
                            //Log.e("TAG", "l filled");
                            if (l == null) return;
                            //Log.e("TAG", "l not null");
                            for (Activities a : l){
                                if (a.getApiId().equals(ce.getApiId())) {
                                    //Log.e("TAG", "a.getApiId().equals(e.getApiId())");
                                    db.cachedEventDao().setWishlistEvent(ce.getApiId(), true);
                                }
                            }
                        });
                        e.setWishlist(ce.isWishlisted());
                        toCache.add(ce);
                    }


                    // 🔧 Move database operations off the main thread
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getInstance(CategoryActivity.this);
                        db.cachedEventDao().deleteEventsForCityAndCategory(city, category);
                        db.cachedEventDao().insertAll(toCache);
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

    private CachedEvent fromEventItem(EventItem e, String city, String category) {
        return new CachedEvent(
                e.getId() != null ? e.getId() : "",
                e.getName() != null ? e.getName() : "Untitled",
                e.getLocalDate(),
                e.getVenueName(),
                city,
                category,
                e.getImageUrl(),
                false
        );
    }

    private void setWishlist(CachedEvent e){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(CategoryActivity.this);
            List<Activities> l = db.activitiesDAO().getAll();
            Log.e("TAG", "l filled");
            if (l == null) return;
            Log.e("TAG", "l not null");
            for (Activities a : l){
                if (a.getApiId().equals(e.getApiId())) {
                    Log.e("TAG", "a.getApiId().equals(e.getApiId())");
                    db.cachedEventDao().setWishlistEvent(e.getApiId(), true);
                }
            }
        });
    }
}