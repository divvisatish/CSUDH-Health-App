package com.csudh.healthapp.csudhhealthapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.csudh.healthapp.csudhhealthapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class AppMainActivity extends AppCompatActivity {

    Button buttonGetStarted;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main);

        addListenerOnButton();
        auth.getInstance().signOut();

    }

    /*@Override
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
        if(auth!=null)
        auth.signOut();
    }*/

    public void addListenerOnButton() {

        buttonGetStarted = (Button) findViewById(R.id.buttonGetStarted);
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(getApplicationContext(), com.csudh.healthapp.csudhhealthapp.LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}
