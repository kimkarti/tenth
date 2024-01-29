package com.example.tenthskate.Supplier.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.R;
import com.example.tenthskate.Supplier.Adapter.OrderdItemAdapter;
import com.example.tenthskate.Supplier.Class.MyOrderItemModel;
import com.example.tenthskate.Supplier.Class.OrderdItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CheckOrderDetails extends AppCompatActivity {

    RecyclerView itemRecyclerView;
    OrderdItemAdapter mAdapter;
    List<OrderdItem> itemList;
    DatabaseReference orderReference;
    DatabaseReference orderItemReference;
    TextView orderID,date,time,fullName,address,mobileNo,value,pesa,email1;
    RadioGroup radioGroup;
    String orderIDString,ppesa,email;
    Button decline;

    RadioButton pending,approved,delivered, currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_order_details);

        final Intent intent = getIntent();
        orderIDString = intent.getStringExtra("sendOrderID");
        ppesa = intent.getStringExtra("mpesa");
        email = intent.getStringExtra("email");
        orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderIDString);
email1= findViewById(R.id.emailq);
        orderID = findViewById(R.id.orderID);
        date = findViewById(R.id.orderDate);
        time = findViewById(R.id.orderTime);
        fullName = findViewById(R.id.customerName);
        address = findViewById(R.id.orderAddress);
        mobileNo = findViewById(R.id.orderMobile);
        value = findViewById(R.id.orderValue);
        pesa = findViewById(R.id.ppesa);
        radioGroup = findViewById(R.id.statusRadioGroup);
      pending = findViewById(R.id.pendingStatus);
        approved = findViewById(R.id.approvedStatus);
        //
        email1.setText(email);
        decline = findViewById(R.id.declineStatus);
        decline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {


                // define Intent object
                // with action attribute as ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);

                // add three fiels to intent using putExtra function
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { email });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Information on Purchase you made");
                intent.putExtra(Intent.EXTRA_TEXT, "dear customer, the purchase you made has been declined" +
                        "because the mpesa code is wrong");

                // set type of intent
                intent.setType("message/rfc822");

                // startActivity with intent with chooser
                // as Email client using createChooser function
                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Choose an Email client :"));
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                currentStatus = radioGroup.findViewById(i);
                orderReference.child("status").setValue(currentStatus.getText().toString());
                statusUser();
            }

        });

        orderID.setText(orderIDString);
        pesa.setText(ppesa);

        //////////////////////////////// ORDERED ITEMS ///////////////////////////////////////

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        itemRecyclerView = findViewById(R.id.orderList);

        itemRecyclerView.setLayoutManager(linearLayoutManager);
        orderItemReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderIDString).child("Items");
        itemList = new ArrayList<>();
        orderItemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot Items:snapshot.getChildren()){
                        OrderdItem items = new OrderdItem(Objects.requireNonNull(Items.child("productName").getValue()).toString(),
                                Objects.requireNonNull(Items.child("productQuantity").getValue()).toString());
                        itemList.add(items);

                    }
                    mAdapter = new OrderdItemAdapter(itemList);
                    itemRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    date.setText(Objects.requireNonNull(snapshot.child("date").getValue()).toString());
                    address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    fullName.setText(Objects.requireNonNull(snapshot.child("fullName").getValue()).toString());
                    mobileNo.setText(Objects.requireNonNull(snapshot.child("mobile").getValue()).toString());
                    time.setText(Objects.requireNonNull(snapshot.child("time").getValue()).toString());
                    value.setText(" Kshs " + Objects.requireNonNull(snapshot.child("value").getValue()).toString()+"/-");

                    if(Objects.requireNonNull(snapshot.child("status").getValue()).toString().equals("Pending"))
                        pending.setChecked(true);
                    else if(Objects.requireNonNull(snapshot.child("status").getValue()).toString().equals("Approved"))
                        approved.setChecked(true);
//                    if(Objects.requireNonNull(snapshot.child("status").getValue()).toString().equals("Delivered"))
//                        delivered.setChecked(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void statusUser(){
        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    final String uid = Objects.requireNonNull(snapshot.child("uId").getValue()).toString();

                    final DatabaseReference userOrderRef = FirebaseDatabase.getInstance().getReference()
                            .child("Users")
                            .child(uid)
                            .child("orders");



                    userOrderRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot orders : snapshot.getChildren()){

                                MyOrderItemModel userOrder = orders.getValue(MyOrderItemModel.class);
                                assert userOrder != null;
                                if(userOrder.getOrderID().equals(orderIDString)){
                                    userOrderRef.child(userOrder.getProductUniqueID())
                                            .child("orderStatus").setValue(currentStatus.getText().toString());
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}