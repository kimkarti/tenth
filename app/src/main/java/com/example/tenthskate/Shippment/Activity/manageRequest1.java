package com.example.tenthskate.Shippment.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tenthskate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class manageRequest1 extends AppCompatActivity {

    private ImageView manageProductImage;
    private EditText manageProductName, manageProductDescription, manageProductPrice;
    private TextView manageProductCategory, txtProductInfo;
    private ProgressDialog loadingBar;
    private String productID, retProductCategory;

    private DatabaseReference productsRef, categoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_request1);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        productsRef = FirebaseDatabase.getInstance().getReference().child("allRequests");
        categoryRef = FirebaseDatabase.getInstance().getReference("SupplierCategory");
        productID = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("retProductID")).toString();

        loadingBar=new ProgressDialog(manageRequest1.this);

        //    manageProductImage = (ImageView) findViewById(R.id.manageProductImage);
        manageProductName = (EditText) findViewById(R.id.manageProductName);
        // manageProductCode = (EditText) findViewById(R.id.manageProductCode);
        manageProductDescription = (EditText) findViewById(R.id.manageProductDescription);
        manageProductPrice = (EditText) findViewById(R.id.manageProductPrice);
        //manageProductCuttedPrice = (EditText) findViewById(R.id.manageProductCuttedPrice);
        manageProductCategory = (TextView) findViewById(R.id.manageProductCategory);
        Button updateButton = (Button) findViewById(R.id.updateButton);
        txtProductInfo = (TextView) findViewById(R.id.txtProductInfo);

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String retProductName = Objects.requireNonNull(snapshot.child("productName").getValue()).toString();
                retProductCategory = Objects.requireNonNull(snapshot.child("productCategory").getValue()).toString();
                //String retProductCode = Objects.requireNonNull(snapshot.child("productCode").getValue()).toString();
                String retProductDescription = Objects.requireNonNull(snapshot.child("productDescription").getValue()).toString();
                //   String retProductImageUrl = Objects.requireNonNull(snapshot.child("productImageUrl").getValue()).toString();
                String retProductPrice = Objects.requireNonNull(snapshot.child("productPrice").getValue()).toString();
                //    String retProductCuttedPrice = Objects.requireNonNull(snapshot.child("productCuttedPrice").getValue()).toString();
                String retProductID = Objects.requireNonNull(snapshot.child("productID").getValue()).toString();

                manageProductName.setText(retProductName);
                //  manageProductCode.setText(retProductCode);
                manageProductDescription.setText(retProductDescription);
                manageProductPrice.setText(retProductPrice);
                //  manageProductCuttedPrice.setText(retProductCuttedPrice);
                manageProductCategory.setText(retProductCategory);
                txtProductInfo.setText(retProductID);
//                Picasso.get()
//                        .load(retProductImageUrl)
//                        .placeholder(R.drawable.image_preview)
//                        .fit()
//                        .centerCrop()
//                        .into(manageProductImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInformation();
            }
        });
    }

    private void updateInformation() {
        if (manageProductName.getText().toString().isEmpty()){
            manageProductName.setError("Product name required");
        }
//        if (manageProductCode.getText().toString().isEmpty()){
//            manageProductCode.setError("Product code required");
//        }
        if (manageProductDescription.getText().toString().isEmpty()){
            manageProductDescription.setError("Product description required");
        }
        if (manageProductPrice.getText().toString().isEmpty()){
            manageProductPrice.setError("Product price required");
        }
//        if (manageProductCuttedPrice.getText().toString().isEmpty()){
//            manageProductCuttedPrice.setError("Product cutted price required");
//        }
        else {
            loadingBar.setTitle("Updating");
            loadingBar.setMessage("Please wait, your information is updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

//            categoryRef.child(retProductCategory).child(productID).child("productName").setValue(manageProductName.getText().toString());
//          //  categoryRef.child(retProductCategory).child(productID).child("productCode").setValue(manageProductCode.getText().toString());
//            categoryRef.child(retProductCategory).child(productID).child("productDescription").setValue(manageProductDescription.getText().toString());
//            categoryRef.child(retProductCategory).child(productID).child("productPrice").setValue(manageProductPrice.getText().toString());
//            //categoryRef.child(retProductCategory).child(productID).child("productCuttedPrice").setValue(manageProductCuttedPrice.getText().toString());
//
//            productsRef.child(productID).child("productName").setValue(manageProductName.getText().toString());
//           // productsRef.child(productID).child("productCode").setValue(manageProductCode.getText().toString());
//            productsRef.child(productID).child("productDescription").setValue(manageProductDescription.getText().toString());
//            productsRef.child(productID).child("productPrice").setValue(manageProductPrice.getText().toString());
            productsRef.child(productID).child("status").setValue("Delivered");

            manageProductName.setText("");
            // manageProductCode.setText("");
            manageProductDescription.setText("");
            manageProductPrice.setText("");
            // manageProductCuttedPrice.setText("");
            manageProductCategory.setText("");
            txtProductInfo.setText("");
            loadingBar.dismiss();
            //sendUserToMainActivity();
            Toast.makeText(this," Status changed to delivered", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendUserToMainActivity() {
        Intent mainIntent=new Intent(manageRequest1.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}