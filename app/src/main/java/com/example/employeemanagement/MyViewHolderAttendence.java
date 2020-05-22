package com.example.employeemanagement;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolderAttendence extends RecyclerView.ViewHolder {
    CircleImageView profile;
    TextView name,gmail;
    RadioGroup radioGroup;
    RadioButton present,leave,half_day,late,absent;
    public MyViewHolderAttendence(@NonNull View itemView) {
        super(itemView);
       profile = itemView.findViewById(R.id.prof_imageid);
       name = itemView.findViewById(R.id.item_name);
       gmail = itemView.findViewById(R.id.phone_number);
       radioGroup = itemView.findViewById(R.id.radioattendence_group);
       present = (RadioButton)itemView.findViewById(R.id.present_radio_id);
       leave = (RadioButton)itemView.findViewById(R.id.leave_radio_id);
       half_day = (RadioButton)itemView.findViewById(R.id.half_radio_id);
       late = (RadioButton)itemView.findViewById(R.id.late_radio_id);
       absent = (RadioButton)itemView.findViewById(R.id.absent_radio_id);
    }
}
