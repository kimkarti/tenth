package com.example.tenthskate.Finance.Fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Finance.Adapter.deliverdAdapter;
import com.example.tenthskate.Finance.Class.Order;
import com.example.tenthskate.R;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
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
public class ApprovedFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private deliverdAdapter mAdapter;

    private ProgressBar progressBar;

    private List<Order> mUploads;

    public ApprovedFragment() {
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.financeapproved, container, false);

        final FragmentActivity fragmentActivity = getActivity();


        mRecyclerView= view.findViewById(R.id.recycler_view_products);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        progressBar= view.findViewById(R.id.progress_circular_products);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF7B5100, android.graphics.PorterDuff.Mode.MULTIPLY);

        mUploads=new ArrayList<>();

        final DatabaseReference mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("Orders");
        Query query = mDatabaseProductsRef.orderByChild("status").equalTo("Approved");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mUploads.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // do with your result
                        Order order=postSnapshot.getValue(Order.class);
                        mUploads.add(order);
                    }
                    mAdapter=new deliverdAdapter(fragmentActivity, mUploads);
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
                mAdapter=new deliverdAdapter(fragmentActivity, mUploads);
                assert fragmentActivity != null;
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

        Button audit=view.findViewById(R.id.audit);
        audit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public  void onClick(View v) {


                PdfGenerator.getBuilder()
                        .setContext(getActivity())
                        .fromViewSource()
                        .fromView(mRecyclerView)
                        .setFileName("Finance Report")
                        .setFolderName("Floor decor-folder")
                        .openPDFafterGeneration(true)
                        .build(new PdfGeneratorListener() {
                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                            }
                            @Override
                            public void onStartPDFGeneration() {

                            }
                            @Override
                            public void onFinishPDFGeneration() {

                            }
                            @Override
                            public void showLog(String log) {
                                super.showLog(log);
                            }

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
                            }
                        });

            }


        });

        return view;
    }
}

