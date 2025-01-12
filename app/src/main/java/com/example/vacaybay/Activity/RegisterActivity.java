package com.example.vacaybay.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacaybay.Activity.Models.Customer;
import com.example.vacaybay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailField, nameField, phoneField, pwdField, confirmPwdField;
    private Button registerButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = findViewById(R.id.email_field);
        nameField = findViewById(R.id.name_field);
        phoneField = findViewById(R.id.phone_field);
        pwdField = findViewById(R.id.pwd_field);
        confirmPwdField = findViewById(R.id.confirm_pwd_field);
        registerButton = findViewById(R.id.register_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String name = nameField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String password = pwdField.getText().toString().trim();
        String confirmPassword = confirmPwdField.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            nameField.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneField.setError("Phone number is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pwdField.setError("Password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPwdField.setError("Passwords do not match");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user != null) {
                            String userID = user.getUid();

                            // Create a Customer object
                            Customer customer = new Customer();
                            customer.setName(name);
                            customer.setPhone(phone);
                            customer.setEmail(email);
                            customer.setUserID(userID);

                            // Save user information to Firestore
                            firestore.collection("customers")
                                    .document(userID)
                                    .set(customer)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("REGISTER_ERROR", task.getException().getMessage());
                    }
                });
    }
}
