package com.example.locsaleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText forgotPassword_email;
    private Button forgotPassword_sendEmail;
    private String email;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotPassword_email = findViewById(R.id.forgotPassword_email);
        forgotPassword_sendEmail = findViewById(R.id.forgotPassword_sendEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        checkEmail();
        forgotPassword_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmail();
                sendEmail(email);

            }
        });
    }

    private void sendEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ForgotPassword.this, "Recovery email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotPassword.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmail() {
        email = forgotPassword_email.getText().toString().trim();

        if(email.isEmpty()){
            forgotPassword_email.setError("Email address is required");
            forgotPassword_email.requestFocus();
            return;
        }
    }
}