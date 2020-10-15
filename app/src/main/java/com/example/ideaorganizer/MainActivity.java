package com.example.ideaorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button imgButton;
    Button audioButton;
    Button showNotesButton;
    ImageButton mProfileButton;
    private Button logout;

    FloatingActionButton mAddFab, mAddNoteFab, mAddImageFab, mAddAudioFab;
    TextView addNoteActionText, addImageActionText, addAudioActionText;
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProfileButton = findViewById(R.id.profile_btn);

        mAddFab = findViewById(R.id.add_fab);

        mAddNoteFab = findViewById(R.id.add_note);
        mAddImageFab = findViewById(R.id.add_image);
        mAddAudioFab = findViewById(R.id.add_audio);

        addNoteActionText = findViewById(R.id.add_note_text);
        addImageActionText = findViewById(R.id.add_image_text);
        addAudioActionText = findViewById(R.id.add_audio_text);

        mAddNoteFab.setVisibility(View.GONE);
        mAddImageFab.setVisibility(View.GONE);
        mAddAudioFab.setVisibility(View.GONE);

        addNoteActionText.setVisibility(View.GONE);
        addImageActionText.setVisibility(View.GONE);
        addAudioActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            mAddNoteFab.show();
                            mAddImageFab.show();
                            mAddAudioFab.show();
                            addNoteActionText.setVisibility(View.VISIBLE);
                            addImageActionText.setVisibility(View.VISIBLE);
                            addAudioActionText.setVisibility(View.VISIBLE);

                            isAllFabsVisible = true;
                        } else {

                            mAddNoteFab.hide();
                            mAddImageFab.hide();
                            mAddAudioFab.hide();
                            addNoteActionText.setVisibility(View.GONE);
                            addImageActionText.setVisibility(View.GONE);
                            addAudioActionText.setVisibility(View.GONE);
                            isAllFabsVisible = false;
                        }
                    }
                });


        mAddImageFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAddImage();
                    }
                });


        mAddNoteFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAddNote();
                    }
                });

        mAddAudioFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAddAudio();
                    }
                });

        mProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openProfileActivity();
                    }
                });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
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

    public void openAddNote() {
        Intent intent = new Intent(this, AddNotes.class);
        startActivity(intent);
    }

    public void openAddImage() {
        Intent intent = new Intent(this, AddImage.class);
        startActivity(intent);
    }

    public void openAddAudio() {

        setContentView(R.layout.framelayout2);

}

    public void openProfileActivity() {

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

}