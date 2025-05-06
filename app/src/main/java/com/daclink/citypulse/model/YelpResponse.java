package com.daclink.citypulse.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class YelpResponse {
    @SerializedName("businesses")
    private List<YelpBusiness> businesses;

    public List<YelpBusiness> getBusinesses() {
        return businesses;
    }
}
