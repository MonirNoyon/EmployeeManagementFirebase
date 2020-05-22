package com.example.employeemanagement;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdmin extends Fragment {

    View view;
    CircleImageView profile;
    TextView nameAdmin,gmailAdmin,phoneAdmin,addressAdmin;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homeadmin, container, false);
        reference = FirebaseDatabase.getInstance().getReference("Admin").child("-M4UvF6TLIrCyrIN3ZWn");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dat = dataSnapshot.child("profile").getValue().toString();
                Uri myUri = Uri.parse(dat);
                Picasso.get().load(myUri).into(profile);
                nameAdmin.setText(dataSnapshot.child("name").getValue().toString());
                gmailAdmin.setText(dataSnapshot.child("email").getValue().toString());
                phoneAdmin.setText(dataSnapshot.child("mobile").getValue().toString());
                addressAdmin.setText(dataSnapshot.child("address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        initReference();
        return view;
    }
    public void initReference(){
        profile =(CircleImageView)view.findViewById(R.id.profileadmin_id);
        nameAdmin = (TextView)view.findViewById(R.id.admin_nameid);
        gmailAdmin = (TextView)view.findViewById(R.id.gmailadmin_id);
        phoneAdmin = (TextView)view.findViewById(R.id.adminphone_id);
        addressAdmin = (TextView)view.findViewById(R.id.admin_addressid);
    }
}
