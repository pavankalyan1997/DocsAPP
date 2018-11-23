package com.example.pk.docsappnitt;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class DiagReport extends AppCompatActivity {
    EditText DocName,PatientName,Age,Address,MobileNumber,Problem,Remarks,BloodGroup;
    TextView Gender,Tests;
    FloatingActionButton FABButton;
    SpinnerDialog spinnerDialogTests;
    ArrayList<String>TestsArray=new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diag_report);

        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();


        final String DoctorNameValue=getIntent().getStringExtra("DoctorKey");
        final String PatientNameValue=getIntent().getStringExtra("PatientNameKey");
        final String MobileValue=getIntent().getStringExtra("MobileKey");
        final String GenderValue=getIntent().getStringExtra("GenderKey");
        final String BloodGroupValue=getIntent().getStringExtra("BloodGroupKey");
        final String ProblemValue=getIntent().getStringExtra("ProblemKey");
        final String AddressValue=getIntent().getStringExtra("AddressKey");
        final String AgeValue=getIntent().getStringExtra("AgeKey");
        TestsArray=getIntent().getStringArrayListExtra("DiagTestsKey");
        final String DocId=getIntent().getStringExtra("DoctorIdKey");


        DocName=findViewById(R.id.txtDocName);
        PatientName=findViewById(R.id.txtPatientName);
        Age=findViewById(R.id.txtAge);
        Address=findViewById(R.id.txtAddress);
        MobileNumber=findViewById(R.id.txtMobileNumber);
        Problem=findViewById(R.id.txtProblem);
        Remarks=findViewById(R.id.txtRemarks);
        Gender=findViewById(R.id.txtGender);
        BloodGroup=findViewById(R.id.txtBloodGroup);
        Tests=findViewById(R.id.txtSelectedDiagnosticTests);
        FABButton=findViewById(R.id.FABButton);

        DocName.setText(DoctorNameValue);
        PatientName.setText(PatientNameValue);
        Age.setText(AgeValue);
        Address.setText(AddressValue);
        MobileNumber.setText(MobileValue);
        Problem.setText(ProblemValue);
        Gender.setText(GenderValue);
        BloodGroup.setText(BloodGroupValue);

        spinnerDialogTests=new SpinnerDialog(DiagReport.this,TestsArray,"Following tests has been Performed");
        spinnerDialogTests.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

            }
        });
        Tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    spinnerDialogTests.showSpinerDialog();
                }catch (Exception e){

                }

            }
        });

        FABButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder=new AlertDialog.Builder(DiagReport.this);
                a_builder.setMessage("Do you want to send the message to "+DoctorNameValue)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final FirebaseUser user=mAuth.getCurrentUser();
                                final MessageToDiagnostician msg=new MessageToDiagnostician();
                                msg.setSubject("Acknowledgment for Diagnostic Tests");
                                msg.setPtName(PatientNameValue);
                                msg.setPtGender(GenderValue);
                                msg.setPtAge(AgeValue);
                                msg.setPtAddress(AddressValue);
                                msg.setPtMobile(MobileValue);
                                msg.setPtBloodGroup(BloodGroupValue);
                                msg.setPtProblem(ProblemValue);
                                msg.setDiagName(user.getDisplayName());
                                msg.setDiagTests(TestsArray);
                                msg.setDoctorName(user.getDisplayName());
                                msg.setDiagId(user.getUid());
                                msg.setDocId(DocId);
                                msg.setRemarks(Remarks.getText().toString());
                                Date todaysDate = new Date();
                                DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String str = df.format(todaysDate);
                                msg.setDate(str);
                                DateFormat time=new SimpleDateFormat("HH:mm:ss");
                                str=time.format(todaysDate);
                                msg.setTime(str);

                                databaseReference.child("users").child(DocId).child("DoctorInBox").child("Diagnostician").push().setValue(msg);
                                databaseReference.child("users").child(user.getUid()).child("DiagnosticSentBox").child("Doctor").push().setValue(msg);
                                Toast.makeText(DiagReport.this, "Message Sent to "+DoctorNameValue, Toast.LENGTH_SHORT).show();


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
}
