package com.csudh.healthapp.csudhhealthapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    String requestTypeName;
    String notificationKeys;
    int requestTypeId = -1;
    DatabaseReference myRef;
    String sendUserId="CQDoH870EbOjqCzZZj6QK1VBQR42";
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        TextView textView = (TextView) findViewById(R.id.textViewRequestType);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.logo); //Converting drawable into bitmap
        ResizeBitmapImage resizeBitmapImage = new ResizeBitmapImage();
        Bitmap new_icon = resizeBitmapImage.resizeBitmapImageFn(icon, 150); //resizing the bitmap
        Drawable d = new BitmapDrawable(getResources(),new_icon); //Converting bitmap into drawable

        Bundle bundle = getIntent().getExtras();
        requestTypeId = bundle.getInt("requestTypeId");
        requestTypeName =  bundle.getString("requestTypeName"); //getIntent().getStringExtra("requestTypeName");

        textView.setText(" Request Type: "+ requestTypeName);

        getSupportActionBar().setLogo(d);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("");

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
                                notificationKeys = loggedInPerson.getNotificationKeys();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(Person.class).getBloodTypeId()==8)
                        sendUserId = dataSnapshot.getValue(Person.class).getUid();
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
        confirmLogOut();
    }

    public void addListenerOnSendButton() {

        final Context context = this;
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(isNotificationDetailsValid()) {
                    mProgress.show();
                    NotificationVO notificationVO = prepareNotificationVO();
                    if(notificationVO!=null && loggedInPerson!=null)
                    {
                        final ArrayList<Integer> compatibleDataType = getCompatibleBloodType();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        myRef = database.getReference();
                        DatabaseReference notificationDetails = myRef.child("notificationDetails").child(auth.getCurrentUser().getUid());
                        String key = notificationDetails.push().getKey();
                        notificationDetails.child(key).setValue(notificationVO);

                        if(notificationKeys!=null){
                            notificationKeys = notificationKeys + "," + key;

                            myRef.child("users").child(auth.getCurrentUser().getUid()).child("notificationKeys").setValue(notificationKeys);
                            Toast.makeText(NotificationActivity.this, "Request has been sent to desired users", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mProgress.dismiss();
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
        if(!editTextComments.getText().toString().isEmpty()) {
            notificationVO.setComments(editTextComments.getText().toString());
        }
        else {
            notificationVO.setComments("-");
        }
        notificationVO.setBloodTypeName(spinnerBloodTypeNotification.getSelectedItem().toString());
        notificationVO.setActiveFlag(1);
        String currentDateAndTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
        notificationVO.setCrtDate(currentDateAndTime);
        notificationVO.setRequestTypeId(requestTypeId);
        notificationVO.setRequestTypeName(requestTypeName);
        notificationVO.setToSendUid(sendUserId);

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

    private void confirmLogOut() {


        final AlertDialog alertDialog = new AlertDialog.Builder(NotificationActivity.this, R.style.alertDialog).create();

        alertDialog.setMessage("Are you sure you want to sign out?");
        alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.setButton(alertDialog.BUTTON_NEGATIVE,"Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(auth!=null) {
                    auth.signOut();
                    Intent intent = new Intent(getApplicationContext(), com.csudh.healthapp.csudhhealthapp.LogInActivity.class);
                    startActivity(intent);
                    onBackPressed();
                    finish();
                }
            }
        });
        alertDialog.show();
        alertDialog.getWindow().setLayout(880,375);
        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams positiveButton1 = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButton1.weight = 10;
        positiveButton.setLayoutParams(positiveButton1);
        negativeButton.setLayoutParams(positiveButton1);
    }
}
