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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText emailEt,passwordEt,confirmpassEt;
    Button signinBtn,loginBtn;
    ProgressBar progressBar;
    CheckBox checkBox;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEt = findViewById(R.id.signup_emailEt);
        passwordEt = findViewById(R.id.signup_passwordEt);
        confirmpassEt = findViewById(R.id.confirm_signup_passwordEt);
        signinBtn = findViewById(R.id.siginBtn);
        loginBtn = findViewById(R.id.signup_loginBtn);
        checkBox = findViewById(R.id.signup_checkbox);
        progressBar = findViewById(R.id.signup_progressbar);
        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmpassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmpassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                String confirm_password = confirmpassEt.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) ||!TextUtils.isEmpty(confirm_password)){
                    if(password.equals(confirm_password)){
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   sendtoMain();
                                   progressBar.setVisibility(View.INVISIBLE);
                               }
                               else {
                                   String error = task.getException().getMessage();
                                   Toast.makeText(RegisterActivity.this, "Error"+ error, Toast.LENGTH_SHORT).show();
                               }
                            }
                        });
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Password And Confirm Password is not marching", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendtoMain() {

        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            sendtoMain();
        }

    }
}