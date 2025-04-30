package com.daclink.citypulse.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TicketmasterResponse {
    @SerializedName("_embedded")
    private Embedded embedded;

    public List<EventItem> getEvents() {
        if (embedded != null) {
            return embedded.events;
        }
        return null;
    }

    private static class Embedded {
        @SerializedName("events")
        List<EventItem> events;
    }
}
