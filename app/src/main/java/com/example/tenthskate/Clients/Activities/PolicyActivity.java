package com.example.tenthskate.Clients.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tenthskate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PolicyActivity extends AppCompatActivity {

    TextView termsConditions;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        termsConditions = findViewById(R.id.terms_and_conditions);
        toolbar = (Toolbar) findViewById(R.id.privacy_policy_toolbar);
        toolbar.setTitle("Privacy Policy");
        setSupportActionBar(toolbar);

        DatabaseReference privacyRef = FirebaseDatabase.getInstance().getReference().child("Terms")
                .child("policyText");

        privacyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    termsConditions.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}