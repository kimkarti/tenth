package com.example.tenthskate.Receipt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Finance.Adapter.OrderdItemAdapter;
import com.example.tenthskate.Finance.Class.OrderdItem;
import com.example.tenthskate.R;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RDetails extends AppCompatActivity {

    RecyclerView itemRecyclerView,itemRecyclerView1;
    OrderdItemAdapter mAdapter;
    List<OrderdItem> itemList;
    DatabaseReference orderReference;
    DatabaseReference orderItemReference;
    TextView orderID,date,time,fullName,address,mobileNo,value,pesa,email1,sta;

    TextView orderID1,date1,time1,fullName1,address1,mobileNo1,value1,pesa1,email11,sta1,item;
    RadioGroup radioGroup;
    String orderIDString,ppesa,email,status,con;
    Button decline,rece;
       View content;
    RadioButton pending,approved,delivered, currentStatus;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rdetails);


        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = inflater.inflate(R.layout.receiptt, null);

        final Intent intent = getIntent();
        orderIDString = intent.getStringExtra("sendOrderID");
        ppesa = intent.getStringExtra("mpesa");
        email = intent.getStringExtra("email");
        status = intent.getStringExtra("status");
        orderReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderIDString);

        email11= content.findViewById(R.id.emailq);
        item= content.findViewById(R.id.con);
        email11.setText(email);
        orderID1 = content.findViewById(R.id.orderID);
        orderID1.setText(orderIDString);
        date1 = content.findViewById(R.id.orderDate);
       // date1.setText(da);
        time1 = content.findViewById(R.id.orderTime);
        fullName1 = content.findViewById(R.id.customerName);
        address1 = content.findViewById(R.id.orderAddress);
        mobileNo1 = content.findViewById(R.id.orderMobile);
        value1 = content.findViewById(R.id.orderValue);
        pesa1 = content.findViewById(R.id.ppesa);
        pesa1.setText(ppesa);
        sta1 = content.findViewById(R.id.load);

        email1= findViewById(R.id.emailq);
        orderID = findViewById(R.id.orderID);
        date = findViewById(R.id.orderDate);
        time = findViewById(R.id.orderTime);
        fullName = findViewById(R.id.customerName);
        address = findViewById(R.id.orderAddress);
        mobileNo = findViewById(R.id.orderMobile);
        value = findViewById(R.id.orderValue);
        pesa = findViewById(R.id.ppesa);
        rece = findViewById(R.id.rec);
        sta = findViewById(R.id.load);
        sta.setText(status);
        sta1.setText(status);
        email1.setText(email);




        orderID.setText(orderIDString);
        pesa.setText(ppesa);

        //////////////////////////////// ORDERED ITEMS ///////////////////////////////////////

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        itemRecyclerView = findViewById(R.id.orderList);
        itemRecyclerView1 = content.findViewById(R.id.orderList);


        itemRecyclerView.setLayoutManager(linearLayoutManager);
        orderItemReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderIDString).child("Items");
        itemList = new ArrayList<>();
        orderItemReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> places = new ArrayList<>();
                    for (DataSnapshot Items:snapshot.getChildren()){
                        OrderdItem items = new OrderdItem(Objects.requireNonNull(Items.child("productName").getValue()).toString(),
                                Objects.requireNonNull(Items.child("productQuantity").getValue()).toString());
                        itemList.add(items);
                        //bk.setText(places.toString().replaceAll("(^\\[|\\]$)", ""));
                     //   places.add(Items.child("productName").getValue().toString()+"\t\t"+Items.child("productQuantity").getValue().toString()+"\n");
                        places.add(Items.child("productName").getValue().toString()
                                +"\t\t"+Items.child("productQuantity").getValue().toString()
                                +" x "+Items.child("productPrice").getValue().toString()+"\n");


                    }
                    item.setText(places.toString().replaceAll("(^\\[|\\]$)", ""));
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
                    date1.setText(Objects.requireNonNull(snapshot.child("date").getValue()).toString());
                    address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    address1.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    fullName.setText(Objects.requireNonNull(snapshot.child("fullName").getValue()).toString());
                    fullName1.setText(Objects.requireNonNull(snapshot.child("fullName").getValue()).toString());
                    mobileNo.setText(Objects.requireNonNull(snapshot.child("mobile").getValue()).toString());
                    mobileNo1.setText(Objects.requireNonNull(snapshot.child("mobile").getValue()).toString());
                    time.setText(Objects.requireNonNull(snapshot.child("time").getValue()).toString());
                    time1.setText(Objects.requireNonNull(snapshot.child("time").getValue()).toString());
                    value.setText(" Kshs " + Objects.requireNonNull(snapshot.child("value").getValue()).toString()+"/-");
                    value1.setText(" Kshs " + Objects.requireNonNull(snapshot.child("value").getValue()).toString()+"/-");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rece.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view)
            {

              //  itemRecyclerView1.setAdapter(mAdapter);
//               Button rece1 = content.findViewById(R.id.rec);
//                rece.setVisibility(View.GONE);

                if(status.equals("Approved") || status.equals("Delivered")){
                    PdfGenerator.getBuilder()
                            .setContext(RDetails.this)
                           .fromViewSource()
                            .fromView(content)
                            .setFileName("Receipt")
                            .setFolderName("PDF-folder")
                            .openPDFafterGeneration(true)
                            .build(new PdfGeneratorListener() {
                                @Override
                                public void onFailure(FailureResponse failureResponse) {
                                    super.onFailure(failureResponse);
                                }
                                @Override
                                public void onStartPDFGeneration() {

                                }
                                @Override
                                public void onFinishPDFGeneration() {

                                }
                                @Override
                                public void showLog(String log) {
                                    super.showLog(log);
                                }

                                @Override
                                public void onSuccess(SuccessResponse response) {
                                    super.onSuccess(response);
                                }
                            });
                }

                else{
                    Toast.makeText(RDetails.this, "To get Receipt the order must be approved", Toast.LENGTH_SHORT).show();

                }




            }
        });

    }


}