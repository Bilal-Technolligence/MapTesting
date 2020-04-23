package com.example.maptesting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {
EditText email ,pass;
Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        email = findViewById(R.id.editText);
        pass = findViewById(R.id.editText2);
        login = findViewById(R.id.button3);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("admin") && pass.getText().toString().equals("admin")){

                }
                else
                    Toast.makeText(getApplicationContext(), "Invalid credentials.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
