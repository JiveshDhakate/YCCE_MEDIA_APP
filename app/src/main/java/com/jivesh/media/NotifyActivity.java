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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotifyActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference AllNotifications,UserNotification;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    NotificationMember member;
    String name,url,privacy,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();

        editText = findViewById(R.id.send_et_notification);
        button = findViewById(R.id.btn_send_an);
        documentReference = db.collection("user").document(currentid);

        AllNotifications = database.getReference("All Notifications");
        UserNotification = database.getReference("User Notification").child(currentid);

        member = new NotificationMember();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notification = editText.getText().toString();

                Calendar cdate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");

                final  String saveDate = currentDate.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");

                final String saveTime = currentTime.format(ctime.getTime());

                String time = saveDate+ "  " +saveTime;

                if(notification != null){
                    member.setNotification(notification);
                    member.setName(name);
                    member.setPrivacy(privacy);
                    member.setUrl(url);
                    member.setUserid(uid);
                    member.setTime(time);
                    String id = UserNotification.push().getKey();
                    UserNotification.child(id).setValue(member);
                    
                    String child = AllNotifications.push().getKey();
                    member.setKey(id);
                    AllNotifications.child(child).setValue(member);

                    Toast.makeText(NotifyActivity.this, "Notified....", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(NotifyActivity.this, "Please Send Notification", Toast.LENGTH_SHORT).show();
                }

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

                             name = task.getResult().getString("name");
                             url = task.getResult().getString("url");
                             privacy = task.getResult().getString("privacy");
                             uid = task.getResult().getString("uid");

                        }
                        else{
                            Toast.makeText(NotifyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

}