package com.example.theaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theaterapp.databinding.ActivityUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    // View binding
    private ActivityUserBinding binding;

    // Firebase Authentication
    private FirebaseAuth firebaseAuth;

    // ArrayList for plays
    private ArrayList<ModelPlay> modelPlayArrayList;

    private AdapterUser adapterUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using view binding
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if the user is logged in or not
        checkUser();

        // Load all the plays from Firebase Database
        loadPlays();

        // Handle search functionality
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    // Filter the adapter data based on user's search input
                    adapterUser.getFilter().filter(s);
                } catch (Exception e) {
                    // Catch any exception that might occur during filtering
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Handle logout button click
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user from Firebase Authentication
                firebaseAuth.signOut();
                // Check if the user is logged in or not
                checkUser();
            }
        });
    }

    private void loadPlays() {
        modelPlayArrayList = new ArrayList<>();
        // Get a reference to the "Plays" node in Firebase Database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Plays");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelPlayArrayList.clear();
                // Loop through all the plays in the "Plays" node
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Get the play data from the snapshot
                    ModelPlay model = ds.getValue(ModelPlay.class);
                    // Add the play to the ArrayList
                    modelPlayArrayList.add(model);
                }
                // Initialize the adapter with the ArrayList
                adapterUser = new AdapterUser(UserActivity.this, modelPlayArrayList);
                // Set the adapter for the RecyclerView
                binding.categoriesRV.setAdapter(adapterUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that might occur during data retrieval
            }
        });
    }

    private void checkUser() {
        // Get the current user from Firebase Authentication
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            // If the user is not logged in, redirect to the MainActivity
            startActivity(new Intent(UserActivity.this, MainActivity.class));
            finish();
        } else {
            // If the user is logged in, show their email in the subtitle TextView
            String email = firebaseUser.getEmail();
            binding.subTitleTv.setText(email);
        }
    }
}

