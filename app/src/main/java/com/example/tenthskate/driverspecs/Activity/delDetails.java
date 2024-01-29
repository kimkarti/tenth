package com.example.tenthskate.driverspecs.Activity;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.R;
import com.example.tenthskate.driverspecs.Adapter.OrderdItemAdapter;
import com.example.tenthskate.driverspecs.Class.OrderdItem;
import com.example.tenthskate.stockinventor.Class.MyOrderItemModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class delDetails extends AppCompatActivity {

    RecyclerView itemRecyclerView;
    OrderdItemAdapter mAdapter;
    List<OrderdItem> itemList;
    DatabaseReference orderReference;
    DatabaseReference orderItemReference;
    TextView idholder,date,time,fullName,address,mobileNo,value,pesa;
    RadioGroup radioGroup;
    String orderIDString,ppesa,id,uidd,key,userName;

    RadioButton pending,approved,delivered, currentStatus;
    Button sendEmail, approve,decline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seldriver);
        DatabaseReference df;
        df=FirebaseDatabase.getInstance().getReference("Temporary").child("data");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot:snapshot.getChildren()){

                  id=snapshot.child("orderid").getValue(String.class);
                  uidd=snapshot.child("uId").getValue(String.class);
                    idholder=findViewById(R.id.nameholder);
                    idholder.setText(id);
                       }
               }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        final Intent intent = getIntent();
      //  orderIDString = intent.getStringExtra("sendOrderID");
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();


        userName = email.split("@")[0];

        ppesa = intent.getStringExtra("mpesa");
        sendEmail=findViewById(R.id.sendEmail);
        approve=findViewById(R.id.approve);
        decline=findViewById(R.id.decline);
        decline.setVisibility(View.GONE);
      //  Toast.makeText(this, orderIDString, Toast.LENGTH_SHORT).show();
        sendEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {


                // define Intent object
                // with action attribute as ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);

                // add three fiels to intent using putExtra function
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { email });
                intent.putExtra(Intent.EXTRA_SUBJECT, " Driver Assignment");
                intent.putExtra(Intent.EXTRA_TEXT, "You have been assigned Shipment, log into your app acco" +
                        "unt to see the details");

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
        approve.setText("Assign");
        approve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                approve.setEnabled(false);
                orderReference = FirebaseDatabase.getInstance().getReference();
                orderReference.child("Orders").child(id).child("status").setValue("intransit");
                //key =FirebaseDatabase.getInstance().getReference("Drivers").child(userName).getKey();
                key = FirebaseDatabase.getInstance().getReference().child("Orders").push().getKey();
                DatabaseReference dbNode1 = FirebaseDatabase.getInstance().getReference("Drivers").child(userName).child(key);
               // DatabaseReference dbNode2 = FirebaseDatabase.getInstance().getReference("Orders").child(id).child("status").setValue("intransit");
                HashMap<String, Object> mHashmap = new HashMap<>();
                mHashmap.put("orderid", id);
                mHashmap.put("uId", uidd);
                mHashmap.put("key", key);
                mHashmap.put("status", "intransit");
                dbNode1.updateChildren(mHashmap);
               // Toast.makeText(delDetails.this, "you have assigned this purchase to: "+name, Toast.LENGTH_SHORT).show();
                Toast.makeText(delDetails.this, "Assigned to \t: "+userName, Toast.LENGTH_SHORT).show();


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

                                com.example.tenthskate.stockinventor.Class.MyOrderItemModel userOrder = orders.getValue(MyOrderItemModel.class);
                                assert userOrder != null;
                                if(userOrder.getOrderID().equals(orderIDString)){
                                    userOrderRef.child(userOrder.getProductUniqueID())
                                            .child("orderStatus").setValue("packaging, Please Wait...");
                                    Toast.makeText(delDetails.this, "done", Toast.LENGTH_SHORT).show();
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