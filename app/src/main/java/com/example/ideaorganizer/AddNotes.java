package com.example.ideaorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


public class AddNotes extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mDatabase.getReference();

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        mButtonUpload = findViewById(R.id.button_upload);
        mEditTextFileName = findViewById(R.id.edit_note);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);

        mProgressBar = findViewById(R.id.progress_bar);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFirebaseUser.getUid();

        mStorageRef = FirebaseStorage.getInstance().getReference("Users/" + userID + "/notes");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users/" + userID + "/notes");

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AddNotes.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTextActivity();
            }
        });
    }
    private void uploadFile() {
        if (mEditTextFileName != null) {

            String content = mEditTextFileName.getText().toString();

            Upload upload = new Upload(mEditTextFileName.getText().toString());
            String uploadId = mDatabaseRef.push().getKey();
            mDatabaseRef.child(uploadId).setValue(upload);

            Toast.makeText(this, "Note uploaded successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openTextActivity() {
        Intent intent = new Intent(this, TextActivity.class);
        startActivity(intent);
    }
}
