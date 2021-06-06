package com.mad.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email,password;
    TextView gotoLogin;
    ProgressBar progressBar;
    FirebaseAuth authentication;
    Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.edit_name);
        email =findViewById(R.id.edit_email);
        password=findViewById(R.id.edit_password);
        gotoLogin=findViewById(R.id.login_text);
        authentication=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        btn_register=findViewById(R.id.btn_register);
        if(authentication.getCurrentUser()!=null)
        {
            startActivity( new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(name.getText().toString().trim()))
                    Toast.makeText(RegisterActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(email.getText().toString().trim()))
                    Toast.makeText(RegisterActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(password.getText().toString().trim()))
                    Toast.makeText(RegisterActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    authentication.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity( new Intent(getApplicationContext(),MainActivity.class));

                            }
                            else
                                Toast.makeText(RegisterActivity.this, "Error occurred: \n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}