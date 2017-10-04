package com.example.mitchell.mychat;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private Button sendFriendRequestButton;
    private Button declineFriendRequestButton;
    private TextView profileName;
    private TextView profileStatus;
    private ImageView profileImage;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String selected_user_id = getIntent().getExtras().get("selected_user_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(selected_user_id);

        sendFriendRequestButton = (Button) findViewById(R.id.send_request_button);
        declineFriendRequestButton = (Button) findViewById(R.id.decline_request_button);
        profileName = (TextView) findViewById(R.id.profile_username);
        profileStatus = (TextView) findViewById(R.id.profile_user_status);
        profileImage = (ImageView) findViewById(R.id.profile_visit_user_image);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String image = dataSnapshot.child("user_image").getValue().toString();

                profileName.setText(name);
                profileStatus.setText(status);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_profile).into(profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
