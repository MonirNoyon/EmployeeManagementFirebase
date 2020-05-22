package com.example.employeemanagement;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ProfileAdmin extends Fragment {

    View view;
    DatabaseReference reference;
    ImageView profile,signeture,qrcode;
    TextView name,emp_id,bloodgroup,phon,email,address,nid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profileadmin, container, false);
        initReference();

        reference = FirebaseDatabase.getInstance().getReference("Admin").child("-M4UvF6TLIrCyrIN3ZWn");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pro= dataSnapshot.child("profile").getValue().toString();
                Uri pro_url = Uri.parse(pro);
                Picasso.get().load(pro_url).into(profile);
                String sig = dataSnapshot.child("signeture").getValue().toString();
                Uri sig_url = Uri.parse(sig);
                Picasso.get().load(sig_url).into(signeture);
                String qr = dataSnapshot.child("qrcode").getValue().toString();
                Uri qr_url = Uri.parse(qr);
                Picasso.get().load(qr_url).into(qrcode);
                name.setText(dataSnapshot.child("name").getValue().toString());
                emp_id.setText(dataSnapshot.child("employeeid").getValue().toString());
                bloodgroup.setText(dataSnapshot.child("bloodgroup").getValue().toString());
                phon.setText(dataSnapshot.child("mobile").getValue().toString());
                email.setText(dataSnapshot.child("email").getValue().toString());
                address.setText(dataSnapshot.child("address").getValue().toString());
                nid.setText(dataSnapshot.child("nid").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    public void initReference(){
        profile = (ImageView)view.findViewById(R.id.profile_id);
        signeture = (ImageView)view.findViewById(R.id.signeture_id);
        qrcode = (ImageView)view.findViewById(R.id.qrcode_id);
        name = (TextView)view.findViewById(R.id.profle_nametxtid);
        emp_id = (TextView)view.findViewById(R.id.profle_emptxtid);
        bloodgroup = (TextView)view.findViewById(R.id.profle_bloodtxtid);
        phon = (TextView)view.findViewById(R.id.profle_mobiletxtid);
        email = (TextView)view.findViewById(R.id.profle_gmailtxtid);
        address = (TextView)view.findViewById(R.id.profle_addresstxtid);
        nid = (TextView)view.findViewById(R.id.profle_nidtxtid);
    }
}
