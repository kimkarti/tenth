package com.example.tenthskate.Clients.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tenthskate.Clients.ModelClass.CartUpload;
import com.example.tenthskate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Objects;

public class ViewProductDetailsActivity extends AppCompatActivity {

    private String productID, userID, retProductImageUrl,retProductPrice,productQuantity,retProductCuttedPrice;
    private ImageView viewProductImage;
    private TextView viewProductName, viewProductDescription, viewProductPrice, viewProductCuttedPrice,viewProductRating;
    private Button addToCartButton;
    private Button goToCartButton;
    float totalRating = 0f;
    String quan,uploadId,productCategory;
    RelativeLayout relativeLayout,relativeLayoutToBeGone;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_details);

        relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayoutToBeGone = findViewById(R.id.relativeLayoutTobeGone);

        /////////////////////////////////  SETTING REFERENCES  /////////////////////////////////////

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("allProducts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userID = FirebaseAuth.getInstance().getUid();
        productID = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("retProductID")).toString();
        quan=getIntent().getExtras().get("quantity").toString();
        uploadId=getIntent().getExtras().get("uploadId").toString();
        productCategory=getIntent().getExtras().get("productCategory").toString();
        ///////////////////////////////////////////////////////////////////////////////////////////

        viewProductImage = (ImageView) findViewById(R.id.view_product_image);
        viewProductName = (TextView) findViewById(R.id.view_product_name);
        viewProductDescription = (TextView) findViewById(R.id.view_product_description);
        viewProductPrice = (TextView) findViewById(R.id.view_product_price);
        viewProductCuttedPrice = (TextView) findViewById(R.id.view_product_cutted_price);
        viewProductRating = findViewById(R.id.view_product_rating);

        addToCartButton = (Button) findViewById(R.id.add_to_cart_button);
        addToCartButton.setEnabled(true);
        goToCartButton = findViewById(R.id.go_to_cart_button);
        goToCartButton.setVisibility(View.GONE);
        goToCartButton.setEnabled(false);



        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String retProductName = Objects.requireNonNull(snapshot.child("productName").getValue()).toString();
                    String retProductDescription = Objects.requireNonNull(snapshot.child("productDescription").getValue()).toString();
                    retProductImageUrl = Objects.requireNonNull(snapshot.child("productImageUrl").getValue()).toString();
                    retProductPrice = Objects.requireNonNull(snapshot.child("productPrice").getValue()).toString();
                    retProductCuttedPrice = Objects.requireNonNull(snapshot.child("productCuttedPrice").getValue()).toString();

                    viewProductName.setText(retProductName);
                    viewProductDescription.setText(retProductDescription);
                    viewProductPrice.setText(retProductPrice);

                    if (Integer.parseInt(retProductCuttedPrice) > Integer.parseInt(retProductPrice)){
                        viewProductCuttedPrice.setVisibility(View.VISIBLE);
                        viewProductCuttedPrice.setText(retProductCuttedPrice);
                        viewProductCuttedPrice.setPaintFlags(viewProductCuttedPrice.getPaintFlags()
                                | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else {
                        viewProductCuttedPrice.setVisibility(View.GONE);
                    }

                    Picasso.get()
                            .load(retProductImageUrl)
                            .placeholder(R.drawable.image_preview)
                            .fit()
                            .centerCrop()
                            .into(viewProductImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        productsRef.child(productID).child("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int children = (int) snapshot.getChildrenCount();

                    for (DataSnapshot rating : snapshot.getChildren()) {

                        Object value = rating.getValue();
                        String rating_str = value.toString();
                        totalRating = Float.parseFloat(rating_str) + totalRating;
                    }
                    float averageRating = totalRating/children;

                    DecimalFormat format = new DecimalFormat("#.##");
                    String s = format.format(averageRating);
                    viewProductRating.setText(s);
                    totalRating=0f;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart();
            }
        });
    }

    private void addProductToCart() {

        DatabaseReference productRef = usersRef.child("cart");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(productID)){
                    addToCartButton.setEnabled(false);
                    goToCartButton.setEnabled(false);
                    goToCartButton.setVisibility(View.GONE);
                    addToCartButton.setText("Added To Cart");
                }
                else {
                    productQuantity="1";

                    CartUpload upload = new CartUpload(
                            viewProductName.getText().toString().trim(),
                            viewProductDescription.getText().toString().trim(),
                            retProductPrice,
                            productID,
                            retProductImageUrl,productQuantity,retProductCuttedPrice,productCategory);

                    //////////////////////////////  UPLOAD CART ITEMS IN DATABASE  /////////////////////////

                    usersRef.child(userID).child("cart").child(productID).setValue(upload);
                    usersRef.child(userID).child("cart").child(productID).child("uploadId").setValue(uploadId);
                    usersRef.child(userID).child("cart").child(productID).child("quantityy").setValue(quan);
                    usersRef.child(userID).child("cart").child(productID).child("productCategory").setValue(productCategory);
                    ///////////////////////////////////// SENDING VALUES TO NEXT ACTIVITY//////////////////////



                    //////////////////////////////////// GO TO CART  //////////////////////////////////////////

                    addToCartButton.setVisibility(View.GONE);
                    addToCartButton.setEnabled(false);
                    goToCartButton.setEnabled(true);
                    goToCartButton.setVisibility(View.VISIBLE);
                    goToCartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            goToCart();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void goToCart(){


        finish();
        MainActivity.openCart=1;
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);


    }
}