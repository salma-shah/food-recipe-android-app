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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText etSignUpPassword, etSignUpEmail, etSignUpUsername;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DBHelper(this);
        // SQLiteDatabase db = dbHelper.getWritableDatabase();

        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpUsername = findViewById(R.id.etSignUpUsername);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView tvLogin = findViewById(R.id.tvLogin);
        
        btnSignUp.setOnClickListener(v -> signUpUser());

        // redirecting to login page
        tvLogin.setOnClickListener(v2->
        {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void signUpUser()
    {
        String username = etSignUpUsername.getText().toString().trim();
        String password = etSignUpPassword.getText().toString().trim();
        String email = etSignUpEmail.getText().toString().trim();

        // validation
        String emailValidation = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String passwordValidation = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        // no empty fields
        if (username.isEmpty() || password.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //email validation
        if (!email.matches(emailValidation)){
            Toast.makeText(this, "Email address is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }

        //password validation
        if (!password.matches(passwordValidation)){
            Toast.makeText(this, "Password needs to be at least 8 characters long.", Toast.LENGTH_LONG).show();
            return;
        }

        // adding the user
        boolean result = dbHelper.registerUser(username, email, password);
        if (result)
        {
            Toast.makeText(this, "The account was created successfully!", Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences = getSharedPreferences("user_email", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.apply();
            // navigating to navigation bar
            Intent intent = new Intent(SignUpActivity.this, NavigationBar.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}