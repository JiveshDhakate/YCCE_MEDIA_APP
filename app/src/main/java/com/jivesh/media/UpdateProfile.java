package com.jivesh.media;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {

    EditText nameEt,bioEt,depEt,collegeidEt;
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUid = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        documentReference = db.collection("user").document(currentUid);
        nameEt = findViewById(R.id.name_up_et);
        bioEt = findViewById(R.id.bio_up_et);
        depEt = findViewById(R.id.dep_up_et);
        collegeidEt = findViewById(R.id.collegeId_up_et);
        button = findViewById(R.id.btn_up);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){

                    String nameResult = task.getResult().getString("name");
                    String bioResult = task.getResult().getString("bio");
                    String depResult = task.getResult().getString("dep");
                    String collegeidResult = task.getResult().getString("collegeId");



                    nameEt.setText(nameResult);
                    bioEt.setText(bioResult);
                    depEt.setText(depResult);
                    collegeidEt.setText(collegeidResult);
                }
                else{
                    Toast.makeText(UpdateProfile.this, "No Such Profile Exists", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void updateProfile() {
        String name = nameEt.getText().toString();
        String bio = bioEt.getText().toString();
        String dep = depEt.getText().toString();
        String collegeid = collegeidEt.getText().toString();

        final DocumentReference sDoc = db.collection("user").document(currentUid);
        
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(sDoc,"name",name);
                transaction.update(sDoc,"dep",dep);
                transaction.update(sDoc,"bio",bio);
                transaction.update(sDoc,"collegeId",collegeid);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateProfile.this, "Updated.....", Toast.LENGTH_SHORT).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                });

    }

}