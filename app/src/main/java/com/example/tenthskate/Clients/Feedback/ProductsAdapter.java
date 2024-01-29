package com.example.tenthskate.Clients.Feedback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;



public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    DatabaseReference allProducts = FirebaseDatabase.getInstance().getReference().child("allRequests");
    DatabaseReference categoryProducts = FirebaseDatabase.getInstance().getReference().child("SupplierCategory");


    public ProductsAdapter(Context context, List<Upload> uploads)
    {
        mContext=context;
        mUploads=uploads;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.feedback, viewGroup,false);
        return new ProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder productsViewHolder, final int i) {
        final Upload uploadCur=mUploads.get(i);
        productsViewHolder.feedback.setText(uploadCur.getFeedback());
//        productsViewHolder.product_pack.setText(uploadCur.getProductPrice());
//
//        productsViewHolder.quant.setText(uploadCur.getStatus());
//        productsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent manageProductIntent = new Intent(mContext, manageRequest.class);
//                manageProductIntent.putExtra("retProductID", uploadCur.getProductID());
//                mContext.startActivity(manageProductIntent);
//            }
//        });
//        productsViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                final int which_item = i;
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//
//                dialog.setTitle("Not available right now?")
//                        .setMessage("Do you want to delete this product?")
//                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int k) {
//                                mUploads.remove(which_item);
//                                allProducts.child(uploadCur.getProductID()).removeValue();
//                                categoryProducts.child(uploadCur.getProductCategory()).child(uploadCur.getProductID()).removeValue();
//                            }
//                        })
//                        .setNegativeButton("NO",null)
//                        .show();
//                return true;
//            }
//        });
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
        public TextView feedback;
        //public ImageView product_image;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            feedback=itemView.findViewById(R.id.feed);
//            product_pack=itemView.findViewById(R.id.product_card_price1);
//            quant=itemView.findViewById(R.id.status1);
        }

    }
}
