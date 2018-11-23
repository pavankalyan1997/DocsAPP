package com.example.pk.docsappnitt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgPropic;
    EditText txtProName;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;
    FirebaseAuth mAuth;
    TextView txtUploadImg;
    TextView txtViewVerified;
    EditText txtPhoneNumber;
    EditText txtAddress;
    EditText txtRollNumber;

    private TextView mName;
    String name;
    Uri photoUrl;
    ImageView mPic;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    boolean isImageFitToScreen;
    private static final int CHOOSE_IMAGE = 101;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtUploadImg=(TextView)findViewById(R.id.txtUploadImg);
        imgPropic=(ImageView)findViewById(R.id.imgPropic);
        txtProName=(EditText)findViewById(R.id.txtProName);
        txtPhoneNumber=(EditText)findViewById(R.id.txtPhoneNumber);
        txtAddress=(EditText)findViewById(R.id.txtAddress);
        txtRollNumber=(EditText)findViewById(R.id.txtRollNumber);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutProfile);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewProfile);


        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();


        txtViewVerified=(TextView)findViewById(R.id.txtViewVerified);

        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        txtUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });
        loadUserInformation();
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 saveUserInformation();
            }
        });

        imgPropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isImageFitToScreen){
                     isImageFitToScreen=false;
                    LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)imgPropic.getLayoutParams();
                    params.width=300;
                    params.height=300;
                    imgPropic.setLayoutParams(params);
                }
                else{
                    isImageFitToScreen=true;
                    LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)imgPropic.getLayoutParams();
                    params.width=800;
                    params.height=800;
                    imgPropic.setLayoutParams(params);

                }
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


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,StartingPage.class));
        }
    }

    private void loadUserInformation(){
        final FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            DatabaseReference df1=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile");
            df1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        DatabaseReference df=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Profile");
                        df.child("MobileNumber").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String PhoneNumber=dataSnapshot.getValue().toString();
                                if(PhoneNumber!=null) {
                                    txtPhoneNumber.setText(PhoneNumber);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        df.child("RollNumber").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String RollNumber=dataSnapshot.getValue().toString();
                                if(!RollNumber.isEmpty()) {
                                    txtRollNumber.setText(RollNumber);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        df.child("Address").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String Address=dataSnapshot.getValue().toString();
                                if(!Address.isEmpty()){
                                    txtAddress.setText(Address);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if(user.getPhotoUrl()!=null){
                String url=user.getPhotoUrl().toString();
                AQuery aq = new AQuery(this);
                aq.id(R.id.imgPropic).image(url);


            }
            if(user.getDisplayName()!=null){
                txtProName.setText(user.getDisplayName());
            }

            if(user.isEmailVerified()){
                txtViewVerified.setText("Email verified");
            }
            else{
                txtViewVerified.setText("Email is not Verified (Click to verify)");
                txtViewVerified.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               Toast.makeText(ProfileActivity.this,"Verification Email Sent",Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                });
            }
        }



    }

    private void saveUserInformation(){
        FirebaseUser user=mAuth.getCurrentUser();
        String DisplayName=txtProName.getText().toString();
        String Name=txtProName.getText().toString();
        String PhoneNumber=txtPhoneNumber.getText().toString();
        String RollNumber=txtRollNumber.getText().toString();
        String Address=txtAddress.getText().toString();

        Profile userProfile=new Profile(Name,RollNumber,PhoneNumber,Address,"no","no","no","no","no","no");
        databaseReference.child("users").child(user.getUid()).child("Profile").setValue(userProfile);

        if(DisplayName.isEmpty()){
            txtProName.setError("Name required");
            txtProName.requestFocus();
            return;
        }

        if(user!=null &&profileImageUrl==null){
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder()
                    .setDisplayName(DisplayName)
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else if(user==null && profileImageUrl!=null){
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else if(user!=null && profileImageUrl!=null){
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder()
                    .setDisplayName(DisplayName)
                    .setPhotoUri(Uri.parse(profileImageUrl)).build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
           uriProfileImage=data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imgPropic.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    private void uploadImageToFirebaseStorage(){
        final StorageReference profileImageRef=
                FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage!= null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           progressBar.setVisibility(View.GONE);
                           profileImageUrl=taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
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

    private void showImageChooser(){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);

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
