package com.example.tenthskate.Driver.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Driver.Activity.MainActivity;
import com.example.tenthskate.Driver.Adapter.OrdersAdapter;
import com.example.tenthskate.Driver.Class.Order;
import com.example.tenthskate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class incomingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private OrdersAdapter mAdapter;

    private ProgressBar progressBar;
    String m;
    private List<Order> mUploads;

    public incomingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_fragment_orders, container, false);

         m =((MainActivity) getActivity()).FragmentMethod();


        final FragmentActivity fragmentActivity = getActivity();
        Toast.makeText(fragmentActivity, m, Toast.LENGTH_SHORT).show();

        mRecyclerView= view.findViewById(R.id.recycler_view_products);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        progressBar= view.findViewById(R.id.progress_circular_products);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF7B5100, android.graphics.PorterDuff.Mode.MULTIPLY);

        mUploads=new ArrayList<>();

        final DatabaseReference mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("Drivers").child(m);
        Query query = mDatabaseProductsRef.orderByChild("status").equalTo("intransit");
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
                    mAdapter=new OrdersAdapter(fragmentActivity, mUploads);
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
                mAdapter=new OrdersAdapter(fragmentActivity, mUploads);
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

