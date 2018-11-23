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
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class diagnostic_home extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    private TextView mName;
    String name;
    Uri photoUrl;
    ImageView mPic;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_home);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutDiagnosticianHome);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewDiagnosticianHome);


        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edtHome:
                        Home();
                        break;
                    case R.id.edtReport:
                        DiagReport();
                        break;
                    case R.id.edtInbox:
                        DiagInbox();
                        break;
                    case R.id.edtSentbox:
                        DiagSentBox();
                        break;
                    case R.id.LogoutAsDiag:
                        LogOutAsDiag();
                        break;

                    case R.id.Logout:
                        Logout();
                        break;
                }
                return false;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void DiagReport(){
        startActivity(new Intent(this,DiagReport.class));
    }
    private void DiagSentBox(){
        startActivity(new Intent(this,diagSentBoxTabbed.class));
    }
    private void DiagInbox(){
        startActivity(new Intent(this,diagInboxTabbed.class));
    }
    private void Home(){
        startActivity(new Intent(this,diagnostic_home.class));
    }
    private void Logout(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,StartingPage.class));
    }
    private void LogOutAsDiag() {
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile").child("flagDiag");
        databaseReference.setValue("no");
        finish();
        Intent intent= new Intent(diagnostic_home.this, Home.class);
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
