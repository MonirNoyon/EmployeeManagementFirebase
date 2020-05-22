package com.example.employeemanagement;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolderSalary extends RecyclerView.ViewHolder {
    CircleImageView profile;
    TextView name,email;
    public MyViewHolderSalary(@NonNull View itemView) {
        super(itemView);
       profile =(CircleImageView) itemView.findViewById(R.id.salry_proimg);
       name = (TextView) itemView.findViewById(R.id.salary_namelist);
       email = (TextView) itemView.findViewById(R.id.salary_gmail);
    }
}
