package com.example.tenthskate.Clients.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tenthskate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth  mAuth;
    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;
    private CheckBox privacy_checkbox;
    String []users ={"Driver","Customer","Inventory","Finance","Supplier","Customer Care","Shipment Manager"};
    Spinner spinner;
    String selectedUrl="",choice;
    String  url="http://"+LoginActivity.ip+"/tenth/empire/insert.php";





    EditText userName,secondname,emailAddress,password,mobile, address1;
    RadioGroup radioGroup;
    Button register;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toast.makeText(this, LoginActivity.ip, Toast.LENGTH_SHORT).show();

        ///////////////////////  HIDE STATUS BAR  ///////////////////////////////////
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //mAuth=FirebaseAuth.getInstance();

        ///////////////////////////////////////////////////////////////////////////////

        RootRef= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
//
//        final EditText userName=(EditText) findViewById(R.id.register_user_name);
//        final EditText userEmail=(EditText)findViewById(R.id.register_user_email);
//        final EditText userMobile=(EditText)findViewById(R.id.register_user_mobile);
//        final EditText userPassword=(EditText)findViewById(R.id.register_user_password);
//        TextView terms_text = (TextView) findViewById(R.id.terms_text);
//        privacy_checkbox = (CheckBox) findViewById(R.id.privacy_checkbox);
//        Button registerUserButton=(Button)findViewById(R.id.register_user_button);
//        TextView alreadyHaveAnAccount=(TextView)findViewById(R.id.already_have_an_account);
        loadingBar = new ProgressDialog(RegisterActivity.this,R.style.MyAlertDialogStyle);

//        terms_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(RegisterActivity.this,PolicyActivity.class));
//            }
//        });

//        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
//            }
//        });

//        registerUserButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String name=userName.getText().toString();
//                String email=userEmail.getText().toString();
//                String mobile=userMobile.getText().toString();
//                String password=userPassword.getText().toString();
//
//                if (name.isEmpty()){
//                    userName.setError("Name required!");
//                    userName.requestFocus();
//                    return;
//                }
//                if (email.isEmpty()){
//                    userEmail.setError("Email Required!");
//                    userEmail.requestFocus();
//                    return;
//                }

//                if (mobile.isEmpty()){
//                    userMobile.setError("Mobile Number Required!");
//                    userMobile.requestFocus();
//                    return;
//                }
//                if (mobile.length()!=10){
//                    userMobile.setError("Enter valid mobile number!");
//                    userMobile.requestFocus();
//                    return;
//                }
//                if (password.isEmpty()){
//                    userPassword.setError("Create a Password!");
//                    userPassword.requestFocus();
//                    return;
//                }
//                if (password.length()<6){
//                    userPassword.setError("Create password with minimum six digits!");
//                    userPassword.requestFocus();
//                    return;
//                }
//                if (!privacy_checkbox.isChecked()){
//                    privacy_checkbox.setError("Check it first!");
//                    privacy_checkbox.requestFocus();
//                    return;
//                }
//
//                register_user(name,email,mobile,password);
//            }
//        });

        userName = findViewById(R.id.username);
        emailAddress = findViewById(R.id.email);
        secondname=findViewById(R.id.sname);

        password = findViewById(R.id.password);

        spinner=findViewById(R.id.choiceOfUser);
        mobile = findViewById(R.id.mobile);
        address1 = findViewById(R.id.address);
        radioGroup = findViewById(R.id.radioButton);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,users);

        spinner.setAdapter(arrayAdapter);

        register = findViewById(R.id.register);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        mobile.setVisibility(View.VISIBLE);
                        radioGroup.setVisibility(View.VISIBLE);
                        address1.setVisibility(View.VISIBLE);
                        selectedUrl="http://"+LoginActivity.ip+"/tenth/empire/RegisterDriver.php";
                        address1.setHint("Enter Licence");
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                final String address = address1.getText().toString().trim();
                                final String user_name = userName.getText().toString().trim();
                                final String secName = secondname.getText().toString().trim();
                                final String email = emailAddress.getText().toString().trim();
                                final String txt_password = password.getText().toString().trim();
                                final String txt_mobile = mobile.getText().toString().trim();
                                int checkedId = radioGroup.getCheckedRadioButtonId();
                                RadioButton selected_gender = radioGroup.findViewById(checkedId);
                                if (selected_gender == null){
                                    Toast.makeText(RegisterActivity.this, "Select gender please", Toast.LENGTH_SHORT).show();
                                }
                                if (!isValidCode(address)){
                                    address1.setError("Wrong licence");
                                    Toast.makeText(RegisterActivity.this, "Wrong license number", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    final String gender = selected_gender.getText().toString();
                                    if((user_name.length()==0) || (email.length()==0) || (txt_password.length()==0) ||
                                            (txt_mobile.length()==0)){
                                        Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                    }
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                        emailAddress.setError("Enter a valid Email Address!");
                                        emailAddress.requestFocus();
                                        return;
                                    }
                                    if (user_name.length()==0){
                                        userName.setError("Enter name");
                                        userName.requestFocus();
                                        return;
                                    }
                                    if (secName.length()<=0){
                                        secondname.setError("Enter name");
                                        secondname.requestFocus();
                                        return;
                                    }
                                    if (txt_mobile.length()!=10){
                                        mobile.setError("Enter valid mobile number!");
                                        mobile.requestFocus();
                                        return;
                                    }
                                    if (txt_password.length()<=6){
                                        password.setError("Enter more than 6 characters for password");
                                        password.requestFocus();
                                        return;
                                    }
                                    if (address.length()==0){
                                        address1.setError("Enter valid license number!");
                                        address1.requestFocus();
                                        return;
                                    }
                                    else{
                                        registerD(user_name,secName,email,txt_password,txt_mobile,gender,address);
                                    }
                                }


                            }
                        });



                        break;
                    case 1:
                        selectedUrl=url;
                        address1.setVisibility(View.GONE);
                        mobile.setVisibility(View.VISIBLE);
                        radioGroup.setVisibility(View.VISIBLE);
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                final String address = address1.getText().toString();
                                final String user_name = userName.getText().toString();
                                final String secName = secondname.getText().toString();
                                final String email = emailAddress.getText().toString();
                                final String txt_password = password.getText().toString();
                                final String txt_mobile = mobile.getText().toString();
                                int checkedId = radioGroup.getCheckedRadioButtonId();
                                RadioButton selected_gender = radioGroup.findViewById(checkedId);
                                if (selected_gender == null){

                                    //radioGroup.setError("Enter name");
                                    Toast.makeText(RegisterActivity.this, "Select gender please", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {
                                    final String gender = selected_gender.getText().toString();
                                    if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password) ||
                                            TextUtils.isEmpty(txt_mobile)){
                                        Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                    }
                                    if (user_name.length()==0){
                                        userName.setError("Enter name");
                                        userName.requestFocus();
                                        return;
                                    }
                                    if (secName.length()<=0){
                                        secondname.setError("Enter name");
                                        secondname.requestFocus();
                                        return;
                                    }
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                        emailAddress.setError("Enter a valid Email Address!");
                                        emailAddress.requestFocus();
                                        return;
                                    }
                                    if (txt_password.length()<=5){
                                        password.setError("Enter more than 6 characters for password");
                                        password.requestFocus();
                                        return;
                                    }
                                    if (txt_mobile.length()!=10){
                                        mobile.setError("Enter valid mobile number!");
                                        mobile.requestFocus();
                                        return;
                                    }
                                    else{
                                        register(user_name,secName,email,txt_password,txt_mobile,gender,address);
                                    }
                                }


                            }
                        });

                        break;
                    case 2:
                         choice = String.valueOf(spinner.getAdapter().getItem(i));
                        selectedUrl="http://"+LoginActivity.ip+"/tenth/empire/users/data-inventory.php";
                        address1.setVisibility(View.GONE);
                        mobile.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.GONE);

                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                               // final String address = address1.getText().toString();
                                final String user_name = userName.getText().toString();
                                final String secName = secondname.getText().toString();
                                final String email = emailAddress.getText().toString();
                                final String txt_password = password.getText().toString();
                                //final String txt_mobile = mobile.getText().toString();

                                {

                                    if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password)){
                                        Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                    }
                                    if (user_name.length()==0){
                                        userName.setError("Enter name");
                                        userName.requestFocus();
                                        return;
                                    }
                                    if (secName.length()<=0){
                                        secondname.setError("Enter name");
                                        secondname.requestFocus();
                                        return;
                                    }
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                        emailAddress.setError("Enter a valid Email Address!");
                                        emailAddress.requestFocus();
                                        return;
                                    }
                                    if (txt_password.length()<=5){
                                        password.setError("Enter more than 6 characters for password");
                                        password.requestFocus();
                                        return;
                                    }
//
                                    else{
                                        registeruser(selectedUrl,user_name+" "+secName,email,txt_password,choice);
                                    }
                                }


                            }
                        });

                        break;
                    case 3:
                        choice = String.valueOf(spinner.getAdapter().getItem(i));
                        selectedUrl="http://"+LoginActivity.ip+"/tenth/empire/users/data-finance.php";
                        address1.setVisibility(View.GONE);
                        mobile.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.GONE);

                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                // final String address = address1.getText().toString();
                                final String user_name = userName.getText().toString();
                                final String secName = secondname.getText().toString();
                                final String email = emailAddress.getText().toString();
                                final String txt_password = password.getText().toString();
                                //final String txt_mobile = mobile.getText().toString();

                                {

                                    if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password)){
                                        Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                    }
                                    if (user_name.length()==0){
                                        userName.setError("Enter name");
                                        userName.requestFocus();
                                        return;
                                    }
                                    if (secName.length()<=0){
                                        secondname.setError("Enter name");
                                        secondname.requestFocus();
                                        return;
                                    }
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                        emailAddress.setError("Enter a valid Email Address!");
                                        emailAddress.requestFocus();
                                        return;
                                    }
                                    if (txt_password.length()<=5){
                                        password.setError("Enter more than 6 characters for password");
                                        password.requestFocus();
                                        return;
                                    }
//
                                    else{
                                        registeruser(selectedUrl,user_name+" "+secName,email,txt_password,choice);
                                    }
                                }


                            }
                        });

                        break;
                    case 4:
                        choice = String.valueOf(spinner.getAdapter().getItem(i));
                        selectedUrl="http://"+LoginActivity.ip+"/tenth/empire/users/data-supplier.php";
                        address1.setVisibility(View.GONE);
                        mobile.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.GONE);

                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                // final String address = address1.getText().toString();
                                final String user_name = userName.getText().toString();
                                final String secName = secondname.getText().toString();
                                final String email = emailAddress.getText().toString();
                                final String txt_password = password.getText().toString();
                                //final String txt_mobile = mobile.getText().toString();

                                {

                                    if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password)){
                                        Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                    }
                                    if (user_name.length()==0){
                                        userName.setError("Enter name");
                                        userName.requestFocus();
                                        return;
                                    }
                                    if (secName.length()<=0){
                                        secondname.setError("Enter name");
                                        secondname.requestFocus();
                                        return;
                                    }
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                        emailAddress.setError("Enter a valid Email Address!");
                                        emailAddress.requestFocus();
                                        return;
                                    }
                                    if (txt_password.length()<=5){
                                        password.setError("Enter more than 6 characters for password");
                                        password.requestFocus();
                                        return;
                                    }
//
                                    else{
                                        registeruser(selectedUrl,user_name+" "+secName,email,txt_password,choice);
                                    }
                                }


                            }
                        });

                        break;
                    case 5:
                        choice = String.valueOf(spinner.getAdapter().getItem(i));
                        selectedUrl="http://"+LoginActivity.ip+"/tenth/empire/users/data-care.php";
                        address1.setVisibility(View.GONE);
                        mobile.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.GONE);

                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                // final String address = address1.getText().toString();
                                final String user_name = userName.getText().toString();
                                final String secName = secondname.getText().toString();
                                final String email = emailAddress.getText().toString();
                                final String txt_password = password.getText().toString();
                                //final String txt_mobile = mobile.getText().toString();

                                {

                                    if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password)){
                                        Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                    }
                                    if (user_name.length()==0){
                                        userName.setError("Enter name");
                                        userName.requestFocus();
                                        return;
                                    }
                                    if (secName.length()<=0){
                                        secondname.setError("Enter name");
                                        secondname.requestFocus();
                                        return;
                                    }
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                        emailAddress.setError("Enter a valid Email Address!");
                                        emailAddress.requestFocus();
                                        return;
                                    }
                                    if (txt_password.length()<=5){
                                        password.setError("Enter more than 6 characters for password");
                                        password.requestFocus();
                                        return;
                                    }
//
                                    else{
                                        registeruser(selectedUrl,user_name+" "+secName,email,txt_password,choice);
                                    }
                                }


                            }
                        });

                        break;
                    case 6:
                        choice = String.valueOf(spinner.getAdapter().getItem(i));
                        selectedUrl="http://"+LoginActivity.ip+"/tenth/empire/users/data-shipment.php";
                        address1.setVisibility(View.GONE);
                        mobile.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.GONE);

                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                // final String address = address1.getText().toString();
                                final String user_name = userName.getText().toString();
                                final String secName = secondname.getText().toString();
                                final String email = emailAddress.getText().toString();
                                final String txt_password = password.getText().toString();
                                //final String txt_mobile = mobile.getText().toString();

                                {

                                    if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password)){
                                        Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                    }
                                    if (user_name.length()==0){
                                        userName.setError("Enter name");
                                        userName.requestFocus();
                                        return;
                                    }
                                    if (secName.length()<=0){
                                        secondname.setError("Enter name");
                                        secondname.requestFocus();
                                        return;
                                    }
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                        emailAddress.setError("Enter a valid Email Address!");
                                        emailAddress.requestFocus();
                                        return;
                                    }
                                    if (txt_password.length()<=5){
                                        password.setError("Enter more than 6 characters for password");
                                        password.requestFocus();
                                        return;
                                    }
//
                                    else{
                                        registeruser(selectedUrl,user_name+" "+secName,email,txt_password,choice);
                                    }
                                }


                            }
                        });

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }
//firebase driver
private void regdriver(final String name, final String email, final String phone1, String pwd){

    mAuth.createUserWithEmailAndPassword(email,pwd)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())//n Activity
                    {
                        String userName = email.split("@")[0];
                        // sendUserToLoginActivity();
                        FirebaseDatabase database;
                        database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseS,databaseS2;
                        databaseS2 = database.getReference("UserD").child("Drivers").child(userName);
                        databaseS2.child("name").setValue(name);
                        databaseS2.child("phone").setValue(phone1);
                        databaseS2.child("password").setValue(pwd);

                        databaseS2.child("email").setValue(email);
                        View View = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(View, "Successfully created your Driver's Account", 20000)
                                .setAction("OKAY goto LOGIN", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Handle the action button click
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
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

                        snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));

                        snackbar.show();
                        Toast.makeText(RegisterActivity.this,"Account created successfully", Toast.LENGTH_LONG).show();
                     //   loadingBar.dismiss();
                    }
                    else//Print the error message incase of failure
                    {
                        String msg = task.getException().toString();

                        View View = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(View, "Could not create your Driver's Account", 20000)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Handle the action button click
                                        //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        //startActivity(intent);
                                    }
                                });
                        View snackbarView = snackbar.getView();
                        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
                        int iconSize = getResources().getDimensionPixelSize(R.dimen.snackbar_icon_size); // Customize the icon size as per your requirements

// Create an ImageView for the icon
                        ImageView iconImageView = new ImageView(getApplicationContext());
                        iconImageView.setImageResource(R.drawable.error); // Replace "your_icon" with the actual resource ID of your icon
                        iconImageView.setPadding(0, 0, iconSize, 0); // Adjust padding as needed

// Add the ImageView to the Snackbar layout
                        snackbarLayout.addView(iconImageView, 0);

                        snackbar.getView().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                        snackbar.show();


                        Toast.makeText(RegisterActivity.this,"Error: "+msg, Toast.LENGTH_SHORT).show();
                       // loadingBar.dismiss();
                    }
                }
            });
}
//mysql driver
    private void registerD(final String username,final String secName,
                           final String email, final String password, final String mobile,
                           final String gender,final  String address) {
        if (mobile.length() == 10) {
            Toast.makeText(getApplicationContext(), secName, Toast.LENGTH_LONG).show();

            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle("Registering your account");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();


            StringRequest request = new StringRequest(Request.Method.POST,
                    "http://"+LoginActivity.ip+"/tenth/empire/RegisterDriver.php",
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("You are registered successfully")) {
                        regdriver(username+" "+secName,email, mobile, password);
                        Toast.makeText(RegisterActivity.this, "Wait for approval", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        progressDialog.dismiss();
                        // finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("username", username);
                    param.put("secondname",secName);
                    param.put("email", email);
                    param.put("psw", password);
                    param.put("mobile", mobile);
                    param.put("gender", gender);
                    param.put("dLicense", address);

                    return param;

                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Singleton.getmInstance(RegisterActivity.this).addToRequestQueue(request);

        }else{
            Toast.makeText(RegisterActivity.this, "max phone length 10", Toast.LENGTH_SHORT).show();
            return;
        }
    }
//firebase users
    private void register_user(final String name, final String email, final String mobile, String password)
    {
       loadingBar.setTitle("Creating New Account");
        loadingBar.setMessage("Please wait, while we are creating new account for you...");
        loadingBar.setCanceledOnTouchOutside(false);
        Toast.makeText(this, "in firebase", Toast.LENGTH_SHORT).show();
        loadingBar.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    final String currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    RootRef.child("Users").child(currentUser).child("name").setValue(name)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        RootRef.child("Users").child(currentUser).child("email").setValue(email)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            RootRef.child("Users").child(currentUser).child("mobile").setValue(mobile)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                RootRef.child("Users").child(currentUser).child("Uid").setValue(currentUser)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()){


                                                                                                    RootRef.child("Users").child(currentUser).child("password").setValue(password)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()){



                                                                                                                        HashMap<String,String> address = new HashMap<>();

                                                                                                    address.put("Name","");
                                                                                                    address.put("Mobile","");
                                                                                                    address.put("Locality","");
                                                                                                    address.put("PinCode","");
                                                                                                    address.put("Flat_Building_No","");
                                                                                                    address.put("Landmark","");
                                                                                                    RootRef.child("Users").child(currentUser).child("ShippingAddress").setValue(address);
                                                                                                   // RootRef.child("Users").child(currentUser).setValue(password);
                                                                                                    FirebaseDatabase database;
                                                                                                    database = FirebaseDatabase.getInstance();
                                                                                                    DatabaseReference databaseS,databaseS2;
                                                                                                 //   databaseS2 = database.getReference("UserApproval");

                                                                                                  //  databaseS2.child("email").setValue(email);

                                                                                                    Toast.makeText(RegisterActivity.this, "set", Toast.LENGTH_SHORT).show();
                                                                                                    loadingBar.dismiss();
                                                                                                   // startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                                                                                    //finish();
                                                                                                }
                                                                                                else {
                                                                                                    loadingBar.dismiss();
                                                                                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                                                                                    Toast.makeText(RegisterActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                                                else {
                                                                                                    loadingBar.dismiss();
                                                                                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                                                                                    Toast.makeText(RegisterActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                            else {
                                                                               loadingBar.dismiss();
                                                                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                                                                Toast.makeText(RegisterActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                        else {
                                                           loadingBar.dismiss();
                                                            String error = Objects.requireNonNull(task.getException()).getMessage();
                                                            Toast.makeText(RegisterActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                       loadingBar.dismiss();
                                        String error = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(RegisterActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    loadingBar.dismiss();
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(RegisterActivity.this,"Error: "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //mysql users
    private void registeruser(String url1,final String username,final String email,
                              final String password,final String position) {
        if (email.length() > 1) {
           // Toast.makeText(getApplicationContext(), secName, Toast.LENGTH_SHORT).show();

            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle("Registering your account");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();


            StringRequest request = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("You are registered successfully")) {
                        //register_user(username,email, mobile, password);
                        progressDialog.dismiss();
                        View View = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(View, "Successfully created your  Account, Wait Approval to Login", 20000)
                                .setAction("OKAY goto LOGIN", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Handle the action button click
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
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
                        Toast.makeText(RegisterActivity.this, "Waiting for approval", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                        // finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("name", username);
                    param.put("email", email);
                    param.put("password", password);
                    param.put("user", position);
                   param.put("status", "0");
//                    param.put("address", address);
                    return param;

                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Singleton.getmInstance(RegisterActivity.this).addToRequestQueue(request);

        }else{
            Toast.makeText(RegisterActivity.this, "no email", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void register(final String username,final String secName, final String email, final String password, final String mobile, final String gender,final  String address) {
        if (mobile.length() == 10) {
            Toast.makeText(getApplicationContext(), secName, Toast.LENGTH_SHORT).show();

            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle("Registering your account");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.setIndeterminate(false);
           progressDialog.show();


            StringRequest request = new StringRequest(Request.Method.POST,"http://"+LoginActivity.ip+"/tenth/empire/insert.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("You are registered successfully")) {
                       register_user(username,email, mobile, password);
                        Toast.makeText(RegisterActivity.this, "Wait for approval", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        progressDialog.dismiss();
                       // finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("username", username);
                    param.put("secondname",secName);
                    param.put("email", email);
                    param.put("psw", password);
                    param.put("mobile", mobile);
                    param.put("gender", gender);
                    param.put("address", address);
                    return param;

                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Singleton.getmInstance(RegisterActivity.this).addToRequestQueue(request);

        }else{
            Toast.makeText(RegisterActivity.this, "max phone length 10", Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (code.length()>=6) {
            length = true;
        }
        if (letterCounter>=3) {
            character = true;
        }
        if (numCounter>=3) {
            number = true;
        }

        if (character && length && number && !symbol){
            Toast.makeText(RegisterActivity.this, "valid license code", Toast.LENGTH_SHORT).show();
            System.out.println("Success");
            return true;
        }
        else {
            System.out.println("Invalid");
            return false;
        }

    }
}