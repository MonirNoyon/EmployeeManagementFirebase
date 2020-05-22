package com.example.employeemanagement;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolderEmployee extends RecyclerView.ViewHolder {
    CircleImageView profile;
    ImageView add,delete;
    TextView nameText,category;
    public MyViewHolderEmployee(@NonNull View itemView) {
        super(itemView);
        profile = itemView.findViewById(R.id.pro_imageid);
        nameText = itemView.findViewById(R.id.item_list);
        category = itemView.findViewById(R.id.categorytext);
        add =(ImageView) itemView.findViewById(R.id.add_epmloyee);
        delete = (ImageView) itemView.findViewById(R.id.delete_employee);
    }
}
