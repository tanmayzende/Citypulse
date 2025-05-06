package com.daclink.citypulse;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daclink.citypulse.model.CachedEvent;
import com.daclink.citypulse.model.CachedEventDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CachedEventDaoTest {

    private AppDatabase db;
    private CachedEventDao dao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getInstance(context);
        dao = db.cachedEventDao();
        dao.clearAll();
    }

    @After
    public void tearDown() {
        dao.clearAll();
    }

    @Test
    public void insertAndRetrieve() {
        List<CachedEvent> events = new ArrayList<>();
        events.add(new CachedEvent("api123", "Jazz Night", "2025-05-01", "The Blue Note", "New York", "Music", "img_url", false));
        dao.insertAll(events);

        List<CachedEvent> retrieved = dao.getEventsByCityAndCategory("New York", "Music");
        assertEquals(1, retrieved.size());
        assertEquals("Jazz Night", retrieved.get(0).getTitle());
    }

    @Test
    public void updateWishlistStatus() {
        List<CachedEvent> events = new ArrayList<>();
        events.add(new CachedEvent("api456", "Food Fest", "2025-06-10", "Union Square", "San Francisco", "Food", "img_url", false));
        dao.insertAll(events);

        dao.setWishlistEvent("api456", true);
        List<CachedEvent> updated = dao.getEventsByCityAndCategory("San Francisco", "Food");
        assertTrue(updated.get(0).isWishlisted());
    }

    @Test
    public void deleteByCityAndCategory() {
        List<CachedEvent> events = new ArrayList<>();
        events.add(new CachedEvent("api789", "Art Walk", "2025-07-15", "Downtown", "Los Angeles", "Art", "img_url", false));
        dao.insertAll(events);

        dao.deleteEventsForCityAndCategory("Los Angeles", "Art");
        List<CachedEvent> deleted = dao.getEventsByCityAndCategory("Los Angeles", "Art");
        assertTrue(deleted.isEmpty());
    }

    @Test
    public void insertEmptyListSafe() {
        dao.insertAll(new ArrayList<>());
        List<CachedEvent> all = dao.getEventsByCityAndCategory("Any", "Any");
        assertTrue(all.isEmpty());
    }
}
