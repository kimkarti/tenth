package com.example.tenthskate.Finance.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tenthskate.Clients.Activities.LoginActivity;
import com.example.tenthskate.Clients.Activities.Singleton;
import com.example.tenthskate.Finance.Adapter.OrderdItemAdapter;
import com.example.tenthskate.Finance.Class.MyOrderItemModel;
import com.example.tenthskate.Finance.Class.OrderdItem;
import com.example.tenthskate.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CheckOrderDetails extends AppCompatActivity {
    List<String> places = new ArrayList<>();
    RecyclerView itemRecyclerView;
    OrderdItemAdapter mAdapter;
    List<OrderdItem> itemList;
    DatabaseReference orderReference,orderReference1;
    DatabaseReference orderItemReference;
    TextView orderID,date,time,fullName,address,mobileNo,value,pesa,email1,Transportfee,orderedproductcost;
    RadioGroup radioGroup;
    String orderIDString,ppesa,email;
    Button decline;
    String dateq,addressq, fullNameq, mobileq, timeq, valueq,uId;
    RadioButton pending,approved,delivered, currentStatus;
    private DatabaseReference mDatabaseCategoryRef = FirebaseDatabase.getInstance().getReference("productsCategory");
    private DatabaseReference mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("allProducts");
    public void calc(String orderid){
        final ProgressDialog progressDialog = new ProgressDialog(CheckOrderDetails.this);
        progressDialog.setTitle("Logging into your account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference resultRef = rootRef.child("Orders").child(orderid).child("Items");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    int quantityb = Integer.parseInt(Objects.requireNonNull(ds.child("productQuantity").getValue(String.class)));
                    String productid = ds.child("productID").getValue(String.class);
                    String category = ds.child("category").getValue(String.class);



                    DatabaseReference  DataRef = FirebaseDatabase.getInstance().getReference().child("allProducts").child(productid);

                    DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            //int total = 0;
                            int quantity = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("quantity").getValue(String.class)));
                            if((quantity-quantityb)>=0) {
                                mDatabaseCategoryRef.child(category).child(productid).
                                        child("quantity").setValue(String.valueOf(quantity - quantityb));
                                mDatabaseProductsRef.child(productid).child("quantity").setValue(String.valueOf(quantity - quantityb));
                                Toast.makeText(CheckOrderDetails.this, "quantity reduced", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(CheckOrderDetails.this, "not enough stock", Toast.LENGTH_SHORT).show();
                                //break;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                //Log.d(TAG, "total= " + total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        resultRef.addListenerForSingleValueEvent(valueEventListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_order_details);

        final Intent intent = getIntent();
        orderIDString = intent.getStringExtra("sendOrderID");
        ppesa = intent.getStringExtra("mpesa");
        email = intent.getStringExtra("email");
        orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderIDString);
      //  orderReference1 = FirebaseDatabase.getInstance().getReference("Users").child(uId).child("orders");
        email1= findViewById(R.id.emailq);
        orderID = findViewById(R.id.orderID);
        date = findViewById(R.id.orderDate);
        time = findViewById(R.id.orderTime);
        fullName = findViewById(R.id.customerName);
        address = findViewById(R.id.orderAddress);
        mobileNo = findViewById(R.id.orderMobile);
        value = findViewById(R.id.orderValue);
        Transportfee = findViewById(R.id.transportfeeValue);
        orderedproductcost =findViewById(R.id.orderedproductcost);
        pesa = findViewById(R.id.ppesa);
        radioGroup = findViewById(R.id.statusRadioGroup);
        pending = findViewById(R.id.pendingStatus);
        approved = findViewById(R.id.approvedStatus);
        //
        email1.setText(email);
        decline = findViewById(R.id.declineStatusq);
        Button app=findViewById(R.id.approveq);
        app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                calc(orderIDString);
                orderReference.child("status").setValue("Approved");

                admin(fullNameq,ppesa,email,mobileq,addressq,"",dateq,places.toString(),valueq,"","2",orderIDString);
                app.setEnabled(false);
                app.setText("Already approved");

            }
        });
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Information about the Purchase you made");
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
                        places.add(Items.child("productName").getValue().toString()+"\t\t"+Items.child("productQuantity").getValue().toString()+"\n");


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

                            dateq=snapshot.child("date").getValue().toString();
                            addressq=snapshot.child("address").getValue().toString();
                            fullNameq=snapshot.child("fullName").getValue().toString();
                             mobileq=snapshot.child("mobile").getValue().toString();
                             timeq=snapshot.child("time").getValue().toString();
                            valueq=snapshot.child("value").getValue().toString();
                    uId=snapshot.child("uId").getValue().toString();

                    date.setText(Objects.requireNonNull(snapshot.child("date").getValue()).toString());
                    address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    fullName.setText(Objects.requireNonNull(snapshot.child("fullName").getValue()).toString());
                    mobileNo.setText(Objects.requireNonNull(snapshot.child("mobile").getValue()).toString());
                    time.setText(Objects.requireNonNull(snapshot.child("time").getValue()).toString());
                    Transportfee.setText("Kshs: 250");
                    orderedproductcost.setText("Kshs: " + (Integer.parseInt(Objects.requireNonNull(snapshot.child("value").getValue()).toString()) - 250));

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

    private void admin(final String username,final String code, final String email, final String phone,
                          final String address, final String shipping,final  String date_time,
                          final String order_list, final String ordertotal,final  String comment,
                            final String status, final String pid) {
        if (phone.length() == 10) {
      final ProgressDialog progressDialog = new ProgressDialog(CheckOrderDetails.this);
            progressDialog.setTitle("Approving");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
           StringRequest request = new StringRequest(Request.Method.POST,"http://"+ LoginActivity.ip+"/tenth/empire/updateShipped.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("successfully")) {
                        View View = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(View, "Successfully Approved", 20000)
                                .setAction("OKAY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //todo
                                    }
                                });
                        snackbar.show();
                      Toast.makeText(CheckOrderDetails.this, "approved", Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(CheckOrderDetails.this, LoginActivity.class));
                        Toast.makeText(CheckOrderDetails.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // finish();
                    } else {
                        Toast.makeText(CheckOrderDetails.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
  }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CheckOrderDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("username", username);
                    param.put("code",code);
                    param.put("email", email);
                    param.put("phone", phone);
                    param.put("address", address);
                    param.put("shipping", shipping);
                    param.put("date_time",date_time);
                    param.put("order_list", order_list);
                    param.put("order_total", ordertotal);
                    param.put("comment", comment);
                    param.put("status", status);
                    param.put("pid", pid);
                    return param;

                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Singleton.getmInstance(CheckOrderDetails.this).addToRequestQueue(request);

        }else{
            Toast.makeText(CheckOrderDetails.this, "max phone length 10", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}