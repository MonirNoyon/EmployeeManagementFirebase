package com.example.employeemanagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterStatus extends RecyclerView.Adapter<MyViewHolderSalary> implements Filterable {
    ArrayList<UploadData> name;
    Context context;

    public MyAdapterStatus(ArrayList<UploadData> name, Context context) {
        this.name = name;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolderSalary onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_salary, parent, false);
        MyViewHolderSalary myViewHolder = new MyViewHolderSalary(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderSalary holder, final int position) {
        UploadData uploadData = name.get(position);
        Picasso.get().load(uploadData.getProfile())
                .fit()
                .centerCrop()
                .into(holder.profile);
        holder.name.setText(uploadData.getName());
        holder.email.setText(uploadData.getEmail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),SingleStatus.class);
                intent.putExtra("mailid",name.get(position).getEmail());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
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
                filteredList.addAll(name);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (UploadData item : name) {
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
            name.clear();
            name.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
