package com.example.tenthskate.SupplyAuto.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.R;
import com.example.tenthskate.SupplyAuto.Activity.delDetails;
import com.example.tenthskate.SupplyAuto.Class.Order;

import java.util.List;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ProductsViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<Order> mUploads;
    View v;

    public OrdersAdapter(Context context, List<Order> orders)
    {
        mContext=context;
        mUploads=orders;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v= LayoutInflater.from(mContext).inflate(R.layout.supplyauto, viewGroup,false);
        return new ProductsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder productsViewHolder, @SuppressLint("RecyclerView") final int i) {
        final Order orderCur=mUploads.get(i);


        productsViewHolder.name.setText(orderCur.getProductName());

        productsViewHolder.email.setText(orderCur.getProductCategory());

        productsViewHolder.rquan.setText(orderCur.getQuantity()+" left");
        productsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkDetailsIntent = new Intent(mContext, delDetails.class);

                checkDetailsIntent.putExtra("name", orderCur.getProductName());
                checkDetailsIntent.putExtra("desc", orderCur.getProductDescription());
                checkDetailsIntent.putExtra("pId", orderCur.getProductID());
                checkDetailsIntent.putExtra("category", orderCur.getProductCategory());
                mContext.startActivity(checkDetailsIntent);
            }
        });

        productsViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final int which_item = i;

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,androidx.appcompat.R.style.Theme_AppCompat_DayNight_Dialog_Alert);

                dialog.setTitle("Is it delivered?")
                        .setMessage("Do you want to delete this order?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                mUploads.remove(which_item);
                                //order.child(orderCur.getOrderID()).removeValue();
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
    public long getItemId(int item_pos) {

        return super.getItemId(item_pos);
    }

    @Override
    public void onClick(View v) {

    }

    public interface mClickListener {
        public void mClick(View v, int position);
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder{
        public TextView name, phone, email, del,rquan;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.dri);
            rquan=itemView.findViewById(R.id.rquan);
           email=itemView.findViewById(R.id.emaill);
           // del=itemView.findViewById(R.id.task);

        }

    }
}
