package com.example.pk.docsappnitt;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class InboxTab2Pharma extends Fragment{
    private static final String TAB="Tab2Fragment";
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private MessageToDoctor msgToDoctor;
    FirebaseRecyclerAdapter<MessageToDoctor,Inbox.ViewHolder> firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.inboxtab2pharma,container,false);
        swipe=view.findViewById(R.id.swipe);
        return view;
    }
}
