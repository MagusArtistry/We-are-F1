package com.example.wearef1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;


public class SignUpActivity extends AppCompatActivity {

    private EditText emailField, usernameField, passwordField, confirmPasswordField;
    private Button signUpButton;
    private TextView alreadyMemberButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            FirebaseApp.initializeApp(this);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
        } catch (Exception e) {
            Toast.makeText(SignUpActivity.this, "Firebase initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


        emailField = findViewById(R.id.email_field_signup);
        usernameField = findViewById(R.id.username_field_signup);
        passwordField = findViewById(R.id.password_field_signup);
        confirmPasswordField = findViewById(R.id.confirm_password_field_signup);
        signUpButton = findViewById(R.id.signup_button_signup);
        alreadyMemberButton = findViewById(R.id.already_a_member_signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String confirmPassword = confirmPasswordField.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                signUpUser(email, username, password);
            }
        });

        alreadyMemberButton.setOnClickListener(v -> {
             Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
             startActivity(intent);
             finish();
        });
    }

    private void signUpUser(String email, String username, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            checkUsernameAndEmail(userId, email, username);
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUsernameAndEmail(String userId, String email, String username) {
        DatabaseReference usersRef = mDatabase.child("users");

        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(SignUpActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                    // Optionally: Delete the created user
                    deleteUserIfExists(userId);
                } else {
                    // Check if username already exists
                    usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(SignUpActivity.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                                deleteUserIfExists(userId);
                            } else {
                                saveUserData(userId, email, username);
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            handleDatabaseError(error);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                handleDatabaseError(error);
            }
        });
    }

    private void deleteUserIfExists(String userId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "User deleted due to username conflict.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed to delete user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveUserData(String userId, String email, String username) {
        // Save user data to the database after checks
        User newUser = new User(email, username);
        mDatabase.child("users").child(userId).setValue(newUser)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Database error: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleDatabaseError(DatabaseError error) {
        Toast.makeText(SignUpActivity.this, "Database Main error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

}