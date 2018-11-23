package com.example.pk.docsappnitt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    ProgressBar progressbar;
    EditText edtTextPassword,edtTextEmail;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        edtTextEmail=(EditText)findViewById(R.id.edtTextEmail);
        edtTextPassword=(EditText)findViewById(R.id.edtTextPassword);

        findViewById(R.id.txtDontHaveAccount).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);

        progressbar=(ProgressBar)findViewById(R.id.progressBar);

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutLogin);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewLogin);
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

    private void userLogin(){
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

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent= new Intent(MainActivity.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtDontHaveAccount:
                finish();
                startActivity(new Intent(this,SignUp.class));
                break;
            case R.id.btnLogin:
                userLogin();
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
