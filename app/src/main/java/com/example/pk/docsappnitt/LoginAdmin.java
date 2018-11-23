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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class LoginAdmin extends AppCompatActivity {
    RadioButton rbDoctor,rbPharmacist,rbDiagnostic;
    LinearLayout ll1,ll2,ll3;
    FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    EditText txtDocId,txtPharmId,txtDiagId;
    Button btnDocLogin,btnPharmaLogin,btnDiag;
    ProgressBar progressBar;

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
        setContentView(R.layout.activity_login_admin);

        rbDoctor=(RadioButton)findViewById(R.id.rbDoctor);
        rbPharmacist=(RadioButton)findViewById(R.id.rbPharmacist);
        rbDiagnostic=(RadioButton)findViewById(R.id.rbDiagnostic);
        ll1=(LinearLayout)findViewById(R.id.ll1);
        ll2=(LinearLayout)findViewById(R.id.ll2);
        ll3=(LinearLayout)findViewById(R.id.ll3);
        txtDocId=(EditText)findViewById(R.id.txtDocId);
        txtPharmId=(EditText)findViewById(R.id.txtPharmId);
        txtDiagId=(EditText)findViewById(R.id.txtDiagId);

        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        btnDocLogin=(Button)findViewById(R.id.btnDocLogin);
        btnPharmaLogin=(Button)findViewById(R.id.btnPharmaLogin);
        btnDiag=(Button)findViewById(R.id.btnDiag);

        progressBar=findViewById(R.id.progressBar1);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayoutLoginAdmin);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewLoginAdmin);

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

                    case R.id.edtProfile:
                        EditProfile();
                        break;

                    case R.id.AdminOptions:
                        AdminOption();
                        break;

                    case R.id.Logout:
                        Logout();
                        break;
                }
                return false;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        RadioGroup radioGroup=(RadioGroup)findViewById(R.id.RadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rbDoctor){
                    if(rbDoctor.isChecked()) {
                        ll1.setVisibility(View.VISIBLE);
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);

                        btnDocLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                progressBar.setVisibility(View.VISIBLE);
                                DoctorLogin();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
                else if(i==R.id.rbPharmacist){
                    if(rbPharmacist.isChecked()) {
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                        ll3.setVisibility(View.GONE);

                        btnPharmaLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PharmacistLogin();
                            }
                        });
                    }
                }
                else if(i== R.id.rbDiagnostic){
                    if(rbDiagnostic.isChecked()) {
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.VISIBLE);

                        btnDiag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DiagnosticLogin();
                            }
                        });
                    }
                }

            }
        });

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

        }
        return true;
    }

    private void DoctorLogin(){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Doctors");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String DocId=txtDocId.getText().toString().trim();
                if(DocId.isEmpty()){
                    txtDocId.setError("Id can't be empty");
                    txtDocId.requestFocus();
                    return;
                }
                else if(dataSnapshot.hasChild(DocId)){
                    final String DocId1=DocId;
                    final FirebaseUser user=mAuth.getCurrentUser();
                    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Doctors").child(DocId).child("Name");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String Name=dataSnapshot.getValue(String.class);
                            if(user.getDisplayName().equals(Name)){
                                final String DocID1=DocId1;
                                DoctorProfile docPro=new DoctorProfile(user.getDisplayName(),user.getUid());
                                DatabaseReference doc=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile");
                                doc.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Profile docProfile=new Profile();
                                        docProfile=dataSnapshot.getValue(Profile.class);
                                        databaseReference.child("Doctors").child(DocID1).child("Profile").setValue(docProfile);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseReference.child("DoctorList").child(user.getUid()).setValue(docPro.Name);
                                databaseReference.child("Doctors").child(DocId1).setValue(docPro);
                                databaseReference.child("users").child(user.getUid()).child("Profile").child("flagDoctor").setValue("yes");
                                databaseReference.child("users").child(user.getUid()).child("Profile").child("isDoctor").setValue("yes");
                                Toast.makeText(LoginAdmin.this,"Doctor Login SuccessFull",Toast.LENGTH_SHORT).show();
                                GotoDocPage();
                            }
                            else{
                                txtDocId.setError("Our database says You are not a Doctor!");
                                txtDocId.requestFocus();
                                Toast.makeText(LoginAdmin.this,"Doctor Id invalid or Id doesn't exist",Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    txtDocId.setError("Invalid Id");
                    txtDocId.requestFocus();
                    Toast.makeText(LoginAdmin.this,"Doctor Id invalid or Id doesn't exist",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void GotoDocPage() {
        finish();
        Intent intent= new Intent(LoginAdmin.this, DoctorHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void PharmacistLogin(){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Pharmacists");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String PharmId=txtPharmId.getText().toString().trim();
                if(PharmId.isEmpty()){
                    txtPharmId.setError("Id can;t be empty");
                    txtPharmId.requestFocus();
                    return;
                }
                else if(dataSnapshot.hasChild(PharmId)){
                    final String PharmId1=PharmId;
                    final FirebaseUser user=mAuth.getCurrentUser();
                    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Pharmacists").child(PharmId).child("Name");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String Name=dataSnapshot.getValue(String.class);
                            if(user.getDisplayName().equals(Name)){
                                final String PharmID1=PharmId1;
                                DoctorProfile docPro=new DoctorProfile(user.getDisplayName(),user.getUid());
                                DatabaseReference doc=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile");
                                doc.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Profile docProfile=new Profile();
                                        docProfile=dataSnapshot.getValue(Profile.class);
                                        databaseReference.child("Pharmacists").child(PharmID1).child("Profile").setValue(docProfile);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseReference.child("Pharmacists").child(PharmID1).setValue(docPro);
                                databaseReference.child("users").child(user.getUid()).child("Profile").child("flagPharma").setValue("yes");
                                databaseReference.child("users").child(user.getUid()).child("Profile").child("isPharma").setValue("yes");
                                Toast.makeText(LoginAdmin.this,"Pharmacist Login SuccessFull",Toast.LENGTH_SHORT).show();
                                GotoPharmacistPage();


                            }
                            else{
                                txtDiagId.setError("Our Database Says you are not a pharmacist");
                                txtDiagId.requestFocus();
                                Toast.makeText(LoginAdmin.this,"Id doesn't Exist",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    txtPharmId.setError("Invalid Id");
                    Toast.makeText(LoginAdmin.this,"Id doesn't Exist",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void GotoPharmacistPage(){
        finish();
        Intent intent= new Intent(LoginAdmin.this, PharmacistHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void DiagnosticLogin(){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Diagnosticians");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String DiagId=txtDiagId.getText().toString().trim();
                if(DiagId.isEmpty()){
                    txtDiagId.setError("Id can't be Empty");
                    txtDiagId.requestFocus();
                    return;
                }
                else if(dataSnapshot.hasChild(DiagId)){
                    final String DiagId1=DiagId;
                    final FirebaseUser user=mAuth.getCurrentUser();
                    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Diagnosticians").child(DiagId).child("Name");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name=dataSnapshot.getValue(String.class);
                            if(user.getDisplayName().equals(name) ){
                                final String DiagID1=DiagId1;
                                DoctorProfile docPro=new DoctorProfile(user.getDisplayName(),user.getUid());
                                DatabaseReference doc=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile");
                                doc.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Profile docProfile=new Profile();
                                        docProfile=dataSnapshot.getValue(Profile.class);
                                        databaseReference.child("Diagnosticians").child(DiagID1).child("Profile").setValue(docProfile);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseReference.child("Diagnosticians").child(DiagId1).setValue(docPro);
                                databaseReference.child("users").child(user.getUid()).child("Profile").child("flagDiag").setValue("yes");
                                databaseReference.child("users").child(user.getUid()).child("Profile").child("isDiagnostician").setValue("yes");
                                Toast.makeText(LoginAdmin.this,"Diagnostician Login Successfull",Toast.LENGTH_SHORT).show();
                                GotoDiagPage();
                            }
                            else{
                                txtDiagId.setError("Our database says You are not a Diagnostician!");
                                txtDiagId.requestFocus();
                                Toast.makeText(LoginAdmin.this,"Id doesn't Exist",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    txtDiagId.setError("Id Invalid or Error");
                    txtDiagId.requestFocus();
                    Toast.makeText(LoginAdmin.this,"Id doesn't Exist",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void GotoDiagPage(){
        finish();
        Intent intent= new Intent(LoginAdmin.this, diagnostic_home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

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
}
