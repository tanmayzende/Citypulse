package com.daclink.citypulse;

import com.daclink.citypulse.model.EventItem;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventItemTest {
    @Test
    public void testGetVenueName() {
        EventItem.EmbeddedVenues.Venue venue = new EventItem.EmbeddedVenues.Venue("Theater");
        EventItem.EmbeddedVenues embeddedVenues = new EventItem.EmbeddedVenues("Theater");
        EventItem item = new EventItem("1", "Event", null, embeddedVenues, null, false);

        assertEquals("Theater", item.getVenueName());
    }
    @Test
    public void testGetLocalDate() {
        EventItem item = new EventItem("1", "Test", null, null, null, false);

        assertEquals("No date", item.getLocalDate());
    }
}
