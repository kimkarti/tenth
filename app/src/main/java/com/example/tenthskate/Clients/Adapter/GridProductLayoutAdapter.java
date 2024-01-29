package com.example.tenthskate.Clients.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tenthskate.Clients.Activities.ViewProductDetailsActivity;
import com.example.tenthskate.Clients.ModelClass.HorizontalPopularProductModel;
import com.example.tenthskate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {
    List<HorizontalPopularProductModel> gridProductsList;
    Context mContext;

    public GridProductLayoutAdapter(List<HorizontalPopularProductModel> gridProductsList, Context mContext) {
        this.gridProductsList = gridProductsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View view1 ;
        if(view == null){
            view1 = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout,null);
            view1.setElevation(3);
            TextView productCuttedPrice=view1.findViewById(R.id.horizontal_product_cutted_price);
            ImageView productImage=view1.findViewById(R.id.horizontal_product_image);
            TextView productPrice=view1.findViewById(R.id.horizontal_product_price);
            TextView productTitle=view1.findViewById(R.id.horizontal_product_name);

            productTitle.setText(gridProductsList.get(i).getProductTitle());
            productPrice.setText("Rs. "+gridProductsList.get(i).getProductPrice()+"/-");

            String grid_mrp = gridProductsList.get(i).getProductCuttedPrice();
            String grid_ourPrice = gridProductsList.get(i).getProductPrice();
            if (Integer.parseInt(grid_mrp) > Integer.parseInt(grid_ourPrice)){
                productCuttedPrice.setVisibility(View.VISIBLE);
                productCuttedPrice.setText("Rs. "+ grid_mrp +"/-");
                productCuttedPrice.setPaintFlags(productCuttedPrice.getPaintFlags()
                        | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                productCuttedPrice.setVisibility(View.GONE);
            }

            Picasso.get().load(gridProductsList.get(i).getProductImage())
                    .fit()
                    .into(productImage);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewProductIntent = new Intent(mContext, ViewProductDetailsActivity.class);
                    viewProductIntent.putExtra("retProductID", gridProductsList.get(i).getProductID());
                    mContext.startActivity(viewProductIntent);
                }
            });
        }
        else view1 = view;
        return view1;
    }
}
