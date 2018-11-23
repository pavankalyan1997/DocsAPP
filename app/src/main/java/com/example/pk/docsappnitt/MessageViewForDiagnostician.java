package com.example.pk.docsappnitt;

import android.content.Intent;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MessageViewForDiagnostician extends AppCompatActivity {
    TextView Subject,DocName,Date,Time,PatientName,Address,Age,PhoneNumber,Gender,BloodGroup,Problem,tests,txtReport;
    EditText txtRemarks;
    ArrayList<String>Tests;
    SpinnerDialog spinnerDialogTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view_for_diagnostician);

        String SubjectValue=getIntent().getStringExtra("SubjectKey");
        final String DocNameValue=getIntent().getStringExtra("DoctorNameKey");
        String DateValue=getIntent().getStringExtra("DateKey");
        String TimeValue=getIntent().getStringExtra("TimeKey");
        final String PtNameValue=getIntent().getStringExtra("PtNameKey");
        final String AddressValue=getIntent().getStringExtra("AddressKey");
        final String AgeValue=getIntent().getStringExtra("AgeKey");
        final String MobileValue=getIntent().getStringExtra("MobileKey");
        final String GenderValue=getIntent().getStringExtra("GenderKey");
        final String BloodGroupValue=getIntent().getStringExtra("BloodGroupKey");
        final String ProblemValue=getIntent().getStringExtra("ProblemKey");
        String RemarksValue=getIntent().getStringExtra("RemarksKey");
        Tests=getIntent().getStringArrayListExtra("DiagTestsKey");
        final String DocId=getIntent().getStringExtra("DoctorId");

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
        txtReport=(TextView)findViewById(R.id.txtReport);
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

        spinnerDialogTests=new SpinnerDialog(MessageViewForDiagnostician.this,Tests,"Please Perform the following tests for the patient");
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

        txtReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MessageViewForDiagnostician.this,DiagReport.class);
                intent.putExtra("DoctorKey",DocNameValue);
                intent.putExtra("PatientNameKey",PtNameValue);
                intent.putExtra("GenderKey",GenderValue);
                intent.putExtra("AgeKey",AgeValue);
                intent.putExtra("AddressKey",AddressValue);
                intent.putExtra("MobileKey",MobileValue);
                intent.putExtra("BloodGroupKey",BloodGroupValue);
                intent.putExtra("ProblemKey",ProblemValue);
                intent.putExtra("DiagTestsKey",Tests);
                intent.putExtra("DoctorIdKey",DocId);
                startActivity(intent);
            }
        });



    }
}
