package com.daclink.citypulse.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EventItem {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("dates")
    private Dates dates;

    @SerializedName("_embedded")
    private EmbeddedVenues embeddedVenues;

    @SerializedName("images")
    private List<Image> images;

    @SerializedName("wishlist")
    private boolean wishlist;

    public EventItem(String id, String name, Dates dates, EmbeddedVenues embeddedVenues, List<Image> images, boolean wishlist) {
        this.id = id;
        this.name = name;
        this.dates = dates;
        this.embeddedVenues = embeddedVenues;
        this.images = images;
        this.wishlist = wishlist;
    }

    public boolean isWishlist() {
        return wishlist;
    }

    public void setWishlist(boolean newWishlistStatus) {
        this.wishlist = newWishlistStatus;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocalDate() {
        if (dates != null && dates.start != null) {
            return dates.start.localDate + " " + dates.start.localTime;
        }
        return "No date";
    }

    public String getVenueName() {
        if (embeddedVenues != null && embeddedVenues.venues != null && !embeddedVenues.venues.isEmpty()) {
            return embeddedVenues.venues.get(0).name;
        }
        return "Unknown venue";
    }

    public String getImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0).url;
        }
        return null;
    }

    // Updated nested classes to be public static
    public static class Dates {
        @SerializedName("start")
        public Start start;

        public Dates(String localDate) {
            this.start = new Start(localDate);
        }

        public static class Start {
            @SerializedName("localDate")
            public String localDate;

            @SerializedName("localTime")
            public String localTime;

            public Start(String date) {
                this.localDate = date;
                this.localTime = "";
            }
        }
    }

    public static class EmbeddedVenues {
        @SerializedName("venues")
        public List<Venue> venues;

        public EmbeddedVenues(String venueName) {
            this.venues = List.of(new Venue(venueName));
        }

        public static class Venue {
            @SerializedName("name")
            public String name;

            public Venue(String name) {
                this.name = name;
            }
        }
    }

    public static class Image {
        @SerializedName("url")
        public String url;

        public Image(String url) {
            this.url = url;
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String dateText) {
        this.dates = new Dates(dateText);
    }

    public void setVenueName(String venueName) {
        this.embeddedVenues = new EmbeddedVenues(venueName);
    }

    public void setVenueCity(String city) {
        if (this.embeddedVenues != null && !this.embeddedVenues.venues.isEmpty()) {
            this.embeddedVenues.venues.get(0).name += " - " + city;
        } else {
            this.embeddedVenues = new EmbeddedVenues(city);
        }
    }

    public void setImageUrl(String imageUrl) {
        this.images = List.of(new Image(imageUrl));
    }

}
