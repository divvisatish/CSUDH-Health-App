package com.csudh.healthapp.csudhhealthapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private String newUserUid = "";
    //DatabaseReference myChild = myRef.child("message");


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetScreen();
    }

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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        resetScreen();
        auth = FirebaseAuth.getInstance();

        addListenerOnRegisterNewUserButton();
        addListenerOnBirthdateEditText();
        addFocusListnerOnBirthdateEditText();
    }

    public void addListenerOnRegisterNewUserButton() {
        final Context context = this;
        buttonRegisterNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                newUserRegistered = false;
                newUserUid="";
                if(isRegistrationDetailsValid()) {

                }

            }
        });

    }

    private boolean isRegistrationDetailsValid()
    {
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
            return false;
        }
    }

    private void updateUI(boolean flag, String uid)
    {
        final Context context = this;
        if(flag && !uid.isEmpty()) {
            newUserRegistered=true;
            newUserUid = uid;

            if(addUserInDatabase())
            {
                auth.signOut();
                Intent intent = new Intent(context, LogInActivity.class);
                startActivity(intent);
            }


        }
        else
        {
            newUserRegistered=false;
            newUserUid="";
        }
    }

    private boolean isEmailValid(String email)
    {
        String alertMessage = "";
        if(TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email-ID cannot be empty");
            editTextEmail.requestFocus();
            return false;
        }
        else if(!email.contains("@"))
        {
            editTextEmail.setError("Please enter valid Email-ID");
            editTextEmail.requestFocus();
            return false;
        }
        else if(!email.contains("csudh") || !email.toLowerCase().contains(".edu"))
        {
            editTextEmail.setError("Please enter valid csudh Email-ID");
            editTextEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password)
    {
        String alertMessage = "";
        if(TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password cannot be empty");
            editTextPassword.requestFocus();
            return false;
        }
        else if(password.length()<8)
        {
            editTextPassword.setError("Minimum length 8 is required");
            editTextPassword.requestFocus();
            editTextPassword.setText("");
            return false;
        }
        else if(!isPasswordPatternFollowed(password))
        {
            editTextPassword.setError("Invalid Password. Password must contain one upper-case, one lower-case, one numeric, and one special character");
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
            editTextConfirmPassword.setError("Confirm Password cannot be empty");
            editTextConfirmPassword.requestFocus();
            return false;
        }
        else if(confirmPassword.equals(editTextPassword.getText().toString()))
        {
            return true;
        }
        else
        {
            editTextConfirmPassword.setError("Password doesn't match");
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
            editTextFirstName.setError("Please enter valid First Name");
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
            editTextLastName.setError("Please enter valid Last Name");
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
                editTextBirthDate.setError("Minimum age 18 is required");
                return false;
            }
            else {
                editTextBirthDate.setError(null);
                return true;
            }
        }
        else{

            editTextBirthDate.setError("Birthday cannot be empty");
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
            Toast.makeText(getApplicationContext(), "Please select valid Blood Type", Toast.LENGTH_SHORT).show(); //enter valid password with one number, one special character
            spinnerBloodType.requestFocus();
            return false;
        }
    }

    private boolean registerNewUser()
    {
        String emailId = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        createAccount(emailId, password);

        if(newUserRegistered && !newUserUid.isEmpty()) {
            return true;
        }
        else {
            return false;
        }

    }

    private boolean addUserInDatabase()
    {
        if (newUserRegistered && !newUserUid.isEmpty()) {
            Person person = new Person();
            person.setUid(newUserUid);
            person.setEmailId(editTextEmail.getText().toString());
            person.setFirstName(editTextFirstName.getText().toString());
            person.setLastName(editTextLastName.getText().toString());
            person.setUserName(editTextEmail.getText().toString());
            person.setBloodTypeName(spinnerBloodType.getSelectedItem().toString());
            person.setBloodTypeId(Integer.valueOf(String.valueOf(spinnerBloodType.getSelectedItemId())));
            person.setDateOfBirth(editTextBirthDate.getText().toString());
            person.setTokenId("");

            String currentDateAndTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
            person.setCrtDate(currentDateAndTime);
            person.setActiveFlag(1);

            DatabaseReference users = myRef.child("users");
            String key = users.push().getKey();
            person.setPersonId(key);
            users.child(newUserUid).setValue(person);
            return true;
        } else {
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
                            Toast.makeText(getApplicationContext(), "Registration Successful",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(true, user.getUid());
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Log.w(TAG, "createUserWithEmail: user already exist", task.getException());
                                Toast.makeText(getApplicationContext(), "User already exist",
                                        Toast.LENGTH_SHORT).show();
                                newUserUid="";
                            }
                            else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Registration failed",
                                        Toast.LENGTH_SHORT).show();
                                newUserUid="";
                            }
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void resetScreen()
    {
        editTextBirthDate.setText("");
        editTextPassword.setText("");
        editTextEmail.setText("");
        editTextConfirmPassword.setText("");
        editTextFirstName.setText("");
        editTextLastName.setText("");
        spinnerBloodType.setSelection(0);
    }

}
