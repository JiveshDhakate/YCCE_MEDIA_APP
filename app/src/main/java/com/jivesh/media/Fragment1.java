package com.jivesh.media;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Fragment1 extends Fragment implements  View.OnClickListener {

    ImageView imageView;
    TextView nameEt,depEt,bioET,collegeidEt;
    ImageButton ib_edit,ib_menu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       imageView = getActivity().findViewById(R.id.iv_f1);
       nameEt = getActivity().findViewById(R.id.name_tv_f1);
       depEt = getActivity().findViewById(R.id.dep_tv_f1);
       bioET = getActivity().findViewById(R.id.bio_tv_f1);
       collegeidEt = getActivity().findViewById(R.id.collegeId_tv_f1);
       ib_edit= getActivity().findViewById(R.id.ib_edit_f1);
       ib_menu= getActivity().findViewById(R.id.ib_menu_f1);


        ib_menu.setOnClickListener(this);
       ib_edit.setOnClickListener(this);

       imageView.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_edit_f1:
                Intent intent = new Intent(getActivity(),UpdateProfile.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_f1:
                BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                bottomSheetMenu .show(getFragmentManager(),"bottomsheet");
                break;
            case R.id.iv_f1:
                Intent intent1 = new Intent(getActivity(),ImageActivity.class);
                startActivity(intent1);
                break;


        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference=firestore.collection("user").document(currentId);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){

                    String nameResult = task.getResult().getString("name");
                    String bioResult = task.getResult().getString("bio");
                    String depResult = task.getResult().getString("dep");
                    String collegeidResult = task.getResult().getString("collegeId");
                    String url = task.getResult().getString("url");

                    Picasso.get().load(url).into(imageView);
                    nameEt.setText(nameResult);
                    bioET.setText(bioResult);
                    depEt.setText(depResult);
                    collegeidEt.setText(collegeidResult);

                }
                else{
                    Intent intent = new Intent(getActivity(),CreateProfile.class);
                    startActivity(intent);

                }

            }
        });
    }
}
