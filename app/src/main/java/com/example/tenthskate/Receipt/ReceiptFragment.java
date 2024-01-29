package com.example.tenthskate.Receipt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Finance.Class.Order;
import com.example.tenthskate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RAdapter mAdapter;

    private ProgressBar progressBar;

    private List<Order> mUploads;

    public ReceiptFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
//        DatabaseReference myOrders;
//        myOrders =  FirebaseDatabase.getInstance().getReference().child("Users")
             // Objects.requireNonNull(FirebaseAuth.getInstance().getUid());
//                .child("orders");

        View view = inflater.inflate(R.layout.stock_fragment_orders, container, false);

        final FragmentActivity fragmentActivity = getActivity();

        mRecyclerView= view.findViewById(R.id.recycler_view_products);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        progressBar= view.findViewById(R.id.progress_circular_products);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF7B5100, android.graphics.PorterDuff.Mode.MULTIPLY);

        mUploads=new ArrayList<>();

        final DatabaseReference mDatabaseProductsRef = FirebaseDatabase.getInstance()
                .getReference("Orders");

        Query query = mDatabaseProductsRef.orderByChild("uId").equalTo(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mUploads.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // do with your result
                        Order order=postSnapshot.getValue(Order.class);
                        mUploads.add(order);
                    }
                    mAdapter=new RAdapter(fragmentActivity, mUploads);
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
//                    Order order=postSnapshot.getValue(Order.class);
//                    mUploads.add(order);
//                }
//                mAdapter=new RAdapter(fragmentActivity, mUploads);
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
                mAdapter=new RAdapter(fragmentActivity, mUploads);
                assert fragmentActivity != null;
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

        return view;
    }
}

