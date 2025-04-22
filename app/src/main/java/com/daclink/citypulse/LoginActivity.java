package com.daclink.citypulse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daclink.citypulse.AppDatabase;
import com.daclink.citypulse.UserDao;

import com.daclink.citypulse.User;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    private UserDao userDao;
    private static final String PREFS_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Room db
        userDao = AppDatabase.getInstance(this).userDao();

        // Initialize views
        etUsername = findViewById(R.id.usernameEditText);
        etPassword = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.loginButton);

        // Set click listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        // Authenticate user
        User user = userDao.getUserByCredentials(username, password);

        if (user != null) {
            // Save login state to SharedPreferences
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("logged_in", true);
            editor.putString("username", username);
            editor.putBoolean("isAdmin", user.isAdmin());
            editor.apply();

            // Login successful, navigate to landing page
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("isAdmin", user.isAdmin());
            startActivity(intent);
            finish(); // Close LoginActivity
        } else {
            // Login failed
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}