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

public class DocumentsActivity extends Fragment {

    View view;
    DatabaseReference reference;
    ArrayList<UploadFile> document_list;
    MyAdapterDocuments adapterDocuments;
    RecyclerView recyclerView;
    public DocumentsActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.document_activity, container, false);
        DetailsActivity activity = (DetailsActivity)getActivity();
        final String designition = activity.getDesignition;
        Toast.makeText(getContext(), ""+designition, Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView)view.findViewById(R.id.documentemployee_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        document_list = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Uploaded_Files");
        reference.orderByChild("file_type").equalTo(designition).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    UploadFile file = dataSnapshot1.getValue(UploadFile.class);
                    document_list.add(file);
                }
                adapterDocuments = new MyAdapterDocuments(document_list,getContext(),"employee");
                recyclerView.setAdapter(adapterDocuments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
