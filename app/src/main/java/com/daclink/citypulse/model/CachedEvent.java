package com.daclink.citypulse.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "cached_events")
public class CachedEvent {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "api_id")
    private String apiId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "venue")
    private String venue;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "wishlisted")
    private boolean wishlisted;

    public CachedEvent(String apiId, String title, String date, String venue, String city, String category, String imageUrl, boolean wishlisted) {
        this.apiId = apiId;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.city = city;
        this.category = category;
        this.imageUrl = imageUrl;
        this.wishlisted = wishlisted;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getApiId() { return apiId; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getVenue() { return venue; }
    public String getCity() { return city; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public boolean isWishlisted() { return wishlisted;}
}
