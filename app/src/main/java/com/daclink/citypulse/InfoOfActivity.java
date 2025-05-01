package com.daclink.citypulse;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daclink.citypulse.databinding.ActivityLandingBinding;

public class InfoOfActivity extends AppCompatActivity {
    private boolean isWishListPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_of);
        // get title of Activity from database
        // replace title in textView
        // get image of Activity from database
        // add image in imageView
        // if wishlist Button clicked, add in wishlist's user <= change users database?
        // if in wishlist, hide wishlist Button
    }
}