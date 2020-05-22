package com.example.employeemanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaveAdmin extends Fragment {
    View view;
    DatabaseReference reference;
    ArrayList<UploadFile> leave_list;
    MyAdapterLeave adapterLeave;
    RecyclerView recyclerView;

    public LeaveAdmin(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.leaveadmin, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.leave_recycleid);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        leave_list = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Upload_leaveFile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    UploadFile file = dataSnapshot1.getValue(UploadFile.class);
                    leave_list.add(file);
                }
                adapterLeave = new MyAdapterLeave(leave_list,getContext());
                recyclerView.setAdapter(adapterLeave);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
