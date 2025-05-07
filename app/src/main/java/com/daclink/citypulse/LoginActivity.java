package com.daclink.citypulse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daclink.citypulse.AppDatabase;
import com.daclink.citypulse.UserDao;

import java.util.concurrent.ExecutorService;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    private UserDao userDao;
    private ExecutorService executorService;
    private static final String PREFS_NAME = "MyAppPrefs"; // Shared preferences file name.
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Room db and background executor.
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();
        executorService = AppDatabase.databaseWriteExecutor;

        // Initialize view components.
        etUsername = findViewById(R.id.usernameEditText1);
        etPassword = findViewById(R.id.passwordEditText1);
        btnLogin = findViewById(R.id.loginButton2);

        // Set login button click listener.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                // Attempt login on background thread, moved from how originally it was on the main cusing the signup to fail
                attemptLogin(username, password);
            }
        });
    }

    public void attemptLogin(final String username, final String password) {
        // Validate input fields first.
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Perform database operations off the main thread.
        executorService.execute(() -> {
            try {
                // Authenticate user via database query.
                final User user = userDao.getUserByCredentials(username, password);

                // Process result back on main thread.
                runOnUiThread(() -> {
                    if (user != null) {
                        // Login successful for any user type.
                        handleLoginSuccess(user);
                    } else {
                        // Login failed, show error message.
                        Log.w(TAG, "Login failed for user: " + username);
                        Toast.makeText(LoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Error during database query for login", e);
                Log.e("LoginActivity", "Database login error", e);
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Database error during login.", Toast.LENGTH_LONG).show());
            }
        });
    }

    private void handleLoginSuccess(User user) {
        Log.d(TAG, "Login successful for user: " + user.getUsername() + " isAdmin: " + user.isAdmin());
        // Save login state using SharedPreferences.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("logged_in", true);
        editor.putString("username", user.getUsername());
        editor.putBoolean("isAdmin", user.isAdmin()); // Save admin status from DB.
        editor.apply();

        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        // Navigate all users to LandingActivity.
        Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity after navigation.
    }
}
