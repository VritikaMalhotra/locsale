package com.example.locsaleapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.locsaleapplication.presentation.otp.OTPActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressWarnings("All")
public class RegisterActivity extends AppCompatActivity {

    private EditText register_name;
    //    private EditText register_username;
    private EditText register_email;
    //    private EditText register_password;
//    private EditText register_confirmPassword;
    private EditText register_number;
    private AppCompatTextView register_dob;
    private TextView register_loginUser;
    private AppCompatButton register_button;
    private String name, email, /*password, confirmPassword,*/
            dob, number/*username*/;
    private CheckBox terms_conditions;
    private TextView terms_conditions_text;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    ProgressDialog pd;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        register_name = findViewById(R.id.register_name);
//        register_username = findViewById(R.id.register_username);
        register_email = findViewById(R.id.register_email);
//        register_password = findViewById(R.id.register_password);
//        register_confirmPassword = findViewById(R.id.register_confirmPassword);
        register_number = findViewById(R.id.register_number);
        register_dob = findViewById(R.id.register_dob);
        register_loginUser = findViewById(R.id.register_loginUser);
        register_button = findViewById(R.id.register_button);
        terms_conditions = findViewById(R.id.terms_conditions);
        pd = new ProgressDialog(this);
        terms_conditions_text = findViewById(R.id.terms_conditions_text);


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        register_loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUserCheck();
            }
        });

        terms_conditions_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, TearmsAndConditionsActivity.class));
            }
        });

        register_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        register_dob.setText(sdf.format(myCalendar.getTime()));
                    }
                });
                datePickerDialog.show();
            }
        });
    }

    private void RegisterUserCheck() {
        name = register_name.getText().toString().trim();
//        username = register_username.getText().toString().trim();
        email = register_email.getText().toString().trim();
//        password = register_password.getText().toString().trim();
//        confirmPassword = register_confirmPassword.getText().toString().trim();
        number = register_number.getText().toString().trim();
        dob = register_dob.getText().toString().trim();

        if (name.isEmpty()) {
            register_name.setError("Full name is required");
            register_name.requestFocus();
            return;
        }
/*        if (username.isEmpty()) {
            register_name.setError("Username is required");
            register_name.requestFocus();
            return;
        }*/
        if (email.isEmpty()) {
            register_email.setError("Email address is required");
            register_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            register_email.setError("Please enter a correct Email pattern");
            register_email.requestFocus();
            return;
        }
        if (dob.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (number.isEmpty()) {
            register_number.setError("Contact number is required");
            register_number.requestFocus();
            return;
        }
        if (number.length() != 10) {
            register_number.setError("Mobile number must be 10 digits");
            register_number.requestFocus();
            return;
        }
        if (!terms_conditions.isChecked()) {

            Toast.makeText(
                    RegisterActivity.this,
                    "You need to agree to terms and conditions before proceding further",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterUser(name, email, number, "91", dob);

    }


    private void RegisterUser(final String name, final String email, String number, String countryCode, final String dob) {

        Intent intentOTP = new Intent(RegisterActivity.this, OTPActivity.class);
        intentOTP.putExtra("name", name);
        intentOTP.putExtra("email", email);
        intentOTP.putExtra("dob", dob);
        intentOTP.putExtra("number", number);
        intentOTP.putExtra("countryCode", countryCode);
        startActivity(intentOTP);

        /*pd.setMessage("please wait");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("username", username);
                    map.put("email", email);
                    map.put("dob", dob);
                    map.put("id", mAuth.getCurrentUser().getUid());
                    map.put("bio", "");
                    map.put("imageurl", "default");
                    map.put("type", "2");
                    map.put("token", token);

                    mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "You are now registered to Locsale", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
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

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
    }
}