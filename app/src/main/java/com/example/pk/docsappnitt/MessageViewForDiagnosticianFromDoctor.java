package com.example.pk.docsappnitt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MessageViewForDiagnosticianFromDoctor extends AppCompatActivity {
    TextView Subject,DocName,Date,Time,PatientName,Address,Age,PhoneNumber,Gender,BloodGroup,Problem,tests;
    EditText txtRemarks;
    ArrayList<String> Tests;
    SpinnerDialog spinnerDialogTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view_for_diagnostician_from_doctor);

        String SubjectValue=getIntent().getStringExtra("SubjectKey");
        String DocNameValue=getIntent().getStringExtra("DoctorNameKey");
        String DateValue=getIntent().getStringExtra("DateKey");
        String TimeValue=getIntent().getStringExtra("TimeKey");
        String PtNameValue=getIntent().getStringExtra("PtNameKey");
        String AddressValue=getIntent().getStringExtra("AddressKey");
        String AgeValue=getIntent().getStringExtra("AgeKey");
        String MobileValue=getIntent().getStringExtra("MobileKey");
        String GenderValue=getIntent().getStringExtra("GenderKey");
        String BloodGroupValue=getIntent().getStringExtra("BloodGroupKey");
        String ProblemValue=getIntent().getStringExtra("ProblemKey");
        String RemarksValue=getIntent().getStringExtra("RemarksKey");
        Tests=getIntent().getStringArrayListExtra("DiagTestsKey");


        Subject=(TextView)findViewById(R.id.Subject);
        DocName=findViewById(R.id.DocName);
        Date=(TextView)findViewById(R.id.Date);
        Time=(TextView)findViewById(R.id.Time);
        PatientName=(TextView)findViewById(R.id.PatientName);
        Address=(TextView)findViewById(R.id.Address);
        Age=(TextView)findViewById(R.id.Age);
        PhoneNumber=(TextView)findViewById(R.id.PhoneNumber);
        Gender=(TextView)findViewById(R.id.Gender);
        BloodGroup=(TextView)findViewById(R.id.BloodGroup);
        Problem=(TextView)findViewById(R.id.Problem);
        tests=(TextView)findViewById(R.id.tests);
        txtRemarks=findViewById(R.id.txtRemarks);

        Subject.setText(SubjectValue);
        DocName.setText(DocNameValue);
        Date.setText(DateValue);
        Time.setText(TimeValue);
        PatientName.setText(PtNameValue);
        Address.setText(AddressValue);
        Age.setText(AgeValue);
        PhoneNumber.setText(MobileValue);
        Gender.setText(GenderValue);
        BloodGroup.setText(BloodGroupValue);
        Problem.setText(ProblemValue);
        txtRemarks.setText(RemarksValue);

        spinnerDialogTests=new SpinnerDialog(MessageViewForDiagnosticianFromDoctor.this,Tests,"Prescribed Diagnostic Tests");
        spinnerDialogTests.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

            }
        });
        tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogTests.showSpinerDialog();
            }
        });
    }
}
