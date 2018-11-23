package com.example.pk.docsappnitt;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class TreatPatient extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;
    FloatingActionButton FABButton;

    private TextView mName;
    String name;
    Uri photoUrl;
    ImageView mPic;

    EditText patientName,patientAge,patientProblem,patientAddress,patientMobileNumber,Remarks;
    TextView patientGender,patientBloodGroup,diagnosticianName,diagnosticTest,selectedDiagnosticTests,removeDiagnosticTests;
    TextView PharmacistName,Medicines,Dosage,removeMedicines;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    ArrayList<String> gender=new ArrayList<>();
    ArrayList<String>BloodGroups=new ArrayList<>();
    ArrayList<String> diagnosticians=new ArrayList<>();
    ArrayList<String> diagnosticTests=new ArrayList<>();
    ArrayList<String>SelectedTest=new ArrayList<>();
    ArrayList<String>Pharmacist=new ArrayList<>();
    ArrayList<String>MedicinesArray=new ArrayList<>();
    ArrayList<String>SelectedMedicinesArray=new ArrayList<>();
    HashMap<String,MedicineClass>MedicineHashMap=new HashMap<String,MedicineClass>();
    SpinnerDialog spinnerDialogGen;
    SpinnerDialog spinnerDialogBloodGroup;
    SpinnerDialog spinnerDialogDiag;
    SpinnerDialog spinnerDialogDiagnosticTests;
    SpinnerDialog spinnerSelectedDiagnostic;
    SpinnerDialog spinnerRemoveTests;
    SpinnerDialog spinnerDialogPharmacist;
    SpinnerDialog spinnerDialogMedicines;
    SpinnerDialog spinnerDialogSelectDosage;
    SpinnerDialog spinnerDialogRemoveMedicines;
    String PtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treat_patient);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutTreatPatient);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewTreatPatient);


        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();


        patientName=findViewById(R.id.txtPatientName);
        patientAge=findViewById(R.id.txtAge);
        patientProblem=findViewById(R.id.txtProblem);
        patientAddress=(EditText) findViewById(R.id.txtAddress);
        patientMobileNumber=findViewById(R.id.txtMobileNumber);
        patientGender=findViewById(R.id.txtGender);
        patientBloodGroup=findViewById(R.id.txtBloodGroup);
        diagnosticianName=findViewById(R.id.txtDiagnosticianName);
        diagnosticTest=findViewById(R.id.txtDiagnosticTest);
        selectedDiagnosticTests=findViewById(R.id.txtSelectedDiagnosticTests);
        removeDiagnosticTests=findViewById(R.id.txtRemoveDiagnosticTests);
        Remarks=findViewById(R.id.txtRemarks);
        PharmacistName=findViewById(R.id.txtPharmacistName);
        Medicines=findViewById(R.id.txtMedicines);
        Dosage=findViewById(R.id.txtMedicineDosage);
        removeMedicines=findViewById(R.id.txtRemoveMedicines);
        FABButton=(FloatingActionButton)findViewById(R.id.FABButton);


        try{
            patientName.setText(getIntent().getStringExtra("PtNameKey"));
            patientAge.setText(getIntent().getStringExtra("AgeKey"));
            patientProblem.setText(getIntent().getStringExtra("ProblemKey"));
            patientAddress.setText(getIntent().getStringExtra("AddressKey"));
            patientMobileNumber.setText(getIntent().getStringExtra("MobileKey"));
            patientGender.setText(getIntent().getStringExtra("GenderKey"));
            patientBloodGroup.setText(getIntent().getStringExtra("BloodGroupKey"));
            PtId=getIntent().getStringExtra("PatientIdKey");
        }catch (Exception e){
            return;
        }

        initBloodGroups();
        initGender();
        initDiagnosticians();
        initDiagnosticTests();
        initMedicinesArray();
        initPharmacist();
        spinnerDialogGen=new SpinnerDialog(TreatPatient.this,gender,"Choose Patient's gender");
        spinnerDialogGen.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                patientGender.setText(s);
            }
        });

        spinnerDialogBloodGroup=new SpinnerDialog(TreatPatient.this,BloodGroups,"Choose Patient's Blood Group");
        spinnerDialogBloodGroup.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                patientBloodGroup.setText(s);
            }
        });
        spinnerDialogDiag=new SpinnerDialog(TreatPatient.this,diagnosticians,"Choose a Diagnostician");
        spinnerDialogDiag.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                diagnosticianName.setText(s);
            }
        });
        spinnerDialogPharmacist=new SpinnerDialog(TreatPatient.this,Pharmacist,"Choose a Pharmacist");
        spinnerDialogPharmacist.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                PharmacistName.setText(s);
            }
        });
        spinnerDialogMedicines=new SpinnerDialog(TreatPatient.this,MedicinesArray,"Choose Medicines");
        spinnerDialogMedicines.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                SelectedMedicinesArray.add(s);
                Toast.makeText(TreatPatient.this, s+" has been added successfully",Toast.LENGTH_SHORT).show();


            }
        });
        spinnerDialogSelectDosage=new SpinnerDialog(TreatPatient.this, SelectedMedicinesArray,"Select Dosage and Usage");
        spinnerDialogSelectDosage.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                ShowDialog(s);
            }
        });

        spinnerDialogDiagnosticTests=new SpinnerDialog(TreatPatient.this,diagnosticTests,"Choose a Diagnostic Test ");
        spinnerDialogDiagnosticTests.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //diagnosticTest.setText(s);
                SelectedTest.add(s);
                Toast.makeText(TreatPatient.this, s+" has been added successfully",Toast.LENGTH_SHORT).show();
            }
        });
        spinnerSelectedDiagnostic=new SpinnerDialog(TreatPatient.this,SelectedTest,"Here are selected Diagnostic Tests");
        spinnerSelectedDiagnostic.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

            }
        });
        spinnerRemoveTests=new SpinnerDialog(TreatPatient.this,SelectedTest,"Choose those tests that you want to remove");
        spinnerRemoveTests.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                SelectedTest.remove(s);
                Toast.makeText(TreatPatient.this, s+" has been removed successfully",Toast.LENGTH_SHORT).show();
            }
        });
        spinnerDialogRemoveMedicines=new SpinnerDialog(TreatPatient.this,SelectedMedicinesArray,"Choose those medicines that you want to remove");
        spinnerDialogRemoveMedicines.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                MedicineHashMap.remove(s);
                SelectedMedicinesArray.remove(s);

            }
        });
        patientGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogGen.showSpinerDialog();
            }
        });
        patientBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogBloodGroup.showSpinerDialog();
            }
        });
        diagnosticianName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogDiag.showSpinerDialog();
            }
        });
        diagnosticTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogDiagnosticTests.showSpinerDialog();
            }
        });
        selectedDiagnosticTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerSelectedDiagnostic.showSpinerDialog();
            }
        });
        removeDiagnosticTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerRemoveTests.showSpinerDialog();
            }
        });
        Medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogMedicines.showSpinerDialog();
            }
        });
        Dosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogSelectDosage.showSpinerDialog();
            }
        });
        PharmacistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogPharmacist.showSpinerDialog();
            }
        });
        removeMedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogRemoveMedicines.showSpinerDialog();
            }
        });

        FABButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder a_builder=new AlertDialog.Builder(TreatPatient.this);
                final String[] msgOptions={"Patient and Pharmacist","Patient and Diagnostician","Patient,Pharmacist and Diagnostician"};
                int checked_item=0;
                a_builder.setSingleChoiceItems(msgOptions, checked_item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if(msgOptions[which].equals("Patient and Pharmacist")){
                           AlertDialog.Builder sub_builder=new AlertDialog.Builder(TreatPatient.this);
                           sub_builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   final FirebaseUser user=mAuth.getCurrentUser();
                                   final MessageToPharmacist msg=new MessageToPharmacist();
                                   msg.setPtName(patientName.getText().toString());
                                   msg.setPtId(PtId);
                                   msg.setPtGender(patientGender.getText().toString());
                                   msg.setPtAge(patientAge.getText().toString());
                                   msg.setPtAddress(patientAddress.getText().toString());
                                   msg.setPtMobile(patientMobileNumber.getText().toString());
                                   msg.setPtBloodGroup(patientBloodGroup.getText().toString());
                                   msg.setPtProblem(patientProblem.getText().toString());
                                   msg.setPharmaName(PharmacistName.getText().toString());
                                   msg.setMedicineHashMap(MedicineHashMap);
                                   msg.setDoctorName(user.getDisplayName());
                                   msg.setDocId(user.getUid());
                                   msg.setSubject("Regarding Pharmacy Medicines");
                                   Date todaysDate = new Date();
                                   DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                   String str = df.format(todaysDate);
                                   msg.setDate(str);
                                   DateFormat time=new SimpleDateFormat("HH:mm:ss");
                                   str=time.format(todaysDate);
                                   msg.setTime(str);
                                   DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Pharmacists").child(msg.getPharmaName());
                                   db.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           String PharmId=dataSnapshot.child("Uid").getValue(String.class);
                                           msg.setPharmId(PharmId);
                                           databaseReference.child("users").child(msg.getPharmId()).child("PharmacistInBox").child("Doctor").push().setValue(msg);
                                           databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Pharmacist").push().setValue(msg);
                                           //databaseReference.child("users").child(PtId).child("Inbox").child("Doctor(Pharmacist)").push().setValue(msg);
                                           //databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Patient").push().setValue(msg);
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {

                                       }
                                   });

                                   final MessageToPatient msgP=new MessageToPatient();
                                   msgP.setPtName(patientName.getText().toString());
                                   msgP.setPtId(PtId);
                                   msgP.setPtGender(patientGender.getText().toString());
                                   msgP.setPtAge(patientAge.getText().toString());
                                   msgP.setPtAddress(patientAddress.getText().toString());
                                   msgP.setPtMobile(patientMobileNumber.getText().toString());
                                   msgP.setPtBloodGroup(patientBloodGroup.getText().toString());
                                   msgP.setPtProblem(patientProblem.getText().toString());
                                   msgP.setPharmaName(PharmacistName.getText().toString());
                                   msgP.setMedicineHashMap(MedicineHashMap);
                                   msgP.setDoctorName(user.getDisplayName());
                                   msgP.setDocId(user.getUid());
                                   msgP.setSubject("Regarding Pharmacy Medicines");
                                   str = df.format(todaysDate);
                                   msgP.setDate(str);
                                   str=time.format(todaysDate);
                                   msgP.setTime(str);
                                   msgP.setDiagName(diagnosticianName.getText().toString());
                                   msgP.setDiagTests(SelectedTest);
                                   databaseReference.child("users").child(PtId).child("Inbox").child("Doctor").push().setValue(msgP);
                                   databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Patient").push().setValue(msgP);



                                   Toast.makeText(TreatPatient.this,"Message sent to Patient and Pharmacist",Toast.LENGTH_SHORT).show();
                               }
                           });
                           sub_builder.setNegativeButton("cancel",null);
                           AlertDialog alert1=sub_builder.create();
                           alert1.setTitle("Are you sure you want to send this message?");
                           alert1.show();
                        }else if(msgOptions[which].equals("Patient and Diagnostician")){
                           AlertDialog.Builder sub_builder=new AlertDialog.Builder(TreatPatient.this);
                           sub_builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   final FirebaseUser user=mAuth.getCurrentUser();
                                   final MessageToDiagnostician msg=new MessageToDiagnostician();
                                   msg.setPtName(patientName.getText().toString());
                                   msg.setPtId(PtId);
                                   msg.setPtGender(patientGender.getText().toString());
                                   msg.setPtAge(patientAge.getText().toString());
                                   msg.setPtAddress(patientAddress.getText().toString());
                                   msg.setPtMobile(patientMobileNumber.getText().toString());
                                   msg.setPtBloodGroup(patientBloodGroup.getText().toString());
                                   msg.setPtProblem(patientProblem.getText().toString());
                                   msg.setDiagName(diagnosticianName.getText().toString());
                                   msg.setDiagTests(SelectedTest);
                                   msg.setDoctorName(user.getDisplayName());
                                   msg.setDocId(user.getUid());
                                   msg.setSubject("Regarding Diagnostic Tests");
                                   msg.setRemarks(Remarks.getText().toString());
                                   Date todaysDate = new Date();
                                   DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                   String str = df.format(todaysDate);
                                   msg.setDate(str);
                                   DateFormat time=new SimpleDateFormat("HH:mm:ss");
                                   str=time.format(todaysDate);
                                   msg.setTime(str);
                                   //final String[] diagId = new String[1];
                                   DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Diagnosticians").child(msg.getDiagName());
                                   db.addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           String DiagId=dataSnapshot.child("Uid").getValue(String.class);
                                           msg.setDiagId(DiagId);
                                           databaseReference.child("users").child(msg.getDiagId()).child("DiagnosticInBox").child("Doctor").push().setValue(msg);
                                           databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Diagnostician").push().setValue(msg);
                                           //databaseReference.child("users").child(PtId).child("Inbox").child("Doctor").push().setValue(msg);
                                           //databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Patient").push().setValue(msg);
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {

                                       }
                                   });
                                   final MessageToPatient msgP=new MessageToPatient();
                                   msgP.setPtName(patientName.getText().toString());
                                   msgP.setPtId(PtId);
                                   msgP.setPtGender(patientGender.getText().toString());
                                   msgP.setPtAge(patientAge.getText().toString());
                                   msgP.setPtAddress(patientAddress.getText().toString());
                                   msgP.setPtMobile(patientMobileNumber.getText().toString());
                                   msgP.setPtBloodGroup(patientBloodGroup.getText().toString());
                                   msgP.setPtProblem(patientProblem.getText().toString());
                                   msgP.setPharmaName(PharmacistName.getText().toString());
                                   msgP.setMedicineHashMap(MedicineHashMap);
                                   msgP.setDoctorName(user.getDisplayName());
                                   msgP.setDocId(user.getUid());
                                   msgP.setSubject("Regarding Pharmacy Medicines");
                                   str = df.format(todaysDate);
                                   msgP.setDate(str);
                                   str=time.format(todaysDate);
                                   msgP.setTime(str);
                                   msgP.setDiagName(diagnosticianName.getText().toString());
                                   msgP.setDiagTests(SelectedTest);
                                   databaseReference.child("users").child(PtId).child("Inbox").child("Doctor").push().setValue(msgP);
                                   databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Patient").push().setValue(msgP);
                                   Toast.makeText(TreatPatient.this,"Message sent to Patient and Diagnostician",Toast.LENGTH_SHORT).show();
                               }
                           });
                           sub_builder.setNegativeButton("cancel",null);
                           AlertDialog alert1=sub_builder.create();
                           alert1.setTitle("Are you sure you want to send this message?");
                           alert1.show();

                        }
                        else if(msgOptions[which].equals("Patient,Pharmacist and Diagnostician")){
                           AlertDialog.Builder sub_builder=new AlertDialog.Builder(TreatPatient.this);
                           sub_builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   final FirebaseUser user=mAuth.getCurrentUser();
                                   final MessageToPharmacist msg=new MessageToPharmacist();
                                   msg.setPtName(patientName.getText().toString());
                                   msg.setPtId(PtId);
                                   msg.setPtGender(patientGender.getText().toString());
                                   msg.setPtAge(patientAge.getText().toString());
                                   msg.setPtAddress(patientAddress.getText().toString());
                                   msg.setPtMobile(patientMobileNumber.getText().toString());
                                   msg.setPtBloodGroup(patientBloodGroup.getText().toString());
                                   msg.setPtProblem(patientProblem.getText().toString());
                                   msg.setPharmaName(PharmacistName.getText().toString());
                                   msg.setMedicineHashMap(MedicineHashMap);
                                   msg.setDoctorName(user.getDisplayName());
                                   msg.setDocId(user.getUid());
                                   msg.setSubject("Regarding Pharmacy Medicines");
                                   Date todaysDate = new Date();
                                   DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                   String str = df.format(todaysDate);
                                   msg.setDate(str);
                                   DateFormat time=new SimpleDateFormat("HH:mm:ss");
                                   str=time.format(todaysDate);
                                   msg.setTime(str);
                                   DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Pharmacists").child(msg.getPharmaName());
                                   db.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           String PharmId=dataSnapshot.child("Uid").getValue(String.class);
                                           msg.setPharmId(PharmId);
                                           databaseReference.child("users").child(msg.getPharmId()).child("PharmacistInBox").child("Doctor").push().setValue(msg);
                                           databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Pharmacist").push().setValue(msg);
                                           //databaseReference.child("users").child(PtId).child("Inbox").child("Doctor(Pharmacist)").push().setValue(msg);
                                           //databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Patient").push().setValue(msg);
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {

                                       }
                                   });

                                   final MessageToDiagnostician msgD=new MessageToDiagnostician();
                                   msgD.setPtName(patientName.getText().toString());
                                   msgD.setPtId(PtId);
                                   msgD.setPtGender(patientGender.getText().toString());
                                   msgD.setPtAge(patientAge.getText().toString());
                                   msgD.setPtAddress(patientAddress.getText().toString());
                                   msgD.setPtMobile(patientMobileNumber.getText().toString());
                                   msgD.setPtBloodGroup(patientBloodGroup.getText().toString());
                                   msgD.setPtProblem(patientProblem.getText().toString());
                                   msgD.setDiagName(diagnosticianName.getText().toString());
                                   msgD.setDiagTests(SelectedTest);
                                   msgD.setDoctorName(user.getDisplayName());
                                   msgD.setDocId(user.getUid());
                                   msgD.setSubject("Regarding Diagnostic Tests");
                                   msgD.setRemarks(Remarks.getText().toString());
                                   String strD = df.format(todaysDate);
                                   msgD.setDate(strD);
                                   strD=time.format(todaysDate);
                                   msgD.setTime(strD);
                                   DatabaseReference dbD=FirebaseDatabase.getInstance().getReference().child("Diagnosticians").child(msgD.getDiagName());
                                   dbD.addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           String DiagId=dataSnapshot.child("Uid").getValue(String.class);
                                           msgD.setDiagId(DiagId);
                                           databaseReference.child("users").child(msgD.getDiagId()).child("DiagnosticInBox").child("Doctor").push().setValue(msgD);
                                           databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Diagnostician").push().setValue(msgD);
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {

                                       }
                                   });

                                   final MessageToPatient msgP=new MessageToPatient();
                                   msgP.setPtName(patientName.getText().toString());
                                   msgP.setPtId(PtId);
                                   msgP.setPtGender(patientGender.getText().toString());
                                   msgP.setPtAge(patientAge.getText().toString());
                                   msgP.setPtAddress(patientAddress.getText().toString());
                                   msgP.setPtMobile(patientMobileNumber.getText().toString());
                                   msgP.setPtBloodGroup(patientBloodGroup.getText().toString());
                                   msgP.setPtProblem(patientProblem.getText().toString());
                                   msgP.setPharmaName(PharmacistName.getText().toString());
                                   msgP.setMedicineHashMap(MedicineHashMap);
                                   msgP.setDoctorName(user.getDisplayName());
                                   msgP.setDocId(user.getUid());
                                   msgP.setSubject("Regarding Pharmacy Medicines");
                                   str = df.format(todaysDate);
                                   msgP.setDate(str);
                                   str=time.format(todaysDate);
                                   msgP.setTime(str);
                                   msgP.setDiagName(diagnosticianName.getText().toString());
                                   msgP.setDiagTests(SelectedTest);
                                   databaseReference.child("users").child(PtId).child("Inbox").child("Doctor").push().setValue(msgP);
                                   databaseReference.child("users").child(user.getUid()).child("DoctorSentBox").child("Patient").push().setValue(msgP);

                                   Toast.makeText(TreatPatient.this,"Message sent to Patient,Pharmacist and Diagnostician",Toast.LENGTH_SHORT).show();
                               }
                           });
                           sub_builder.setNegativeButton("cancel",null);
                           AlertDialog alert1=sub_builder.create();
                           alert1.setTitle("Are you sure you want to send this message?");
                           alert1.show();

                        }
                    }
                });
                a_builder.setNegativeButton("cancel",null);
                AlertDialog alert=a_builder.create();
                alert.setTitle("Please check the details Once again and Choose the option");
                alert.show();
            }
        });

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

                    case R.id.LogoutAsDoctor:
                        LogOutAsDoctor();
                        break;

                    case R.id.edtInbox:
                        DoctorInboxOpen();
                        break;
                    case R.id.edtSentbox:
                        DoctorSentBoxOpen();
                        break;

                    case R.id.edtTreatPatient:
                        PatientTreat();
                        break;
                }
                return false;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






    }
    private void ShowDialog(String s){
        final String MedicineName=s;
        AlertDialog.Builder sub_builder=new AlertDialog.Builder(TreatPatient.this);
        LayoutInflater inflater=this.getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.dosage_and_usage,null);
        final EditText Dosage=dialogView.findViewById(R.id.txtDosage);
        final EditText Usage=dialogView.findViewById(R.id.txtUsage);
        final TextView Composition=dialogView.findViewById(R.id.txtComposition);
        final TextView Name=dialogView.findViewById(R.id.txtMedicineName);
        Name.setText(MedicineName);
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Medicines").child(s).child("Composition");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String composition=dataSnapshot.getValue(String.class);
                Composition.setText(composition);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(MedicineHashMap.containsKey(MedicineName)) {
            MedicineClass Mc = MedicineHashMap.get(MedicineName);
            if (!Mc.getDosage().isEmpty()) {
                Dosage.setText(Mc.getDosage());
            }
            if (!Mc.getUsage().isEmpty()) {
                Usage.setText(Mc.getUsage());
            }
        }
        sub_builder.setView(dialogView);
        sub_builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Dosage1=Dosage.getText().toString();
                String Usage1=Usage.getText().toString();
                MedicineClass mc=new MedicineClass();
                mc.setComposition(Composition.getText().toString());
                mc.setDosage(Dosage1);
                mc.setUsage(Usage1);
                MedicineHashMap.put(MedicineName,mc);
                Toast.makeText(TreatPatient.this,"Dosage and Usage Saved successfully",Toast.LENGTH_SHORT).show();
            }
        });
        sub_builder.setNegativeButton("cancel",null);
        AlertDialog alert1=sub_builder.create();
        alert1.setTitle("Please Specify Dosage and Usage for the medicine!");
        alert1.show();
    }
    private void initPharmacist(){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Pharmacists");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Pharmacist.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void initBloodGroups() {
        BloodGroups.add("A+");
        BloodGroups.add("A-");
        BloodGroups.add("B+");
        BloodGroups.add("B-");
        BloodGroups.add("O+");
        BloodGroups.add("O-");
        BloodGroups.add("AB+");
        BloodGroups.add("AB-");
        BloodGroups.add("Others");


    }
    private void initGender(){
        gender.add("Male");
        gender.add("Female");
    }
    private void initDiagnosticians(){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Diagnosticians");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    diagnosticians.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void initDiagnosticTests(){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Diagnostic Tests");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    diagnosticTests.add(data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void initMedicinesArray(){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Medicines");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    MedicinesArray.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void DoctorSentBoxOpen(){
        startActivity(new Intent(this,DoctorSentboxTabbed.class));
    }
    private void  PatientTreat(){
        startActivity(new Intent(this,TreatPatient.class));
    }
    private void DoctorInboxOpen(){
        startActivity(new Intent(this,doctorInboxTabbed.class));
    }
    private void Home(){
        startActivity(new Intent(this,Home.class));
    }
    private void Logout(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,StartingPage.class));
    }
    private void LogOutAsDoctor() {
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile").child("flagDoctor");
        databaseReference.setValue("no");
        finish();
        Intent intent= new Intent(TreatPatient.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_profile,menu);
        return true;
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
                startActivity(new Intent(this,DoctorHome.class));
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
