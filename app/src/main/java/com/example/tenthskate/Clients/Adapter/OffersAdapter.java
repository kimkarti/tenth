package com.example.tenthskate.Clients.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Clients.ModelClass.OfferZoneModel;
import com.example.tenthskate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {

    private Context mContext;
    private List<OfferZoneModel> mOffers;

    public OffersAdapter (Context context, List<OfferZoneModel> offerZoneModel){
        mContext = context;
        mOffers = offerZoneModel;
    }

    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.offer_item,parent,false);
        return new OffersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {
        final OfferZoneModel offerZoneModel = mOffers.get(position);
        holder.offerText.setText(offerZoneModel.getOfferName());
        Picasso.get()
                .load(offerZoneModel.getOfferImage())
                .placeholder(R.drawable.image_preview)
                .fit()
                .centerInside()
                .into(holder.offerImage);
    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }

    public class OffersViewHolder extends RecyclerView.ViewHolder {

        public TextView offerText;
        public ImageView offerImage;

        public OffersViewHolder(@NonNull View itemView) {
            super(itemView);
            offerText = itemView.findViewById(R.id.offer_text);
            offerImage = itemView.findViewById(R.id.offer_image);
        }
    }
}
