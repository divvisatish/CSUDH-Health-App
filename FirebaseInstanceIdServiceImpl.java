package com.csudh.healthapp.csudhhealthapp;

/**
 * Created by divvi on 11/26/2017.
 */

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by divvi on 11/21/2017.
 */

public class FirebaseInstanceIdServiceImpl extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("csudh-health-app");
        dbRef.child("Token").setValue(refreshToken);
    }
}
