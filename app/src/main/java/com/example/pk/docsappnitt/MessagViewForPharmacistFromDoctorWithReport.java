package com.example.pk.docsappnitt;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MessagViewForPharmacistFromDoctorWithReport extends AppCompatActivity {
    TextView Subject,DocName,Date,Time,PatientName,Address,Age,PhoneNumber,Gender,BloodGroup,Problem,medicines,ReportDoctor,YesOrNo;
    ArrayList<String> MedicineArray=new ArrayList<>();
    HashMap<String,ArrayList<String>> MedicineHashMap;
    SpinnerDialog spinnerDialogMedicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messag_view_for_pharmacist_from_doctor_with_report);

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
        final String DocIdValue=getIntent().getStringExtra("DocIdKey");

        String PharmaName=getIntent().getStringExtra("PharmaNameKey");
        MedicineHashMap=(HashMap<String, ArrayList<String>>) getIntent().getSerializableExtra("Medicines");
        Set<String> keys = MedicineHashMap.keySet();
        for(String key:keys){
            MedicineArray.add(key);
        }
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
        medicines=findViewById(R.id.medicines);
        ReportDoctor=findViewById(R.id.ReportDoctor);
        YesOrNo=findViewById(R.id.YesOrNo);

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

        spinnerDialogMedicines=new SpinnerDialog(MessagViewForPharmacistFromDoctorWithReport.this,MedicineArray,"Medicines");
        spinnerDialogMedicines.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                if(!s.equals("No Medicines were prescribed")){
                    ShowDialog(s);
                }
            }
        });
        medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogMedicines.showSpinerDialog();

            }
        });
        ReportDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MessagViewForPharmacistFromDoctorWithReport.this,PharmacistReport.class);
                intent.putExtra("DoctorNamekey",DocNameValue);
                intent.putExtra("PatientNameKey",PtNameValue);
                intent.putExtra("MobileKey",MobileValue);
                intent.putExtra("GenderKey",GenderValue);
                intent.putExtra("AgeKey",AgeValue);
                intent.putExtra("AddressKey",AddressValue);
                intent.putExtra("BloodGroupKey",BloodGroupValue);
                intent.putExtra("ProblemKey",ProblemValue);
                intent.putExtra("MedicinesKey",MedicineHashMap);
                intent.putExtra("DocIdKey",DocIdValue);
                startActivity(intent);
            }
        });
    }
    private void ShowDialog(String s){
        final String MedicineName=s;
        AlertDialog.Builder sub_builder=new AlertDialog.Builder(MessagViewForPharmacistFromDoctorWithReport.this);
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
}
