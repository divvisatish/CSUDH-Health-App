package com.divvisatish.csudhhealthapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by divvi on 11/21/2017.
 */

public class myFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("cs-581");
        dbRef.child("Token").setValue(refreshToken);
    }
}
