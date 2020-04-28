package com.example.maptesting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnLogin2, btnSignup;
    TextView btnRecoverPass ,register;
    EditText email,password;
    ProgressDialog progressDialog;
    final FirbaseAuthenticationClass firbaseAuthenticationClass=new FirbaseAuthenticationClass();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin2 = (Button) findViewById(R.id.button3);
        btnSignup = (Button) findViewById(R.id.btnSignup);
       // final String arr[] = getResources().getStringArray(R.array.selection);
        password = (EditText) findViewById(R.id.editText2);
        email=(EditText) findViewById(R.id.editText);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging In..... ");
        register = findViewById(R.id.textRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL = email.getText().toString().trim();
                String PASSWORD = password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()) {
                    email.setError("Invalid email");
                    email.setFocusable(true);
                } else {
                    if(EMAIL.equals("admin@gmail.com") && PASSWORD.equals("admin")){
                        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        progressDialog.show();
                        firbaseAuthenticationClass.LoginUser(EMAIL,PASSWORD, LoginActivity.this, progressDialog);
                    }


                }
            }
        });

    }
}
