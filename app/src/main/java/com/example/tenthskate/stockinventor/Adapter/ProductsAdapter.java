package com.example.tenthskate.stockinventor.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.tenthskate.R;
import com.example.tenthskate.stockinventor.Activity.ManageProductActivity;
import com.example.tenthskate.stockinventor.Class.Upload;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    DatabaseReference allProducts = FirebaseDatabase.getInstance().getReference().child("allProducts");
    DatabaseReference categoryProducts = FirebaseDatabase.getInstance().getReference().child("productsCategory");


    public ProductsAdapter(Context context, List<Upload> uploads)
    {
        mContext=context;
        mUploads=uploads;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.stock_product_item, viewGroup,false);
        return new ProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder productsViewHolder, final int i) {
        final Upload uploadCur=mUploads.get(productsViewHolder.getAdapterPosition());
        productsViewHolder.product_name.setText(uploadCur.getProductName());
        productsViewHolder.product_code.setText(uploadCur.getProductCode());
        productsViewHolder.product_price.setText(uploadCur.getProductPrice());
        productsViewHolder.product_cutted_price.setText(uploadCur.getProductCuttedPrice());
        Picasso.get()
                .load(uploadCur.getProductImageUrl())
                .placeholder(R.drawable.image_preview)
                .fit()
                .into(productsViewHolder.product_image);

        productsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageProductIntent = new Intent(mContext, ManageProductActivity.class);
                manageProductIntent.putExtra("retProductID", uploadCur.getProductID());
                mContext.startActivity(manageProductIntent);
            }
        });
        productsViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final int which_item = i;

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,R.style.AppTheme);

                dialog.setTitle("Not available right now?")
                        .setMessage("Do you want to delete this product?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                mUploads.remove(which_item);
                                allProducts.child(uploadCur.getProductID()).removeValue();
                                categoryProducts.child(uploadCur.getProductCategory()).child(uploadCur.getProductID()).removeValue();
                            }
                        })
                        .setNegativeButton("NO",null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    @Override
    public long getItemId(int item_pos)
    {
        return super.getItemId(item_pos);
    }





    public static class ProductsViewHolder extends RecyclerView.ViewHolder{
        public TextView product_name, product_code, product_price, product_cutted_price;
        public ImageView product_image;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name=itemView.findViewById(R.id.product_card_name);
            product_code=itemView.findViewById(R.id.product_card_code);
            product_price=itemView.findViewById(R.id.product_card_price);
            product_cutted_price=itemView.findViewById(R.id.product_card_cutted_price);
            product_image=itemView.findViewById(R.id.product_card_image);
        }

    }
}
