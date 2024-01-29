package com.example.tenthskate.Clients.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Clients.Feedback.feedback;
import com.example.tenthskate.R;
import com.example.tenthskate.stockinventor.Adapter.ChatAdapter;
import com.example.tenthskate.stockinventor.Class.Contacts;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerCare extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference ChatsRef, UsersRef;
    private ChatAdapter mAdapter;
    private List<Contacts> mChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_care);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("TENTH-EMPIRE HELP DESK");

        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        ChatsRef= FirebaseDatabase.getInstance().getReference().child("Messages").child("admin");

        FirebaseApp.initializeApp(this);




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerCare.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView = findViewById(R.id.chats_recycler_adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ChatAdapter();
        mChats = new ArrayList<>();


        ChatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mChats.clear();
                for(DataSnapshot messageSender: snapshot.getChildren()){
                    UsersRef.child(Objects.requireNonNull(messageSender.getKey())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            Contacts contacts = new Contacts(Objects.requireNonNull(snapshot.child("name").getValue()).toString()
                                    , Objects.requireNonNull(snapshot.child("Uid").getValue()).toString());
                            Log.i("NAME", Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                            Log.i("UID", Objects.requireNonNull(snapshot.child("Uid").getValue()).toString());
                            mChats.add(contacts);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ChatAdapter(CustomerCare.this,mChats);

                        CustomerCare.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feddy:
                Intent manageProductIntent = new Intent(CustomerCare.this, feedback.class);
                // manageProductIntent.putExtra("retProductID", uploadCur.getProductID());
                startActivity(manageProductIntent);
                return true;

            case R.id.logou:
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerCare.this);
                builder.setTitle("customer care Logout");
                builder.setMessage("Are you sure you want to logout your from customer care account ?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                //DeleteToken();
                                //     FirebaseAuth.getInstance().signOut();
                                Intent intent_signout = new Intent(CustomerCare.this, LoginActivity.class);
                                startActivity(intent_signout);
                                finish();
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}