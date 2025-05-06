package com.daclink.citypulse;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.citypulse.model.EventItem;
import com.daclink.citypulse.model.TicketmasterResponse;
import com.daclink.citypulse.model.YelpBusiness;
import com.daclink.citypulse.model.YelpResponse;
import com.daclink.citypulse.network.ApiService;
import com.daclink.citypulse.network.RetrofitClient;
import com.daclink.citypulse.network.YelpApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private EventItemAdapter adapter;

    private static final String API_KEY = "A1igDbj4DxNkXX0VGnIb8YXKhEROppl7";
    private static final String YELP_API_KEY = "Bearer jTnsIDBmDqUQ3E7mYHwj2ID9yDlikT8GKtjMDo5AXHOZXIHyFZ-oAyxrhPDHOxFnvEt2sdgl46S9t5BIfgQyxNTgHVr6EX8xBLInRGD42-vtWMBTmD1aUzNtd2caaHYx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String city = getIntent().getStringExtra("city");

        findViewById(R.id.btnConcerts).setOnClickListener(v -> launchCategory("Music"));
        findViewById(R.id.btnSports).setOnClickListener(v -> launchCategory("Sports"));
        findViewById(R.id.btnArts).setOnClickListener(v -> launchCategory("Arts & Theatre"));

        findViewById(R.id.btnDining).setOnClickListener(v -> {
            if (city != null && !city.isEmpty()) {
                fetchDiningFromYelp(city);
                Log.d("EventsActivity", "Fetching dining info for city: " + city);
            } else {
                showError("City not provided.");
            }
        });
    }

    private void fetchEventsFromApi(String city, String category) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<TicketmasterResponse> call = apiService.getEventsByCity(API_KEY, city, 10);

        call.enqueue(new Callback<TicketmasterResponse>() {
            @Override
            public void onResponse(Call<TicketmasterResponse> call, Response<TicketmasterResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getEvents() != null) {
                    List<EventItem> events = response.body().getEvents();
                    adapter = new EventItemAdapter(events, city, category);
                    recyclerView.setAdapter(adapter);
                } else {
                    showError("No events found or API error.");
                }
            }

            @Override
            public void onFailure(Call<TicketmasterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Failed to fetch events: " + t.getMessage());
                Log.e("EventsActivity", "API Failure", t);
            }
        });
    }

    private void fetchDiningFromYelp(String city) {
        progressBar.setVisibility(View.VISIBLE);
        YelpApiService yelpService = RetrofitClient.getYelpClient().create(YelpApiService.class);
        Call<YelpResponse> call = yelpService.getDiningOptions(YELP_API_KEY, city, "restaurants", 10);

        call.enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getBusinesses() != null) {
                    List<YelpBusiness> businesses = response.body().getBusinesses();
                    YelpAdapter yelpAdapter = new YelpAdapter(businesses, EventsActivity.this);
                    recyclerView.setAdapter(yelpAdapter);
                } else {
                    showError("Yelp returned no results.");
                }
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Yelp fetch failed: " + t.getMessage());
                Log.e("EventsActivity", "Yelp Error", t);
            }
        });
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void launchCategory(String category) {
        Intent intent = new Intent(EventsActivity.this, CategoryActivity.class);
        intent.putExtra("city", getIntent().getStringExtra("city"));
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
