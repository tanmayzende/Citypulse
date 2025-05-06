package com.daclink.citypulse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private AppDatabase db;
    private UserDao userDao;
    private EditText searchUserInput;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);

        // Initialize DB and DAO
        db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();


        // RecyclerView setup
        searchUserInput = findViewById(R.id.searchUserInput);
        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initial load
        loadAllUsers();

        // Filter on text change
        searchUserInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userAdapter != null) {
                    userAdapter.filter(s.toString());
                }
            }
        });
    }

    private void loadAllUsers() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<User> users = userDao.getAllUsers();
            runOnUiThread(() -> {
                userAdapter = new UserAdapter(users, userDao);
                userRecyclerView.setAdapter(userAdapter);
            });
        });
    }

    private void refreshUserList() {
        // Re-fetch users after a deletion
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<User> users = userDao.getAllUsers();
            runOnUiThread(() -> {
                userAdapter.updateList(users); // uses helper in adapter
            });
        });
    }
}
