package com.example.tenthskate.Shippment.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Activity.MessageActivity;
import com.example.tenthskate.Shippment.Class.Contacts;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>  {
    private Context mContext;
    private List<Contacts> mContacts;
    DatabaseReference message = FirebaseDatabase.getInstance().getReference().child("Messages");

    public ChatAdapter(Context context, List<Contacts> contacts) {
        mContext = context;
        mContacts = contacts;
    }
    public ChatAdapter(){

    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.stock_custom_chat_bar,parent,false);
       return new ChatViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, final int position) {
        final Contacts chats = mContacts.get(holder.getAdapterPosition());
        holder.uID.setText(chats.getUid());
        holder.name.setText(chats.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent=new Intent(mContext, MessageActivity.class);
                chatIntent.putExtra("visit_user_id",chats.getUid());
                chatIntent.putExtra("visit_user_name",chats.getName());
                mContext.startActivity(chatIntent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int which_item = position;
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_Dialog_Alert);

                        dialog.setTitle("Are you sure?")
                        .setMessage("Do you want to delete this session?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mContacts.remove(position);
                                message.child(chats.getUid()).removeValue();
                                message.child("admin").child(chats.getUid()).removeValue();
                            }
                        })
                        .setNegativeButton("NO",null)
                        .show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {


        return mContacts.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }



    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView name, uID;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_profile_name);
            uID = itemView.findViewById(R.id.custom_profile_uid);
        }
    }
}

/**

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context mContext;
    private List<Contacts> mOChats;

    public ChatAdapter (Context context, List<Contacts> offerZoneModel){
        mContext = context;
        mOChats = offerZoneModel;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_chat_bar,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final Contacts contacts = mOChats.get(position);
        holder.name.setText(contacts.getName());
        holder.uid.setText(contacts.getUid());
    }

    @Override
    public int getItemCount() {
        return mOChats.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView name, uid;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_profile_name);
            uid = itemView.findViewById(R.id.custom_profile_uid);
        }
    }
}
 */
