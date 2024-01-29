package com.example.tenthskate.Supplier.Adapter;

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
import com.example.tenthskate.Supplier.Activity.CheckOrderDetails;
import com.example.tenthskate.Supplier.Class.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class ApproveAdapter extends RecyclerView.Adapter<ApproveAdapter.ProductsViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<Order> mUploads;
    View v;

    DatabaseReference order = FirebaseDatabase.getInstance().getReference().child("Orders");
    public ApproveAdapter(Context context, List<Order> orders)
    {
        mContext=context;
        mUploads=orders;
    }

    @SuppressLint("NotifyDataSetChanged")
    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v= LayoutInflater.from(mContext).inflate(R.layout.finance_order_item, viewGroup,false);
        notifyDataSetChanged();
        return new ProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder productsViewHolder, @SuppressLint("RecyclerView") final int i) {
        final Order orderCur=mUploads.get(i);


        productsViewHolder.orderID.setText(orderCur.getOrderID());
        productsViewHolder.date.setText(orderCur.getDate());
        productsViewHolder.time.setText(orderCur.getTime());
        productsViewHolder.value.setText(orderCur.getValue());
        productsViewHolder.pesa.setText(orderCur.getPesa());
        productsViewHolder.status.setText(orderCur.getStatus());

        productsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkDetailsIntent = new Intent(mContext, CheckOrderDetails.class);
                checkDetailsIntent.putExtra("sendOrderID", orderCur.getOrderID());
                checkDetailsIntent.putExtra("mpesa", orderCur.getPesa());
               // mContext.startActivity(checkDetailsIntent);
            }
        });

        productsViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final int which_item = i;
              //  notifyDataSetChanged();
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,R.style.AppTheme);

                dialog.setTitle("Is it delivered?")
                        .setMessage("Do you want to delete this order?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                mUploads.remove(which_item);
                                order.child(orderCur.getOrderID()).removeValue();
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
        public TextView orderID, date, time, status,pesa,value;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID=itemView.findViewById(R.id.orderID);
            date=itemView.findViewById(R.id.orderDate);
            time=itemView.findViewById(R.id.orderTime);
            status=itemView.findViewById(R.id.orderStatus);
            pesa=itemView.findViewById(R.id.orderpesa);
            value=itemView.findViewById(R.id.value);
        }

    }
}
