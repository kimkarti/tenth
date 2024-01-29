package com.example.tenthskate.Shippment.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Class.Upload;


public class AddProductActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    private String category;
    private ProgressDialog loadingBar;

    private StorageReference mStorageProductImagesRef;
    private DatabaseReference mDatabaseProductsRef, mDatabaseCategoryRef;
    private Uri imgUrl;

    private ImageView imgPreview;
    private EditText productName, productCode, productDescription, productPrice, productQuantity;
    private ProgressBar uploadProgress;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_activity_add_product);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mStorageProductImagesRef = FirebaseStorage.getInstance().getReference("products");
        mDatabaseCategoryRef = FirebaseDatabase.getInstance().getReference("productsCategory");
        mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("allProducts");

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

        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        Button uploadButton = (Button) findViewById(R.id.uploadButton);
        FloatingActionButton chooseImage = (FloatingActionButton) findViewById(R.id.chooseImage);
        uploadProgress = (ProgressBar) findViewById(R.id.uploadProgress);
        loadingBar = new ProgressDialog(AddProductActivity.this);

        productName = (EditText) findViewById(R.id.uploadProductName);
        productCode = (EditText) findViewById(R.id.uploadProductCode);
        productDescription = (EditText) findViewById(R.id.uploadProductDescription);
        productPrice = (EditText) findViewById(R.id.uploadProductPrice);
        productQuantity = (EditText) findViewById(R.id.uploadProductQuantity);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoose();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AddProductActivity.this, "Upload in progress", Toast.LENGTH_LONG).show();
                }
                else if (productName.getText().toString().isEmpty()){
                    productName.setError("Product name required");
                }
                else if (productCode.getText().toString().isEmpty()){
                    productCode.setError("Product code required");
                }
                else if (productDescription.getText().toString().isEmpty()){
                    productDescription.setError("Product description required");
                }
                else if (productPrice.getText().toString().isEmpty()){
                    productPrice.setError("Product price required");
                }
                else if (productQuantity.getText().toString().isEmpty()){
                    productQuantity.setError("Product quantity required");
                }

                else
                uploadProduct();
            }

        });
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUrl = data.getData();
            Picasso.get().load(imgUrl).into(imgPreview);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadProduct() {

        loadingBar.setTitle("Adding Product");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if (imgUrl != null){
            final StorageReference fileReference = mStorageProductImagesRef
                    .child(System.currentTimeMillis() + "." + getFileExtension(imgUrl));
            mUploadTask = fileReference.putFile(imgUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadProgress.setProgress(0);
                                }
                            }, 500);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uploadID = mDatabaseProductsRef.push().getKey();
                                    assert uploadID != null;

                                    String searchableString = productName.getText().toString().trim()
                                            .replaceAll(" ","");

                                    Upload upload = new Upload(
                                            productName.getText().toString().trim(),
                                            productCode.getText().toString().trim(),
                                            productDescription.getText().toString().trim(),
                                            uri.toString(),
                                            category.trim(),
                                            productPrice.getText().toString().trim(),
                                            productQuantity.getText().toString().trim(),
                                            uploadID,searchableString.toLowerCase());

                                    mDatabaseCategoryRef.child(category).child(uploadID).setValue(upload);
                                    mDatabaseProductsRef.child(uploadID).setValue(upload);

                                    Toast.makeText(AddProductActivity.this,
                                            "Uploaded successfully", Toast.LENGTH_LONG).show();
                                    imgPreview.setImageResource(R.drawable.image_preview);
                                    productName.setText("");
                                    productCode.setText("");
                                    productDescription.setText("");
                                    productPrice.setText("");
                                    productQuantity.setText("");
                                    loadingBar.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingBar.dismiss();
                            Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgress.setProgress((int) progress);
                        }
                    });
        }
        else {
            loadingBar.dismiss();
            Toast.makeText(AddProductActivity.this, "No image selected for this product", Toast.LENGTH_SHORT).show();
        }
    }
}