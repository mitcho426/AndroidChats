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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog loadingBar;

    private EditText statusInput;
    private Button saveChangesButton;

    private DatabaseReference statusRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        statusRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        loadingBar = new ProgressDialog(this);


        setupToolBar();
        initializeUI();
    }

    private void initializeUI() {
        statusInput = (EditText) findViewById(R.id.status_input);
        saveChangesButton = (Button) findViewById(R.id.status_save_change);

        String old_status = getIntent().getExtras().get("user_status").toString();
        statusInput.setText(old_status);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newStatus = statusInput.getText().toString();

                changeProfileStatus(newStatus);
            }
        });
    }

    private void changeProfileStatus(String newStatus) {
        if(TextUtils.isEmpty(newStatus)) {
            Toast.makeText(StatusActivity.this, "Status field is empty.", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Change Profile Status");
            loadingBar.setMessage("Please wait while we are upadating your profile stauts.");
            loadingBar.show();
            statusRef.child("user_status").setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        loadingBar.dismiss();

                        Intent settingsIntent = new Intent(StatusActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);

                        Toast.makeText(StatusActivity.this,
                                "Profile status updated sucessfully.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(StatusActivity.this,
                                "Error occurred.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void setupToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.status_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
