package gr.uth.cardshop;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText mFullName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mRepeatPassword;
    private EditText mPhone;
    private Button mRegBtn;
    private String userID;
    private FirebaseAuth mAuth;
    private MaterialToolbar mToolbar;
    private FirebaseFirestore mStore;
    final LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);
    ValidationForm val = new ValidationForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName = findViewById(R.id.reg_fullName);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mRepeatPassword = findViewById(R.id.reg_repeatPassword);
        mPhone = findViewById(R.id.reg_phone);
        mRegBtn = findViewById(R.id.signup_btn);
        mToolbar = findViewById(R.id.reg_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase instance//
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = mFullName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String repeatPassword = mRepeatPassword.getText().toString();
                String phone = mPhone.getText().toString();
                if(!TextUtils.isEmpty(fullName) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(repeatPassword) || !TextUtils.isEmpty(phone)){
                    if(!val.isValidFullName(fullName)){
                        mFullName.setError("FullName must not contain numbers, only one space in between and only 1-20 characters long");
                        return;
                    }
                    if(!val.isValidEmail(email)){
                        mEmail.setError("Email is not valid!");
                        return;
                    }
                    if(!repeatPassword.equals(password)){
                        mRepeatPassword.setError("Passwords do not match!");
                        return;
                    }
                    if(!val.isValidPassword(password)){
                        mPassword.setError("Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one digit, one special character, and no whitespace characters");
                        return;
                    }

                    if(!val.isValidPhone(phone)){
                        mPhone.setError("Phone must be 10 characters long");
                        return;
                    }
                    loadingDialog.startLoadingDialog();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                //send verification link
                                FirebaseUser fUser = mAuth.getCurrentUser();
                                fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"onFailure: Email not send" + e.getMessage());
                                    }
                                });
                                Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = mStore.collection("UserProfile").document(userID).collection("Information").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("fName",fullName);
                                user.put("email",email);
                                user.put("phone",phone);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"onSuccess: user profile is created for "+userID);
                                        loadingDialog.dismissDialog();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"onFailure " + e);
                                    }
                                });
                                Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissDialog();
                                    }
                                },3000);
                            }
                        }
                    });
                }else{
                    mFullName.setError("fullName is Required");
                    mEmail.setError("Email is Required");
                    mPassword.setError("Password is Required");
                    mRepeatPassword.setError("Repeat Password is Required");
                    mPhone.setError("phone is Required");
                    return;
                }
            }
        });
    }
    public void signIn(View view) {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}