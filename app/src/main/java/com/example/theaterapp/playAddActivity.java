package com.example.theaterapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theaterapp.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class playAddActivity extends AppCompatActivity {

    //view binding
    private ActivityCategoryAddBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;
    private String play = "";
    private final long ticket_money = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //telling people whats happening so the user experience will be good and to prepare for the next semester with Körmöczi
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Kérlek várj");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle click, upload, back
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void validateData() {
        //get Data
        play = binding.categoryEt.getText().toString().trim();
        //validate
        if (TextUtils.isEmpty(play)) {
            Toast.makeText(this, "Kérlek add meg a darab nevét", Toast.LENGTH_SHORT).show();
        } else {
            addPlayFirebase();
        }
    }

    private void addPlayFirebase() {
        //writing stuff cuz we are still nice to people
        progressDialog.setMessage("Színházi darab hozzáadása");
        progressDialog.show();

        //get timestamp
        long timestamp = System.currentTimeMillis();

        //setup data
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("play", "" + play);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", "" + firebaseAuth.getUid());
        hashMap.put("ticket_money", ticket_money);

        //adding data to firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Plays");
        ref.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(playAddActivity.this, "A darab létrejött", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(playAddActivity.this, AdminActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(playAddActivity.this, "A hozzáadás nem sikerült", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
