package com.example.ideaorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView banner, registerUser;
    private EditText editTextName, editTExtEmail, editTextPassword;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView)findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button)findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        editTextName = (EditText)findViewById(R.id.fullName);
        editTExtEmail= (EditText)findViewById(R.id.email);
        editTextPassword= (EditText)findViewById(R.id.password);

        progressBar= (ProgressBar)findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        final String email = editTExtEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String fullName = editTextName.getText().toString().trim();

        if(fullName.isEmpty()) {
            editTextName.setError("Full Name Required");
            editTextName.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            editTExtEmail.setError("Email Required");
            editTExtEmail.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Password Needs To Be Atleast 6 Characters Long");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(fullName, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(RegisterUser.this, "Failed to register. Try Again", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, "Failed to register. Try Again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}