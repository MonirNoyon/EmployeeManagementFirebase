package com.example.employeemanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MyAdapterEmployee extends RecyclerView.Adapter<MyViewHolderEmployee> {

    ArrayList<UploadData> nameArray;
    Context context;
    DatabaseReference reference;
    String generategmailkey,getGenerategmailkey_item;

    public MyAdapterEmployee(ArrayList<UploadData> nameArray, Context context) {
        this.nameArray = nameArray;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolderEmployee onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee_list, parent, false);
        MyViewHolderEmployee myViewHolder = new MyViewHolderEmployee(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderEmployee holder, int position) {
        final UploadData uploadData = nameArray.get(position);
        Picasso.get().load(uploadData.getProfile())
                .fit()
                .centerCrop()
                .into(holder.profile);
        holder.nameText.setText(uploadData.getName());
        holder.category.setText(uploadData.getDesignition());
        reference = FirebaseDatabase.getInstance().getReference("uploads");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                reference.orderByChild("email").equalTo(uploadData.getEmail()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            getGenerategmailkey_item = dataSnapshot1.getKey();
                        }
                        if (getGenerategmailkey_item != null) {
                            reference.child(getGenerategmailkey_item).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                                    alertBuilder.setTitle("Employee Deatails");
                                    alertBuilder.setMessage("Name: " + dataSnapshot.child("name").getValue().toString() + "\n" +
                                            "Designition: " + dataSnapshot.child("designition").getValue().toString() + "\n" +
                                            "Blood Group: " + dataSnapshot.child("bloodgroup").getValue().toString() + "\n" +
                                            "Phone Number: " + dataSnapshot.child("mobile").getValue().toString() + "\n" +
                                            "Email: " + dataSnapshot.child("email").getValue().toString() + "\n" +
                                            "Address: " + dataSnapshot.child("address").getValue().toString() + "\n" +
                                            "NID: " + dataSnapshot.child("nid").getValue().toString() + "\n" +
                                            "Workplace: " + dataSnapshot.child("workplace").getValue().toString() + "\n"
                                    );
                                    alertBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    alertBuilder.setCancelable(false);
                                    AlertDialog alert = alertBuilder.create();
                                    alert.show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.orderByChild("email").equalTo(uploadData.getEmail()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            generategmailkey = dataSnapshot1.getKey();
                        }
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("status", "1");
                        reference.child(generategmailkey).updateChildren(updates);
                        Toast.makeText(context, "Added ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("Alert");
                alertBuilder.setMessage("Do you want to delete "+uploadData.getName()+" ?");
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Query apply_query = reference.orderByChild("email").equalTo(uploadData.getEmail());
                        apply_query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    dataSnapshot1.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toasty.success(context,"Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameArray.size();
    }
}
