package com.example.tenthskate.Shippment.Activity;

/*import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Adapter.OrderdItemAdapter;
import com.example.tenthskate.Shippment.Class.MyOrderItemModel;
import com.example.tenthskate.Shippment.Class.OrderdItem;
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
    DatabaseReference orderReference;
    DatabaseReference orderItemReference;
    TextView orderID,date,time,fullName,address,mobileNo,value;
    RadioGroup radioGroup;
    String orderIDString, uidd;
    String dateq,addressq, fullNameq, mobileq, timeq, valueq,ppesa,email;
    RadioButton pending,approved,delivered, currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toshipment);

        final Intent intent = getIntent();
        orderIDString = intent.getStringExtra("sendOrderID");
        uidd = intent.getStringExtra("uidd");
        Toast.makeText(this, uidd, Toast.LENGTH_SHORT).show();
        orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderIDString);

        orderID = findViewById(R.id.orderID);
        date = findViewById(R.id.orderDate);
        time = findViewById(R.id.orderTime);
        fullName = findViewById(R.id.customerName);
        address = findViewById(R.id.orderAddress);
        mobileNo = findViewById(R.id.orderMobile);
        value = findViewById(R.id.orderValue);

   FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference driversReference;
        driversReference = FirebaseDatabase.getInstance().getReference("UserD");

        DatabaseReference fDatabaseRoot = database.getReference();

        fDatabaseRoot.child("UserD").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> Drivers = new ArrayList<String>();

                for (DataSnapshot addressSnapshot: dataSnapshot.getChildren()) {
                    String drivers = addressSnapshot.child("Drivers").getValue(String.class);
                    if (drivers!=null){
                        Drivers.add(drivers);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        orderID.setText(orderIDString);

        Button approve=findViewById(R.id.driversel);
        approve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {

                admin(fullNameq,ppesa,email,mobileq,addressq,"",dateq,places.toString(),valueq,"","0",orderIDString);
                statusUser();

                //orderReference.child("status").setValue("intransit");

                Intent i = new Intent(CheckOrderDetails.this, MainActivity.class);
                i.putExtra("sendOrderID", orderIDString);
                i.putExtra("uidd", uidd);
                startActivity(i);


            }
        });



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
                    ppesa=snapshot.child("pesa").getValue().toString();
                    email=snapshot.child("email").getValue().toString();
                    date.setText(Objects.requireNonNull(snapshot.child("date").getValue()).toString());
                    address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    fullName.setText(Objects.requireNonNull(snapshot.child("fullName").getValue()).toString());
                    mobileNo.setText(Objects.requireNonNull(snapshot.child("mobile").getValue()).toString());
                    time.setText(Objects.requireNonNull(snapshot.child("time").getValue()).toString());
                    value.setText(" kshs " + Objects.requireNonNull(snapshot.child("value").getValue()).toString()+"/-");


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
                                            .child("orderStatus").setValue("On Transit, wait a few hours");
                                    View View = findViewById(android.R.id.content);
                                    Snackbar snackbar = Snackbar.make(View, "Customer order Status Updated to On Transit", 20000)
                                            .setAction("OKAY", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                }
                                            });
                                    snackbar.show();

                                    Toast.makeText(CheckOrderDetails.this, "continue", Toast.LENGTH_SHORT).show();
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
        Spinner spinner = findViewById(R.id.spinnerq);
        String selectedDriver = spinner.getSelectedItem().toString();

        if (phone.length() == 10) {
            final ProgressDialog progressDialog = new ProgressDialog(CheckOrderDetails.this);
            progressDialog.setTitle("processing");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST,"http://"+ LoginActivity.ip+"/tenth/empire/approved-orders.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("successfully")) {
                       // Toast.makeText(CheckOrderDetails.this, "approved", Toast.LENGTH_SHORT).show();
                     //   startActivity(new Intent(CheckOrderDetails.this, LoginActivity.class));
                        progressDialog.dismiss();
                        View View = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(View, "Successfully sent to Driver", 20000)
                                .setAction("OKAY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                        snackbar.show();
                        Toast.makeText(CheckOrderDetails.this, "Sent To DRIVER", Toast.LENGTH_LONG).show();
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
                    //param.put("driver",selectedDriver);
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

*/


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
import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Adapter.OrderdItemAdapter;
import com.example.tenthskate.Shippment.Class.MyOrderItemModel;
import com.example.tenthskate.Shippment.Class.OrderdItem;
import com.example.tenthskate.driverspecs.Activity.MainActivity;
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
    DatabaseReference orderReference;
    DatabaseReference orderItemReference;
    TextView orderID,date,time,fullName,address,mobileNo,value;
    RadioGroup radioGroup;
    String orderIDString, uidd;
    String dateq,addressq, fullNameq, mobileq, timeq, valueq,ppesa,email;
    RadioButton pending,approved,delivered, currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toshipment);

        final Intent intent = getIntent();
        orderIDString = intent.getStringExtra("sendOrderID");
        uidd = intent.getStringExtra("uidd");
        Toast.makeText(this, uidd, Toast.LENGTH_SHORT).show();
        orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderIDString);

        orderID = findViewById(R.id.orderID);
        date = findViewById(R.id.orderDate);
        time = findViewById(R.id.orderTime);
        fullName = findViewById(R.id.customerName);
        address = findViewById(R.id.orderAddress);
        mobileNo = findViewById(R.id.orderMobile);
        value = findViewById(R.id.orderValue);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference();

        fDatabaseRoot.child("UserD").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> Drivers = new ArrayList<String>();

                for (DataSnapshot addressSnapshot: dataSnapshot.getChildren()) {
                    String drivers = addressSnapshot.child("Drivers").getValue(String.class);
                    if (drivers!=null){
                        Drivers.add(drivers);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        orderID.setText(orderIDString);

        Button approve=findViewById(R.id.driversel);
        approve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                admin(fullNameq,ppesa,email,mobileq,addressq,"",dateq,places.toString(),valueq,"","0",orderIDString);

                statusUser();
                Intent i = new Intent(CheckOrderDetails.this, MainActivity.class);
                i.putExtra("sendOrderID", orderIDString);
                i.putExtra("uidd", uidd);
                startActivity(i);



            }
        });


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
                    ppesa=snapshot.child("pesa").getValue().toString();
                    email=snapshot.child("email").getValue().toString();
                    date.setText(Objects.requireNonNull(snapshot.child("date").getValue()).toString());
                    address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    fullName.setText(Objects.requireNonNull(snapshot.child("fullName").getValue()).toString());
                    mobileNo.setText(Objects.requireNonNull(snapshot.child("mobile").getValue()).toString());
                    time.setText(Objects.requireNonNull(snapshot.child("time").getValue()).toString());
                    value.setText(" kshs " + Objects.requireNonNull(snapshot.child("value").getValue()).toString()+"/-");


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
                                            .child("orderStatus").setValue("On Transit, wait a few hours");
                                    Toast.makeText(CheckOrderDetails.this, "continue", Toast.LENGTH_SHORT).show();
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
            progressDialog.setTitle("processing");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST,"http://"+ LoginActivity.ip+"/tenth/empire/approved-orders.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("successfully")) {
                         Toast.makeText(CheckOrderDetails.this, "to drivers...", Toast.LENGTH_LONG).show();
                           //startActivity(new Intent(CheckOrderDetails.this, com.example.tenthskate.Shippment.Activity.MainActivity.class));

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