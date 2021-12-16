package com.jivesh.media;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Splashscreen extends AppCompatActivity {
    ImageView logo;
    TextView name1,name2;
    long animeTime =2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        logo = findViewById(R.id.logosplash);
        name1= findViewById(R.id.namesplash);
        name2 = findViewById(R.id.namesplash2);

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(logo,"y",400f) ;
        ObjectAnimator animatorname = ObjectAnimator.ofFloat(name1,"x",350f);
        animatorY.setDuration(animeTime);
        animatorname.setDuration(animeTime);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorY,animatorname);
        animatorSet.start();



    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user != null){
                    Intent intent = new Intent(Splashscreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent2 = new Intent(Splashscreen.this,LoginActivity.class);
                    startActivity(intent2);
                }
            }
        },4000);

    }
}