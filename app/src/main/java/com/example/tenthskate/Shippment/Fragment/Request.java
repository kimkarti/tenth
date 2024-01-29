package com.example.tenthskate.Shippment.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageTask;

import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Class.apload;


public class Request extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    private String category;
    private ProgressDialog loadingBar;

  //  private StorageReference mStorageProductImagesRef;
    private DatabaseReference mDatabaseProductsRef, mDatabaseCategoryRef;
    private Uri imgUrl;

    private ImageView imgPreview;
    private EditText productName, productCode, productDescription, productPrice, productQuantity;
    private ProgressBar uploadProgress;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestsupplier);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

       // mStorageProductImagesRef = FirebaseStorage.getInstance().getReference("products");
        mDatabaseCategoryRef = FirebaseDatabase.getInstance().getReference("SupplierCategory");
        mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("allRequests");

        Spinner dropdown = findViewById(R.id.uploadProductCategory);
        String[] items = new String[]{"Packed Kit", "Knee Pad", "Roller Skate", "Wrist Guard", "Elbow Pad", "Helmet", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
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

      //  imgPreview = (ImageView) findViewById(R.id.imgPreview);
        Button uploadButton = (Button) findViewById(R.id.uploadButton);
        FloatingActionButton chooseImage = (FloatingActionButton) findViewById(R.id.chooseImage);
        uploadProgress = (ProgressBar) findViewById(R.id.uploadProgress);
        loadingBar = new ProgressDialog(Request.this);

        productName = (EditText) findViewById(R.id.uploadProductName);
        //productCode = (EditText) findViewById(R.id.uploadProductCode);
        productDescription = (EditText) findViewById(R.id.uploadProductDescription);
        productPrice = (EditText) findViewById(R.id.uploadProductPrice);
       // productQuantity = (EditText) findViewById(R.id.uploadProductQuantity);



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Request.this, "Upload in progress", Toast.LENGTH_LONG).show();
                }
                else if (productName.getText().toString().isEmpty()){
                    productName.setError("Product name required");
                }

                else if (productDescription.getText().toString().isEmpty()){
                    productDescription.setError("Product description required");
                }
                else if (productPrice.getText().toString().isEmpty()){
                    productPrice.setError("Product price required");
                }


                else
                    uploadProduct();
            }

        });
    }

    private void uploadProduct() {

        loadingBar.setTitle("Adding Request");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

                                    String uploadID = mDatabaseProductsRef.push().getKey();
                                    assert uploadID != null;

                                    String searchableString = productName.getText().toString().trim()
                                            .replaceAll(" ","");

                                    apload upload = new apload(
                                            productName.getText().toString().trim(),
                                           // productCode.getText().toString().trim(),
                                            productDescription.getText().toString().trim(),

                                            category.trim(),
                                            productPrice.getText().toString().trim(),
                                           // productQuantity.getText().toString().trim(),
                                            uploadID,searchableString.toLowerCase());

                                    mDatabaseCategoryRef.child(category).child(uploadID).setValue(upload);
                                    mDatabaseProductsRef.child(uploadID).setValue(upload);

        mDatabaseProductsRef.child(uploadID).child("status").setValue("Approved");
        mDatabaseCategoryRef.child(category).child(uploadID).child("status").setValue("Approved");

                                    Toast.makeText(Request.this,
                                            "Requested successfully", Toast.LENGTH_LONG).show();
                                   // imgPreview.setImageResource(R.drawable.image_preview);
                                    productName.setText("");
                                  //  productCode.setText("");
                                    productDescription.setText("");
                                    productPrice.setText("");
                                 //   productQuantity.setText("");
                                    loadingBar.dismiss();
                                }

    }

