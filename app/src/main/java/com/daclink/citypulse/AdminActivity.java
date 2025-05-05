package com.daclink.citypulse;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private AppDatabase db;
    private UserDao userDao;
    private EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);

        // Initialize database and DAO
        db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();

        // Input field for user deletion
        usernameInput = findViewById(R.id.usernameInput);

        // Delete user
        Button deleteUserButton = findViewById(R.id.deleteUserButton);
        deleteUserButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (!username.isEmpty()) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    User user = userDao.getUserByUsername(username);
                    if (user != null) {
                        userDao.deleteUser(user);
                        runOnUiThread(() -> Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show());
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
