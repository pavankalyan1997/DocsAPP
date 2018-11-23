package com.example.pk.docsappnitt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MessageViewComplete extends AppCompatActivity {

    TextView Subject,PtName,Date,Time,PatientName,PhoneNumber,Gender,BloodGroup,Problem,AppointmentTime,PatientId,Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view_complete);

        String SubjectValue=getIntent().getStringExtra("SubjectKey");
        String PtNameValue=getIntent().getStringExtra("PtNameKey");
        String DateValue=getIntent().getStringExtra("DateKey");
        String TimeValue=getIntent().getStringExtra("TimeKey");
        String PatientNameValue=getIntent().getStringExtra("PatientNameKey");
        String MobileValue=getIntent().getStringExtra("MobileKey");
        String GenderValue=getIntent().getStringExtra("GenderKey");
        String BloodGroupValue=getIntent().getStringExtra("BloodGroupKey");
        String ProblemValue=getIntent().getStringExtra("ProblemKey");
        String AppointmentTimeValue=getIntent().getStringExtra("AppointmentTimeKey");
        String PatientIdValue=getIntent().getStringExtra("PatientIdKey");
        String AddressValue=getIntent().getStringExtra("AddressKey");



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
        PatientId=(TextView)findViewById(R.id.PatientId);
        Address=(TextView)findViewById(R.id.Address);

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
        PatientId.setText(PatientIdValue);
        Address.setText(AddressValue);


    }
}
