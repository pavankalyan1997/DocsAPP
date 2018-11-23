package com.example.pk.docsappnitt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartingPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();



        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutStartingPage);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewStartingPage);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this,Home.class));
        }
    }


    @Override
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
                startActivity(new Intent(this,AboutUs.class));
                break;
            case R.id.MenuLogin:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.MenuSignUp:
                startActivity(new Intent(this,SignUp.class));
                break;

        }
        return true;
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
}
