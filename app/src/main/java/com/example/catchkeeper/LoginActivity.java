package com.example.catchkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Set up click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        registerButton.setOnClickListener(v -> handleRegister());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMain();
        }
    }

    private void handleLogin() {
        // Reset error states
        emailLayout.setError(null);
        passwordLayout.setError(null);

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            return;
        }

        // Show loading state
        setLoadingState(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        navigateToMain();
                    } else {
                        // If sign in fails, display a message to the user
                        String errorMessage = task.getException() != null ? 
                            task.getException().getMessage() : "Authentication failed";
                        Toast.makeText(LoginActivity.this, errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                    // Reset loading state
                    setLoadingState(false);
                });
    }

    private void handleRegister() {
        // Reset error states
        emailLayout.setError(null);
        passwordLayout.setError(null);

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            return;
        }

        // Show loading state
        setLoadingState(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Create user document in Firestore
                        db.collection("users").document(user.getUid())
                                .set(new UserData(email))
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(LoginActivity.this, "Registration successful!",
                                            Toast.LENGTH_SHORT).show();
                                    navigateToMain();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(LoginActivity.this, "Failed to create user profile: " + 
                                            e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // If registration fails, display a message to the user
                        String errorMessage = task.getException() != null ? 
                            task.getException().getMessage() : "Registration failed";
                        Toast.makeText(LoginActivity.this, errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                    // Reset loading state
                    setLoadingState(false);
                });
    }

    private void setLoadingState(boolean isLoading) {
        loginButton.setEnabled(!isLoading);
        registerButton.setEnabled(!isLoading);
        emailInput.setEnabled(!isLoading);
        passwordInput.setEnabled(!isLoading);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
} 