package com.example.tenthskate.Clients.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.tenthskate.Clients.Adapter.ProductsAdapter;
import com.example.tenthskate.Clients.ModelClass.CartUpload;
import com.example.tenthskate.Clients.ModelClass.Order;
import com.example.tenthskate.Clients.ModelClass.Upload;
import com.example.tenthskate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlaceOrderActivity extends AppCompatActivity {
    List<String> places = new ArrayList<>();
    String fullName,shippingAddress,mobileNo,email;
    String orderId,uid;
    TextView totalAmount_view, fullName_view, address_view, mobileNo_view;
    private RecyclerView mRecyclerView;
    private ProductsAdapter mAdapter;
    private ProgressBar progressBar;
    private List<Upload> mUploads;
    Context mcontext;
    EditText pesa;
    int totalPrice,tot=250;
    DatabaseReference orderReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        orderReference = FirebaseDatabase.getInstance().getReference();

        mcontext=this.getApplicationContext();
        mRecyclerView= findViewById(R.id.delivery_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads=new ArrayList<>();

        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Toast.makeText(mcontext, uid, Toast.LENGTH_SHORT).show();
        final DatabaseReference mDatabaseCartRef = FirebaseDatabase.getInstance()
                .getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("cart");

        mDatabaseCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        mUploads.add(upload);
                        places.add(postSnapshot.child("productName").getValue().toString()
                                +"\t\t"+postSnapshot.child("productQuantity").getValue().toString()
                                +" x "+postSnapshot.child("productPrice").getValue().toString()+"\n");

                        TextView bk=findViewById(R.id.breakdown);
                        bk.setText(places.toString().replaceAll("(^\\[|\\]$)", ""));

                    }

                    totalAmount_view.setText("Kshs. " + (totalPrice+tot) + " /-  (250 fee)");
                    mAdapter = new ProductsAdapter(PlaceOrderActivity.this, mUploads);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });



        totalAmount_view = findViewById(R.id.total_cart_amount_);
        fullName_view = findViewById(R.id.full_name);
        address_view = findViewById(R.id.address);
        mobileNo_view = findViewById(R.id.shipping_mobile_no);

        /////////////////// GETTING & SETTING  VALUES FROM ADDRESS ACTIVITY ///////////////////////

        Intent intent = getIntent();
        fullName = intent.getStringExtra("FullName");
        email = intent.getStringExtra("email");
        shippingAddress = intent.getStringExtra("ShippingAddress");
        mobileNo = intent.getStringExtra("MobileNo");
        totalPrice = Integer.parseInt(intent.getStringExtra("value"));

        fullName_view.setText(fullName);
        address_view.setText(shippingAddress);
        mobileNo_view.setText(mobileNo);

        //////////////////////////////////////////////////////////////////////////////////////////
        Button confirmOrderButton = (Button) findViewById(R.id.place_order_btn);
        pesa= findViewById(R.id.pesa);
        confirmOrderButton.setOnClickListener(v -> {
            if(isValidCode(pesa.getText().toString())){
            ConfirmOrder(pesa.getText().toString());
//            finish();
            }
            else{
                Toast.makeText(mcontext, "Invalid M-pesa code!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ConfirmOrder(String pesa) {

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("cart");

        orderId = FirebaseDatabase.getInstance().getReference().child("Orders").push().getKey();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        final String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        final String status = "Pending";

        //////////////////////////////////// For Overall Orders List ///////////////////////////////

        Order order = new Order(
                fullName,
                mobileNo,
                shippingAddress,
                String.valueOf((totalPrice+tot)),
                orderId,
                saveCurrentDate,
                saveCurrentTime,
                status,
                Objects.requireNonNull(FirebaseAuth.getInstance().getUid()),
                pesa
        );

        orderReference.child("Orders").child(orderId).setValue(order);
        orderReference.child("Orders").child(orderId).setValue(order);
        orderReference.child("Orders").child(orderId).child("email").setValue(email);
        //////////////////////////////////// For Item List ///////////////////////////////

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for(DataSnapshot items: snapshot.getChildren()){
                        final CartUpload cartUpload = items.getValue(CartUpload.class);

                        //////////////////////////////// For Customer's Orders List /////////////////////////////////

                        assert cartUpload != null;
                        final String productUniqueID =FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("orders")
                                .push().getKey();

                        HashMap myOrders = new HashMap();
                        myOrders.put("productImage",cartUpload.getProductImageUrl());
                        myOrders.put("productTitle",cartUpload.getProductName());
                        myOrders.put("orderDate","Ordered on "+saveCurrentDate);
                        myOrders.put("productID",cartUpload.getProductID());
                        myOrders.put("productUniqueID",productUniqueID);
                        myOrders.put("orderStatus","Pending");
                        myOrders.put("orderId",orderId);

                        myOrders.put("id",uid);

                        assert productUniqueID != null;
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                .child("orders")
                                .child(productUniqueID)
                                .setValue(myOrders).addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        admin(fullName,pesa,email,mobileNo,shippingAddress,"",saveCurrentDate,places.toString(), String.valueOf((totalPrice+tot)),"","1",productUniqueID);

                                        orderReference.child("Orders").child(orderId).child("Items")
                                                .child(cartUpload.getProductID())
                                                .setValue(cartUpload)
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()){



                                                        Toast.makeText(mcontext,"Thank You For Shopping With Us.",Toast.LENGTH_LONG).show();
                                                        finish();
                                                        totalPrice=0;
                                                        Intent main = new Intent(PlaceOrderActivity.this,MainActivity.class);
                                                        startActivity(main);

                                                    }
                                                    else {
                                                        Toast.makeText(mcontext, "Something Went Wrong. Please Try later.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                    else
                                        Toast.makeText(mcontext, "Something Went Wrong. Please Try later.", Toast.LENGTH_SHORT).show();
                                });


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/////////////////////////////////////////////////////////////////////////////////////////////////

        cartRef.removeValue();

    }
    public boolean isValidCode(final String code) {

        boolean number = false;
        boolean character = false;
        boolean symbol = false;
        boolean length = false;
        int letterCounter = 0;
        int numCounter = 0;

        char [] letters = code.toCharArray();

        for (char c: letters){
            if(Character.isLetter(c)) {
                letterCounter++;
            }
            else if(Character.isDigit(c)) {
                numCounter++;
            }
            else {
                symbol = true;
            }
        }

        //Checking booleans
        if (code.length()>=10) {
            length = true;
        }
        if (letterCounter>=8) {
            character = true;
        }
        if (numCounter>=2) {
            number = true;
        }

        if (character && length && number && !symbol){
            Toast.makeText(PlaceOrderActivity.this, "valid mpesa code", Toast.LENGTH_SHORT).show();
            System.out.println("Success");
            return true;
        }
        else {
            System.out.println("Invalid");
            return false;
        }

    }
    private void admin(final String username,final String code, final String email, final String phone,
                       final String address, final String shipping,final  String date_time,
                       final String order_list, final String ordertotal,final  String comment,
                       final String status, final String pid) {
        if (phone.length() == 10) {
            final ProgressDialog progressDialog = new ProgressDialog(PlaceOrderActivity.this);
            progressDialog.setTitle("loading");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST,"http://"+ LoginActivity.ip+"/tenth/empire/pending-orders.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("successfully")) {
                        Toast.makeText(PlaceOrderActivity.this, "approved", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(CheckOrderDetails.this, LoginActivity.class));
                        Toast.makeText(PlaceOrderActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // finish();
                    } else {
                        Toast.makeText(PlaceOrderActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PlaceOrderActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
            Singleton.getmInstance(PlaceOrderActivity.this).addToRequestQueue(request);

        }else{
            Toast.makeText(PlaceOrderActivity.this, "max phone length 10", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}