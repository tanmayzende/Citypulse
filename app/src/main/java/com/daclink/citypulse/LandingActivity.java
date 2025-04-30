package com.daclink.citypulse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        boolean isAdmin = prefs.getBoolean("isAdmin", false);

        // If no username is stored, go to login screen
        if (username == null) {
            Intent intent = new Intent(LandingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText("Welcome, " + username);

        Button adminButton = findViewById(R.id.AdminButton1);
        adminButton.setVisibility(isAdmin ? View.VISIBLE : View.INVISIBLE);

        Button logoutButton = findViewById(R.id.LogoutButton1);
        logoutButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(LandingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // City selection buttons
        ImageButton buttonMiami = findViewById(R.id.button1);
        ImageButton buttonNewYork = findViewById(R.id.button2);
        ImageButton buttonLasVegas = findViewById(R.id.button3);
        ImageButton buttonLosAngeles = findViewById(R.id.button4);

        buttonMiami.setOnClickListener(view -> showToast("Miami"));
        buttonNewYork.setOnClickListener(view -> showToast("New York"));
        buttonLasVegas.setOnClickListener(view -> showToast("Las Vegas"));
        buttonLosAngeles.setOnClickListener(view -> showToast("Los Angeles"));
    }

    private void showToast(String cityName) {
        Toast.makeText(this, "Selected: " + cityName, Toast.LENGTH_SHORT).show();
    }
}
