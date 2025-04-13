package com.example.mobil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText nameEditText;
    EditText emailEditText;

    EditText passwordEditText;
    EditText rePasswordEditText;

    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        registerButton = findViewById(R.id.registerButton);
        emailEditText=findViewById(R.id.userEmailEditText);
        passwordEditText=findViewById(R.id.userPasswordEditText);
        nameEditText=findViewById(R.id.userNameEditText);
        rePasswordEditText=findViewById(R.id.userRePasswordEditText);
        Bundle bundle = getIntent().getExtras();
        int secret_key= bundle.getInt("SECRET_KEY",0);
        if (secret_key!=37){
            finish();
        }
        registerButton.setOnClickListener(view -> register());
    }

    public void cancel(View view) {
        finish();
    }

    private void register() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (passwordEditText.getText().toString().equals(rePasswordEditText.getText().toString()) && !nameEditText.getText().toString().isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Successful registration!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

}