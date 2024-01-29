package com.example.tenthskate.Clients.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Clients.ModelClass.MyOrderItemModel;
import com.example.tenthskate.R;
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

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Context mContext;
    private List<MyOrderItemModel> myOrderItemModelList;
    private String ourMob;
    View view1;

    public MyOrderAdapter(Context mContext, List<MyOrderItemModel> myOrderItemModelList) {

        this.mContext = mContext;
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout, viewGroup, false);
        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String resource = myOrderItemModelList.get(position).getProductImage();
        final String productUniqueID = myOrderItemModelList.get(position).getProductUniqueID();
        final String title=myOrderItemModelList.get(position).getProductTitle();
        final String productID = myOrderItemModelList.get(position).getProductID();
        final String orderID = myOrderItemModelList.get(position).getOrderId();
        String deliveredDate=myOrderItemModelList.get(position).getOrderDate();
        String orderStatus = myOrderItemModelList.get(position).getOrderStatus();
        viewHolder.setData(resource,title,deliveredDate,productID,orderStatus,orderID);
        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateItem(title,v,productID,productID);
            }
        });
        viewHolder.productTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewHolder.rece.setVisibility(View.GONE);
                // viewHolder.ratingBar.setVisibility(View.GONE);
                //viewHolder.cancelOrder.setVisibility(View.GONE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,R.style.AppTheme);

                dialog.setTitle("Order delivery")
                        .setMessage("Have you received this order?")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                Toast.makeText(mContext, "Status to be updated", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                Toast.makeText(mContext, "Okay wait", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(R.drawable.ic_launcher)
                        .show();

            }
        });

        viewHolder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewHolder.rece.setVisibility(View.GONE);
               // viewHolder.ratingBar.setVisibility(View.GONE);
                //viewHolder.cancelOrder.setVisibility(View.GONE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,R.style.AppTheme);

                dialog.setTitle("Accept/Reject this order")
                        .setMessage("Do you want to reject this order, by sending an email to us?")
                        .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                Toast.makeText(mContext, "continue", Toast.LENGTH_SHORT).show();
                                // define Intent object
                                // with action attribute as ACTION_SEND
                                Intent intent = new Intent(Intent.ACTION_SEND);

                                // add three fiels to intent using putExtra function
                                intent.putExtra(Intent.EXTRA_EMAIL,
                                        new String[] { "markellytech@gmail.com" });
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Rejecting Order Ref: "+orderID);
                                intent.putExtra(Intent.EXTRA_TEXT, "Hello.." +
                                        "I ordered my products on: "+deliveredDate+" I'm rejecting because...");

                                // set type of intent
                                intent.setType("message/rfc822");

                                // startActivity with intent with chooser
                                // as Email client using createChooser function
                                mContext.startActivity(
                                        Intent
                                                .createChooser(intent,
                                                        "Choose an Email client :"));

                            }
                        })
                        .setNegativeButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                Toast.makeText(mContext, "thanks for the support", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(R.drawable.ic_launcher)
                        .show();

            }
        });


        if(orderStatus.equals("Pending")){
            viewHolder.cancelOrder.setVisibility(View.VISIBLE);

            viewHolder.cancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,R.style.AppTheme);

                    dialog.setTitle("Cancel Item")
                            .setMessage("Do you want to cancel this order?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int k) {
                                    deleteOrder(productID,orderID,productUniqueID);
                                }
                            })
                            .setNegativeButton("NO",null)
                            .setIcon(R.drawable.ic_launcher)
                            .show();

                }
            });

        }

        else if (orderStatus.equals("Approved")){
            viewHolder.cancelOrder.setVisibility(View.VISIBLE);

            FirebaseDatabase.getInstance().getReference()
                    .child("ourContacts")
                    .child("mobile")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        ourMob = Objects.requireNonNull(snapshot.getValue()).toString();
                    }
                    else {
                        ourMob = "+254759867556";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            viewHolder.cancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,R.style.AppTheme);

                    dialog.setTitle("Cancel Item")
                            .setMessage("Do you want to cancel this order? " +
                                    "This order is approved by Floor Decor. " +
                                    "Please Call\n" +
                                    ourMob +
                                    "\nto cancel the order.")
                            .setNegativeButton("OK",null)
                            .setIcon(R.drawable.ic_launcher)
                            .show();

                }
            });
        }

        else {
            viewHolder.cancelOrder.setVisibility(View.GONE);
        }
    }

    private void deleteOrder(final String productID, String orderID, final String productUniqueID) {


        final DatabaseReference mainOrder = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(orderID);

        final DatabaseReference userOrder = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("orders")
                .child(productUniqueID);

       userOrder.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {

               Toast.makeText(mContext,"Item cancelled successfully.",Toast.LENGTH_SHORT).show();
           }
       });

        mainOrder.child("Items").child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mainOrder.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild("Items")){
                            mainOrder.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(mContext,"Item cancelled successfully.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void rateItem(String itemName , float rating,String productID ,String productUniqueID) {

        DatabaseReference itemOverallRating = FirebaseDatabase.getInstance().getReference()
                .child("allProducts").child(productID);
        itemOverallRating.child("rating")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(rating);
    }

    @Override
    public int getItemCount() {

        return myOrderItemModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView orderDate;
        public RatingBar ratingBar;
        public TextView orderStatus;
        public Button cancelOrder,rece;
        public TextView orderId;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle=itemView.findViewById(R.id.product_title);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            orderDate = itemView.findViewById(R.id.order_delivered_date);
            orderStatus = itemView.findViewById(R.id.order_status);
            cancelOrder = itemView.findViewById(R.id.cancel_order);
            orderId = itemView.findViewById(R.id.order_id_view);
            //rece = itemView.findViewById(R.id.product_image);

        }
        private void setData(String resource, String title, String deliveredDate, String productID, String order_status, String orderID){
            Picasso.get()
                    .load(resource)
                    .placeholder(R.drawable.image_preview)
                    .fit()
                    .centerCrop()
                    .into(productImage);
            productTitle.setText(title);
            orderDate.setText(deliveredDate);

            DatabaseReference orderItemRating = FirebaseDatabase.getInstance().getReference()
                    .child("allProducts").child(productID)
                    .child("rating").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

            orderItemRating.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()){
                        ratingBar.setRating(Float.parseFloat(Objects.requireNonNull(snapshot.getValue()).toString()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            orderStatus.setText(order_status);
            orderId.setText("Order ID: "+ orderID );

        }
    }
}
