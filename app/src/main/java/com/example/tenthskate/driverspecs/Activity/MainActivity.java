package com.example.tenthskate.driverspecs.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.tenthskate.R;
import com.example.tenthskate.driverspecs.Adapter.OrdersAdapter;
import com.example.tenthskate.driverspecs.Class.Order;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private OrdersAdapter mAdapter;
TextView order1;
    private ProgressBar progressBar;

    private List<Order> mUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_fragment_orders);
        String sendOrderID = getIntent().getStringExtra("sendOrderID");
        String uidd = getIntent().getStringExtra("uidd");
        DatabaseReference dbNode1 = FirebaseDatabase.getInstance().getReference("Temporary").child("data");
        HashMap<String, Object> mHashmap = new HashMap<>();
        mHashmap.put("orderid", sendOrderID);
        mHashmap.put("uId", uidd);
        dbNode1.updateChildren(mHashmap);
        //Toast.makeText(this, "temp", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, sendOrderID, Toast.LENGTH_SHORT).show();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        order1=findViewById(R.id.order);
        order1.setText(sendOrderID);

        mRecyclerView= findViewById(R.id.recycler_view_products);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        progressBar= findViewById(R.id.progress_circular_products);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF7B5100, android.graphics.PorterDuff.Mode.MULTIPLY);

        mUploads=new ArrayList<>();

        final DatabaseReference mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("UserD").child("Drivers");


       mDatabaseProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mUploads.clear();
               for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
              {
                   Order order=postSnapshot.getValue(Order.class);
                   mUploads.add(order);
                }
              mAdapter=new OrdersAdapter(MainActivity.this, mUploads);
               mRecyclerView.setAdapter(mAdapter);

               progressBar.setVisibility(View.INVISIBLE);
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

        //return view;
    }


}