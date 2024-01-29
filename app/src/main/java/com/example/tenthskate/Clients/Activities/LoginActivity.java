package com.example.tenthskate.Clients.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tenthskate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;



public class LoginActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;Spinner spinner;
    EditText password,email,phoneq;
    Button register,login;
    CheckBox checkedStatus;
    public DatabaseReference databaseReference;
    public FirebaseDatabase firebaseDatabase;
    String uRl="";
    RelativeLayout coordinatorLayout;
    String teaser="";
    TextView ref;
    String choice="";
    ProgressDialog progressDialog;
    private RequestQueue mRequestQueue;
    public static String ip="192.168.0.103";
    String  url="http://"+ip+"tenth/empire/loginCustomer.php";
    public String GetPassword(int length){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();

        Random rand = new Random();

        for(int i = 0; i < length; i++){
            char c = chars[rand.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }
    private void resetpsw(final String email,final String reset,final String choice){
        coordinatorLayout =  findViewById(R.id.coordinatorLayout);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("sending a reset password to your gmail");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST,"http://"+LoginActivity.ip+"/tenth/empire/mailer.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Login ="successfully";
                if(response.trim().equals(Login)) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Email with password sent!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    progressDialog.dismiss();

                }

                else {
                    Toast.makeText(LoginActivity.this,response.trim(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",reset);
                param.put("choice",choice);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }
    private void showRecoverPasswordDialog() {
        final Dialog quantityDialog = new Dialog(this);
        quantityDialog.setContentView(R.layout.custom);
        String [] resett={"Select user","Inventory","Finance","Customer", "Drivers","Supplier","Customer Care","Shipment Manager"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,resett);
        spinner= quantityDialog.findViewById(R.id.choiceOf);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        choice="choose user";
                       // ref.setText("Inventory");

                        break;
                    case 1:
                      //  ref.setText("Finance");
                     //   uRl=url;
                        choice="Inventory";
                        break;
                    case 2:
                     //   ref.setText("Customer");
                        choice="Finance";

                        break;
                    case 3:
                     //   ref.setText("Drivers");
                        //
                    //    uRl=url;
                        choice="Customer";
                        break;
                    case 4:
                       // ref.setText("Supplier");
                        choice="Drivers";
                        break;
                    case 5:
                       // ref.setText("Customer Care");
                        choice="Supplier";
                        break;

                    case 6:
                       // ref.setText("Shipment Manager");
                        choice="Customer Care";
                        break;
                    case 7:
                        // ref.setText("Shipment Manager");
                        choice="Shipment Manager";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        quantityDialog.setCancelable(false);
        final EditText quantityNo =quantityDialog.findViewById(R.id.email1);
        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityDialog.dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productQuantity_str = quantityNo.getText().toString() ;
                if(choice.equals("choose user")) {

                    Toast.makeText(LoginActivity.this, "choose user first", Toast.LENGTH_SHORT).show();
                }
                else if(choice.equals("Finance")){


                    if (TextUtils.isEmpty(productQuantity_str) || TextUtils.isEmpty(productQuantity_str)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        resetpsw(productQuantity_str,GetPassword(8),choice);
                        quantityDialog.dismiss();
                    }
                }
                else if(choice.equals("Inventory")){

                    if (TextUtils.isEmpty(productQuantity_str) || TextUtils.isEmpty(productQuantity_str)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                        //quantityDialog.dismiss();
                    }
                    else{
                        resetpsw(productQuantity_str,GetPassword(8),choice);
                        quantityDialog.dismiss();}
                }
                else if(choice.equals("Shipment Manager")){

                    if (TextUtils.isEmpty(productQuantity_str) || TextUtils.isEmpty(productQuantity_str)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        resetpsw(productQuantity_str,GetPassword(8),choice);
                        quantityDialog.dismiss();}
                }
                else if(choice.equals("Supplier")){

                    if (TextUtils.isEmpty(productQuantity_str) || TextUtils.isEmpty(productQuantity_str)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        resetpsw(productQuantity_str,GetPassword(8),choice);
                        quantityDialog.dismiss();}
                }
                else if(choice.equals("Drivers")){

                    if (TextUtils.isEmpty(productQuantity_str) || TextUtils.isEmpty(productQuantity_str)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        beginRecovery(productQuantity_str);
                        quantityDialog.dismiss();
                    }
                }

                else if(choice.equals("Customer Care")){
               //     String tex_email = email.getText().toString();
                  //  String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(productQuantity_str) || TextUtils.isEmpty(productQuantity_str)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        resetpsw(productQuantity_str,GetPassword(8),choice);
                        quantityDialog.dismiss();}
                }

                else if(choice.equals("Customer")){
                   // String tex_email = email.getText().toString();
                    //String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(productQuantity_str) || TextUtils.isEmpty(productQuantity_str)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        beginRecovery(productQuantity_str);
                        quantityDialog.dismiss();
                    }
                }
            }
        });
        quantityDialog.show();
    }

    private void beginRecovery(String email) {
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginActivity.this,"Done sent",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occurred",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this,"Error Failed",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ///////////////////////  HIDE STATUS BAR  ///////////////////////////////////
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        mAuth=FirebaseAuth.getInstance();
        ref=findViewById(R.id.ref);

        ///////////////////////////////////////////////////////////////////////////////


        loadingBar = new ProgressDialog(LoginActivity.this,R.style.MyAlertDialogStyle);

        email = findViewById(R.id.email);

        spinner=findViewById(R.id.choiceOf);
        password = findViewById(R.id.password);
        checkedStatus = findViewById(R.id.checkbox);


//        final String curator=ipp.getIp();




        String [] userers={"Inventory","Finance","Customer", "Drivers","Supplier","Customer Care","Shipment Manager"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,userers);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        ref.setText("Inventory");

                        break;
                    case 1:
                        ref.setText("Finance");
                        uRl=url;

                        break;
                    case 2:
                        ref.setText("Customer");


                        break;
                    case 3:
                        ref.setText("Drivers");
                        //
                        uRl=url;

                        break;
                    case 4:
                        ref.setText("Supplier");
                        break;
                    case 5:
                        ref.setText("Customer Care");

                        break;

                    case 6:
                        ref.setText("Shipment Manager");

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button reset=findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
                //Intent MainIntent = new Intent(LoginActivity.this, ResetPassword.class);
                //MainIntent.putExtra("name",userName);
                //startActivity(MainIntent);
            }
        });
        login = findViewById(R.id.login1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ref.getText().toString().equals("")) {
                    String tex_email = email.getText().toString();
                    String tex_password = password.getText().toString();
                    Toast.makeText(LoginActivity.this, "Select user", Toast.LENGTH_SHORT).show();
                }
                else if(ref.getText().toString().equals("Inventory")){

                    String tex_email = email.getText().toString();
                    String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    loginInve(tex_email,tex_password);}
                }
                else if(ref.getText().toString().equals("Shipment Manager")){

                    String tex_email = email.getText().toString();
                    String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    loginship(tex_email,tex_password);}
                }
                else if(ref.getText().toString().equals("Finance")){
                    String tex_email = email.getText().toString();
                    String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loginF(tex_email,tex_password);}
                }
                else if(ref.getText().toString().equals("Supplier")){
                    String tex_email = email.getText().toString();
                    String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loginsupplier(tex_email,tex_password);}
                }
                else if(ref.getText().toString().equals("Drivers")){
                    String tex_email = email.getText().toString();
//                    String pho = phoneq.getText().toString();
                    String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loginD(tex_email,tex_password);
                    }
                }

                else if(ref.getText().toString().equals("Customer Care")){
                    String tex_email = email.getText().toString();
                    String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    logincare(tex_email,tex_password);}
                }

                else if(ref.getText().toString().equals("Customer")){
                    String tex_email = email.getText().toString();
                    String tex_password = password.getText().toString();
                    if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                        Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        login(tex_email,tex_password);
                    }
                }


            }
        });

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
               // finish();
            }
        });


    }

    private void loginD(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging into your account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST,"http://"+ip+"/tenth/empire/driverlogin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Login ="Driver Login";
                if(response.trim().equals(Login)) {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())//If account login successful print message and send user to main Activity
                                    {
                                        String userName = email.split("@")[0];
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        databaseReference = firebaseDatabase.getReference("UserD").child("Drivers").child(userName);
                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists())
                                                {

                                                    progressDialog.dismiss();
                                                    View View = findViewById(android.R.id.content);
                                                    Snackbar snackbar = Snackbar.make(View, "Login Driver Successful", 20000)
                                                            .setAction("Goto Homepage", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    // Handle the action button click
                                                                    Intent MainIntent = new Intent(LoginActivity.this, com.example.tenthskate.Driver.Activity.MainActivity.class);
                                                                    MainIntent.putExtra("name",userName);
                                                                    startActivity(MainIntent);
                                                                }
                                                            });

                                                    View snackbarView = snackbar.getView();
                                                    Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
                                                    int iconSize = getResources().getDimensionPixelSize(R.dimen.snackbar_icon_size); // Customize the icon size as per your requirements

// Create an ImageView for the icon
                                                    ImageView iconImageView = new ImageView(getApplicationContext());
                                                    iconImageView.setImageResource(R.drawable.successicon); // Replace "your_icon" with the actual resource ID of your icon
                                                    iconImageView.setPadding(0, 0, iconSize, 0); // Adjust padding as needed

// Add the ImageView to the Snackbar layout
                                                    snackbarLayout.addView(iconImageView, 0);
                                                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

                                                    snackbar.show();
                                                    //sendToMainActivity();
                                                    Toast.makeText(LoginActivity.this, "Welcome driver", Toast.LENGTH_SHORT).show();
                                                    Intent MainIntent = new Intent(LoginActivity.this, com.example.tenthskate.Driver.Activity.MainActivity.class);
                                                    MainIntent.putExtra("name",userName);
                                                    startActivity(MainIntent);
                                                }
                                                else{
                                                    View View = findViewById(android.R.id.content);
                                                    Snackbar snackbar = Snackbar.make(View, "Username Is case Sensitive brian@gamil is not Brian@gmail", 20000)
                                                            .setAction("Retry", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    // Handle the action button click

                                                                }
                                                            });

                                                    View snackbarView = snackbar.getView();
                                                    Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
                                                    int iconSize = getResources().getDimensionPixelSize(R.dimen.snackbar_icon_size); // Customize the icon size as per your requirements

// Create an ImageView for the icon
                                                    ImageView iconImageView = new ImageView(getApplicationContext());
                                                    iconImageView.setImageResource(R.drawable.erroricon); // Replace "your_icon" with the actual resource ID of your icon
                                                    iconImageView.setPadding(2, 0, iconSize, 5); // Adjust padding as needed

// Add the ImageView to the Snackbar layout
                                                    //snackbarLayout.addView(iconImageView, 0);
                                                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                                                    snackbar.show();
                                                    loadingBar.dismiss();
                                                    Toast.makeText(LoginActivity.this, userName +"\terror!  ", Toast.LENGTH_LONG).show();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    } else//Print the error message incase of failure
                                    {
                                        String msg = task.getException().toString();
                                        Toast.makeText(LoginActivity.this, "Error: Could not log you in" + msg, Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }

                else {
                    View View = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(View, "Credentials / Server / Network Error!", 20000)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Handle the action button click
                                    loginD(email,password);
                                }
                            });
                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                    snackbar.show();
                    Toast.makeText(LoginActivity.this,"Check Your Credentials / server or Network"+ response.trim(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Check Your Credentials / server or Network"+ error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",password);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }


    private void login(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging into your account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        loadingBar.show();
        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST,"http://"+ip+"/tenth/empire/loginCustomer.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Login ="Login";
                if(response.trim().equals(Login)) {


                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference("UserApproval").child("email");
                                //DatabaseReference zone1Ref = zonesRef.child("ZONE_1");
                                //DatabaseReference zone1NameRef = zone1Ref.child("ZNAME");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String ans=dataSnapshot.getValue(String.class);
                                        //Toast.makeText(LoginActivity.this, "status: "+dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                                      //  if(ans.equals(email)){
                                            //   Toast.makeText(LoginActivity.this, "inside if"+ ans, Toast.LENGTH_SHORT).show();
                                         progressDialog.dismiss();
                                        loadingBar.dismiss();
                                            Toast.makeText(LoginActivity.this,"home", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        // Replace "YourActivity.this" with your actual activity or fragment reference

                                   // Get the root view of the layout
                                        View rootView = findViewById(android.R.id.content);

                                  // Create a Snackbar instance
                                        Snackbar snackbar = Snackbar.make(rootView, "Success!", Snackbar.LENGTH_SHORT);

                                      // Customize the Snackbar appearance
                                        View snackbarView = snackbar.getView();
                                        snackbarView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

// Display the Snackbar
                                        snackbar.show();


                                        Toast.makeText(LoginActivity.this, "Approved:: Welcome!", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
//                                        }else{
//
//
//                                            Toast.makeText(LoginActivity.this, "You haven't been Approved, be patient please", Toast.LENGTH_LONG).show();
//                                            Toast.makeText(LoginActivity.this, "Try signing in later!", Toast.LENGTH_SHORT).show();
//                                            progressDialog.dismiss();
//                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                      //  Log.w(TAG, "onCancelled", databaseError.toException());
                                    }
                                });

                              //  finish();
                            }else {
                                progressDialog.dismiss();
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(LoginActivity.this,"Error: "+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,response.trim()+"Could not log you in", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something went Wrong "+ error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",password);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }
    private void login_user(String loginEmail, String loginPassword) {


    }

    @Override
    public void onBackPressed() {

    }
    private void logincare(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging into your service care account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST,"http://"+ip+"/tenth/empire/users/carelogin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Login ="Login";
                if(response.trim().equals(Login)) {

                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, CustomerCare.class));
                    Toast.makeText(LoginActivity.this, "Welcome customer care", Toast.LENGTH_SHORT).show();


                }

                else {
                    Toast.makeText(LoginActivity.this,"Check Your Credentials / server or Network"+response.trim(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Check Your Credentials / server or Network"+ error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",password);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }
    private void loginF(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging into your finance account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST,"http://"+ip+"/tenth/empire/users/Flogin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Login ="Login";
                if(response.trim().equals(Login)) {


                    startActivity(new Intent(LoginActivity.this, com.example.tenthskate.Finance.Activity.MainActivity.class));
                    View View = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(View, "Finance Login Successful", 20000)
                            .setAction("Okay", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Handle the action button click
                                    //loginF(email,password);
                                }
                            });
                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));

                    snackbar.show();
                    Toast.makeText(LoginActivity.this, "Welcome finance", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

                else {
                    View View = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(View, "Check credentials Network or Server", 20000)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Handle the action button click
                                    //loginF(email,password);
                                }
                            });
                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                    snackbar.show();


                    Toast.makeText(LoginActivity.this,"Wrong Credentials "+ response.trim(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                View View = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(View, "Server error", 20000)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Handle the action button click
                                loginF(email,password);
                            }
                        });
                snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                snackbar.show();

                Toast.makeText(LoginActivity.this, " server or Network issues"+ error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",password);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }
    private void loginInve(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging into your inventory manager account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST,"http://"+ip+"/tenth/empire/users/Invlogin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Login ="Login";
                if(response.trim().equals(Login)) {


                    startActivity(new Intent(LoginActivity.this, com.example.tenthskate.stockinventor.Activity.MainActivity.class));

                    View View = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(View, "Inventory Login successful", 20000)
                            .setAction("OKAY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Handle the action button click
                                    loginInve(email,password);
                                }
                            });
                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));

                    snackbar.show();
                    Toast.makeText(LoginActivity.this, "Welcome inventory", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

                else {
                    View View = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(View, "Credentials are Incorrect", 20000)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Handle the action button click
                                    //loginInve(email,password);
                                }
                            });
                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                    snackbar.show();
                    Toast.makeText(LoginActivity.this," server or Network"+ response.trim(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString()+"kk", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("psw",password);
                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }
    private void loginsupplier(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging into your supplier account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST,"http://"+ip+"/tenth/empire/users/Slogin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Login ="Login";
                if(response.trim().equals(Login)) {


                    startActivity(new Intent(LoginActivity.this, com.example.tenthskate.Supplier.Activity.MainActivity.class));
                    Toast.makeText(LoginActivity.this, "Welcome supplier", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

                else {
                    Toast.makeText(LoginActivity.this,"Check Your Credentials / server or Network"+ response.trim(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Check Your Credentials / server or Network"+ error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",password);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }
    private void loginship(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging into your shipment manager account");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST,"http://"+ip+"/tenth/empire/users/Shiplogin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Login ="Login";
                if(response.trim().equals(Login)) {


                    startActivity(new Intent(LoginActivity.this, com.example.tenthskate.Shippment.Activity.MainActivity.class));
                    View View = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(View, "Shipment Login Was successful", 20000)
                            .setAction("OKAY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Handle the action button click
                                    //LoginUser(username,password);
                                }
                            });
                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));

                    snackbar.show();

                    Toast.makeText(LoginActivity.this, "Welcome shipment manager", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

                else {
                    View View = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(View, "Check Credentials", 20000)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Handle the action button click
                                    //LoginUser(username,password);
                                }
                            });
                    snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                    snackbar.show();
                    Toast.makeText(LoginActivity.this," server or Network"+ response.trim(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Check Your Credentials / server or Network"+ error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",password);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance(LoginActivity.this).addToRequestQueue(request);

    }
}