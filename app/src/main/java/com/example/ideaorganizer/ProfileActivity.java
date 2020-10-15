package com.example.ideaorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button logoutButton;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        logoutButton = (Button)findViewById(R.id.sign_out_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity((new Intent(ProfileActivity.this, LoginActivity.class)));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView fullnametext = (TextView)findViewById(R.id.name_text);
        final TextView emailtext = (TextView)findViewById(R.id.email_text);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);

                if(userProfile != null) {
                    String fullname = userProfile.fullName;
                    String email = userProfile.email;

                    fullnametext.setText(fullname);
                    emailtext.setText(email);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.shownotes:
                        startActivity(new Intent(getApplicationContext()
                                , TextActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.showimages:
                        startActivity(new Intent(getApplicationContext()
                                , ImagesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.showaudio:
                        setContentView(R.layout.framelayout);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }

        });

    }


}