package com.example.tenthskate.Clients.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Clients.Adapter.MyOrderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.tenthskate.Clients.ModelClass.MyOrderItemModel;
import com.example.tenthskate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {

    DatabaseReference myOrders;
    private MyOrderAdapter mAdapter;
    private ProgressBar progressBar;
    ImageView cartEmpty;
    TextView emptyCartText;

    public MyOrdersFragment() {

    }

    private RecyclerView myOrdersRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FragmentActivity fragmentActivity = getActivity();
        myOrders =  FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("orders");
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        progressBar= view.findViewById(R.id.progress_circular_my_orders);
        cartEmpty = (ImageView) view.findViewById(R.id.empty_cart);
        emptyCartText = view.findViewById(R.id.empty_cart_text);

        myOrdersRecyclerView=view.findViewById(R.id.my_orders_recyclerview);
        myOrdersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(fragmentActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        myOrdersRecyclerView.setLayoutManager(linearLayoutManager);
        final List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.clear();
        myOrders.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOrderItemModelList.clear();
                mAdapter.notifyDataSetChanged();
                if (snapshot.exists()){

                    cartEmpty.setVisibility(View.GONE);

                    for(DataSnapshot items:snapshot.getChildren()){
                        MyOrderItemModel myOrderItemModel = items.getValue(MyOrderItemModel.class);
                        myOrderItemModelList.add(myOrderItemModel);
                    }
                    mAdapter=new MyOrderAdapter(fragmentActivity,myOrderItemModelList);
                    myOrdersRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    cartEmpty.setVisibility(View.VISIBLE);
                    emptyCartText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter=new MyOrderAdapter(fragmentActivity, myOrderItemModelList);
                assert fragmentActivity != null;
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myOrdersRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
        return view;
    }
}