package com.jivesh.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AnswerActivity extends AppCompatActivity {


    String uid,notification,postkey;
    EditText editText;
    Button button;
    AnswerMember member;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference AllNotifications;
    String name,url,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        member = new AnswerMember();
        editText= findViewById(R.id.answer_et);
        button = findViewById(R.id.btn_answer);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            uid = bundle.getString("u");
            postkey = bundle.getString("p");
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        AllNotifications = database.getReference("All Notifications").child(postkey).child("Answer");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswer();
            }
        });

    }

    void saveAnswer() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();

        String answer = editText.getText().toString();
        if(answer != null){
            Calendar cdate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-mm-yyyy");

            final  String saveDate = currentDate.format(cdate.getTime());

            Calendar ctime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");

            final String saveTime = currentTime.format(ctime.getTime());

             time = saveDate+ ":" +saveTime;

             member.setTime(time);
             member.setAnswer(answer);
             member.setName(name);
             member.setUid(currentid);
             member.setUrl(url);

             String id = AllNotifications.push().getKey();
             AllNotifications.child(id).setValue(member);

            Toast.makeText(this, "Submitted...", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Please Ask Anything", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        FirebaseFirestore d = FirebaseFirestore.getInstance();
        DocumentReference reference;
        reference= d.collection("user").document(currentid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                     url = task.getResult().getString("url");
                     name = task.getResult().getString("name");
                }
                else {
                    Toast.makeText(AnswerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}