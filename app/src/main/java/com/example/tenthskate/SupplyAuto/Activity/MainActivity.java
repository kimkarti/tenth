package com.example.tenthskate.SupplyAuto.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.example.tenthskate.R;
import com.example.tenthskate.SupplyAuto.Adapter.OrdersAdapter;
import com.example.tenthskate.SupplyAuto.Class.Order;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private OrdersAdapter mAdapter;

    private ProgressBar progressBar;

    private List<Order> mUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplyorder);
      //  String sendOrderID = getIntent().getStringExtra("sendOrderID");
       // String uidd = getIntent().getStringExtra("uidd");

        mRecyclerView= findViewById(R.id.recycler_view_products);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        progressBar= findViewById(R.id.progress_circular_products);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF7B5100, android.graphics.PorterDuff.Mode.MULTIPLY);

        mUploads=new ArrayList<>();

        final DatabaseReference mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("allProducts");

        //Query query = mDatabaseProductsRef.orderByChild("status").equalTo("Pending");
        mDatabaseProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // do with your result
                        Order order=postSnapshot.getValue(Order.class);
                        mUploads.add(order);
                    }
                    mAdapter=new OrdersAdapter(MainActivity.this, mUploads);
                    mRecyclerView.setAdapter(mAdapter);

                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter=new OrdersAdapter(MainActivity.this, mUploads);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
    }


}