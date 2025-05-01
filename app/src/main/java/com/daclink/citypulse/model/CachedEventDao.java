package com.daclink.citypulse.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CachedEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CachedEvent> events);

    @Query("SELECT * FROM cached_events WHERE city = :city AND category = :category")
    List<CachedEvent> getEventsByCityAndCategory(String city, String category);

    @Query("DELETE FROM cached_events WHERE city = :city AND category = :category")
    void deleteEventsForCityAndCategory(String city, String category);

    @Query("DELETE FROM cached_events")
    void clearAll();
}
