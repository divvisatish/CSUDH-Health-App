package com.csudh.healthapp.csudhhealthapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

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
    Calendar myCalendar = Calendar.getInstance();
    boolean newUserRegistered=false;
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

        addListenerOnRegisterNewUserButton();
        addListenerOnBirthdateEditText();
        addFocusListnerOnBirthdateEditText();
    }

    public void addListenerOnRegisterNewUserButton() {
        buttonRegisterNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(isRegistrationDetailsValid()) {

                }

            }
        });

    }

    private boolean isRegistrationDetailsValid()
    {
        boolean flag=true;
        String alertMessage = "";

        try {
            if (isEmailValid(editTextEmail.getText().toString())) {
                if (isPasswordValid(editTextPassword.getText().toString())) {
                    if (isConfirmedPasswordMatched(editTextConfirmPassword.getText().toString())) {
                        if(isFirstNameValid(editTextFirstName.getText().toString())) {
                            if(isLastNameValid(editTextLastName.getText().toString())) {
                                if(isBirthDateValid(editTextBirthDate.getText().toString())) {
                                    if (isBloodTypeValid()) {
                                        if (registerNewUser()) {
                                            return true;
                                        }
                                        else {
                                            return false;
                                        }
                                    }
                                    else {
                                        return false;
                                    }
                                }
                                else{
                                    return false;
                                }
                            }
                            else {
                                return false;
                            }
                        }
                        else {
                            return false;
                        }
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
        if(flag) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            newUserRegistered=true;
        }
        else
        {
            newUserRegistered=false;
        }
    }

    private boolean isEmailValid(String email)
    {
        String alertMessage = "";
        if(TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email can not be empty");
            editTextEmail.requestFocus();
            return false;
        }
        else if(!email.contains("@"))
        {
            editTextEmail.setError("Enter valid e-mail Id");
            editTextEmail.requestFocus();
            return false;
        }
        else if(!email.contains("csudh"))
        {
            editTextEmail.setError("Enter valid csudh email id");
            editTextEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password)
    {
        String alertMessage = "";
        if(TextUtils.isEmpty(password)) {
            editTextPassword.setError("");
            editTextPassword.requestFocus();
            return false;
        }
        else if(password.length()<8)
        {
            editTextPassword.setError("");
            editTextPassword.requestFocus();
            editTextPassword.setText("");
            return false;
        }
        else if(!isPasswordPatternFollowed(password))
        {
            editTextPassword.setError("");
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
            editTextConfirmPassword.setError("");
            editTextConfirmPassword.requestFocus();
            return false;
        }
        else if(confirmPassword.equals(editTextPassword.getText().toString()))
        {
            return true;
        }
        else
        {
            editTextConfirmPassword.setError("");
            editTextConfirmPassword.requestFocus();
            editTextConfirmPassword.setText("");
            return false;
        }

    }

    private boolean isFirstNameValid(String firstName)
    {
        //"^[a-zA-Z0-9_]*$"
        String pattern = "^[a-zA-Z0-9_]+$";
        if(firstName.matches(pattern)) {
            return true;
        }
        else {
            editTextFirstName.setError("");
            editTextFirstName.requestFocus();
            return false;
        }
    }

    private boolean isLastNameValid(String lastName)
    {
        String pattern = "^[a-zA-Z0-9_]+$";
        if(lastName.matches(pattern)) {
            return true;
        }
        else {
            editTextLastName.setError("");
            editTextLastName.requestFocus();
            return false;
        }
    }

    private boolean isBirthDateValid(String birthday)
    {
        if(!birthday.isEmpty())
        {
            String[] birthdayStr = birthday.split("/");

            Calendar userAge = new GregorianCalendar(Integer.parseInt(birthdayStr[2]),Integer.parseInt(birthdayStr[0]),Integer.parseInt(birthdayStr[1]));
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            if (minAdultAge.before(userAge)) {
                editTextBirthDate.setError("Minimum 18 required");
                return false;
            }
            else {
                editTextBirthDate.setError(null);
                return true;
            }
        }
        else{

            editTextBirthDate.setError("birthday cannot be empty");
            return false;
        }
    }

    private boolean isBloodTypeValid()
    {
        if(spinnerBloodType.getSelectedItemId() > 0)
        {
            return true;
        }
        else {
            Toast.makeText(getApplicationContext(), "please select valid blood type", Toast.LENGTH_SHORT).show(); //enter valid password with one number, one special character
            spinnerBloodType.requestFocus();
            return false;
        }
    }

    private boolean registerNewUser()
    {
        DatabaseReference users = myRef.child("users");

        Person person = new Person();
        person.setEmailId(editTextEmail.getText().toString());
        person.setFirstName(editTextFirstName.getText().toString());
        person.setLastName(editTextLastName.getText().toString());
        person.setPassword(editTextPassword.getText().toString());
        person.setUserName(editTextEmail.getText().toString());
        person.setBloodTypeName(spinnerBloodType.getSelectedItem().toString());//TODO add code for spinner
        person.setBloodTypeId(Integer.valueOf(String.valueOf(spinnerBloodType.getSelectedItemId())));

        String key = users.push().getKey();
        person.setPersonId(key);
        users.child(key).setValue(person);

        createAccount(person.getEmailId(),person.getPassword());
        if(newUserRegistered)
        {
            return true;
        }
        else {
            return false;
        }

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    public void addListenerOnBirthdateEditText()
    {
        editTextBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());

            }
        });
    };

    public void addFocusListnerOnBirthdateEditText()
    {
        editTextBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                    datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextBirthDate.setText(sdf.format(myCalendar.getTime()));
    }


    /*private void readDataAndPrint()
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
    }*/

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
    }

    private void createAccount(String email, String password) {
        final String TAG = "EmailPassword";
        Log.d(TAG, "createAccount:" + email);

        // [START create_user_with_email]
        auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Successful",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END create_user_with_email]
    }
}
