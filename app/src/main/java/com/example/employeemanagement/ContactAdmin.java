package com.example.employeemanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ContactAdmin extends Fragment {

    View view;
    private ArrayList<UploadData> uploadList;
    private DatabaseReference reference;
    private RecyclerView recyclerView;

    private MyAdapter myAdapter;
    private Spinner contact_category_spinner;
    private String getCategory;
    SearchView search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contactadmin, container, false);
        contact_category_spinner=(Spinner) view.findViewById(R.id.contact_spinner_id);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_contactid);
        search = (SearchView)view.findViewById(R.id.searchView);
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        uploadList = new ArrayList<>();
        contactSpinner();
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

    private void contactSpinner() {
        String names[] = {"Select Category", "Office", "Market", "Patuakhali", "Barishal", "Bhola", "Jhalakhati", "Barguna", "Pirojpur"};
        final List<String> namelist = new ArrayList<>(Arrays.asList(names));
        final ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(getContext(), R.layout.categorry_spinner, namelist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        contact_category_spinner.setAdapter(category_adapter);
        contact_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getCategory = (String) adapterView.getItemAtPosition(i);
                    reference = FirebaseDatabase.getInstance().getReference("uploads");
                    reference.orderByChild("category").equalTo(getCategory).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            uploadList.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                UploadData uploadData = dataSnapshot1.getValue(UploadData.class);
                                uploadList.add(uploadData);
                            }
                            CategoryData();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toasty.info(getContext(),"select category",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void CategoryData(){
        reference.orderByChild("status").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myAdapter = new MyAdapter(uploadList,getContext());
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
