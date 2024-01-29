package com.example.tenthskate.Shippment.Fragment;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageTask;

import java.util.Objects;

import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Class.apload;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment{
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
    public RequestFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requestsupplier, container, false);

        final FragmentActivity  fragmentActivity= getActivity();



        // mStorageProductImagesRef = FirebaseStorage.getInstance().getReference("products");
        mDatabaseCategoryRef = FirebaseDatabase.getInstance().getReference("SupplierCategory");
        mDatabaseProductsRef = FirebaseDatabase.getInstance().getReference("allRequests");

        Spinner dropdown = view.findViewById(R.id.uploadProductCategory);
        String[] items = new String[]{"Packed Kit", "Knee Pad", "Roller Skate", "Wrist Guard", "Elbow Pad", "Helmet", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, items);
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
        Button uploadButton = (Button) view.findViewById(R.id.uploadButton);
        FloatingActionButton chooseImage = (FloatingActionButton) view.findViewById(R.id.chooseImage);
        uploadProgress = (ProgressBar) view.findViewById(R.id.uploadProgress);
        loadingBar = new ProgressDialog(fragmentActivity);

        productName = (EditText) view.findViewById(R.id.uploadProductName);
        //productCode = (EditText) findViewById(R.id.uploadProductCode);
        productDescription = (EditText) view.findViewById(R.id.uploadProductDescription);
        productPrice = (EditText) view.findViewById(R.id.uploadProductPrice);
        // productQuantity = (EditText) findViewById(R.id.uploadProductQuantity);



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(fragmentActivity, "Upload in progress", Toast.LENGTH_LONG).show();
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

        return view;
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
        mDatabaseProductsRef.child(uploadID).child("status").setValue("Pending");
        mDatabaseCategoryRef.child(category).child(uploadID).child("status").setValue("Pending");
        Toast.makeText(getActivity(),
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