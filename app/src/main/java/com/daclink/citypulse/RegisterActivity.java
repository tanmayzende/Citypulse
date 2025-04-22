package com.daclink.citypulse;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daclink.citypulse.UserDao;
import com.daclink.citypulse.User;
import com.daclink.citypulse.AppDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister, btnBack;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializ roomdb
        userDao = AppDatabase.getInstance(this).userDao();

        // Initialize views
        etUsername = findViewById(R.id.usernameEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etConfirmPassword = findViewById(R.id.confirmpasswordEditText);
        btnRegister = findViewById(R.id.SigninButton);


        // Set click listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Check if username already exists
        User existingUser = userDao.getUserByUsername(username);
        if (existingUser != null) {
            etUsername.setError("Username already exists");
            return;
        }

        // Create and insert new user (regular user, not admin)
        User newUser = new User(username, password, false);
        userDao.insertUser(newUser);

        Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
        finish(); // Go back to MainActivity
    }
}
