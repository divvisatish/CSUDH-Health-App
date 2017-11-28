package com.csudh.healthapp.csudhhealthapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.csudh.healthapp.csudhhealthapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Darshit on 11/6/2017.
 */

public class NotificationActivity extends AppCompatActivity {

    Button buttonRequest,buttonCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        addListenerOnRequestButton();
        addListenerOnCancelButton();
    }

    public void addListenerOnRequestButton() {

        final Context context = this;
        buttonRequest = (Button) findViewById(R.id.buttonRequest);
        buttonRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("csudh-health-app-8ffe6");
                dbRef.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                Intent intent = new Intent(context, HomepageActivity.class);
                startActivity(intent);
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
}
