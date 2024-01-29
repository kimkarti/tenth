package com.example.tenthskate.Clients.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tenthskate.Clients.Activities.AddAddress;
import com.example.tenthskate.Clients.Activities.MainActivity;
import com.example.tenthskate.Clients.Activities.PolicyActivity;
import com.example.tenthskate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    DatabaseReference user;
    TextView name,mobile,address,privacy,developer,uid;
    Button cancel,edit;

    public MyAccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        user = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        name = view.findViewById(R.id.addressName);
        mobile = view.findViewById(R.id.addressMobile);
        address = view.findViewById(R.id.addressAddress);
        cancel = view.findViewById(R.id.back);
        edit = view.findViewById(R.id.editAddress);
        privacy = view.findViewById(R.id.my_account_privacy_policy);
        developer = view.findViewById(R.id.my_account_developer_details);
        uid = view.findViewById(R.id.show_uid);
        uid.setText("Customer ID : "+Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

///////////////////////////////////// DISPLAYING ADDRESS //////////////////////////////////////////

        user.child("ShippingAddress").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    name.setText(Objects.requireNonNull(snapshot.child("Name").getValue()).toString());
                    mobile.setText(Objects.requireNonNull(snapshot.child("Mobile").getValue()).toString());
                    address.setText(snapshot.child("Flat_Building_No").getValue().toString()+"\n"
                            +snapshot.child("Landmark").getValue().toString()+"\n"
                            +snapshot.child("Locality").getValue().toString()+"\n"
                            +snapshot.child("PinCode").getValue().toString());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ///////////////////////////  EDIT BUTTON   //////////////////////////////////////

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAddress.comingFromMyAccount = 1;
                Intent address = new Intent(getContext(), AddAddress.class);
                startActivity(address);
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        //////////////////////////////// CANCEL BUTTON ///////////////////////////////////

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getContext(), MainActivity.class);
                startActivity(home);
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        //////////////////////////////////////////////////////////////////////////////////

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PolicyActivity.class));
            }
        });

        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"KIMANI DAVE",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}