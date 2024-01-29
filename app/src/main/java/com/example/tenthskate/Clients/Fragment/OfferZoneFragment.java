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

import com.example.tenthskate.Clients.Adapter.OffersAdapter;
import com.example.tenthskate.Clients.ModelClass.OfferZoneModel;
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
public class OfferZoneFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OffersAdapter mAdapter;
    private ProgressBar progressBar;
    private List<OfferZoneModel> mOffers;
    TextView  emptyCartText;
    ImageView cartEmpty;

    public OfferZoneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_offer_zone, container, false);

        final FragmentActivity fragmentActivity = getActivity();
        mRecyclerView = view.findViewById(R.id.recycler_view_offer_zone);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));

        progressBar = view.findViewById(R.id.progress_circular_offer_zone);
        cartEmpty = (ImageView) view.findViewById(R.id.empty_cart);
        emptyCartText = view.findViewById(R.id.empty_cart_text);

        mOffers = new ArrayList<>();
        mAdapter = new OffersAdapter(fragmentActivity,mOffers);
        final DatabaseReference mOffersRef = FirebaseDatabase.getInstance().getReference("offers");
        mOffersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mOffers.clear();
                if (snapshot.exists()){

                    for (DataSnapshot postSnapshot: snapshot.getChildren()){
                        OfferZoneModel offerZoneModel = postSnapshot.getValue(OfferZoneModel.class);
                        mOffers.add(offerZoneModel);
                    }
                    mAdapter = new OffersAdapter(fragmentActivity,mOffers);
                    mRecyclerView.setAdapter(mAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    mAdapter.notifyDataSetChanged();
                    cartEmpty.setVisibility(View.GONE);
                    emptyCartText.setVisibility(View.GONE);
                }

                else {
                    cartEmpty.setVisibility(View.VISIBLE);
                    emptyCartText.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }
}