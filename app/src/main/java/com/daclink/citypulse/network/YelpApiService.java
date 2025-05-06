package com.daclink.citypulse.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import com.daclink.citypulse.model.YelpResponse;

public interface YelpApiService {
    @GET("businesses/search")
    Call<YelpResponse> getDiningOptions(
            @Header("Authorization") String authHeader,
            @Query("location") String location,
            @Query("categories") String categories,
            @Query("limit") int limit
    );
}
