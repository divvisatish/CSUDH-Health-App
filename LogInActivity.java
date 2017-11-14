package com.csudh.healthapp.csudhhealthapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    private Button logIn, forgotPassword, register;
    private EditText email;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        addListenerOnLoginButton();
        addListenerOnForgotPasswordButton();
        addListenerOnNotRegisteredButton();
    }


    public boolean isEmailValid() {

        boolean emailCheck = true;
        String emailValidation="";
        email = (EditText) findViewById(R.id.editTextEmail);
        emailValidation = email.getText().toString();

        if(TextUtils.isEmpty(emailValidation)) {
            email.setError("Email cannot be empty");
            emailCheck = false;
        } else if (!emailValidation.contains("@csudh.edu")) {
            email.setError("Please enter valid email");
            emailCheck = false;
        }

        return emailCheck;

    }

    public boolean isPasswordValid() {

        boolean emailCheck = true;
        String passwordValidation="";
        password = (EditText) findViewById(R.id.editTextPassword);
        passwordValidation = password.getText().toString();

        if(TextUtils.isEmpty(passwordValidation)) {
            password.setError("Please enter valid password");
            emailCheck = false;
        }

        return emailCheck;
    }

    public void addListenerOnLoginButton() {
        logIn = (Button) findViewById(R.id.buttonLogin);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmailValid()) {
                    Log.i("Validation" ,"Tested");
                    if (isPasswordValid()) {
                        Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });
    }

    public void addListenerOnForgotPasswordButton() {
        forgotPassword = (Button) findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addListenerOnNotRegisteredButton() {
        register = (Button) findViewById(R.id.buttonRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(intent);
            }
        });
    }

}
