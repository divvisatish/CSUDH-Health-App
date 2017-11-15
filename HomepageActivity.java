package com.csudh.healthapp.csudhhealthapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.csudh.healthapp.csudhhealthapp.R;

/**
 * Created by Darshit on 11/6/2017.
 */

public class HomepageActivity extends AppCompatActivity {

    Button buttonBloodRequired;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        addListenerOnBloodRequiredButton();

    }

    public void addListenerOnBloodRequiredButton() {

        final Context context = this;
        buttonBloodRequired = (Button) findViewById(R.id.buttonBloodRequired);
        buttonBloodRequired.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, com.csudh.healthapp.csudhhealthapp.NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

}
