package com.example.foodbunny.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;

public class LoginActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);

        // initializing the elements from the ui
        EditText etLoginEmail = findViewById(R.id.etLoginEmail);
        EditText etLoginPassword = findViewById(R.id.etLoginPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvSignUp = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(v -> {
            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (email.isEmpty())
            {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty())
            {
                Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUser(email, password))
            {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // saving user's email in shared preferences so that user account info will pass smoothly
                SharedPreferences sharedPreferences = getSharedPreferences("user_email", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.apply();

                // then navigate to the home page of the app
                Intent intent = new Intent(LoginActivity.this, NavigationBar.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(this, "The login was unsuccessful. The email or password was incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        // redirecting to sign up page
        tvSignUp.setOnClickListener(v2->
        {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}