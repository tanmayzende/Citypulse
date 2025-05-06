package com.daclink.citypulse;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.daclink.citypulse.database.Activities;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private AppDatabase db;
    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getInstance(context);
        db.activitiesDAO().clearAll();
    }

    @Test
    public void insert() throws Exception {
        List<Activities> l = db.activitiesDAO().getAll();
        assertEquals(l.size(), 0);
        l.add(new Activities(
                "1",
                "test",
                "1/1/1",
                "testV",
                "gotham",
                "Sport",
                "nop",
                false
        ));
        db.activitiesDAO().insertAll(l);
        assertEquals(db.activitiesDAO().getAll().size(), 1);
    }
    @Test
    public void update() throws Exception {
        List<Activities> l = db.activitiesDAO().getAll();
        l.add(new Activities(
                "1",
                "test",
                "1/1/1",
                "testV",
                "gotham",
                "Sport",
                "nop",
                false
        ));
        db.activitiesDAO().insertAll(l);
        db.activitiesDAO().setWishlistEvent("1", true);
        l = db.activitiesDAO().getAll();
        Log.e("title", "got: %s".formatted(l.getFirst().getTitle()));
        assertTrue(l.getFirst().isWishlisted());
    }
    @Test
    public void delete() throws Exception {
        db.activitiesDAO().clearAll();
        List<Activities> l = db.activitiesDAO().getAll();
        assertEquals(l.size(), 0);
    }


}