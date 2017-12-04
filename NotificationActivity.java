package com.csudh.healthapp.csudhhealthapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.csudh.healthapp.csudhhealthapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Darshit on 11/6/2017.
 */

public class NotificationActivity extends AppCompatActivity {

    Button buttonSend,buttonCancel;
    Spinner spinnerBloodTypeNotification;
    EditText editTextComments;
    Person loggedInPerson = null;
    private FirebaseAuth auth;
    int bloodTypeAPlus = 1;
    int bloodTypeBPlus = 3;
    int bloodTypeOPlus = 7;
    int bloodTypeABPlus = 5;
    int bloodTypeAMinus = 2;
    int bloodTypeBMinus = 4;
    int bloodTypeOMinus = 8;
    int bloodTypeABMinus = 6;
    int bloodTypeHH = 9;
    private DatabaseReference root,users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        auth = FirebaseAuth.getInstance();
        if(auth!=null)
        {
            FirebaseUser firebaseUser = auth.getCurrentUser();

            if(firebaseUser!=null)
            {
                root = FirebaseDatabase.getInstance().getReference();
                users = root.child("users");

                users.child(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                loggedInPerson = snapshot.getValue(Person.class);
                                Toast.makeText(getApplicationContext(), "Hi, " + loggedInPerson.getFirstName(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
            }
        }
        editTextComments = (EditText) findViewById(R.id.editTextComments);
        spinnerBloodTypeNotification = (Spinner) findViewById(R.id.spinnerBloodTypeNotification);

        addListenerOnSendButton();
        addListenerOnCancelButton();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        if(auth!=null) {
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(), com.csudh.healthapp.csudhhealthapp.LogInActivity.class);
            startActivity(intent);
        }
    }

    public void addListenerOnSendButton() {

        final Context context = this;
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(isNotificationDetailsValid()) {
                    NotificationVO notificationVO = prepareNotificationVO();
                    Toast.makeText(getApplicationContext(), "Notification VO, " +  notificationVO.getBloodTypeName(),
                            Toast.LENGTH_SHORT).show();
                    if(notificationVO!=null && loggedInPerson!=null)
                    {
                        NotificationVO[] currentNotificationVO = new NotificationVO[1];
                        currentNotificationVO[0] = new NotificationVO();
                        currentNotificationVO[0] = notificationVO;
                        loggedInPerson.setNotificationVOArr(currentNotificationVO);

                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("csudh-health-app");
                        dbRef.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                    }

                    Intent intent = new Intent(context, HomepageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void addListenerOnCancelButton() {

        final Context context = this;
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, HomepageActivity.class);
                startActivity(intent);
            }
        });
    }

    private NotificationVO prepareNotificationVO()
    {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setBloodTypeId(Integer.parseInt((String.valueOf(spinnerBloodTypeNotification.getSelectedItemId()))));
        notificationVO.setComments(editTextComments.getText().toString());
        notificationVO.setBloodTypeName(spinnerBloodTypeNotification.getSelectedItem().toString());
        notificationVO.setActiveFlag(1);
        notificationVO.setCrtDate(DateFormat.getDateTimeInstance().format(new Date()));

        return notificationVO;
    }

    private boolean isNotificationDetailsValid()
    {
        if(isBloodTypeValid())
        {
            return true;
        }
        else
            return false;
    }

    private boolean isBloodTypeValid()
    {
        if(spinnerBloodTypeNotification.getSelectedItemId() > 0)
        {
            return true;
        }
        else {
            Toast.makeText(getApplicationContext(), "Please select valid Blood Type", Toast.LENGTH_SHORT).show(); //enter valid password with one number, one special character
            spinnerBloodTypeNotification.requestFocus();
            return false;
        }
    }

    private boolean isCommentsValid(String lastName)
    {
        String pattern = "^[a-zA-Z0-9_]+$";
        if(lastName.matches(pattern)) {
            return true;
        }
        else {
            editTextComments.setError("Please enter valid Last Name");
            editTextComments.requestFocus();
            return false;
        }
    }

    private ArrayList<Integer> getCompatibleBloodType()
    {
        ArrayList<Integer> compatibleBloodTypeList = new ArrayList<Integer>();

        int selectedId = Integer.parseInt(String.valueOf(spinnerBloodTypeNotification.getSelectedItemId()));

        switch (selectedId) {
            case 1: //A+
            {
                compatibleBloodTypeList.add(bloodTypeAPlus);
                compatibleBloodTypeList.add(bloodTypeAMinus);
                compatibleBloodTypeList.add(bloodTypeOPlus);
                compatibleBloodTypeList.add(bloodTypeOMinus);
                break;
            }
            case 2: //A-
            {
                compatibleBloodTypeList.add(bloodTypeAMinus);
                compatibleBloodTypeList.add(bloodTypeOMinus);
                break;
            }
            case 3:  //B+
            {
                compatibleBloodTypeList.add(bloodTypeBMinus);
                compatibleBloodTypeList.add(bloodTypeOMinus);
                compatibleBloodTypeList.add(bloodTypeBPlus);
                compatibleBloodTypeList.add(bloodTypeOPlus);
                break;
            }
            case 4: //B-
            {
                compatibleBloodTypeList.add(bloodTypeBMinus);
                compatibleBloodTypeList.add(bloodTypeOMinus);
                break;
            }
            case 5: //AB+
            {
                compatibleBloodTypeList.add(bloodTypeAPlus);
                compatibleBloodTypeList.add(bloodTypeAMinus);
                compatibleBloodTypeList.add(bloodTypeOPlus);
                compatibleBloodTypeList.add(bloodTypeOMinus);
                compatibleBloodTypeList.add(bloodTypeBPlus);
                compatibleBloodTypeList.add(bloodTypeBMinus);
                compatibleBloodTypeList.add(bloodTypeABPlus);
                compatibleBloodTypeList.add(bloodTypeABMinus);
                break;
            }
            case 6: //AB-
            {
                compatibleBloodTypeList.add(bloodTypeAMinus);
                compatibleBloodTypeList.add(bloodTypeOMinus);
                compatibleBloodTypeList.add(bloodTypeBMinus);
                compatibleBloodTypeList.add(bloodTypeABMinus);
                break;
            }
            case 7: //O+
            {
                compatibleBloodTypeList.add(bloodTypeOPlus);
                compatibleBloodTypeList.add(bloodTypeOMinus);
                break;
            }
            case 8: //O-
            {
                compatibleBloodTypeList.add(bloodTypeOMinus);
                break;
            }
            case 9: //H/H
            {
                compatibleBloodTypeList.add(bloodTypeHH);
                break;
            }
            default: {
                break;
            }
        }

        return compatibleBloodTypeList;
    }
}
