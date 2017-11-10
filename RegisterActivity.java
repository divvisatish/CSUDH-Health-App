package com.csudh.healthapp.csudhhealthapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.csudh.healthapp.csudhhealthapp.HomepageActivity;
import com.csudh.healthapp.csudhhealthapp.R;

/**
 * Created by Darshit on 11/6/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    Button buttonRegisterNewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        addListenerOnLoginButton();
    }

    public void addListenerOnLoginButton() {

        final Context context = this;
        buttonRegisterNewUser = (Button) findViewById(R.id.buttonRegisterNewUser);
        buttonRegisterNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, HomepageActivity.class);
                startActivity(intent);
            }
        });
    }
}
