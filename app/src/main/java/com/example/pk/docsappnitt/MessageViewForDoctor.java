package com.example.pk.docsappnitt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MessageViewForDoctor extends AppCompatActivity {
    TextView Subject,PtName,Date,Time,PatientName,PhoneNumber,Gender,BloodGroup,Problem,AppointmentTime,Address,Reply,Age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view_for_doctor);

        String SubjectValue=getIntent().getStringExtra("SubjectKey");
        final String PtNameValue=getIntent().getStringExtra("PtNameKey");
        String DateValue=getIntent().getStringExtra("DateKey");
        String TimeValue=getIntent().getStringExtra("TimeKey");
        String PatientNameValue=getIntent().getStringExtra("PatientNameKey");
        final String MobileValue=getIntent().getStringExtra("MobileKey");
        final String GenderValue=getIntent().getStringExtra("GenderKey");
        final String BloodGroupValue=getIntent().getStringExtra("BloodGroupKey");
        final String ProblemValue=getIntent().getStringExtra("ProblemKey");
        String AppointmentTimeValue=getIntent().getStringExtra("AppointmentTimeKey");
        final String PatientIdValue=getIntent().getStringExtra("PatientIdKey");
        final String AddressValue=getIntent().getStringExtra("AddressKey");
        final String AgeValue=getIntent().getStringExtra("AgeKey");


        Subject=(TextView)findViewById(R.id.Subject);
        PtName=(TextView)findViewById(R.id.PtName);
        Date=(TextView)findViewById(R.id.Date);
        Time=(TextView)findViewById(R.id.Time);
        PatientName=(TextView)findViewById(R.id.PatientName);
        PhoneNumber=(TextView)findViewById(R.id.PhoneNumber);
        Gender=(TextView)findViewById(R.id.Gender);
        BloodGroup=(TextView)findViewById(R.id.BloodGroup);
        Problem=(TextView)findViewById(R.id.Problem);
        AppointmentTime=(TextView)findViewById(R.id.AppointmentTime);
        Address=(TextView)findViewById(R.id.Address);
        Reply=(TextView)findViewById(R.id.txtReply);
        Age=(TextView)findViewById(R.id.Age);

        Subject.setText(SubjectValue);
        PtName.setText(PtNameValue);
        Date.setText(DateValue);
        Time.setText(TimeValue);
        PatientName.setText(PatientNameValue);
        PhoneNumber.setText(MobileValue);
        Gender.setText(GenderValue);
        BloodGroup.setText(BloodGroupValue);
        Problem.setText(ProblemValue);
        AppointmentTime.setText(AppointmentTimeValue);
        Address.setText(AddressValue);
        Age.setText(AgeValue);

        Reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MessageViewForDoctor.this,TreatPatient.class);
                intent.putExtra("PtNameKey",PtNameValue);
                intent.putExtra("MobileKey",MobileValue);
                intent.putExtra("GenderKey",GenderValue);
                intent.putExtra("BloodGroupKey",BloodGroupValue);
                intent.putExtra("ProblemKey",ProblemValue);
                intent.putExtra("AddressKey",AddressValue);
                intent.putExtra("AgeKey",AgeValue);
                intent.putExtra("PatientIdKey",PatientIdValue);
                startActivity(intent);
            }
        });
    }
    private void  PatientTreat(){
        startActivity(new Intent(this,TreatPatient.class));
    }
}
