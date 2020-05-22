package com.example.employeemanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyAdapterAttendence extends RecyclerView.Adapter<MyViewHolderAttendence> implements Filterable {

    ArrayList<UploadData> nameArray;
    Context context;
    DatabaseReference reference;
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date pickdate = new Date();
    String month = monthFormat.format(pickdate);

    String kdate;

    public MyAdapterAttendence(ArrayList<UploadData> nameArray,String kdate, Context context) {
        this.nameArray = nameArray;
        this.kdate = kdate;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolderAttendence onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendence, parent, false);
        MyViewHolderAttendence myViewHolder = new MyViewHolderAttendence(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderAttendence holder, int position) {
        final UploadData uploadData = nameArray.get(position);
        Picasso.get().load(uploadData.getProfile())
                .fit()
                .centerCrop()
                .into(holder.profile);
        holder.name.setText(uploadData.getName());
        holder.gmail.setText(uploadData.getMobile());
        reference = FirebaseDatabase.getInstance().getReference("Attendence");

        holder.present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadAttendence attendence = new UploadAttendence("Present","present");
                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence);
                Toasty.success(context, "" + holder.present.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Select Leave Type");
                String[] items = {"Sick Leave", "Casual Leave", "Annual Leave", "Leave Without Pay"};
                int checkedItem = -1;
                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                UploadAttendence attendence = new UploadAttendence("Sick Leave","leave");
                                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence);
                                Toasty.success(context, "Sick Leave", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                break;
                            case 1:
                                UploadAttendence attendence1 = new UploadAttendence("Casual Leave","leave");
                                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence1);
                                Toasty.success(context, "Casual Leave", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                break;
                            case 2:
                                UploadAttendence attendence2 = new UploadAttendence("Annual Leave","leave");
                                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence2);
                                Toasty.success(context, "Annual Leave", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                break;
                            case 3:
                                UploadAttendence attendence3 = new UploadAttendence("Leave Without Pay","leave");
                                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence3);
                                Toasty.success(context, "Leave Without Pay", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });
        holder.half_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadAttendence attendence = new UploadAttendence("Half-Day","half_day");
                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence);
                Toasty.success(context, "" + holder.half_day.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Select Late Type");
                String[] items = {"30 minute Late", "1 hour Late", "2 hour Late", "3 hour Late"};
                int checkedItem = -1;
                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                UploadAttendence attendence = new UploadAttendence("30 minute Late","late");
                                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence);
                                Toasty.success(context, "30 minute Late", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                break;
                            case 1:
                                UploadAttendence attendence1 = new UploadAttendence("1 hour Late","late");
                                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence1);
                                Toasty.success(context, "1 hour Late", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                break;
                            case 2:
                                UploadAttendence attendence2 = new UploadAttendence("2 hour Late","late");
                                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence2);
                                Toasty.success(context, "2 hour Late", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                break;
                            case 3:
                                UploadAttendence attendence3 = new UploadAttendence("3 hour Late","late");
                                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence3);
                                Toasty.success(context, "3 hour Late", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });
        holder.absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadAttendence attendence = new UploadAttendence("Absent","absent");
                reference.child(month).child(uploadData.getMobile()).child(kdate).setValue(attendence);
                Toasty.success(context, holder.absent.getText(), Toast.LENGTH_SHORT).show();
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
