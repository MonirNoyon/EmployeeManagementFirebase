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

public class EmployeeManage extends Fragment {

    View view;
    private ArrayList<UploadData> uploadList;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private MyAdapterEmployee myAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.employee_manage, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.employee_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        uploadList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("uploads");
        reference.orderByChild("status").equalTo("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UploadData uploadData = dataSnapshot1.getValue(UploadData.class);
                    uploadList.add(uploadData);
                }
                myAdapter = new MyAdapterEmployee(uploadList, getContext());
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
