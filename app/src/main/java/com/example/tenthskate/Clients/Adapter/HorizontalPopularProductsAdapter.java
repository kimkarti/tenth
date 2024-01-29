package com.example.tenthskate.Clients.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Clients.Activities.ViewProductDetailsActivity;
import com.example.tenthskate.Clients.ModelClass.HorizontalPopularProductModel;
import com.example.tenthskate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalPopularProductsAdapter extends RecyclerView.Adapter<HorizontalPopularProductsAdapter.ViewHolder> {

    private List<HorizontalPopularProductModel> horizontalPopularProductModelList;
    private Context mContext;

    public HorizontalPopularProductsAdapter(List<HorizontalPopularProductModel> horizontalPopularProductModelList, Context mContext) {
        this.horizontalPopularProductModelList = horizontalPopularProductModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HorizontalPopularProductModel horizontalPopularProductModel = horizontalPopularProductModelList.get(position);

        Picasso.get().load(horizontalPopularProductModel.getProductImage())
                .fit()
                .into(holder.productImage);

        holder.productTitle.setText(horizontalPopularProductModel.getProductTitle());
        holder.productPrice.setText("Rs. "+horizontalPopularProductModel.getProductPrice()+"/-");

        String horizontal_mrp = horizontalPopularProductModel.getProductCuttedPrice();
        String horizontal_ourPrice = horizontalPopularProductModel.getProductPrice();
        if (Integer.parseInt(horizontal_mrp) > Integer.parseInt(horizontal_ourPrice)){
            holder.productCuttedPrice.setVisibility(View.VISIBLE);
            holder.productCuttedPrice.setText("Rs. "+ horizontal_mrp +"/-");
            holder.productCuttedPrice.setPaintFlags(holder.productCuttedPrice.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.productCuttedPrice.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProductIntent = new Intent(mContext, ViewProductDetailsActivity.class);
                viewProductIntent.putExtra("retProductID", horizontalPopularProductModel.getProductID());
                mContext.startActivity(viewProductIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalPopularProductModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle,productCuttedPrice,productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productCuttedPrice=itemView.findViewById(R.id.horizontal_product_cutted_price);
            productImage=itemView.findViewById(R.id.horizontal_product_image);
            productPrice=itemView.findViewById(R.id.horizontal_product_price);
            productTitle=itemView.findViewById(R.id.horizontal_product_name);

        }
    }
}
