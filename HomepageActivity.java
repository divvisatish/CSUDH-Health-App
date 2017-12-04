package com.csudh.healthapp.csudhhealthapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.csudh.healthapp.csudhhealthapp.R;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Darshit on 11/6/2017.
 */

public class HomepageActivity extends AppCompatActivity {

    Button buttonBloodRequired;
    RadioGroup radioGroupRequestType;
    private FirebaseAuth auth;
    DatabaseReference root, users;
    private TextView textViewWelcomeMessage;
    int bloodTypeAPlus = 1;
    int bloodTypeAMinus = 2;
    int bloodTypeBPlus = 3;
    int bloodTypeBMinus = 4;
    int bloodTypeABPlus = 5;
    int bloodTypeABMinus = 6;
    int bloodTypeOPlus = 7;
    int bloodTypeOMinus = 8;
    int bloodTypeHH = 9;

    //for chart
    PieChart pieChart;
    ArrayList<Entry> entries;
    ArrayList<String> pieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;
    int countAPlus = 0;
    int countBPlus = 0;
    int countABPlus = 0;
    int countOPlus = 0;
    int countAMinus = 0;
    int countBMinus = 0;
    int countABMinus = 0;
    int countOMinus = 0;
    int countHH = 0;
    int countTotal=0;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        radioGroupRequestType = (RadioGroup)findViewById(R.id.radioGroupRequestType);
        textViewWelcomeMessage = (TextView) findViewById(R.id.textViewWelcomeMessage);
        pieChart = (PieChart) findViewById(R.id.chart1);
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
                                textViewWelcomeMessage.setText("Hi "+snapshot.getValue(Person.class).getFirstName()+"");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                collectPhoneNumbers((Map<String,Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        addListenerOnBloodRequiredButton();
    }

    private void collectPhoneNumbers(Map<String,Object> users) {

        resetCount();

        for (Map.Entry<String, Object> entry : users.entrySet()){
            Map singleUser = (Map) entry.getValue();
            if(singleUser.get("bloodTypeId")!=null && !singleUser.get("bloodTypeId").toString().isEmpty()) {
                countTotal++;
                if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeAMinus) {
                    countAMinus++;
                } else if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeAPlus) {
                    countAPlus++;
                } else if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeBMinus) {
                    countBMinus++;
                } else if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeBPlus) {
                    countBPlus++;
                } else if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeABMinus) {
                    countABMinus++;
                } else if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeABPlus) {
                    countABPlus++;
                } else if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeOMinus) {
                    countOMinus++;
                } else if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeOPlus) {
                    countOPlus++;
                } else if (Integer.parseInt(singleUser.get("bloodTypeId").toString()) == bloodTypeHH) {
                    countHH++;
                }
            }
        }

        if(countTotal>0)
        {
            populatePieChart();
        }
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
        builder.setTitle("Please select request type");
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

    private void resetCount()
    {
        pieChart.setData(null);
        entries = new ArrayList<>();
        pieEntryLabels = new ArrayList<>();
        countAPlus = 0;
        countBPlus = 0;
        countABPlus = 0;
        countOPlus = 0;
        countAMinus = 0;
        countBMinus = 0;
        countABMinus = 0;
        countOMinus = 0;
        countHH = 0;
        countTotal=0;
    }

    private void populatePieChart(){
        entries = new ArrayList<>();
        pieEntryLabels = new ArrayList<String>();
        int countIndex=0;

        if(countAPlus>0) {
            entries.add(new BarEntry(countAPlus, countIndex));
            pieEntryLabels.add("A+");
        }
        if(countAMinus>0) {
            entries.add(new BarEntry(countAMinus, countIndex++));
            pieEntryLabels.add("A-");
        }
        if(countBPlus>0) {
            entries.add(new BarEntry(countBPlus, countIndex++));
            pieEntryLabels.add("B+");
        }
        if(countBMinus>0) {
            entries.add(new BarEntry(countBMinus, countIndex++));
            pieEntryLabels.add("B-");
        }
        if(countABPlus>0) {
            entries.add(new BarEntry(countABPlus, countIndex++));
            pieEntryLabels.add("AB+");
        }
        if(countABMinus>0) {
            entries.add(new BarEntry(countABMinus, countIndex++));
            pieEntryLabels.add("AB-");
        }
        if(countOPlus>0) {
            entries.add(new BarEntry(countOPlus, countIndex++));
            pieEntryLabels.add("O+");
        }
        if(countOMinus>0) {
            entries.add(new BarEntry(countOMinus, countIndex++));
            pieEntryLabels.add("O-");
        }
        if(countHH>0) {
            entries.add(new BarEntry(countHH, countIndex++));
            pieEntryLabels.add("H/H");
        }

        if(!entries.isEmpty()) {
            pieDataSet = new PieDataSet(entries, "");
            pieData = new PieData(pieEntryLabels, pieDataSet);
            pieData.setValueTextColor(Color.WHITE);
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieDataSet.setValueTextSize(20f);
            pieData.setValueFormatter(new ValueFormatterInt());
            pieChart.setData(pieData);

            Paint p = pieChart.getPaint(pieChart.PAINT_DESCRIPTION);
            p.setColor(Color.WHITE);
            pieChart.setDescription("");
            Legend legend = pieChart.getLegend();
            legend.setEnabled(false);
            legend.setTextColor(Color.WHITE);
            //pieChart.animateY(3000);
        }

    }
}
