package com.example.tenthskate.Shippment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Class.OrderdItem;

import java.util.List;


public class OrderdItemAdapter extends RecyclerView.Adapter<OrderdItemAdapter.ViewHolder> {

    private List<OrderdItem> mList;

    public OrderdItemAdapter(List<OrderdItem> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_items_ordered,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderdItem item = mList.get(position);
        holder.quantity.setText(item.getProductQuantity());
        holder.product.setText(item.getProductName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView product,quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.productID);
            quantity = itemView.findViewById(R.id.productQuantity);
        }
    }
}
