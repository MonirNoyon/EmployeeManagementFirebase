package com.example.employeemanagement;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    ArrayList<UploadData> nameArray;
    Context context;
    DatabaseReference reference;
    String getKey;

    public MyAdapter(ArrayList<UploadData> nameArray, Context context) {
        this.nameArray = nameArray;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final UploadData uploadData = nameArray.get(position);
        Picasso.get().load(uploadData.getProfile())
                .fit()
                .centerCrop()
                .into(holder.profile);
        holder.nameText.setText(uploadData.getName());
        holder.number.setText(uploadData.getMobile());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("uploads");
                reference.orderByChild("email").equalTo(uploadData.getEmail()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            getKey = dataSnapshot1.getKey();
                        }
                       if (getKey != null){
                           reference.child(getKey).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                                   alertBuilder.setTitle(uploadData.getName()+" Deatails");
                                   alertBuilder.setMessage("Name: " + dataSnapshot.child("name").getValue().toString() + "\n" +
                                           "Designition: " + dataSnapshot.child("designition").getValue().toString() + "\n" +
                                           "Blood Group: " + dataSnapshot.child("bloodgroup").getValue().toString() + "\n" +
                                           "Phone Number: " + dataSnapshot.child("mobile").getValue().toString() + "\n" +
                                           "Email: " + dataSnapshot.child("email").getValue().toString() + "\n" +
                                           "Address: " + dataSnapshot.child("address").getValue().toString() + "\n" +
                                           "NID: " + dataSnapshot.child("nid").getValue().toString() + "\n" +
                                           "Workplace: " + dataSnapshot.child("workplace").getValue().toString() + "\n"+
                                           "Birth Day: "+dataSnapshot.child("birthdate").getValue().toString()+"\n"+
                                           "Employee ID: "+dataSnapshot.child("employeeid").getValue().toString()+"\n"+
                                           "Joining Date: "+dataSnapshot.child("joindate").getValue().toString()+"\n"+
                                           "Shop Name: "+dataSnapshot.child("shop").getValue().toString()+"\n"
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
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("Delete Employee");
                alertBuilder.setMessage("Do you want to delete " + uploadData.getName() + " ?");
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference = FirebaseDatabase.getInstance().getReference("uploads");
                        reference.orderByChild("email").equalTo(uploadData.getEmail()).
                                addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            dataSnapshot1.getRef().removeValue();
                                        }
                                        Toast.makeText(context, uploadData.getName()+"deleted successfully", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = uploadData.getMobile();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number));
                context.startActivity(callIntent);
            }
        });
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = uploadData.getMobile();  // The number on which you want to send SMS
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            }
        });
    }


    @Override
    public int getItemCount() {
        return nameArray.size();
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UploadData> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(nameArray);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (UploadData item : nameArray) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            nameArray.clear();
            nameArray.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
