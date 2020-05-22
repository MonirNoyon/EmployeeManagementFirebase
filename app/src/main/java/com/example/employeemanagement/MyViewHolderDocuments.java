package com.example.employeemanagement;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderDocuments extends RecyclerView.ViewHolder {
    ImageView doc_image,download,cancel;
    TextView name;
    public MyViewHolderDocuments(@NonNull View itemView) {
        super(itemView);
       doc_image =(ImageView) itemView.findViewById(R.id.docimage_id);
       cancel =(ImageView) itemView.findViewById(R.id.delete_doc);
       download =(ImageView) itemView.findViewById(R.id.downld_ic);
       name = (TextView) itemView.findViewById(R.id.docnameid);

    }
}
