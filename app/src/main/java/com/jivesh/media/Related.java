package com.jivesh.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Related extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();

        recyclerView = findViewById(R.id.rv_related_activity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference= database.getReference("favoriteList").child(currentid);

        FirebaseRecyclerOptions<NotificationMember> options = new FirebaseRecyclerOptions.Builder<NotificationMember>()
                .setQuery(reference,NotificationMember.class)
                .build();

        FirebaseRecyclerAdapter<NotificationMember,Viewholder_Notification> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<NotificationMember, Viewholder_Notification>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Viewholder_Notification holder, int position, @NonNull NotificationMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentid = user.getUid();

                        final  String postkey = getRef(position).getKey();

                        holder.setitemRelated(getApplication(),model.getName(),model.getUrl(),model.getUserid(),model.getKey(),model.getNotification(),model.getPrivacy(),model.getTime());

//                        String notification = getItem(position).getNotification();
//                        String name = getItem(position).getName();
//                        String url = getItem(position).getUrl();
//                        final  String time = getItem(position).getTime();
//                        String privacy = getItem(position).getPrivacy();
//                        String userid = getItem(position).getUserid();


                    }

                    @NonNull
                    @Override
                    public Viewholder_Notification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.related_item,parent,false);

                        return new Viewholder_Notification(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
}