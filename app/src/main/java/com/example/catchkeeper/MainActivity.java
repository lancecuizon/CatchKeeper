package com.example.catchkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView welcomeText;
    private Button logoutButton;
    private Button btnLogInfo, btnWeather, btnEtc1, btnEtc2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);
        btnLogInfo = findViewById(R.id.btnLogInfo);
        btnWeather = findViewById(R.id.btnWeather);
        btnEtc1 = findViewById(R.id.btnEtc1);
        btnEtc2 = findViewById(R.id.btnEtc2);

        // Set username in welcome message
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String username = email != null ? email.split("@")[0] : "user";
            welcomeText.setText("hello, " + username + "!");
        } else {
            navigateToLogin();
        }

        // Logout
        logoutButton.setOnClickListener(v -> handleLogout());

        // Button click listeners (add your navigation logic here)
        btnLogInfo.setOnClickListener(v -> {
            // TODO: Open Log Information activity
        });
        btnWeather.setOnClickListener(v -> {
            // TODO: Open Weather activity
        });
        btnEtc1.setOnClickListener(v -> {
            // TODO: Open etc activity
        });
        btnEtc2.setOnClickListener(v -> {
            // TODO: Open etc activity
        });
    }

    private void handleLogout() {
        mAuth.signOut();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}