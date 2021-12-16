package com.jivesh.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ReplyActivity extends AppCompatActivity {

    String uid,notifications,post_key,key;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference;
    DocumentReference reference2;

    TextView nametv,notificationtv,tvreply;
    RecyclerView recyclerView;
    ImageView imageViewNot,imageViewUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    DatabaseReference voteRef,AllNotification;
    Boolean votechecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);



        nametv =findViewById(R.id.name_reply_tv);
        notificationtv = findViewById(R.id.notification_reply_tv);
        imageViewNot = findViewById(R.id.iv_reply_not);
        imageViewUser = findViewById(R.id.iv_reply_user);
        tvreply = findViewById(R.id.reply_answer_tv);

        recyclerView = findViewById(R.id.rv_reply);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReplyActivity.this));




        Bundle extra = getIntent().getExtras();
        if(extra != null){
            uid = extra.getString("uid");
            post_key = extra.getString("postkey");
            notifications = extra.getString("n");
            //key = extra.getString("key");
        }
        else {
            Toast.makeText(this, "Opps", Toast.LENGTH_SHORT).show();
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();

        AllNotification = database.getReference("All Notifications").child(post_key).child("Answer");
        voteRef= database.getReference("votes");


        reference = db.collection("user").document(uid);
        reference2 = db.collection("user").document(currentid);

        tvreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReplyActivity.this,AnswerActivity.class);
                intent.putExtra("u",uid);
                //intent.putExtra("n",notifications);
                intent.putExtra("p", post_key);
                //intent.putExtra("key",privacy);
                startActivity(intent);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Notification User Reference
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String url = task.getResult().getString("url");
                    String name = task.getResult().getString("name");

                    Picasso.get().load(url).into(imageViewNot);
                    notificationtv.setText(notifications);

                    nametv.setText(name);

                }
                else {
                    Toast.makeText(ReplyActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }
        });
        //Notification Replying User Reference
        reference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String url = task.getResult().getString("url");

                    Picasso.get().load(url).into(imageViewUser);

                }
                else {
                    Toast.makeText(ReplyActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }
        });

        FirebaseRecyclerOptions<AnswerMember> options = new FirebaseRecyclerOptions.Builder<AnswerMember>()
                .setQuery(AllNotification,AnswerMember.class)
                .build();

        FirebaseRecyclerAdapter<AnswerMember,AnswerViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AnswerMember, AnswerViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AnswerViewholder holder, int position, @NonNull AnswerMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentid = user.getUid();
                        final  String postkey = getRef(position).getKey();

                        holder.setAnswer(getApplication(),model.getName(),model.getAnswer(),model.getUid(),model.getTime(),model.getUrl());

                        holder.upvoteChecker(postkey);
                        holder.upvoteTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                votechecker=true;
                                voteRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(votechecker.equals(true)){
                                            if(snapshot.child(postkey).hasChild(currentid));
                                            voteRef.child(postkey).child(currentid).removeValue();

                                            votechecker = false;
                                        }
                                        else {
                                            voteRef.child(postkey).child(currentid).setValue(true);

                                            votechecker = false;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });




                    }

                    @NonNull
                    @Override
                    public AnswerViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.ans_layout,parent,false);

                        return new AnswerViewholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}