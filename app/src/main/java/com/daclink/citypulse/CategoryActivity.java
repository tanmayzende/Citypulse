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
import com.daclink.citypulse.model.YelpBusiness;
import com.daclink.citypulse.model.YelpResponse;
import com.daclink.citypulse.network.ApiService;
import com.daclink.citypulse.network.RetrofitClient;
import com.daclink.citypulse.network.YelpApiService;
import com.daclink.citypulse.network.YelpClient;

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

    private static final String TICKETMASTER_API_KEY = "A1igDbj4DxNkXX0VGnIb8YXKhEROppl7";
    private static final String YELP_AUTH = "Bearer wrZ-urPcGN7PeuN5auARIZWrwDWfzOkyuFQPnBPJjU1ZdKM9nY3pfYX9841OjkN8HSV_4Dykq_lLDeG4ePS_VVi1O6KsQx5iMNRYSahOb_MGuNmTPTnYju5DCVgZaHYx";

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

        if ("Dining".equalsIgnoreCase(category)) {
            fetchDiningFromYelp(city, category);
        } else {
            fetchEventsFromTicketmaster(city, category);
        }
    }

    private void fetchEventsFromTicketmaster(String city, String category) {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<TicketmasterResponse> call = apiService.getEventsByCategory(TICKETMASTER_API_KEY, city, category, 10);

        call.enqueue(new Callback<TicketmasterResponse>() {
            @Override
            public void onResponse(Call<TicketmasterResponse> call, Response<TicketmasterResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getEvents() != null) {
                    List<EventItem> events = response.body().getEvents();
                    List<CachedEvent> toCache = new ArrayList<>();
                    for (EventItem e : events) {
                        CachedEvent ce = fromEventItem(e, city, category);
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            AppDatabase db = AppDatabase.getInstance(CategoryActivity.this);
                            List<Activities> l = db.activitiesDAO().getAll();
                            if (l != null) {
                                for (Activities a : l) {
                                    if (a.getApiId().equals(ce.getApiId())) {
                                        db.cachedEventDao().setWishlistEvent(ce.getApiId(), true);
                                    }
                                }
                            }
                        });
                        e.setWishlist(ce.isWishlisted());
                        toCache.add(ce);
                    }
                    adapter = new EventItemAdapter(events, city, category);
                    recyclerView.setAdapter(adapter);

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getInstance(CategoryActivity.this);
                        db.cachedEventDao().deleteEventsForCityAndCategory(city, category);
                        db.cachedEventDao().insertAll(toCache);
                    });

                } else {
                    showError("No events found.");
                }
            }

            @Override
            public void onFailure(Call<TicketmasterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Failed to load events.");
            }
        });
    }
    private EventItem fromYelpBusiness(YelpBusiness b) {
        return new EventItem(
                b.getId() != null ? b.getId() : "",
                b.getName() != null ? b.getName() : "Untitled",
                new EventItem.Dates(b.getLocation().address1), // fake date info
                new EventItem.EmbeddedVenues(b.getLocation().address1), // fake venue
                List.of(new EventItem.Image(b.getImageUrl())), // fake image list
                false
        );
    }
    private void fetchDiningFromYelp(String city, String category) {
        progressBar.setVisibility(View.VISIBLE);

        YelpApiService yelpService = YelpClient.getInstance().create(YelpApiService.class);
        Call<YelpResponse> call = yelpService.getDiningOptions(YELP_AUTH, city, "restaurants", 10);

        call.enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<YelpBusiness> businesses = response.body().getBusinesses();
                    Log.d("YELP_SUCCESS", "Businesses found: " + businesses.size());

                    List<EventItem> events = new ArrayList<>();
                    List<CachedEvent> toCache = new ArrayList<>();

                    for (YelpBusiness b : businesses) {
                        Log.d("YELP_MAPPING", "Name: " + b.getName() + ", Address: " + b.getLocation().address1);
                        EventItem item = fromYelpBusiness(b);
                        CachedEvent ce = fromEventItem(item, city, category);
                        item.setWishlist(ce.isWishlisted());
                        events.add(item);
                        toCache.add(ce);
                    }

                    Log.d("ADAPTER_SET", "Adapter has " + events.size() + " items");
                    adapter = new EventItemAdapter(events, city, category);
                    recyclerView.setAdapter(adapter);

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getInstance(CategoryActivity.this);
                        db.cachedEventDao().deleteEventsForCityAndCategory(city, category);
                        db.cachedEventDao().insertAll(toCache);
                    });

                } else {
                    showError("No dining options found.");
                    Log.e("YELP_FAILURE", "Empty or bad Yelp response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Failed to load dining data.");
                Log.e("YELP_FAILURE", "Yelp call failed", t);
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
}
