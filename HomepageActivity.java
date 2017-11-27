package com.csudh.healthapp.csudhhealthapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csudh.healthapp.csudhhealthapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by Darshit on 11/6/2017.
 */

public class HomepageActivity extends AppCompatActivity {

    Button buttonBloodRequired;
    RadioGroup radioGroupRequestType;
    private FirebaseAuth auth;
    DatabaseReference root, users;
    private TextView textViewWelcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        radioGroupRequestType = (RadioGroup)findViewById(R.id.radioGroupRequestType);
        textViewWelcomeMessage = (TextView) findViewById(R.id.textViewWelcomeMessage);
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
                                textViewWelcomeMessage.setText("Hope you are doing well "+snapshot.getValue(Person.class).getFirstName()+"");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
            }
        }


        addListenerOnBloodRequiredButton();

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

    public void addListenerOnBloodRequiredButton() {

        final boolean flag=true;
        buttonBloodRequired = (Button) findViewById(R.id.buttonBloodRequired);

        buttonBloodRequired.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRequestTypeValid()) {
                    Intent intent = new Intent(getApplicationContext(), com.csudh.healthapp.csudhhealthapp.NotificationActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    private boolean isRequestTypeValid()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("@string/requestTypeAlert");
        //builder.setMessage("Please select one Option");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();

        int checkedRadioButtonId = radioGroupRequestType.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            // No item selected
            dialog.show();
            dialog.getWindow().setLayout(800,350);
            /*TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            Button button = (Button) dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setGravity(Gravity.CENTER);*/
            return false;
        }
        else{
            if (checkedRadioButtonId == R.id.radioButtonVeryUrgent) {
                // Do something with the button

            }
            else if(checkedRadioButtonId == R.id.radioButtonImmediate)
            {

            }
            else if(checkedRadioButtonId == R.id.radioButtonImmediate)
            {

            }
            //dialog.show();
            return true;
        }
    }

}
