package com.example.tenthskate.Shippment.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Adapter.ChatAdapter;
import com.example.tenthskate.Shippment.Class.Contacts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference ChatsRef, UsersRef;
    private ChatAdapter mAdapter;
    private List<Contacts> mChats;

    /**private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private ProgressBar progressBar;
    private List<Contacts> mChats;*/

    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageView = inflater.inflate(R.layout.stock_fragment_message, container, false);

        final FragmentActivity fragmentActivity = getActivity();

        /**
        mRecyclerView = view.findViewById(R.id.chats_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));

        mChats = new ArrayList<>();
        final DatabaseReference mChatsRef = FirebaseDatabase.getInstance().getReference("Messages").child("admin");

        mChatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChats.clear();
                for(DataSnapshot messageSender: snapshot.getChildren()){

                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(Objects.requireNonNull(messageSender.getKey()))
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Contacts contacts = new Contacts(Objects.requireNonNull(snapshot.child("name").getValue()).toString()
                                    , Objects.requireNonNull(snapshot.child("Uid").getValue()).toString());
                            mChats.add(contacts);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                mAdapter = new ChatAdapter(fragmentActivity,mChats);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        ChatsRef= FirebaseDatabase.getInstance().getReference().child("Messages").child("admin");






        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(fragmentActivity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView = messageView.findViewById(R.id.chats_recycler_adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ChatAdapter();
        mChats = new ArrayList<>();


        ChatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mChats.clear();
                for(DataSnapshot messageSender: snapshot.getChildren()){
                    UsersRef.child(messageSender.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                                Contacts contacts = new Contacts(snapshot.child("name").getValue().toString()
                                        ,snapshot.child("Uid").getValue().toString());
                            Log.i("NAME",snapshot.child("name").getValue().toString());
                            Log.i("UID",snapshot.child("Uid").getValue().toString());
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
                        mAdapter = new ChatAdapter(getContext(),mChats);
                        assert fragmentActivity != null;
                        fragmentActivity.runOnUiThread(new Runnable() {
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


        return messageView;
    }
/***
    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(ChatsRef, Contacts.class)
                        .build();

        FirebaseRecyclerAdapter<Contacts, ChatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Contacts model) {

                        final String usersIDs=getRef(position).getKey();

                        assert usersIDs != null;
                        UsersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                final String retName= Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                                holder.userName.setText(retName);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent chatIntent=new Intent(getContext(), MessageActivity.class);
                                        chatIntent.putExtra("visit_user_id",usersIDs);
                                        chatIntent.putExtra("visit_user_name",retName);
                                        startActivity(chatIntent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_bar,parent,false);
                        return new ChatsViewHolder(view);
                    }


                    @Override
                    public int getItemCount() {
                        return
                    }


                };

        chatList.setAdapter(adapter);
        adapter.startListening();  // LELPA MAAL ACHE
    }*/
}