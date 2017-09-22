package com.example.mitchell.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.name;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private Toolbar mToolBar;
    private ProgressDialog loadingBar;

    private EditText registerName;
    private EditText registerEmail;
    private EditText registerPassword;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mToolBar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerName = (EditText) findViewById(R.id.login_email);
        registerEmail = (EditText) findViewById(R.id.emailText);
        registerPassword = (EditText) findViewById(R.id.login_password);

        loadingBar = new ProgressDialog(this);

        registerButton = (Button) findViewById(R.id.createAccountButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();

                registerAccount(name, email, password);
            }


        });
    }


    private void registerAccount(final String name, final String email, final String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "One of your fields are empty. Please try again.", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        String current_user_id = mAuth.getCurrentUser().getUid();
                        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(current_user_id);

                        userRef.child("user_name").setValue(name);
                        userRef.child("user_status").setValue("I am god");
                        userRef.child("user_image").setValue("default_profile");
                        userRef.child("user_thumb_image").setValue("default_image")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Could not create account.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}