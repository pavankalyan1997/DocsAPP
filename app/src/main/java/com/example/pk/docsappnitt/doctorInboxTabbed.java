package com.example.pk.docsappnitt;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class doctorInboxTabbed extends AppCompatActivity {
    private static final String TAG="InboxTabbed";
    private SectionPageAdapter mSectionsPageAdapter;
    private ViewPager mviewPager;

    private TextView mName;
    String name;
    Uri photoUrl;
    ImageView mPic;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_inbox_tabbed);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutDoctorInboxTabbed);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewDoctorInboxTabbed);

        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();

        mSectionsPageAdapter=new SectionPageAdapter(getSupportFragmentManager());
        mviewPager=(ViewPager)findViewById(R.id.container);
        setupViewPager(mviewPager);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);

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

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter=new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new DoctorInboxTab1Patient(),"Patient");
        adapter.addFragment(new DoctorInboxTab2Pharmacist(),"Pharmacist");
        adapter.addFragment(new DoctorInboxTab3Diagnostic(),"Diagnostician");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_doctor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Toggle.onOptionsItemSelected(item)){
            return true;
        }
        switch(item.getItemId()){
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,StartingPage.class));
                break;
            case R.id.edtProfile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;

            case R.id.AdminOptions:
                startActivity(new Intent(this,LoginAdmin.class));
                break;

            case R.id.edtViewProfile:
                studentView();
                break;


        }
        return true;
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
    private void LogOutAsDoctor() {
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile").child("flagDoctor");
        databaseReference.setValue("no");
        finish();
        Intent intent= new Intent(doctorInboxTabbed.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void studentView(){
        startActivity(new Intent(this,studentView.class));
    }
    private void Home(){
        startActivity(new Intent(this,Home.class));
    }
    private void Logout(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,StartingPage.class));
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
