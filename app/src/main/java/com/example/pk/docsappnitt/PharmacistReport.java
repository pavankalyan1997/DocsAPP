package com.example.pk.docsappnitt;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ReportFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class PharmacistReport extends AppCompatActivity {
    EditText DocName,PatientName,Age,Address,MobileNumber,Problem,Remarks,BloodGroup;
    TextView Gender,Medicines,YesOrNo;
    FloatingActionButton FABButton;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    private TextView mName;
    String name;
    Uri photoUrl;
    ImageView mPic;

    ArrayList<String> MedicineArray=new ArrayList<>();
    HashMap<String,ArrayList<String>> MedicineHashMap;
    SpinnerDialog spinnerDialogMedicines;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_report);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        DocName=findViewById(R.id.txtDocName);
        PatientName=findViewById(R.id.txtPatientName);
        Age=findViewById(R.id.txtAge);
        Address=findViewById(R.id.txtAddress);
        MobileNumber=findViewById(R.id.txtMobileNumber);
        Problem=findViewById(R.id.txtProblem);
        Remarks=findViewById(R.id.txtRemarks);
        BloodGroup=findViewById(R.id.txtBloodGroup);
        Gender=findViewById(R.id.txtGender);
        Medicines=findViewById(R.id.txtSelectedMedicines);
        YesOrNo=findViewById(R.id.txtYesOrNo);
        FABButton=findViewById(R.id.FABButton);
        MedicineHashMap=(HashMap<String, ArrayList<String>>) getIntent().getSerializableExtra("MedicinesKey");
        Set<String> keys = MedicineHashMap.keySet();
        for(String key:keys){
            MedicineArray.add(key);
        }

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutPharmacistReport);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewPharmacistReport);

        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edtHome:
                        Home();
                        break;

                    case R.id.Logout:
                        Logout();
                        break;
                }
                return false;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String DocIdvalue=getIntent().getStringExtra("DocIdKey");
        final String DoctorNameValue=getIntent().getStringExtra("DoctorNamekey");
        final String PatientNameValue=getIntent().getStringExtra("PatientNameKey");
        final String MobileValue=getIntent().getStringExtra("MobileKey");
        final String GenderValue=getIntent().getStringExtra("GenderKey");
        final String AgeValue=getIntent().getStringExtra("AgeKey");
        final String AddressValue=getIntent().getStringExtra("AddressKey");
        final String BloodGroupValue=getIntent().getStringExtra("BloodGroupKey");
        final String ProblemValue=getIntent().getStringExtra("ProblemKey");

        DocName.setText(DoctorNameValue);
        PatientName.setText(PatientNameValue);
        MobileNumber.setText(MobileValue);
        Gender.setText(GenderValue);
        Age.setText(AgeValue);
        Address.setText(AddressValue);
        BloodGroup.setText(BloodGroupValue);
        Problem.setText(ProblemValue);

        spinnerDialogMedicines=new SpinnerDialog(PharmacistReport.this,MedicineArray,"Medicines");
        spinnerDialogMedicines.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                if(!s.equals("No Medicines were prescribed")){
                    ShowDialog(s);
                }
            }
        });
        Medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogMedicines.showSpinerDialog();

            }
        });

        FABButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder=new AlertDialog.Builder(PharmacistReport.this);
                a_builder.setMessage("Do you want to send the message to "+DoctorNameValue)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final FirebaseUser user=mAuth.getCurrentUser();
                                final MessageToPharmacist msg=new MessageToPharmacist();
                                msg.setPtName(PatientNameValue);
                                msg.setPtGender(GenderValue);
                                msg.setPtAge(AgeValue);
                                msg.setPtAddress(AddressValue);
                                msg.setPtMobile(MobileValue);
                                msg.setPtBloodGroup(BloodGroupValue);
                                msg.setPtProblem(ProblemValue);
                                msg.setPharmaName(user.getDisplayName());
                                msg.setMedicineArrayHashMap(MedicineHashMap);
                                msg.setDoctorName(DoctorNameValue);
                                msg.setDocId(DocIdvalue);
                                msg.setRemarks(Remarks.getText().toString());
                                msg.setSubject("Regarding Pharmacy Medicines");
                                Date todaysDate = new Date();
                                DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String str = df.format(todaysDate);
                                msg.setDate(str);
                                DateFormat time=new SimpleDateFormat("HH:mm:ss");
                                str=time.format(todaysDate);
                                msg.setTime(str);

                                databaseReference.child("users").child(DocIdvalue).child("DoctorInBox").child("Pharmacist").push().setValue(msg);
                                databaseReference.child("users").child(user.getUid()).child("PharmacistSentBox").child("Doctor").push().setValue(msg);
                                Toast.makeText(PharmacistReport.this, "Message Sent to "+DoctorNameValue, Toast.LENGTH_SHORT).show();



                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert=a_builder.create();
                alert.setTitle("Please check your details Once again and Click Yes");
                alert.show();

            }
        });



    }
    private void Home(){
        startActivity(new Intent(this,Home.class));
    }
    private void Logout(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,StartingPage.class));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_profile,menu);
        return true;
    }

    private void ShowDialog(String s){
        final String MedicineName=s;
        AlertDialog.Builder sub_builder=new AlertDialog.Builder(PharmacistReport.this);
        LayoutInflater inflater=this.getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.dosage_and_usage,null);
        final EditText Dosage=dialogView.findViewById(R.id.txtDosage);
        final EditText Usage=dialogView.findViewById(R.id.txtUsage);
        final TextView Composition=dialogView.findViewById(R.id.txtComposition);
        final TextView Name=dialogView.findViewById(R.id.txtMedicineName);
        Name.setText(MedicineName);

        if(MedicineHashMap.containsKey(MedicineName)) {
            ArrayList<String>Mc = MedicineHashMap.get(MedicineName);
            if (!Mc.get(0).isEmpty()) {
                Composition.setText(Mc.get(0));
            }
            if (!Mc.get(1).isEmpty()) {
                Dosage.setText(Mc.get(1));
            }
            if (!Mc.get(2).isEmpty()) {
                Usage.setText(Mc.get(2));
            }
        }
        sub_builder.setView(dialogView);
        sub_builder.setNegativeButton("cancel",null);
        AlertDialog alert1=sub_builder.create();
        alert1.setTitle("Please Specify Dosage and Usage for the medicine!");
        alert1.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Toggle.onOptionsItemSelected(item)){
            return true;
        }
        switch(item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,StartingPage.class));
                break;
            case R.id.Home:
                startActivity(new Intent(this,Home.class));
                break;
            case R.id.AdminOptions:
                startActivity(new Intent(this,LoginAdmin.class));
                break;
        }
        return true;
    }
    private void getCurrentinfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                try {
                    name = profile.getDisplayName();
                    photoUrl = profile.getPhotoUrl();
                    if (!name.isEmpty()) {
                        mName.setText(name);
                    }

                    String url = profile.getPhotoUrl().toString();
                    if (!url.isEmpty()) {
                        AQuery aq = new AQuery(this);
                        aq.id(mPic).image(url);
                    }
                }catch (Exception e){

                }
            };
        }
    }
}
