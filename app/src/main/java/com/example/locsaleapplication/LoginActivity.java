package com.example.locsaleapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.presentation.otp.OTPActivity;
import com.example.locsaleapplication.utils.AppGlobal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("All")
public class LoginActivity extends AppCompatActivity {

    private EditText login_mobile_number;
    private AppCompatButton login_button;
    private TextView login_register;
    private FirebaseAuth mAuth;
    private TextView login_forgotPassword;

    private String email, password;
    private String mobileNumber;

    private String type;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                token = task.getResult();
            }
        });

        login_mobile_number = findViewById(R.id.login_mobile_number);
        login_button = findViewById(R.id.login_button);
        login_register = findViewById(R.id.login_register);
        login_forgotPassword = findViewById(R.id.login_forgotPassword);

        mAuth = FirebaseAuth.getInstance();

        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private void LoginUserCheck() {

        mobileNumber = login_mobile_number.getText().toString().trim();

        if (mobileNumber.isEmpty()) {
            login_mobile_number.setError("Mobile number is required");
            login_mobile_number.requestFocus();
            return;
        }
        if (mobileNumber.length() != 10) {
            login_mobile_number.setError("Mobile number must be 10 digits");
            login_mobile_number.requestFocus();
            return;
        }

        Intent intentOTP = new Intent(LoginActivity.this, OTPActivity.class);
        intentOTP.putExtra("number", mobileNumber);
        intentOTP.putExtra("from", "login");
        intentOTP.putExtra("countryCode", "91");
        startActivity(intentOTP);
        //checkMobileNumberIsExist();
        /*LoginUser(email, password);*/
    }

    private void checkMobileNumberIsExist() {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User userMain = null;
                        String type = "";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            //user.setId(dataSnapshot.getKey());
                            if (user.getId() != null && user.getId().equals(mAuth.getUid())) {
                                type = "exist";
                                userMain = user;
                                break;
                            } else if (user.getContact_number() != null) {
                                if (mobileNumber.contains(user.getContact_number())) {
                                    type = "change";
                                    userMain = user;
                                    break;
                                }
                            }
                        }

                        if (userMain != null && userMain.getType() != null && userMain.getType().equals("2")) {
                            Intent intentOTP = new Intent(LoginActivity.this, OTPActivity.class);
                            intentOTP.putExtra("number", mobileNumber);
                            intentOTP.putExtra("from", "login");
                            intentOTP.putExtra("countryCode", "91");
                            startActivity(intentOTP);
                        } else {

                            FirebaseAuth.getInstance().signOut();
                            if (userMain == null) {
                                Toast.makeText(LoginActivity.this, "This number is not registered, Please register first.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "This ID is associated with your Shopkeeper account", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Appname", "issue OTP : " + databaseError.getMessage());
                    }
                });
    }

    private void LoginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                                        updateToken(map);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateToken(Map<StringBuffer, String> map) {

        HashMap<String, Object> mapToken = new HashMap<>();
        mapToken.put("token", token);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).updateChildren(mapToken, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (map.get("type").equals("2")) {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, "This ID is associated with your Shopkeeper account", Toast.LENGTH_LONG).show();
                    Toast.makeText(LoginActivity.this, "Please register with another email for Customer side", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            }
        });
    }

    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            AppGlobal.showLog("User Id : " + mAuth.getCurrentUser().getUid());
            Bundle bundleSend = null;
            Bundle intent = getIntent().getExtras();
            if (intent != null) {
                if (intent.containsKey("action_type")) {

                    bundleSend = new Bundle();
                    bundleSend.putString("title", intent.getString("title", ""));
                    bundleSend.putString("message", intent.getString("message", ""));
                    bundleSend.putString("icon", intent.getString("icon", ""));
                    bundleSend.putString("senderid", intent.getString("senderid", ""));
                    bundleSend.putString("receiverid", intent.getString("receiverid", ""));
                    bundleSend.putString("action_type", intent.getString("action_type", ""));
                } else if (intent.containsKey("postId")) {
                    bundleSend = new Bundle();
                    bundleSend.putString("postId", intent.getString("postId", ""));
                }
            }

            finish();
            Intent mainIntent = new Intent(this, MainActivity.class);
            if (bundleSend != null) {
                mainIntent.putExtras(bundleSend);
            }
            startActivity(mainIntent);
        }
    }
}