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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidquery.AQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    DatabaseReference databaseReference;

    ViewFlipper v_flipper;


    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mName;

    String name;
    Uri photoUrl;
    ImageView mPic;
    TextView txtSample;
    Button btnSample;

    LinearLayout ConsultDoctor,Inbox,SentBox,EditProfile,HealthTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int images[]={R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5};

        v_flipper=findViewById(R.id.v_flipper);


        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();

        ConsultDoctor=findViewById(R.id.ConsultDoctor);
        Inbox=findViewById(R.id.Inbox);
        SentBox=findViewById(R.id.SentBox);
        EditProfile=findViewById(R.id.EditProfile);
        HealthTips=findViewById(R.id.HealthTips);

        ConsultDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultDoctor();
            }
        });

        Inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inbox();
            }
        });
        SentBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sentbox();
            }
        });
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile();
            }
        });
        HealthTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HealthTipsOpen();
            }
        });

        for(int image:images){
            flipperImages(image);
        }


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
   @Override
    protected void onStart() {
        super.onStart();
        try{
            if(mAuth.getCurrentUser()!=null) {
                FirebaseUser user = mAuth.getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile");
                if(databaseReference!=null) {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String loggedInDoctor = dataSnapshot.child("flagDoctor").getValue(String.class);
                            String loggedInDIag=dataSnapshot.child("flagDiag").getValue(String.class);
                            String loggedInPharma=dataSnapshot.child("flagPharma").getValue(String.class);
                            try{
                                if (loggedInDoctor.equals("yes")) {
                                    DoctorHome();
                                }
                                else if(loggedInDIag.equals("yes")){
                                    DiagHome();
                                }
                                else if(loggedInPharma.equals("yes")){
                                    PharmaHome();
                                }
                            }catch (Exception e){
                                return ;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

        }catch (Exception e){
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_home,menu);
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
            case R.id.edtInbox:
                Inbox();
                break;
            case R.id.edtSentbox:
                Sentbox();
        }
        return false;
    }
    private void HealthTipsOpen(){
        startActivity(new Intent(this,HealthTips.class));
    }
    private void Sentbox(){startActivity(new Intent(this,SentBoxTabbed.class));}
    private void Inbox(){
        startActivity(new Intent(this,InboxTabbed.class));
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

   private void DoctorHome(){
       finish();
       Intent intent= new Intent(Home.this, DoctorHome.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(intent);
   }
   private void DiagHome(){
        finish();
       Intent intent= new Intent(Home.this, diagnostic_home.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(intent);
   }
   private void PharmaHome(){
       finish();
       Intent intent= new Intent(Home.this, PharmacistHome.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(intent);
   }
}
