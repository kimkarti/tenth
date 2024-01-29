package com.example.tenthskate.stockinventor.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.tenthskate.R;
import com.example.tenthskate.stockinventor.Adapter.MessageAdapter;
import com.example.tenthskate.stockinventor.Class.Messages;


public class MessageActivity extends AppCompatActivity {

    private String messageReceiverID;
    private String messageSenderID;
    private DatabaseReference RootRef;
    private EditText MessageInputText;
    private List<Messages> messagesList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;
    private String saveCurrentTime, saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_activity_message);

        messageSenderID= "admin";
        RootRef= FirebaseDatabase.getInstance().getReference();
        messageReceiverID= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("visit_user_id")).toString();


        ImageButton sendMessageButton = findViewById(R.id.send_message_btn);
        MessageInputText= findViewById(R.id.input_message);
        messageAdapter = new MessageAdapter(messagesList);
        userMessagesList = findViewById(R.id.private_messages_list_of_users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
        messagesList.clear();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });
        RootRef.child("Messages").child(messageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messagesList.add(messages);
                        messageAdapter.notifyDataSetChanged();
                        userMessagesList.smoothScrollToPosition(Objects.requireNonNull(userMessagesList.getAdapter()).getItemCount());

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        messageAdapter.notifyDataSetChanged();
        userMessagesList.smoothScrollToPosition(Objects.requireNonNull(userMessagesList.getAdapter()).getItemCount());
    }

    @Override
    public void onStart() {
        super.onStart();
        userMessagesList.smoothScrollToPosition(Objects.requireNonNull(userMessagesList.getAdapter()).getItemCount());
        RootRef.child("Messages").child(messageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                                           @Override
                                           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                               userMessagesList.smoothScrollToPosition(Objects.requireNonNull(userMessagesList.getAdapter()).getItemCount());

                                           }

                                           @Override
                                           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                           }

                                           @Override
                                           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                           }

                                           @Override
                                           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });

    }

    private void SendMessage(){
        String messageText=MessageInputText.getText().toString();
        if (TextUtils.isEmpty(messageText)){
            Toast.makeText(MessageActivity.this,"First write your message...", Toast.LENGTH_SHORT).show();
        }
        else {
            String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                    .child(messageSenderID).child(messageReceiverID).push();
            String messagePushID=userMessageKeyRef.getKey();

            Map<String, String> messageTextBody = new HashMap<String, String>();
            messageTextBody.put("message",messageText);
            messageTextBody.put("from",messageSenderID);
            messageTextBody.put("to",messageReceiverID);
            messageTextBody.put("messageID",messagePushID);
            messageTextBody.put("time",saveCurrentTime);
            messageTextBody.put("date",saveCurrentDate);

            Map<String, Object> messageBodyDetails = new HashMap<>();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);
            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MessageActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    MessageInputText.setText("");
                }
            });
        }
    }
}