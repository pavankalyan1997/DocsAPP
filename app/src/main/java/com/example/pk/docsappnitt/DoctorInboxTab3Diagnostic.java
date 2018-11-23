package com.example.pk.docsappnitt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DoctorInboxTab3Diagnostic extends Fragment{
    private static final String TAB="Tab1Fragment";
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private MessageToDoctor msgToDoctor;
    FirebaseRecyclerAdapter<MessageToDiagnostician, ViewHolder> firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.doctorinboxtab1patient,container,false);
        swipe=view.findViewById(R.id.swipe);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager();
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("DoctorInBox").child("Diagnostician");
        databaseReference.keepSynced(true);

        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<MessageToDiagnostician, ViewHolder>(MessageToDiagnostician.class
                ,R.layout.messagetodoctor,ViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, final MessageToDiagnostician model, int position) {
                viewHolder.txtView.setText(model.getDoctorName());
                viewHolder.txtTime.setText(model.getTime());
                viewHolder.txtSubject.setText(model.getSubject());
                viewHolder.txtDate.setText(model.getDate());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(),MessageViewForDiagnosticianFromDoctor.class);
                        intent.putExtra("AgeKey",model.getPtAge());
                        intent.putExtra("SubjectKey",model.getSubject());
                        intent.putExtra("PtNameKey",model.getPtName());
                        intent.putExtra("DateKey",model.getDate());
                        intent.putExtra("TimeKey",model.getTime());
                        intent.putExtra("MobileKey",model.getPtMobile());
                        intent.putExtra("GenderKey",model.getPtGender());
                        intent.putExtra("BloodGroupKey",model.getPtBloodGroup());
                        intent.putExtra("ProblemKey",model.getPtProblem());
                        intent.putExtra("AddressKey",model.getPtAddress());
                        intent.putExtra("DiagTestsKey",model.getDiagTests());
                        intent.putExtra("DoctorNameKey",model.getDoctorName());
                        intent.putExtra("RemarksKey",model.getRemarks());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public MessageToDiagnostician getItem(int position) {
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


        return view;
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


}
