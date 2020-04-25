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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnLogin2, btnSignup;
    TextView btnRecoverPass;
    EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin2 = (Button) findViewById(R.id.button3);
        btnRecoverPass = findViewById(R.id.recoverpasswordtv);
        btnSignup = (Button) findViewById(R.id.btnSignup);
       // final String arr[] = getResources().getStringArray(R.array.selection);
        password = (EditText) findViewById(R.id.editText2);
        email=(EditText) findViewById(R.id.editText);

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


                }
            }
        });

    }
}
