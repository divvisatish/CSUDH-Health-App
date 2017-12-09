package com.csudh.healthapp.csudhhealthapp;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Darshit on 11/6/2017.
 */

public class LogInActivity extends AppCompatActivity {

    Button buttonLogin,buttonRegister;
    private EditText inputEmail, inputPassword;
    private Button signIn, register;
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;
    private ProgressDialog mProgress;


    @Override
    public void onBackPressed() {
        resetScreen();
        Intent intent = new Intent(getApplicationContext(), AppMainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userDatabase = database.getReference();
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent (this, HomepageActivity.class) );
            finish();
        }
        setContentView(R.layout.log_in);

        inputEmail = (EditText) findViewById(R.id.editTextEmail);
        inputPassword = (EditText) findViewById(R.id.editTextPassword);
        register = (Button) findViewById(R.id.buttonRegister);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        resetScreen();

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

                if(!isEmailValid(email))
                {
                    return;
                }
                else if(!isPasswordValid(password)) {
                    return;
                }
                else {
                    mProgress.show();
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                mProgress.dismiss();
                                Toast.makeText(LogInActivity.this, "Log In successful", Toast.LENGTH_SHORT).show();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                userDatabase.child("users").child(auth.getCurrentUser().getUid()).child("deviceToken").setValue(deviceToken);

                                Intent intent = new Intent(context, HomepageActivity.class);
                                startActivity(intent);
                            } else {
                                mProgress.dismiss();
                                Toast.makeText(LogInActivity.this, "User is not registered in the system: ", Toast.LENGTH_SHORT).show();
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
            inputEmail.setError("Email-ID cannot be empty");
            inputEmail.requestFocus();
            return false;
        }
        else if(!email.contains("@"))
        {
            inputEmail.setError("Please enter valid Email-ID");
            inputEmail.requestFocus();
            return false;
        }
        else if(!email.toLowerCase().contains("csudh") || !email.toLowerCase().contains(".edu"))
        {
            inputEmail.setError("Please enter valid csudh Email-ID");
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
            inputPassword.setText("");
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
                resetScreen();
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resetScreen()
    {
        inputPassword.setText("");
        inputEmail.setText("");
    }
}