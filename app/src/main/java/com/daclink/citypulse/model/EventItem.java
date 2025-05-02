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

    // Nested Classes
    private static class Dates {
        @SerializedName("start")
        Start start;

        private static class Start {
            @SerializedName("localDate")
            String localDate;

            @SerializedName("localTime")
            String localTime;
        }
    }

    private static class EmbeddedVenues {
        @SerializedName("venues")
        List<Venue> venues;

        private static class Venue {
            @SerializedName("name")
            String name;
        }
    }

    private static class Image {
        @SerializedName("url")
        String url;
    }
}
