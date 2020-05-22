package com.example.employeemanagement;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class AttendenceAdmin extends Fragment {


    View view;
    FloatingActionButton addDate;
    private ArrayList<UploadData> uploadList;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private MyAdapterAttendence myAdapter;
    String date ;
    SearchView search;

    public AttendenceAdmin() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.attendenceadmin, container, false);
        Toasty.info(getContext(),"Please select date",Toast.LENGTH_SHORT).show();
        addDate = (FloatingActionButton)view.findViewById(R.id.addattendenceDate);
        search = (SearchView)view.findViewById(R.id.searchView);

        recyclerView = (RecyclerView) view.findViewById(R.id.attendence_recycleid);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        uploadList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("uploads");
        reference.orderByChild("status").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UploadData uploadData = dataSnapshot1.getValue(UploadData.class);
                    uploadList.add(uploadData);
                }
                addDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.Theme_Design_BottomSheetDialog,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        month = month + 1;
                                        String date = day + "-" + month + "-" + year;
                                        myAdapter = new MyAdapterAttendence(uploadList,date,getContext());
                                        recyclerView.setAdapter(myAdapter);
                                    }
                                }, year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                        dialog.show();


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return true;
            }
        });
        return view;
    }
}
