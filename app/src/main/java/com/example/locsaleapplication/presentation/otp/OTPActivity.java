package com.example.locsaleapplication.presentation.otp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.locsaleapplication.LoginActivity;
import com.example.locsaleapplication.MainActivity;
import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;

@SuppressWarnings("All")
public class OTPActivity extends AppCompatActivity {

    private TextView login_register;

    private String type;
    String token;


    private AppCompatTextView tvResendOTP, tvMobileNo;
    private OtpTextView otpTextView;
    private AppCompatButton btnVerify;
    private AppCompatImageView imageBack;


    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String TAG = "OTP";
    ProgressDialog pd;

    private String name, email, dob, countryCode, number;
    private String stMobileVerify = "";
    private String stFrom = "", addressName, addressCity, addressLat, addressLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setMessage("please wait");

        Intent getIntent = getIntent();
        if (getIntent != null) {
            if (getIntent.hasExtra("from")) {
                number = getIntent.getStringExtra("number");
                countryCode = getIntent.getStringExtra("countryCode");

                stMobileVerify = "+" + countryCode + "" + number;
            } else {
                name = getIntent.getStringExtra("name");
                email = getIntent.getStringExtra("email");
                dob = getIntent.getStringExtra("dob");
                countryCode = getIntent.getStringExtra("countryCode");
                number = getIntent.getStringExtra("number");
                addressName = getIntent.getStringExtra("addressName");
                addressCity = getIntent.getStringExtra("addressCity");
                addressLat = getIntent.getStringExtra("addressLat");
                addressLng = getIntent.getStringExtra("addressLng");

                stMobileVerify = "+" + countryCode + "" + number;
            }
        }

        otpTextView = (OtpTextView) findViewById(R.id.otpViewVerifyNumber);
        btnVerify = (AppCompatButton) findViewById(R.id.btnOtpVerify);
        imageBack = (AppCompatImageView) findViewById(R.id.imageOTPBack);
        tvResendOTP = (AppCompatTextView) findViewById(R.id.tvVerifyResend);
        tvMobileNo = (AppCompatTextView) findViewById(R.id.tvOTPMobileNumber);
        tvMobileNo.setText(stMobileVerify);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                resendVerificationCode(stMobileVerify, mResendToken);
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpTextView.getOTP() != null && otpTextView.getOTP().length() == 6) {
                    if (mVerificationId != null) {
                        verifyPhoneNumberWithCode(mVerificationId, otpTextView.getOTP());
                    }
                }
            }
        });

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


        initOTPCallback();


        pd.show();
        startPhoneNumberVerification(stMobileVerify);
    }

    private void initOTPCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                pd.dismiss();
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.e(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                pd.dismiss();
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(OTPActivity.this, "Invalid otp, Please try again!", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(OTPActivity.this, "Too many attampt, Please try again later!", Toast.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                pd.dismiss();
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.e(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pd.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            FirebaseDatabase.getInstance().getReference().child("Users")/*.child(mAuth.getCurrentUser().getUid())*/
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
                                                    if (stMobileVerify.contains(user.getContact_number())) {
                                                        type = "change";
                                                        userMain = user;
                                                        /*updateToken(user);*/
                                                        break;
                                                    }
                                                }
                                            }
                                            if (userMain == null) {
                                                if (getIntent().hasExtra("from")) {
                                                    pd.dismiss();
                                                    FirebaseAuth.getInstance().signOut();
                                                    Toast.makeText(OTPActivity.this, "This number is not registered, Please register first.", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(OTPActivity.this, LoginActivity.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                    finishAffinity();
                                                } else {
                                                    registerUser();
                                                }
                                            } else {
                                                if (userMain.getType() != null && userMain.getType().equals("2")) {
                                                    if (getIntent().hasExtra("from")) {
                                                        if (type.equalsIgnoreCase("exist")) {
                                                            updateToken(userMain);
                                                        } else if (type.equalsIgnoreCase("change")) {
                                                            updateUserData(userMain, mAuth.getCurrentUser().getUid());
                                                        }
                                                    } else {
                                                        if (type.equalsIgnoreCase("exist") || type.equalsIgnoreCase("change")) {
                                                            pd.dismiss();
                                                            Toast.makeText(OTPActivity.this, "This number is already registed, Please use another number", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                } else {
                                                    pd.dismiss();
                                                    FirebaseAuth.getInstance().signOut();
                                                    Toast.makeText(OTPActivity.this, "This ID is associated with your Business account", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(OTPActivity.this, LoginActivity.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                    finishAffinity();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e("Appname", "issue OTP : " + databaseError.getMessage());
                                        }
                                    });
                            if (getIntent().hasExtra("from")) {
                            } else {


                            }
                            // Update UI
                        } else {
                            pd.dismiss();
                            // Sign in failed, display a message and update the UI
                            Log.e(TAG, "signInWithCredential:failure :: " + task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(OTPActivity.this, "Invalid otp, Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void registerUser() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("username", "");
        map.put("email", email);
        map.put("dob", dob);
        map.put("id", mAuth.getCurrentUser().getUid());
        map.put("contact_number", number.toString());
        map.put("bio", "");
        map.put("imageurl", "default");
        map.put("type", "2");
        map.put("token", token);
        map.put("addressName", addressName);
        map.put("addressCity", addressCity);
        map.put("addressLat", addressLat);
        map.put("addressLng", addressLng);

        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(OTPActivity.this, "You are now registered to Locsale", Toast.LENGTH_SHORT).show();

                    //FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    pd.dismiss();
                    Toast.makeText(OTPActivity.this, "Something happened :( Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData(User user, String uid) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", user.getName());
        map.put("username", user.getUsername());
        map.put("bio", user.getBio());
        map.put("contact_number", user.getContact_number());
        map.put("dob", user.getAddress());
        map.put("email", user.getEmail());
        map.put("id", uid);
        map.put("imageurl", user.getImageurl());
        map.put("type", user.getType());
        map.put("token", token);

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).updateChildren(map);

        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId()).removeValue();

        pd.dismiss();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finishAffinity();

    }

    private void updateToken(User user) {

        HashMap<String, Object> mapToken = new HashMap<>();
        mapToken.put("token", token);

        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId()).updateChildren(mapToken, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}