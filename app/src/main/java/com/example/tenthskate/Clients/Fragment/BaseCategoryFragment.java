package com.example.tenthskate.Clients.Fragment;

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

import com.example.tenthskate.Clients.Adapter.ProductsAdapter;
import com.example.tenthskate.Clients.ModelClass.Upload;
import com.example.tenthskate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseCategoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProductsAdapter mAdapter;

    private ProgressBar progressBar;

    private List<Upload> mUploads;

    public BaseCategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detergents, container, false);

        final FragmentActivity fragmentActivity = getActivity();

        mRecyclerView= view.findViewById(R.id.recycler_view_knee_pad);
        mRecyclerView.setHasFixedSize(true);LinearLayoutManager linearLayoutManager = new LinearLayoutManager(fragmentActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        progressBar= view.findViewById(R.id.progress_circular_babies_needs);

        mUploads=new ArrayList<>();
        final DatabaseReference mDatabaseBabiesNeedsRef = FirebaseDatabase.getInstance()
                .getReference("productsCategory").child("Knee Pad");
        mDatabaseBabiesNeedsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mUploads.clear();
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        Upload upload=postSnapshot.getValue(Upload.class);
                        mUploads.add(upload);
                    }
                    mAdapter=new ProductsAdapter(fragmentActivity, mUploads);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
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
                mAdapter=new ProductsAdapter(fragmentActivity, mUploads);
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