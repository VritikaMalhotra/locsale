package com.example.locsaleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email;
    private EditText login_password;
    private Button login_button;
    private TextView login_register;
    private FirebaseAuth mAuth;
    private TextView login_forgotPassword;

    private String email,password;

    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        login_register = findViewById(R.id.login_register);
        login_forgotPassword = findViewById(R.id.login_forgotPassword);

        mAuth = FirebaseAuth.getInstance();

        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUserCheck();
            }
        });

        login_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private void LoginUserCheck() {

        email = login_email.getText().toString().trim();
        password = login_password.getText().toString().trim();

        if(email.isEmpty()){
            login_email.setError("Email address is required");
            login_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            login_password.setError("Password is required");
            login_password.requestFocus();
            return;
        }
        LoginUser(email,password);
    }

    private void LoginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue() != null){
                                        Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                                        if(map.get("type").equals("2")){
                                            finish();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }else{
                                            Toast.makeText(LoginActivity.this, "This ID is associated with your Shopkeeper account", Toast.LENGTH_LONG).show();
                                            Toast.makeText(LoginActivity.this, "Please register with another email for Customer side", Toast.LENGTH_LONG).show();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(LoginActivity.this,LoginActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            finish();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }
                else
                {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}