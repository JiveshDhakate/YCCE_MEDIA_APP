package com.jivesh.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class YourNotifications extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference AllNotifications,UserNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_notifications);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();

        recyclerView = findViewById(R.id.rv_your_notification_activity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AllNotifications = database.getReference("All Notifications");
        UserNotification= database.getReference("User Notification").child(currentid);

        FirebaseRecyclerOptions<NotificationMember> options = new FirebaseRecyclerOptions.Builder<NotificationMember>()
                .setQuery(UserNotification,NotificationMember.class)
                .build();

        FirebaseRecyclerAdapter<NotificationMember,Viewholder_Notification> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<NotificationMember, Viewholder_Notification>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Viewholder_Notification holder, int position, @NonNull NotificationMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentid = user.getUid();

                        final  String postkey = getRef(position).getKey();

                        holder.setitemYourNotification(getApplication(),model.getName(),model.getUrl(),model.getUserid(),model.getKey(),model.getNotification(),model.getPrivacy(),model.getTime());


                        final String time = getItem(position).getTime();
                        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete(time);
                            }
                        });



                    }

                    @NonNull
                    @Override
                    public Viewholder_Notification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.your_notication_item,parent,false);

                        return new Viewholder_Notification(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
    void delete(String time){
        Query query = UserNotification.orderByChild("time").equalTo(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();
                    Toast.makeText(YourNotifications.this, "Deleted....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query1 = AllNotifications.orderByChild("time").equalTo(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();
                    Toast.makeText(YourNotifications.this, "Deleted....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}