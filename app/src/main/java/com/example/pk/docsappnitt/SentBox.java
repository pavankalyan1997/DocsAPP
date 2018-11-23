package com.example.pk.docsappnitt;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SentBox extends AppCompatActivity {

    private TextView mName;
    String name;
    Uri photoUrl;
    ImageView mPic;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    NavigationView navigationView;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private MessageToDoctor msgToDoctor;
    FirebaseRecyclerAdapter<MessageToDoctor,Inbox.ViewHolder> firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_box);

        drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayoutSentBox);
        Toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationViewSentBox);


        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.pronameHeader);
        mPic= (ImageView)navigationView.getHeaderView(0).findViewById(R.id.proImgHeader);
        getCurrentinfo();

        swipe=(SwipeRefreshLayout)findViewById(R.id.swipe);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SentBox.this));
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("SentBox");
        databaseReference.keepSynced(true);

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<MessageToDoctor, Inbox.ViewHolder>(MessageToDoctor.class
                ,R.layout.messagetodoctor,Inbox.ViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(Inbox.ViewHolder viewHolder, final MessageToDoctor model, int position) {
                viewHolder.txtView.setText(model.getFrom());
                viewHolder.txtTime.setText(model.getTime());
                viewHolder.txtSubject.setText(model.getSubject());
                viewHolder.txtDate.setText(model.getDate());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(SentBox.this,MessageViewComplete.class);
                        intent.putExtra("SubjectKey",model.getSubject());
                        intent.putExtra("PtNameKey",model.getPatientName());
                        intent.putExtra("DateKey",model.getDate());
                        intent.putExtra("TimeKey",model.getTime());
                        intent.putExtra("PatientNameKey",model.getPatientName());
                        intent.putExtra("MobileKey",model.getPatientPhoneNumber());
                        intent.putExtra("GenderKey",model.getGender());
                        intent.putExtra("BloodGroupKey",model.getBloodGroup());
                        intent.putExtra("ProblemKey",model.getProblem());
                        intent.putExtra("AppointmentTimeKey",model.getAppointmentTime());
                        intent.putExtra("PatientIdKey",model.getPatientId());
                        intent.putExtra("AddressKey",model.getAddress());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public MessageToDoctor getItem(int position) {
                return super.getItem(getItemCount()-1-position);
            }

            @Override
            protected void onDataChanged() {
                firebaseRecyclerAdapter.notifyDataSetChanged();
                super.onDataChanged();
            }
        };
        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                firebaseRecyclerAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
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

                    case R.id.edtInbox:
                        Inbox();
                        break;

                }
                return false;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtView,txtTime,txtSubject,txtDate;
        View view;
        public ViewHolder(View itemView){
            super(itemView);
            txtView=(TextView)itemView.findViewById(R.id.From);
            txtTime=(TextView)itemView.findViewById(R.id.Time);
            txtDate=(TextView)itemView.findViewById(R.id.Date);
            txtSubject=(TextView)itemView.findViewById(R.id.Subject);
            view=itemView;
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

    private void Inbox(){
        startActivity(new Intent(this,Inbox.class));
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

}
