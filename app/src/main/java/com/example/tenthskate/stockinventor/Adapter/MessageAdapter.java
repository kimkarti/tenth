package com.example.tenthskate.stockinventor.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.R;
import com.example.tenthskate.stockinventor.Class.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> userMessagesList;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public MessageAdapter(List<Messages> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView senderMessageText, receiverMessageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_custom_messages_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        String messageSenderID = "admin";
        Messages messages = userMessagesList.get(position);

        String fromUserID = messages.getFrom();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

        holder.receiverMessageText.setVisibility(View.GONE);
        holder.senderMessageText.setVisibility(View.GONE);

        if (fromUserID.equals(messageSenderID)){
            holder.senderMessageText.setVisibility(View.VISIBLE);
            holder.senderMessageText.setBackgroundResource(R.drawable.sent_message);
            holder.senderMessageText.setTextColor(Color.BLACK);
            holder.senderMessageText.setText(messages.getMessage() + "\n \n" +messages.getTime() + "-" + messages.getDate());
        }
        else {
            holder.receiverMessageText.setVisibility(View.VISIBLE);
            holder.receiverMessageText.setBackgroundResource(R.drawable.received_message);
            holder.receiverMessageText.setText(messages.getMessage() + "\n \n" +messages.getTime() + "-" + messages.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }
}
