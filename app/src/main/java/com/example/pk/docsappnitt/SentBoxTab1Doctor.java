package com.example.pk.docsappnitt;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SentBoxTab1Doctor extends Fragment{
    private static final String TAB="Tab1Fragment";
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private MessageToDoctor msgToDoctor;
    FirebaseRecyclerAdapter<MessageToDoctor, ViewHolder> firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipe;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sentboxtab1doctor,container,false);
        swipe=view.findViewById(R.id.swipe);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("SentBox").child("Doctor");
        databaseReference.keepSynced(true);
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<MessageToDoctor, ViewHolder>(MessageToDoctor.class
                ,R.layout.messagetodoctor,ViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final MessageToDoctor model, final int position) {
                viewHolder.txtView.setText(model.getFrom());
                viewHolder.txtTime.setText(model.getTime());
                viewHolder.txtSubject.setText(model.getSubject());
                viewHolder.txtDate.setText(model.getDate());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(),MessageViewComplete.class);
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

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder a_builder=new AlertDialog.Builder(getContext());
                        a_builder.setMessage("Do you want to delete this message "+String.valueOf(getId()))
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        DatabaseReference db=databaseReference;
                                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                int count=0;
                                                for(DataSnapshot dp:dataSnapshot.getChildren()){
                                                    if(count==dataSnapshot.getChildrenCount()-1-position){
                                                        dp.getRef().removeValue();
                                                        break;
                                                    }
                                                    else{
                                                        count++;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        db.removeValue();
                                        /*viewHolder.ref.removeValue();
                                        firebaseRecyclerAdapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(),"Message Deleted",Toast.LENGTH_SHORT).show();*/


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alert=a_builder.create();
                        alert.setTitle("Please check your details Once again and Click Yes");
                        alert.show();
                        return false;
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

        /*firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<MessageToDoctor, ViewHolder>(MessageToDoctor.class
                ,R.layout.messagetodoctor,ViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final MessageToDoctor model, int position) {
                viewHolder.txtView.setText(model.getFrom());
                viewHolder.txtTime.setText(model.getTime());
                viewHolder.txtSubject.setText(model.getSubject());
                viewHolder.txtDate.setText(model.getDate());
                //Toast.makeText(getContext(),model.,Toast.LENGTH_SHORT).show();
                //int currPosition=position;
                //viewHolder.ref=getRef(currPosition);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(),MessageViewComplete.class);
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
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder a_builder=new AlertDialog.Builder(getContext());
                        a_builder.setMessage("Do you want to delete this message ")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        viewHolder.ref.removeValue();
                                        firebaseRecyclerAdapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(),"Message Deleted",Toast.LENGTH_SHORT).show();


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alert=a_builder.create();
                        alert.setTitle("Please check your details Once again and Click Yes");
                        alert.show();
                        return false;
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
        };*/
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
        /*if(firebaseRecyclerAdapter.getItemCount()==0){
            view.findViewById(R.id.NoMessages).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.NoMessages).setVisibility(View.GONE);
        }*/

        return view;
    }



    /*public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtView,txtTime,txtSubject,txtDate;
        View view;
        //DatabaseReference ref;
        public ViewHolder(View itemView){
            super(itemView);
            txtView=(TextView)itemView.findViewById(R.id.From);
            txtTime=(TextView)itemView.findViewById(R.id.Time);
            txtDate=(TextView)itemView.findViewById(R.id.Date);
            txtSubject=(TextView)itemView.findViewById(R.id.Subject);
            view=itemView;

        }

    }*/
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
