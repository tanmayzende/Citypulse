package com.daclink.citypulse.network;

import com.daclink.citypulse.model.TicketmasterResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.http.GET;
import retrofit2.http.Query;
public interface ApiService {
    @GET("discovery/v2/events.json")
    Call<TicketmasterResponse> getEventsByCity(
            @Query("apikey") String apiKey,
            @Query("city") String city,
            @Query("size") int size
    );
}
