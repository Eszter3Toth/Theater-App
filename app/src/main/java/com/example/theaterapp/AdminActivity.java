package com.example.theaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theaterapp.databinding.ActivityAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    //view binding
    private ActivityAdminBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //arraylist for category
    private ArrayList<ModelPlay> playArrayList;

    private AdapterPlay adapterPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadplays();

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterPlay.getFilter().filter(s);
                }catch(Exception e){

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //click handle, logout, play
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, playAddActivity.class));
            }
        });
    }

    private void loadplays() {
        playArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Plays");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get data
                    ModelPlay model = ds.getValue(ModelPlay.class);

                    //add to arraylist
                    playArrayList.add(model);
                }
                adapterPlay = new AdapterPlay(AdminActivity.this, playArrayList);

                binding.categoriesRV.setAdapter(adapterPlay);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            //not logged in
            startActivity(new Intent(AdminActivity.this, MainActivity.class));
            finish();
        } else {
            //logged in
            String email = firebaseUser.getEmail();
            //set in textview
            binding.subTitleTv.setText(email);
        }
    }
}
