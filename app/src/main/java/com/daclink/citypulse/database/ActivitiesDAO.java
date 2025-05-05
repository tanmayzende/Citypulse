package com.daclink.citypulse.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daclink.citypulse.model.CachedEvent;

import org.jspecify.annotations.Nullable;

import java.util.List;
@Dao
public interface ActivitiesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Activities> events);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Activities events);

    @Query("SELECT * FROM activities")
    @Nullable List<Activities> getAll();

    @Query("SELECT * FROM activities WHERE api_id = :id")
    List<Activities> getActivities(String id);


    @Query("DELETE FROM activities WHERE api_id = :id")
    void deleteEvent(String id);

    @Query("DELETE FROM activities")
    void clearAll();

    @Query("SELECT * FROM activities ORDER BY dates ASC")
    List<Activities> sortDateAsc();
    @Query("SELECT * FROM activities ORDER BY dates DESC")
    List<Activities> sortDateDesc();

    @Query("UPDATE activities SET wishlisted= :newWishlistStatus WHERE api_id = :id")
    void setWishlistEvent(String id, boolean newWishlistStatus);
}
