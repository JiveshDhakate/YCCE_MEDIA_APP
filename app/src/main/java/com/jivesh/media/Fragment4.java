package com.jivesh.media;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment4 extends Fragment implements View.OnClickListener {

    Button button;
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference ,likesref;
    Boolean likeschecker= false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment4, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button = getActivity().findViewById(R.id.create_post_f4);
        reference = database.getReference("All posts");
        likesref = database.getReference("post likes");
        recyclerView = getActivity().findViewById(R.id.rv_post);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.create_post_f4:
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<PostMember> options = new FirebaseRecyclerOptions.Builder<PostMember>()
                .setQuery(reference,PostMember.class)
                .build();

        FirebaseRecyclerAdapter<PostMember,PostViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostMember, PostViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewholder holder, int position, @NonNull PostMember model) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String currentuid = user.getUid();

                final String postkey = getRef(position).getKey();
                holder.SetPost(getActivity(),model.getName(),model.getUrl(),model.getPostUri(),model.getTime(),model.getUid(),model.getType(),model.getDesc());

                holder.likesChecker(postkey);
                holder.likebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeschecker = true;

                        likesref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(likeschecker.equals(true)){
                                    if(snapshot.child(postkey).hasChild(currentuid)){
                                        likesref.child(postkey).child(currentuid).removeValue();

                                        Toast.makeText(getActivity(), "Disliked..", Toast.LENGTH_SHORT).show();
                                        likeschecker =false;
                                    }
                                    else {
                                        likesref.child(postkey).child(currentuid).setValue(true);
                                        likeschecker =false;
                                        Toast.makeText(getActivity(), "Liked..", Toast.LENGTH_SHORT).show();
                                    }
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
            public PostViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
                return new PostViewholder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}