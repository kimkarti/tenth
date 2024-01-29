package com.example.tenthskate.Clients.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tenthskate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class AddAddress extends AppCompatActivity {

    public String value,name4,phoneq;
    private String category;
    public static int comingFromMyAccount = 0;
    EditText namee, phonee;
    private EditText fullName, mobile, locality,flat,landMark;
    DatabaseReference user;
    String shippingAddress,fullName_str,mobile_str,locality_str,pin_str,flat_str,landMark_str,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        namee =  findViewById(R.id.shipping_full_name);
        phonee =  findViewById(R.id.shipping_mobile);
        DatabaseReference df;
        df=FirebaseDatabase.getInstance().getReference("Users").child(uid);

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot:snapshot.getChildren()){

                    name4=snapshot.child("name").getValue(String.class);
                    phoneq=snapshot.child("mobile").getValue(String.class);
                    email=snapshot.child("email").getValue(String.class);

                    namee.setText(name4);
                    phonee.setText(phoneq);




                }
              //  Toast.makeText(getActivity(), Name4+" noo", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        ///////////////////////  HIDE STATUS BAR  ///////////////////////////////////

        final Spinner dropdown = findViewById(R.id.uploadProductCategory);
        dropdown.setVisibility(View.INVISIBLE);
        String[] items = new String[]{"10100","60200"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///////////////////////////////////////////////////////////////////////////////

        Intent intent = getIntent();
        value = intent.getStringExtra("value");

        user = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        Button save = findViewById(R.id.save_btn);

        namee.setText(name4);
        phonee.setText(phoneq);
        locality = (EditText) findViewById(R.id.shipping_locality);
        flat = (EditText) findViewById(R.id.shipping_building);landMark = (EditText) findViewById(R.id.shipping_landmark);

////////////////////////////////DISPLAYING PREVIOUSLY STORED ADDRESS///////////////////////////////

           user.child("ShippingAddress").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        namee.setText(Objects.requireNonNull(snapshot.child("Name").getValue()).toString());
                        phonee.setText(Objects.requireNonNull(snapshot.child("Mobile").getValue()).toString());
                        locality.setText(Objects.requireNonNull(snapshot.child("Locality").getValue()).toString());
                        dropdown.setSelection(adapter.getPosition(category));
                        flat.setText(Objects.requireNonNull(snapshot.child("Flat_Building_No").getValue()).toString());
                        landMark.setText(Objects.requireNonNull(snapshot.child("Landmark").getValue()).toString());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

           save.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  // fullName_str = fullName.getText().toString();
               //    mobile_str = mobile.getText().toString();
                   locality_str = locality.getText().toString();
                   pin_str = category;
                   flat_str = flat.getText().toString();
                   landMark_str = landMark.getText().toString();





                   if (locality_str.isEmpty()){
                       locality.setError("Locality required!");
                       locality.requestFocus();
                       return;
                   }

                   if (flat_str.isEmpty()){
                       flat.setError("Flat required!");
                       flat.requestFocus();
                       return;
                   }

                   else {
                       putDataAndContinue();
                   }
               }
           });

    }

    private void putDataAndContinue() {

        shippingAddress = flat_str + "\n" + locality_str+ "\n" + landMark_str + "\n" + pin_str ;

        HashMap<String, String> address = new HashMap<>();

        address.put("Name", name4);
        address.put("Mobile", phoneq);
        address.put("Locality", locality_str);
        address.put("PinCode", pin_str);
        address.put("Flat_Building_No", flat_str);
        address.put("Landmark", landMark_str);

        user.child("ShippingAddress").setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(comingFromMyAccount == 0) {
                    placeOrder();
                }
                else if (comingFromMyAccount == 1){
                    comingFromMyAccount = 0;
                    Intent mainActivity = new Intent(AddAddress.this,MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(comingFromMyAccount == 0) {
            super.onBackPressed();
        }
        else if (comingFromMyAccount == 1){

            Intent mainActivity = new Intent(AddAddress.this,MainActivity.class);
            startActivity(mainActivity);
            finish();
            comingFromMyAccount = 0;
        }
    }

    public void placeOrder(){
        Intent placeOrder = new Intent(AddAddress.this,PlaceOrderActivity.class);
        placeOrder.putExtra("value",value);
        placeOrder.putExtra("ShippingAddress",shippingAddress);
        placeOrder.putExtra("FullName",name4);
        placeOrder.putExtra("MobileNo",phoneq);
        placeOrder.putExtra("email",email);
        startActivity(placeOrder);
        finish();

    }
}