package com.csudh.healthapp.csudhhealthapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Darshit on 11/6/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    Button buttonRegisterNewUser;
    EditText editTextEmail,editTextConfirmPassword,editTextLastName,editTextFirstName,editTextBirthDate;
    static EditText editTextPassword;
    Spinner spinnerBloodType;
    private FirebaseAuth auth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    //DatabaseReference myChild = myRef.child("message");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextBirthDate = (EditText) findViewById(R.id.editTextBirthDate);
        spinnerBloodType = (Spinner) findViewById(R.id.spinnerBloodType);
        buttonRegisterNewUser = (Button) findViewById(R.id.buttonRegisterNewUser);
        auth = FirebaseAuth.getInstance();

        addListenerOnLoginButton();
        readDataAndPrint();

    }

    public void addListenerOnLoginButton() {

        final Context context = this;

        buttonRegisterNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(isRegistrationDetailsValid()) {
                    Intent intent = new Intent(context, LogInActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean isRegistrationDetailsValid()
    {
        boolean flag=true;
        String alertMessage = "";
        DatabaseReference users = myRef.child("users");
        DatabaseReference newUserEmailId = users.child("emailId");
        DatabaseReference newUserPassword = users.child("password");
        DatabaseReference newUserUserName = users.child("username");
        Person person = null;
        try {
            if (isEmailValid(editTextEmail.getText().toString())) {
                if (isPasswordValid(editTextPassword.getText().toString())) {
                    if (isConfirmedPasswordMatched(editTextConfirmPassword.getText().toString())) {


                        person = new Person();
                        person.setEmailId(editTextEmail.getText().toString());
                        person.setFirstName("");
                        person.setLastName("");
                        person.setPassword(editTextPassword.getText().toString());
                        person.setUserName(editTextEmail.getText().toString());
                        /*newUserEmailId.setValue(editTextEmail.getText().toString());
                        newUserPassword.setValue(editTextPassword.getText().toString());
                        newUserUserName.setValue(editTextEmail.getText().toString());*/

                        String key = users.push().getKey();
                        person.setPersonId(key);
                        users.child(key).setValue(person);


                        /*auth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.i("new user registered", "createUserWithEmail:success");
                                            FirebaseUser user = auth.getCurrentUser();
                                            updateUI(true);
                                            //updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.i("error in registration", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(false);
                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });*/
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return flag;
    }

    private void updateUI(boolean flag)
    {
        if(flag)
        {

        }
        else
        {

        }
    }

    private boolean isEmailValid(String email)
    {
        String alertMessage = "";
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return false;
        }
        else if(!email.contains("@"))
        {
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return false;
        }
        else if(!email.contains("csudh"))
        {
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password)
    {
        String alertMessage = "";
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "can not be empty", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            return false;
        }
        else if(password.length()<8)
        {
            Toast.makeText(getApplicationContext(), "minimum 8 characters required", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            editTextPassword.setText("");
            return false;
        }
        else if(!isPasswordPatternFollowed(password))
        {
            Toast.makeText(getApplicationContext(), "Enter valid password", Toast.LENGTH_SHORT).show(); //enter valid password with one number, one special character
            editTextPassword.requestFocus();
            editTextPassword.setText("");
            return false;
        }
        return true;
    }

    private boolean isPasswordPatternFollowed(String password)
    {
        //"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}"

        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
        if(password.matches(pattern))
            return true;
        else
            return false;
    }

    private boolean isConfirmedPasswordMatched(String confirmPassword)
    {
        if(confirmPassword.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "field cannot be empty", Toast.LENGTH_SHORT).show(); //enter valid password with one number, one special character
            editTextConfirmPassword.requestFocus();
            return false;
        }
        else if(confirmPassword.equals(editTextPassword.getText().toString()))
        {
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "password does not matched", Toast.LENGTH_SHORT).show(); //enter valid password with one number, one special character
            editTextConfirmPassword.requestFocus();
            editTextConfirmPassword.setText("");
            return false;
        }

    }

    private void readDataAndPrint()
    {
        DatabaseReference users = myRef.child("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Person person = singleSnapshot.getValue(Person.class);
                    Log.i("emailId",person.getEmailId());
                    Log.i("username",person.getUserName());
                    Log.i("password",person.getPassword());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
