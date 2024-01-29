package com.example.tenthskate.Clients.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Clients.Activities.ViewProductDetailsActivity;
import com.example.tenthskate.Clients.ModelClass.Upload;
import com.example.tenthskate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> implements Filterable {
    private Context mContext;
    private  List<Upload> mUploads;
    private  List<Upload> mUploadsAll=new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("allProducts");



    private float totalRating = 0f;

    public ProductsAdapter(Context context, List<Upload> uploads)
    {
        mContext=context;
        mUploads=uploads;

    }



    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.product_item, viewGroup,false);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploadsAll.clear();
                for(DataSnapshot prods: snapshot.getChildren()){
                    Upload productData = prods.getValue(Upload.class);
                    mUploadsAll.add(productData);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return new ProductsViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder productsViewHolder, final int i) {
        final Upload uploadCur = mUploads.get(i);
        productsViewHolder.product_name.setText(uploadCur.getProductName());
        productsViewHolder.product_price.setText("Kshs. "+ uploadCur.getProductPrice()+" /-");

        if(uploadCur.getQuantity() != null){
//            int sum=Integer.parseInt(uploadCur.getProductQuantity())*Integer.parseInt(uploadCur.getProductPrice());
//            productsViewHolder.quanty.setText("total. "+ uploadCur.getProductQuantity()
//                    +" x "+uploadCur.getProductPrice()+" = "+ sum);
            productsViewHolder.quanty.setText("pkgs. "+ uploadCur.getQuantity()+" :left");
        }
        if(uploadCur.getQuantity() == null){
            productsViewHolder.quanty.setVisibility(View.GONE);
        }

        String mrp = uploadCur.getProductCuttedPrice();
        String ourPrice = uploadCur.getProductPrice();
        if (Integer.parseInt(mrp) > Integer.parseInt(ourPrice)){
            productsViewHolder.product_CuttedPrice.setVisibility(View.VISIBLE);
            productsViewHolder.product_CuttedPrice.setText("Kshs. "+ mrp +" /");
            productsViewHolder.product_CuttedPrice
                    .setPaintFlags(productsViewHolder.product_CuttedPrice.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            productsViewHolder.product_CuttedPrice.setVisibility(View.GONE);
        }

        productsViewHolder.product_details.setText(uploadCur.getProductDescription());
        productsViewHolder.overallRating(uploadCur.getProductID(),productsViewHolder.product_rating);

        Picasso.get()
                .load(uploadCur.getProductImageUrl())
                .placeholder(R.drawable.image_preview)
                .fit()
                .centerCrop()
                .into(productsViewHolder.product_image);


            productsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewProductIntent = new Intent(mContext, ViewProductDetailsActivity.class);
                    viewProductIntent.putExtra("retProductID", uploadCur.getProductID());
                    viewProductIntent.putExtra("quantity", uploadCur.getQuantity());
                    viewProductIntent.putExtra("uploadId", uploadCur.getUploadId());
                    viewProductIntent.putExtra("productCategory", uploadCur.getProductCategory());
                    mContext.startActivity(viewProductIntent);
                }
            });




    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    @Override
    public long getItemId(int item_pos) {
        return super.getItemId(item_pos);
    }

    @Override
    public Filter getFilter() {


        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Upload> filteredList = new ArrayList<>();
                Log.i("FILTER CHAR SEQ: ",charSequence.toString());

                if(charSequence.toString().isEmpty()){
                    filteredList.addAll(mUploadsAll);
                }
                else {

                    for (Upload product : mUploadsAll) {

                        if (product.getSearchableName().contains(charSequence.toString())) {
                            filteredList.add(product);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mUploads.clear();
                mUploads.addAll((Collection<? extends Upload>) filterResults.values);
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, product_price, product_details, product_rating, product_CuttedPrice,quanty;
        public ImageView product_image;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.productCardName);
            quanty = itemView.findViewById(R.id.qny);
            product_price = itemView.findViewById(R.id.productCardPrice);
            product_image = itemView.findViewById(R.id.productCardImage);
            product_details = itemView.findViewById(R.id.productCardDetails);
            product_rating = itemView.findViewById(R.id.productCardRatings);
            product_CuttedPrice = itemView.findViewById(R.id.productCardCuttedPrice);
        }


        private void overallRating(final String productID, final TextView rating) {
            DatabaseReference itemOverallRating = FirebaseDatabase.getInstance().getReference()
                    .child("allProducts").child(productID).child("rating");
            itemOverallRating.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int children = (int) snapshot.getChildrenCount();
                    for (DataSnapshot rating : snapshot.getChildren()) {

                        Object value = rating.getValue();
                        String rating_str = value.toString();
                        totalRating = Float.parseFloat(rating_str) + totalRating;
                    }
                    float averageRating = totalRating/children;

                    DecimalFormat format = new DecimalFormat("#.##");
                    String s = format.format(averageRating);
                    rating.setText(s);
                    totalRating=0f;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

}

