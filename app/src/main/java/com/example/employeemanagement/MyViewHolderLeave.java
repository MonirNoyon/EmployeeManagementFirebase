package com.example.employeemanagement;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderLeave extends RecyclerView.ViewHolder {
    ImageView download,delete;
    TextView doc,empname;
    public MyViewHolderLeave(@NonNull View itemView) {
        super(itemView);
       download =(ImageView) itemView.findViewById(R.id.downld_ic);
       delete =(ImageView) itemView.findViewById(R.id.delet_leave);
       doc = (TextView) itemView.findViewById(R.id.leave_docname);
       empname =(TextView)itemView.findViewById(R.id.leaveemp_nameid);
    }
}
