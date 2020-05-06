package com.example.maptesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText txtName, txtEmail, txtPassword, phone;
    ProgressDialog progressDialog;
    TextView mHaveAccountTv;
    ProgressDialog pd;
    Button btnSignup;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail = (EditText) findViewById(R.id.editTextEmail);
        txtPassword = (EditText) findViewById(R.id.editTextPassword);
        txtName = (EditText) findViewById(R.id.editTextName);
        mHaveAccountTv = findViewById(R.id.have_accounttv);
        btnSignup = (Button) findViewById(R.id.txtSignUp);
        progressDialog = new ProgressDialog(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Logging In..... ");
        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String name = txtName.getText().toString();

                if (name == null || name.equals("")) {
                    txtName.setError("Please Fill Name");
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    txtEmail.setError("Invalid Email");
                    txtEmail.setFocusable(true);

                } else if (password.length() < 6) {
                    txtPassword.setError("Pasword Length Must Be grester than 6 characters");
                    txtPassword.setFocusable(true);

                } else {
                    progressDialog.setMessage("Registering ....");
                    registeruser(email, password, name);
                }

            }
        });
    }

    private void registeruser(final String email, String password, final String name) {
        progressDialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //progressDialog.dismiss();
                            //FirebaseUser user = mAuth.getCurrentUser();

                            String Email = email;
                            String Name = name;
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).child("name").setValue(Name.toUpperCase());
                            reference.child(uid).child("email").setValue(Email);
                            reference.child(uid).child("id").setValue(uid);
                            Save.save(getApplicationContext(),"session","true");

                            Intent intent = new Intent(RegisterActivity.this, AllPlaces.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(RegisterActivity.this, "Registered....\n" + email, Toast.LENGTH_SHORT).show();
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
