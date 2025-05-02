package com.daclink.citypulse;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.daclink.citypulse.databinding.ActivityInfoOfBinding;

public class InfoOfActivity extends AppCompatActivity {
    private boolean isWishListPage;

    private ActivityInfoOfBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoOfBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_info_of);


        binding.activityTextView.setText("example");
        // get title of Activity from database
        // replace title in textView
        // get image of Activity from database
        // add image in imageView
        // if wishlist Button clicked, add in wishlist's user <= change users database?
        // if in wishlist, hide wishlist Button
    }
}