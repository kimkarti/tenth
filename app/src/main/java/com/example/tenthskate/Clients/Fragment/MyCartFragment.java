package com.example.tenthskate.Clients.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Clients.Activities.AddAddress;
import com.example.tenthskate.Clients.Adapter.CartItemAdapter;
import com.example.tenthskate.Clients.ModelClass.Upload;
import com.example.tenthskate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CartItemAdapter mAdapter;
    private ProgressBar progressBar;
    private List<Upload> mUploads;
    TextView cartValue, shippingCharges, emptyCartText;
    ImageView cartEmpty;
    int totalPrice = 0;

    public MyCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        final FragmentActivity fragmentActivity = getActivity();

        mRecyclerView= view.findViewById(R.id.recycler_view_my_cart);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));


        progressBar= view.findViewById(R.id.progress_circular_my_cart);

        final Button orderNowButton = (Button) view.findViewById(R.id.order_now_button);
        cartValue = (TextView) view.findViewById(R.id.cart_value);
        shippingCharges = (TextView) view.findViewById(R.id.shipping_charge);
        cartEmpty = (ImageView) view.findViewById(R.id.empty_cart);
        emptyCartText = view.findViewById(R.id.empty_cart_text);

        shippingCharges.setVisibility(View.GONE);
        cartValue.setVisibility(View.GONE);
        orderNowButton.setVisibility(View.GONE);

        mUploads=new ArrayList<>();
        String userID = FirebaseAuth.getInstance().getUid();
        assert userID != null;
        final DatabaseReference mDatabaseCartRef = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("cart");
        mDatabaseCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                totalPrice=0;

                if (dataSnapshot.exists()){

                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        Upload upload=postSnapshot.getValue(Upload.class);
                        mUploads.add(upload);
                    }

                    mAdapter=new CartItemAdapter(fragmentActivity, mUploads);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();


                    progressBar.setVisibility(View.INVISIBLE);
                    for (int i = 0; i<mUploads.size(); i++)
                    {
                        totalPrice += (Integer.parseInt(mUploads.get(i).getProductPrice())
                                * Integer.parseInt(mUploads.get(i).getProductQuantity()));

                    }
                    if (totalPrice == 0){
                        shippingCharges.setVisibility(View.GONE);
                        cartValue.setVisibility(View.GONE);
                        orderNowButton.setVisibility(View.GONE);
                        cartEmpty.setVisibility(View.VISIBLE);
                        emptyCartText.setVisibility(View.VISIBLE);
                    }

                    else if(totalPrice < 200 && totalPrice> 0){
                        shippingCharges.setVisibility(View.VISIBLE);
                        shippingCharges.setVisibility(View.VISIBLE);
                        cartValue.setVisibility(View.VISIBLE);
                        orderNowButton.setVisibility(View.VISIBLE);
                        totalPrice+= 20;
                        cartValue.setText(new StringBuilder().append("Total Cart Price: Kshs. ")
                                .append((totalPrice)).append("/- only").toString());
                    }
                    else if(totalPrice >= 200) {
                        shippingCharges.setVisibility(View.GONE);
                        cartValue.setVisibility(View.VISIBLE);
                        orderNowButton.setVisibility(View.VISIBLE);
                        cartValue.setText(new StringBuilder().append("Total Cart Price: Kshs. ")
                                .append((totalPrice)).append("/- only").toString());
                    }
                }
                else {
                    
                    progressBar.setVisibility(View.GONE);
                    cartEmpty.setVisibility(View.VISIBLE);
                    shippingCharges.setVisibility(View.GONE);
                    cartValue.setVisibility(View.GONE);
                    orderNowButton.setVisibility(View.GONE);
                    emptyCartText.setVisibility(View.VISIBLE);

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter=new CartItemAdapter(fragmentActivity, mUploads);
                assert fragmentActivity != null;
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

        orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order();
            }
        });

        return view;
    }

    private void Order() {
        Intent confirmOrderIntent = new Intent(getContext(), AddAddress.class);
        confirmOrderIntent.putExtra("value", String.valueOf(totalPrice));
        Objects.requireNonNull(getContext()).startActivity(confirmOrderIntent);

    }
}