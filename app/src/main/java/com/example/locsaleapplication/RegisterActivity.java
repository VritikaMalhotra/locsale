package com.example.locsaleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_name;
    private EditText register_username;
    private EditText register_email;
    private EditText register_password;
    private EditText register_confirmPassword;
    private EditText register_dob;
    private TextView register_loginUser;
    private Button register_button;
    private String name,email,password,confirmPassword,dob,username;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_name = findViewById(R.id.register_name);
        register_username = findViewById(R.id.register_username);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_confirmPassword = findViewById(R.id.register_confirmPassword);
        register_dob = findViewById(R.id.register_dob);
        register_loginUser = findViewById(R.id.register_loginUser);
        register_button = findViewById(R.id.register_button);
        pd = new ProgressDialog(this);


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        register_loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUserCheck();
            }
        });
    }

    private void RegisterUserCheck(){
        name = register_name.getText().toString().trim();
        username = register_username.getText().toString().trim();
        email = register_email.getText().toString().trim();
        password = register_password.getText().toString().trim();
        confirmPassword = register_confirmPassword.getText().toString().trim();
        dob = register_dob.getText().toString().trim();

        if(name.isEmpty()){
            register_name.setError("Full name is required");
            register_name.requestFocus();
            return;
        }
        if(username.isEmpty()){
            register_name.setError("Username is required");
            register_name.requestFocus();
            return;
        }
        if(email.isEmpty()){
            register_email.setError("Email address is required");
            register_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            register_email.setError("Please enter a correct Email pattern");
            register_email.requestFocus();
            return;
        }
        if(dob.isEmpty()){
            register_dob.setError("DOB is required");
            register_dob.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            register_password.setError("Password is required");
            register_password.requestFocus();
            return;
        }
        if(password.length()<8)
        {
            register_password.setError("Minimum length of password should be 8");
            register_password.requestFocus();
            return;
        }
        if(confirmPassword.isEmpty())
        {
            register_confirmPassword.setError("Confirmed password required");
            register_confirmPassword.requestFocus();
            return;
        }
        if(!(password.equals(confirmPassword)))
        {
            register_confirmPassword.setError("Password does not match");
            register_confirmPassword.requestFocus();
            return;
        }

        RegisterUser(name,email,password,dob);

    }


    private void RegisterUser(final String name, final String email, String password, final String dob) {
        pd.setMessage("please wait");
        pd.show();
        Toast.makeText(this, password, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("name",name);
                    map.put("username",username);
                    map.put("email",email);
                    map.put("dob",dob);
                    map.put("id",mAuth.getCurrentUser().getUid());
                    map.put("bio","");
                    map.put("imageurl","default");
                    map.put("type","2");

                    mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "You are now registered to Locsale", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "Something happened :( Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}