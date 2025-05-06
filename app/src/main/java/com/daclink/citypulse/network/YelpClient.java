package com.daclink.citypulse.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpClient {
    private static final String BASE_URL = "https://api.yelp.com/v3/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static YelpApiService getYelpService() {
        return getInstance().create(YelpApiService.class);
    }
}
