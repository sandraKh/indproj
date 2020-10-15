package com.example.ideaorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, forgotPassword;
    private EditText editTextemail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button)findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextemail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.signIn:
                userLogin();
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()) {
            editTextemail.setError("Email Required");
            editTextemail.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to Login.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}