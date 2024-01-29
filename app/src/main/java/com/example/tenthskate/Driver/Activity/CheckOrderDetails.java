package com.example.tenthskate.Driver.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.tenthskate.Driver.Adapter.OrderdItemAdapter;
import com.example.tenthskate.Driver.Class.MyOrderItemModel;
import com.example.tenthskate.Driver.Class.OrderdItem;
import com.example.tenthskate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    String orderID1,date1,time1,fullName1,address1,mobileNo1,value1,email1;
    RecyclerView itemRecyclerView;
    OrderdItemAdapter mAdapter;
    List<OrderdItem> itemList;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    DatabaseReference orderReference;
    DatabaseReference orderItemReference,databaseReference1;
    TextView orderID,date,time,fullName,address,mobileNo,value,pesa;
    RadioGroup radioGroup;
    String orderIDString,ppesa,uidd,m,correct,key,pesa1;
    EditText pass;
    private FirebaseAuth mAuth;
    RadioButton pending,approved,delivered, currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverorderdetails);
        m=MainActivity.startactivity();

        mAuth = FirebaseAuth.getInstance();
        final Intent intent = getIntent();
        orderIDString = intent.getStringExtra("sendOrderID");
        ppesa = intent.getStringExtra("mpesa");
        uidd = intent.getStringExtra("uidd");
        key = intent.getStringExtra("key");
        orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderIDString);
        pass = findViewById(R.id.pass);
        orderID = findViewById(R.id.orderID);
        date = findViewById(R.id.orderDate);
        time = findViewById(R.id.orderTime);
        fullName = findViewById(R.id.customerName);
        address = findViewById(R.id.orderAddress);
        mobileNo = findViewById(R.id.orderMobile);
        value = findViewById(R.id.orderValue);
        pesa = findViewById(R.id.ppesa);
        orderID.setText(orderIDString);


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

                        places.add(Items.child("productName").getValue().toString()
                                +"\t\t"+Items.child("productQuantity").getValue().toString()
                                +" x "+Items.child("productPrice").getValue().toString()+"\n");

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

                    date1=Objects.requireNonNull(snapshot.child("date").getValue()).toString();
                    email1=Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    address1=Objects.requireNonNull(snapshot.child("address").getValue()).toString();
                    fullName1=Objects.requireNonNull(snapshot.child("fullName").getValue()).toString();
                    mobileNo1=Objects.requireNonNull(snapshot.child("mobile").getValue()).toString();
                    time1=Objects.requireNonNull(snapshot.child("time").getValue()).toString();
                    value1=Objects.requireNonNull(snapshot.child("value").getValue()).toString();
                                        Toast.makeText(CheckOrderDetails.this, email1, Toast.LENGTH_SHORT).show();
                    pesa1=Objects.requireNonNull(snapshot.child("pesa").getValue()).toString();
                    pesa.setText(pesa1);
                    date.setText(Objects.requireNonNull(snapshot.child("date").getValue()).toString());
                    address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    fullName.setText(Objects.requireNonNull(snapshot.child("fullName").getValue()).toString());
                    mobileNo.setText(Objects.requireNonNull(snapshot.child("mobile").getValue()).toString());
                    time.setText(Objects.requireNonNull(snapshot.child("time").getValue()).toString());
                    value.setText(" Kshs " + Objects.requireNonNull(snapshot.child("value").getValue()).toString()+"/-");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button confirmpass=findViewById(R.id.confirmpass);
        confirmpass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                if(pass.getText().toString().equals("")){
                    pass.setError("Enter valid password");
                    pass.requestFocus();
                }
                else{
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Users").child(uidd);


                    databaseReference1 = firebaseDatabase.getReference("Drivers").child(m);
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists())
//                        {
//                            orderReference.child("status").setValue("Delivered");
//                            databaseReference1.child(key).child("status").setValue("Delivered");
//                            DatabaseReference df;
//                            df=FirebaseDatabase.getInstance().getReference("Users").child(uidd);
//                            df.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                                    for(DataSnapshot childSnapshot:snapshot.getChildren()){
//
//                                        correct=snapshot.child("password").getValue(String.class);
//
//                                    }
//                                    // Toast.makeText(getActivity(), Name4+" noo", Toast.LENGTH_SHORT).show();
//                                    if(pass.getText().toString().trim().equals(correct)){
//                                        statusUser();
//                                        admin(fullName1,ppesa,email1,mobileNo1,address1,"",date1,places.toString(), value1,"","3",orderIDString);
//
////                                DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("Drivers").child(m).child(key);
////                                dbNode.setValue(null);
//                                    }
//                                    else{
//                                      //  Toast.makeText(CheckOrderDetails.this, uidd, Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(CheckOrderDetails.this, "Wrong Password!!", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                                }
//                            });
//
//
//                           // loadingBar.dismiss();
//                        }
//                        else{
//                            Toast.makeText(CheckOrderDetails.this, "Wrong Password!", Toast.LENGTH_LONG).show();
//                            //loadingBar.dismiss();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                    mAuth.signInWithEmailAndPassword(email1.trim(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference("Users").child(uidd);


                                databaseReference1 = firebaseDatabase.getReference("Drivers").child(m);
                                orderReference.child("status").setValue("Delivered");
                                databaseReference1.child(key).child("status").setValue("Delivered");

                                admin(pesa.getText().toString(),"4");

                                confirmpass.setEnabled(false);
                                confirmpass.setText("Delivered");
                                Toast.makeText(CheckOrderDetails.this, "Correct Password, Shipment confirmed!", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(CheckOrderDetails.this,"..Please Try Again",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }

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
                                            .child("orderStatus").setValue("Delivered!");

                                    Toast.makeText(CheckOrderDetails.this, "done", Toast.LENGTH_SHORT).show();
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
    private void admin(final String code,final String status) {
       // if (phone.length() == 10) {
            final ProgressDialog progressDialog = new ProgressDialog(CheckOrderDetails.this);
            progressDialog.setTitle("loading");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST,"http://"+ LoginActivity.ip+"/tenth/empire/updateShipped.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("Record updated successfully")) {
                        //Toast.makeText(CheckOrderDetails.this, "approved", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(CheckOrderDetails.this, LoginActivity.class));
                        Toast.makeText(CheckOrderDetails.this, "Successfully delivered", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // finish();
                    } else {
                        Toast.makeText(CheckOrderDetails.this, response+"\t Error", Toast.LENGTH_SHORT).show();
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
//                    param.put("username", username);
                    param.put("code",code);
//                    param.put("email", email);
//                    param.put("phone", phone);
//                    param.put("address", address);
//                    param.put("shipping", shipping);
//                    param.put("date_time",date_time);
//                    param.put("order_list", order_list);
//                    param.put("order_total", ordertotal);
//                    param.put("comment", comment);
                    param.put("status", status);
                   /// param.put("pid", pid);
                    return param;

                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Singleton.getmInstance(CheckOrderDetails.this).addToRequestQueue(request);

//        }else{
//            Toast.makeText(CheckOrderDetails.this, "max phone length 10", Toast.LENGTH_SHORT).show();
//            return;
//        }
    }
}