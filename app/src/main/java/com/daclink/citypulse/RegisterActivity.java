package com.daclink.citypulse;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;

    private UserDao userDao;
    private ExecutorService executorService;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize DB and executor
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();
        executorService = AppDatabase.databaseWriteExecutor;

        // Initialize views
        etUsername = findViewById(R.id.usernameEditText2);
        etPassword = findViewById(R.id.passwordEditText2);
        etConfirmPassword = findViewById(R.id.confirmpasswordEditText);
        btnRegister = findViewById(R.id.signupButton2);

        // Set click listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                // Attempt registration background
                attemptRegistration(username, password, confirmPassword);
            }
        });
    }

    private void attemptRegistration(final String username, final String password, final String confirmPassword) {
        // Validate input first
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
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirmation is required");
            etConfirmPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.setText("");
            etConfirmPassword.requestFocus();
            return;
        }

        // Perform DB ops background
        executorService.execute(() -> {
            try {
                // Check username existence (on background thread)
                User existingUser = userDao.getUserByUsername(username);

                if (existingUser != null) {
                    // Username exists, show error on main thread
                    runOnUiThread(() -> {
                        Log.w(TAG, "Registration failed: Username '" + username + "' already exists.");
                        etUsername.setError("Username already exists");
                        etUsername.requestFocus();
                    });
                } else {
                    // Username available, insert new user (still on background thread)
                    insertNewUser(username, password); // <-- Correct placement
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking username existence", e);
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Database error checking username.", Toast.LENGTH_LONG).show());
            }
        });
    }

    private void insertNewUser(final String username, final String password) {
        // This method is now correctly called ONLY from the background thread
        try {
            Log.d(TAG, "Username available. Inserting new user: " + username);
            User newUser = new User(username, password, false);
            userDao.insert(newUser); //  Database insert on background thread

            // Show success message and finish on Main Thread AFTER successful insert
            runOnUiThread(() -> {
                Toast.makeText(RegisterActivity.this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();
                finish(); // Go back to previous activity
            });
        } catch (Exception e) {
            // Log error and show error message on Main Thread
            Log.e(TAG, "Error inserting new user", e); // Log the detailed error
            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Database error during registration.", Toast.LENGTH_LONG).show());
        }
    }
}
