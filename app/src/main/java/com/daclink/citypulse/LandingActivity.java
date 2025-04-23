package com.daclink.citypulse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // Find UI elements from XML
        TextView titleTextView = findViewById(R.id.textView2);
        ImageButton buttonMiami = findViewById(R.id.button1);
        ImageButton buttonNewYork = findViewById(R.id.button2);
        ImageButton buttonLasVegas = findViewById(R.id.button3);
        ImageButton buttonLosAngeles = findViewById(R.id.button4);

        // Set click listeners for each button
        buttonMiami.setOnClickListener(view -> showToast("Miami"));
        buttonNewYork.setOnClickListener(view -> showToast("New York"));
        buttonLasVegas.setOnClickListener(view -> showToast("Las Vegas"));
        buttonLosAngeles.setOnClickListener(view -> showToast("Los Angeles"));
    }

    private void showToast(String cityName) {
        // Show a toast message indicating the selected city
        Toast.makeText(this, "Selected: " + cityName, Toast.LENGTH_SHORT).show();
    }
}
