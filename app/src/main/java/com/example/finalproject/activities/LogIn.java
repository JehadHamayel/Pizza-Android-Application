package com.example.finalproject.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.databaserhelper.DataBaseHelper;
import com.example.finalproject.models.Hash;
import com.example.finalproject.R;
import com.example.finalproject.preferencesShared.SharedPrefManager;


public class LogIn extends AppCompatActivity {
    EditText email;
    EditText password;
    Button login;
    Button signUp;

    SharedPrefManager sharedPrefManager;
    Intent intent;
    CheckBox checkBoxRememberMe;
    DataBaseHelper dataBaseHelper =new DataBaseHelper(LogIn.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        signUp = (Button) findViewById(R.id.SIGNUPButton);
        login = (Button) findViewById(R.id.SIGNINButton);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        sharedPrefManager = SharedPrefManager.getInstance(LogIn.this);
        signUp.setOnClickListener(v -> {
            intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
        });
        if (! sharedPrefManager.readString("Remember Me email", "").isEmpty()) {
            email.setText(sharedPrefManager.readString("Remember Me email", ""));
        }
        login.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String passwordText = Hash.hashPassword(password.getText().toString().trim());
            if (emailText.isEmpty()) {
                email.setError("Please enter your email");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches() && !emailText.isEmpty()) {
                email.setError("Please enter a valid email address");
            }
            if (password.getText().toString().trim().isEmpty()) {
                password.setError("Please enter your password");
            }
            boolean userIsExist = true;
            boolean adminIsExist = true;
            Cursor cursor = dataBaseHelper.getUserByEmail(emailText);
            if (cursor.getCount() == 0) {
                userIsExist = false;
            }else{
                if (cursor.moveToFirst()) {
                    String passwordFromDB = cursor.getString(5);
                    if (passwordFromDB.equals(passwordText)) {
                        sharedPrefManager.writeString("logIn email",emailText);
                        intent = new Intent(LogIn.this, MainNavigationPageForUser.class);
                        startActivity(intent);
                    } else {
                        password.setError("Wrong password");
                    }
                }
            }
            Cursor cursor1 = dataBaseHelper.getAdminByEmail(emailText);
            if (cursor1.getCount() == 0) {
                adminIsExist = false;
            }else{
                if (cursor1.moveToFirst()) {
                    String passwordFromDB = cursor1.getString(5);
                    if (passwordFromDB.equals(passwordText)) {
                        sharedPrefManager.writeString("logIn email admin",emailText);
                        intent = new Intent(LogIn.this, MainNavigationPageForAdmin.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LogIn.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        password.setError("Wrong password");
                    }
                }
            }

            if (!userIsExist && !adminIsExist) {
                Toast.makeText(LogIn.this, "User not found", Toast.LENGTH_SHORT).show();
            }

            if (checkBoxRememberMe.isChecked()) {
                sharedPrefManager.writeString("Remember Me email",emailText);
            }else {
                sharedPrefManager.writeString("Remember Me email","");
            }

        });

    }
    @Override
    public void onBackPressed() {
    }
}