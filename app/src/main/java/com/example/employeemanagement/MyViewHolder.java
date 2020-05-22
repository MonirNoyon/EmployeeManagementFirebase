package com.example.employeemanagement;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profile;
    ImageView call,message,delete;
    TextView nameText,number;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        profile = itemView.findViewById(R.id.pro_imageid);
        nameText = itemView.findViewById(R.id.item_list);
        number = itemView.findViewById(R.id.number);
        delete = itemView.findViewById(R.id.delete_employee_contact);
        call =(ImageView) itemView.findViewById(R.id.call_contactid);
        message = (ImageView) itemView.findViewById(R.id.message_contactid);
    }
}
