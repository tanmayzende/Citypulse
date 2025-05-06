package com.daclink.citypulse;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daclink.citypulse.database.Activities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ActivitiesDaoTest {

    private AppDatabase db;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getInstance(context);
        db.activitiesDAO().clearAll();
    }

    @After
    public void tearDown() {
        db.activitiesDAO().clearAll();
    }

    @Test
    public void insert() {
        List<Activities> l = new ArrayList<>();
        assertEquals(0, db.activitiesDAO().getAll().size());
        l.add(new Activities("1", "test", "1/1/1", "testV", "gotham", "Sport", "nop", false));
        db.activitiesDAO().insertAll(l);
        assertEquals(1, db.activitiesDAO().getAll().size());
    }

    @Test
    public void update() {
        List<Activities> l = new ArrayList<>();
        l.add(new Activities("1", "test", "1/1/1", "testV", "gotham", "Sport", "nop", false));
        db.activitiesDAO().insertAll(l);
        db.activitiesDAO().setWishlistEvent("1", true);
        List<Activities> updated = db.activitiesDAO().getAll();
        assertTrue(updated.get(0).isWishlisted());
    }

    @Test
    public void delete() {
        List<Activities> l = new ArrayList<>();
        l.add(new Activities("1", "test", "1/1/1", "testV", "gotham", "Sport", "nop", false));
        db.activitiesDAO().insertAll(l);
        assertEquals(1, db.activitiesDAO().getAll().size());
        db.activitiesDAO().clearAll();
        assertEquals(0, db.activitiesDAO().getAll().size());
    }

    @Test
    public void insertEmptyListDoesNotCrash() {
        List<Activities> emptyList = new ArrayList<>();
        db.activitiesDAO().insertAll(emptyList);
        assertEquals(0, db.activitiesDAO().getAll().size());
    }
}
