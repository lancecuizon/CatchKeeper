package com.example.catchkeeper;

public class UserData {
    private String email;
    private long createdAt;

    // Required empty constructor for Firestore
    public UserData() {
    }

    public UserData(String email) {
        this.email = email;
        this.createdAt = System.currentTimeMillis();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
} 