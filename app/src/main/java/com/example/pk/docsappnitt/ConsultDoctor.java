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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ConsultDoctor extends AppCompatActivity {

    ArrayList<String> doctors=new ArrayList<>();
    ArrayList<String>gender=new ArrayList<>();
    ArrayList<String>Time=new ArrayList<>();
    ArrayList<String>BloodGroups=new ArrayList<>();
    TextView txtDocSelect,txtDocName,txtGenderSelect,txtGender,txtSelectTime,txtBloodGroup;
    FloatingActionButton FABButton;
    EditText txtAge,txtProblem;
    SpinnerDialog spinnerDialogDoc;
    SpinnerDialog spinnerDialogGen;
    SpinnerDialog spinnerDialogTime;
    SpinnerDialog spinnerDialogBloodGroup;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    private TextView mName;
    String name;
    Uri photoUrl;
    ImageView mPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_doctor);

        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        initDoctors();
        initGender();
        initTime();
        initBloodGroups();
        txtDocSelect=(TextView)findViewById(R.id.txtDocSelect);
        txtDocName=(TextView)findViewById(R.id.txtDocname);
        txtGenderSelect=(TextView)findViewById(R.id.txtGenderSelect);
        txtGender=(TextView)findViewById(R.id.txtGender);
        txtSelectTime=(TextView)findViewById(R.id.txtSelectTime);
        txtAge=(EditText)findViewById(R.id.txtAge);
        txtProblem=(EditText)findViewById(R.id.txtProblem);
        txtBloodGroup=(TextView)findViewById(R.id.txtBloodGroup);
        FABButton=(FloatingActionButton)findViewById(R.id.FABButton);

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutConsultDoctor);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewConsultDoctor);

        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();



        spinnerDialogDoc=new SpinnerDialog(ConsultDoctor.this,doctors,"Choose a Doctor");
        spinnerDialogDoc.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txtDocName.setText(s);
                //txtDocName.setVisibility(View.VISIBLE);
            }
        });
        spinnerDialogGen=new SpinnerDialog(ConsultDoctor.this,gender,"Choose your gender");
        spinnerDialogGen.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txtGender.setText(s);
            }
        });
        spinnerDialogTime=new SpinnerDialog(ConsultDoctor.this,Time,"Select Your preferred Time");
        spinnerDialogTime.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txtSelectTime.setText(s);
            }
        });
        spinnerDialogBloodGroup=new SpinnerDialog(ConsultDoctor.this,BloodGroups,"Choose your Blood Group");
        spinnerDialogBloodGroup.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txtBloodGroup.setText(s);
            }
        });
        txtBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogBloodGroup.showSpinerDialog();
            }
        });
        txtSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogTime.showSpinerDialog();
            }
        });

        txtDocName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogDoc.showSpinerDialog();

            }
        });
        txtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialogGen.showSpinerDialog();
            }
        });

        FABButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user=mAuth.getCurrentUser();
                final MessageToDoctor msg=new MessageToDoctor();
                Date todaysDate = new Date();
                DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String str = df.format(todaysDate);
                msg.setDate(str);
                DateFormat time=new SimpleDateFormat("HH:mm:ss");
                str=time.format(todaysDate);
                msg.setTime(str);
                msg.setPatientName(user.getDisplayName());
                msg.setPatientId(user.getUid());
                msg.setDoctorName(txtDocName.getText().toString().trim());
                msg.setGender(txtGender.getText().toString());
                msg.setAge(txtAge.getText().toString());
                msg.setProblem(txtProblem.getText().toString());
                msg.setBloodGroup(txtBloodGroup.getText().toString());
                msg.setAppointmentTime(txtSelectTime.getText().toString());
                msg.setSubject("Requesting For Appointment");
                DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile").child("MobileNumber");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       final String MobileNumber=dataSnapshot.getValue(String.class);
                       msg.setPatientPhoneNumber(MobileNumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });
                db=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile").child("Address");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String Address=dataSnapshot.getValue(String.class);
                        msg.setAddress(Address);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });
                db=FirebaseDatabase.getInstance().getReference().child("Doctors").child(txtDocName.getText().toString());
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String DocId=dataSnapshot.child("Uid").getValue(String.class);
                        msg.setDocId(DocId);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                msg.setFrom(user.getDisplayName());
                msg.setTo(txtDocName.getText().toString());
                AlertDialog.Builder a_builder=new AlertDialog.Builder(ConsultDoctor.this);
                a_builder.setMessage("Do you want to send the message to "+msg.getDoctorName())
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child("users").child(msg.getDocId()).child("DoctorInBox").child("Patient").push().setValue(msg);
                                databaseReference.child("users").child(user.getUid()).child("SentBox").child("Doctor").push().setValue(msg);
                                Toast.makeText(ConsultDoctor.this,"Message Sent",Toast.LENGTH_SHORT).show();
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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edtHome:
                        Home();
                        break;

                    case R.id.edtProfile:
                        EditProfile();
                        break;

                    case R.id.AdminOptions:
                        AdminOption();
                        break;

                    case R.id.Logout:
                        Logout();
                        break;

                    case R.id.edtViewProfile:
                        studentView();
                        break;

                    case R.id.edtConsultDoctor:
                        consultDoctor();
                        break;

                }
                return false;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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

    private void consultDoctor(){
        startActivity(new Intent(this,ConsultDoctor.class));
    }
    private void studentView(){
        startActivity(new Intent(this,studentView.class));
    }
    private void Home(){
        startActivity(new Intent(this,Home.class));
    }
    private void EditProfile(){
        startActivity(new Intent(this,ProfileActivity.class));
    }
    private void AdminOption(){
        startActivity(new Intent(this,LoginAdmin.class));
    }
    private void Logout(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,StartingPage.class));
    }

    private void initGender(){
        gender.add("Male");
        gender.add("Female");
    }
    private void initDoctors(){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Doctors");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                   doctors.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void initTime(){
        Time.add("9 am to 12 pm");
        Time.add("12 pm to 5pm");
        Time.add("5pm to 10pm");
        Time.add("Any time when doctor is available");
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
