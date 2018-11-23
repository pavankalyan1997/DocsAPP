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
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class AboutUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText txtAboutUs;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        findViewById(R.id.txtAboutUs);

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutAboutUs);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewAboutUs);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
