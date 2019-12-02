package com.example.tinder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mEditEmail, mEditPassword,mName;

    private RadioGroup mRadioGroup;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener fireBaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        //Always login
        fireBaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mRegister = findViewById(R.id.register);
        mEditEmail = findViewById(R.id.editEmail);
        mEditPassword = findViewById(R.id.editPassword);
        mName = findViewById(R.id.editName);

        mRadioGroup = findViewById(R.id.radioGroup);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = findViewById(userId);

                if (radioButton.getText() == null){
                    return;
                }

                final String name = mName.getText().toString();
                final String email = mEditEmail.getText().toString();
                final String password = mEditPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                        (RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(RegistrationActivity.this,"Sign Up Error",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(RegistrationActivity.this,"Sign Up Successful",Toast.LENGTH_LONG)
                                            .show();
                                    String userId = mAuth.getCurrentUser().getUid();

                                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                                    Map userInfo = new HashMap<>();
                                    userInfo.put("name",name);
                                    userInfo.put("sex",radioButton.getText().toString());
                                    userInfo.put("profileImageUrl","default");

                                    currentUserDb.updateChildren(userInfo);
                                }

                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fireBaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fireBaseAuthStateListener);
    }
}
