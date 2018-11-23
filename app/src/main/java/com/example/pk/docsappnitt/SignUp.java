package com.example.pk.docsappnitt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    ProgressBar progressbar;
    EditText edtTextPassword,edtTextEmail;
    private FirebaseAuth mAuth;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtTextEmail=(EditText)findViewById(R.id.edtTextEmail);
        edtTextPassword=(EditText)findViewById(R.id.edtTextPassword);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.txtLogin).setOnClickListener(this);
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        progressbar=(ProgressBar)findViewById(R.id.progressbar);

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutSignUp);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewSignUp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edtHome:
                        Home();
                        break;
                    case R.id.AboutUs:
                        AboutUs();
                        break;
                    case R.id.MenuLogin:
                        Login();
                        break;
                    case R.id.MenuSignUp:
                        SignUp();
                        break;
                }
                return false;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void AboutUs(){
        startActivity(new Intent(this,AboutUs.class));
    }
    private void Login(){
        startActivity(new Intent(this,MainActivity.class));
    }
    private void SignUp(){
        startActivity(new Intent(this,SignUp.class));
    }
    private void Home(){
        startActivity(new Intent(this,StartingPage.class));
    }
    private void registerUser(){
        String email=edtTextEmail.getText().toString().trim();
        String password=edtTextPassword.getText().toString().trim();
        if(email.isEmpty()){
            edtTextEmail.setError("Email is Required");
            edtTextEmail.requestFocus();
            return;
        }
        if(! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtTextEmail.setError("Please enter Valid Email");
            edtTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            edtTextPassword.setError("Password is Required");
            edtTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            edtTextPassword.setError("Minimum Length of Password should be 6");
            edtTextPassword.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(),"User registered Successfull",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(SignUp.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                registerUser();
                break;

            case R.id.txtLogin:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_welcome,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Toggle.onOptionsItemSelected(item)){
            return true;
        }

        switch(item.getItemId()){
            case R.id.edtHome:
                Home();
                break;
            case R.id.AboutUs:
                AboutUs();
                break;
            case R.id.MenuLogin:
                Login();
                break;
            case R.id.MenuSignUp:
                SignUp();
                break;

        }
        return true;
    }
}
