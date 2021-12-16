package com.jivesh.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.profile_bottom:
                        Fragment1 fragment1 = new Fragment1();
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.frame_layout, fragment1, "");
                        ft1.commit();
                        return true;
                    case R.id.ask_bottom:

                        Fragment2 fragment2 = new Fragment2();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.frame_layout, fragment2, "");
                        ft2.commit();
                        return true;
                    case R.id.queue_bottom:
                        Fragment3 fragment3 = new Fragment3();
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.frame_layout, fragment3, "");
                        ft3.commit();
                        return true;

                    case R.id.home_bottom:
                        Fragment4 fragment4 = new Fragment4();
                        FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                        ft4.replace(R.id.frame_layout, fragment4, "");
                        ft4.commit();
                        return true;

                }


                return false;
            }
        });


    }


    public void logout(View view) {
        auth.signOut();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null){
//            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }

}
