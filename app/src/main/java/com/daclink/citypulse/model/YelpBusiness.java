package com.daclink.citypulse.model;

import com.google.gson.annotations.SerializedName;

public class YelpBusiness {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("location")
    private Location location;

    @SerializedName("rating")
    private double rating;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Location getLocation() {
        return location;
    }

    public double getRating() {
        return rating;
    }

    public static class Location {
        @SerializedName("address1")
        public String address1;

        @SerializedName("city")
        public String city;
    }
}
