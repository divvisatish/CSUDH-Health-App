package com.csudh.healthapp.csudhhealthapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent (this, HomepageActivity.class) );
            finish();
        }
        setContentView(R.layout.log_in);

        inputEmail = (EditText) findViewById(R.id.editTextEmail);
        inputPassword = (EditText) findViewById(R.id.editTextPassword);
        register = (Button) findViewById(R.id.buttonRegister);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        addListenerOnLoginButton();
        Log.i("info", "Login screen loaded");
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

                if(!isEmailValid(email))
                {
                    return;
                }
                else if(!isPasswordValid(password)) {
                    return;
                }
                else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if(task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "Log In successfull" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, HomepageActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LogInActivity.this, "User is not present in the database" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isEmailValid(String email)
    {
        String alertMessage = "";
        if(TextUtils.isEmpty(email)) {
            inputEmail.setError("Email cannot be empty");
            inputEmail.requestFocus();
            return false;
        }
        else if(!email.contains("@"))
        {
            inputEmail.setError("Invalid Email");
            inputEmail.requestFocus();
            return false;
        }
        else if(!email.contains("csudh"))
        {
            inputEmail.setError("Email ID should contain csudh");
            inputEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password)
    {
        String alertMessage = "";
        if(TextUtils.isEmpty(password)) {
            inputPassword.setError("Password cannot be empty");
            inputPassword.requestFocus();
            return false;
        }
        else if(password.length()<8)
        {
            inputPassword.setError("Invalid Password");
            inputPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void addListenerOnRegisterButton() {

        final Context context = this;
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.i("Button","BUtton pressed");
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}