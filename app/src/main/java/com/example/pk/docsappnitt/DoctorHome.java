package com.example.pk.docsappnitt;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.androidquery.AQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DoctorHome extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    private TextView mName;
    String name;
    Uri photoUrl;
    ImageView mPic;
    TextView txtDocmsg;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    ViewFlipper v_flipper;
    LinearLayout TreatPatient,Inbox,SentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        int images[]={R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5};
        v_flipper=findViewById(R.id.v_flipper);

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutDoctorHome);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewDoctorHome);


        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();

        TreatPatient=findViewById(R.id.TreatPatient);
        Inbox=findViewById(R.id.Inbox);
        SentBox=findViewById(R.id.SentBox);

        TreatPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               PatientTreat();
            }
        });
        Inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorInboxOpen();
            }
        });
        SentBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorSentBoxOpen();
            }
        });
        for(int image:images){
            flipperImages(image);
        }

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
    public void flipperImages(int Image){
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(Image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(5000);
        v_flipper.setAutoStart(true);
        v_flipper.setInAnimation(this,R.anim.slide_in_bottom);
        v_flipper.setOutAnimation(this,R.anim.slide_out_top);

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
        Intent intent= new Intent(DoctorHome.this, Home.class);
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
    private void StartingPage(){
        startActivity(new Intent(this,StartingPage.class));
    }



}
