package com.example.tenthskate.Clients.Feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class feedback extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProductsAdapter mAdapter;

    private ProgressBar progressBar;

    private List<Upload> mUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_fragment_products);


        mRecyclerView= findViewById(R.id.recycler_view_products);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar= findViewById(R.id.progress_circular_products);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF7B5100, android.graphics.PorterDuff.Mode.MULTIPLY);

        mUploads=new ArrayList<>();
        final DatabaseReference mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("Feedback");

        //Query query = mDatabaseProductsRef.orderByChild("status").equalTo("Pending");
        mDatabaseProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {



                    mUploads.clear();
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        Upload upload=postSnapshot.getValue(Upload.class);
                        mUploads.add(upload);
                    }
                    mAdapter=new ProductsAdapter(feedback.this, mUploads);
                    mRecyclerView.setAdapter(mAdapter);

                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


//        mDatabaseProductsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUploads.clear();
//                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
//                {
//                    Upload upload=postSnapshot.getValue(Upload.class);
//                    mUploads.add(upload);
//                }
//                mAdapter=new ProductsAdapter(fragmentActivity, mUploads);
//                mRecyclerView.setAdapter(mAdapter);
//
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter=new ProductsAdapter(feedback.this, mUploads);
                feedback.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

      //  return view;
    }
}