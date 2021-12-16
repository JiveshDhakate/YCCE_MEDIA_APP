package com.jivesh.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailEt,passwordEt;
    Button signupBtn,loginBtn;
    ProgressBar progressBar;
    CheckBox checkBox;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt =findViewById(R.id.login_emailEt);
        passwordEt = findViewById(R.id.login_passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.login_SignupBtn);
        checkBox = findViewById(R.id.login_checkbox);
        progressBar = findViewById(R.id.login_progressbar);
        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    confirmpassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    confirmpassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendtoMain();
                            }
                            else {
                                String error = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error"+ error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else {
                    Toast.makeText(LoginActivity.this, "Please fill all Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendtoMain() {
        Intent intent = new Intent(LoginActivity.this,Splashscreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            sendtoMain();
        }
    }
}