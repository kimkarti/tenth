package com.example.tenthskate.stockinventor.Activity;

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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tenthskate.Clients.Activities.LoginActivity;
import com.example.tenthskate.Clients.Activities.Singleton;
import com.example.tenthskate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class manageRequest1 extends AppCompatActivity {
    public FirebaseDatabase firebaseDatabase;
    private ImageView manageProductImage;
    String m;
    private EditText manageProductName, manageProductDescription, manageProductPrice,price1;
    private TextView manageProductCategory, txtProductInfo;
    private ProgressDialog loadingBar;
    private String productID, retProductCategory,retProductDescription,retProductPrice,retProductID,retProductName,iD;
    private String  price;
    public DatabaseReference databaseReference;
    private DatabaseReference productsRef, categoryRef,pref,catref;
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
            Toast.makeText(manageRequest1.this, "valid mpesa code", Toast.LENGTH_SHORT).show();
            System.out.println("Success");
            return true;
        }
        else {
            System.out.println("Invalid");
            return false;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_request1);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        productsRef = FirebaseDatabase.getInstance().getReference().child("allRequests");
        categoryRef = FirebaseDatabase.getInstance().getReference("SupplierCategory");

        pref = FirebaseDatabase.getInstance().getReference().child("allProducts");
        catref = FirebaseDatabase.getInstance().getReference("productsCategory");

        productID = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("retProductID")).toString();
        iD = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("ID")).toString();
        loadingBar=new ProgressDialog(manageRequest1.this);
        price1 =  findViewById(R.id.price);
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
                price = Objects.requireNonNull(snapshot.child("price").getValue()).toString();
                price1.setText(price);
                retProductName = Objects.requireNonNull(snapshot.child("productName").getValue()).toString();
                retProductCategory = Objects.requireNonNull(snapshot.child("productCategory").getValue()).toString();
                //String retProductCode = Objects.requireNonNull(snapshot.child("productCode").getValue()).toString();
                retProductDescription = Objects.requireNonNull(snapshot.child("productDescription").getValue()).toString();
                //   String retProductImageUrl = Objects.requireNonNull(snapshot.child("productImageUrl").getValue()).toString();
                retProductPrice = Objects.requireNonNull(snapshot.child("productPrice").getValue()).toString();
                //    String retProductCuttedPrice = Objects.requireNonNull(snapshot.child("productCuttedPrice").getValue()).toString();
                retProductID = Objects.requireNonNull(snapshot.child("productID").getValue()).toString();

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
        DatabaseReference df;
        df=FirebaseDatabase.getInstance().getReference("allProducts").child(iD);
        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot:snapshot.getChildren()){

                    m=snapshot.child("quantity").getValue(String.class);
                    //Toast.makeText(manageRequest1.this, m, Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInformation();
                updateButton.setBackgroundColor(getResources().getColor(R.color.productPriceGreen));
                updateButton.setEnabled(false);
                updateButton.setText("Delivered");
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
        EditText code=findViewById(R.id.code);
        if (code.getText().toString().isEmpty() || !isValidCode(code.getText().toString())){
            code.setError("mpesa code required");
            return;
        }
        else {
            loadingBar.setTitle("Updating");
            loadingBar.setMessage("Please wait, your information is updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            productsRef.child(productID).child("status").setValue("Delivered");
            Toast.makeText(this, iD, Toast.LENGTH_SHORT).show();

            int mm = Integer.parseInt(m.trim());
            int mk = Integer.parseInt(manageProductPrice.getText().toString().trim());
            int sum = mm + mk;
            pref.child(iD).child("quantity").setValue(String.valueOf(sum));
            catref.child(retProductCategory).child(iD).child("quantity").setValue(String.valueOf(sum));

            productsRef.child(productID).child("mpesa").setValue(code.getText().toString());

            manageProductName.setText("");
            // manageProductCode.setText("");
            manageProductDescription.setText("");
            manageProductPrice.setText("");
            // manageProductCuttedPrice.setText("");
            manageProductCategory.setText("");
            txtProductInfo.setText("");
            loadingBar.dismiss();
            //sendUserToMainActivity();

            admin(retProductCategory,retProductDescription,retProductName,retProductPrice,price,"1");


            Toast.makeText(this," Status changed to delivered", Toast.LENGTH_SHORT).show();
        }
    }

    private void fb(){
        firebaseDatabase = FirebaseDatabase.getInstance();
       //databaseReference = firebaseDatabase.getReference("UserD").child("Drivers").child(userName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {


                    Toast.makeText(manageRequest1.this, "yes", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else{
                    Toast.makeText(manageRequest1.this, "userName", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToMainActivity() {
        Intent mainIntent=new Intent(manageRequest1.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void admin(final String category,final String description, final String name, final String price,final String qprice,
                       final String status) {

            final ProgressDialog progressDialog = new ProgressDialog(manageRequest1.this);
            progressDialog.setTitle("Approving");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST,"http://"+ LoginActivity.ip+"/tenth/empire/supply-orders.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("successfully")) {
                        Toast.makeText(manageRequest1.this, "approved", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(manageRequest1.this, LoginActivity.class));
                        progressDialog.dismiss();
                        // finish();
                    } else {
                        Toast.makeText(manageRequest1.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(manageRequest1.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("category", category);
                    param.put("description",description);
                    param.put("name", name);
                    param.put("price", price);
                    param.put("qprice", qprice);
                    param.put("status", status);

                    return param;

                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Singleton.getmInstance(manageRequest1.this).addToRequestQueue(request);


    }
}