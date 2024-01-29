package com.example.tenthskate.Clients.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import com.example.tenthskate.Clients.ModelClass.Upload;
import com.example.tenthskate.R;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ProductsViewHolder>{

    private Context mContext;
    private List<Upload> mUploads;
    //private DatabaseReference mDatabaseProductsRef, mDatabaseCategoryRef;
    private DatabaseReference mDatabaseCategoryRef = FirebaseDatabase.getInstance().getReference("productsCategory");
    private DatabaseReference mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("allProducts");




    public CartItemAdapter(Context mContext, List<Upload> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.cart_item_layout, parent,false);

        return new ProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder holder, final int position) {
        final Upload uploadCur=mUploads.get(position);
        final DatabaseReference productReference;
        String productID;
        productID = uploadCur.getProductID();

        holder.product_name.setText(uploadCur.getProductName());
        holder.quan.setText(uploadCur.getQuantityy()+": left only");
        holder.product_price.setText("Kshs. "+ uploadCur.getProductPrice()+"/-");

        String cart_mrp = uploadCur.getProductCuttedPrice();
        String cart_ourPrice = uploadCur.getProductPrice();
        if (Integer.parseInt(cart_mrp) > Integer.parseInt(cart_ourPrice)){
            holder.productCuttedPrice.setVisibility(View.VISIBLE);
            holder.productCuttedPrice.setText("Kshs. "+ cart_mrp +" /");
            holder.productCuttedPrice
                    .setPaintFlags(holder.productCuttedPrice.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.productCuttedPrice.setVisibility(View.GONE);
        }

        productReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid()).child("cart").child(productID);



            productReference.child("productQuantity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        try {
                            holder.productQuantity.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                        }
                        catch (Exception e){
                            Log.i("Exception",e.toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        holder.productQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog quantityDialog = new Dialog(view.getContext());
                quantityDialog.setContentView(R.layout.quantity_dialog);
                quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                quantityDialog.setCancelable(false);
                final EditText quantityNo =quantityDialog.findViewById(R.id.quantiy_no);
                Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityDialog.dismiss();
                    }
                });
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String productQuantity_str = quantityNo.getText().toString() ;
                        quantityDialog.dismiss();

                        int mm=Integer.parseInt(productQuantity_str);
                        int mk=Integer.parseInt(uploadCur.getQuantityy());
                        if (!(mm >mk)){
                            productReference.child("productQuantity").setValue(productQuantity_str);

                            ;

//                            mDatabaseCategoryRef.child(uploadCur.getProductCategory()).child(uploadCur.getUploadId()).child("quantity").setValue(String.valueOf(mk-mm));
//                            mDatabaseProductsRef.child(uploadCur.getUploadId()).child("quantity").setValue(String.valueOf(mk-mm));

                           // productReference.child("quantityy").setValue(String.valueOf(mk-mm));
                           // holder.quan.setText(mk + mm +": left only");

                        }
                        else {
                            quantityDialog.dismiss();
                            productReference.child("productQuantity").setValue(uploadCur.getQuantityy());
                            Toast.makeText(mContext, "Maximum Quantity Exceeded, set to available Stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                quantityDialog.show();

            }
        });

        holder.removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUploads.remove(position);
                productReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mContext, "Cart Item Removed", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.removeItemBtn.setVisibility(View.GONE);

            }
        });

        Picasso.get()
                .load(uploadCur.getProductImageUrl())
                .placeholder(R.drawable.image_preview)
                .fit()
                .centerCrop()
                .into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    @Override
    public long getItemId(int item_pos) {

        return super.getItemId(item_pos);
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder{
        public TextView product_name, product_price,productQuantity, productCuttedPrice,quan;
        public ImageView product_image;
        public LinearLayout removeItemBtn;

        public ProductsViewHolder(@NonNull final View itemView) {
            super(itemView);

            product_name=itemView.findViewById(R.id.product_name);
            product_price=itemView.findViewById(R.id.product_price);
            product_image=itemView.findViewById(R.id.product_image);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            removeItemBtn = itemView.findViewById(R.id.remove_item_btn);
            productCuttedPrice = itemView.findViewById(R.id.product_Cuttedprice);
            quan=itemView.findViewById(R.id.quan);
        }
    }
}
