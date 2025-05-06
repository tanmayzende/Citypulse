package com.daclink.citypulse.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daclink.citypulse.model.EventItem;

@Entity(tableName = "Activities")
public class Activities {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "api_id")
    private String apiId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "dates")
    private String dates;

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

    public Activities(String apiId, String title, String dates, String venue, String city, String category, String imageUrl, boolean wishlisted) {
        this.apiId = apiId;
        this.title = title;
        this.dates = dates;
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

    public String getDates() { return dates; }

    public String getVenue() { return venue; }
    public String getCity() { return city; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public boolean isWishlisted() { return wishlisted;}

    public void setWishlisted(boolean wishlisted) {
        this.wishlisted = wishlisted;
    }
}
