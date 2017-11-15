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
import com.csudh.healthapp.csudhhealthapp.HomepageActivity;
import com.csudh.healthapp.csudhhealthapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Darshit on 11/6/2017.
 */

public class LogInActivity extends AppCompatActivity {

    Button buttonLogin,buttonRegister;
    private EditText inputEmail, inputPassword;
    private Button signIn, register;
    private FirebaseAuth auth;

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

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent (this, HomepageActivity.class) );
            finish();
        }
        setContentView(R.layout.log_in);

        inputEmail = (EditText) findViewById(R.id.editTextEmail);
        inputPassword = (EditText) findViewById(R.id.editTextPassword);

        addListenerOnLoginButton();
        addListenerOnRegisterButton();
    }

    public void addListenerOnLoginButton() {

        final Context context = this;
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Field cannot be empty, Please enter valid email address.", Toast.LENGTH_SHORT).show();
                    inputEmail.requestFocus();
                    return;
                }
                else if(!email.contains("@csudh"))
                {
                    Toast.makeText(getApplicationContext(), "Email Id is invalid, Please enter valid email address.", Toast.LENGTH_SHORT).show();
                    inputEmail.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Field cannot be empty, Please enter valid password", Toast.LENGTH_SHORT).show();
                    inputPassword.requestFocus();
                    return;
                }
                else if(password.length()<8)
                {
                    Toast.makeText(getApplicationContext(), "Please enter correct password", Toast.LENGTH_SHORT).show();
                    inputPassword.requestFocus();
                    return;
                }
                else {
                    boolean flag=false;
                    if(flag) {
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            if (password.length() < 6) {
                                                Toast.makeText(getApplicationContext(), "Please enter valid password", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Intent intent = new Intent(context, HomepageActivity.class);
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
               }
        });
    }
}
