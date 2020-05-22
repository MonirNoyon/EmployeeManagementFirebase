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

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends Fragment {

    View view;
    ImageView profile,signeture,qrcode;
    TextView name,designation,emp_id,joiningdate,birthdate,bloodgroup,phon,email,address,nid;
    private DatabaseReference reference;
    public ProfileActivity(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_activity, container, false);

        DetailsActivity activity = (DetailsActivity)getActivity();
        final String data = activity.values;

        profile = (ImageView)view.findViewById(R.id.profile_id);
        signeture = (ImageView)view.findViewById(R.id.signeture_id);
        qrcode = (ImageView)view.findViewById(R.id.qrcode_id);
        name = (TextView)view.findViewById(R.id.profle_nametxtid);
        designation = (TextView)view.findViewById(R.id.profle_desigtxtid);
        emp_id = (TextView)view.findViewById(R.id.profle_emptxtid);
        joiningdate = (TextView)view.findViewById(R.id.profle_jointxtid);
        birthdate = (TextView)view.findViewById(R.id.profle_birthtxtid);
        bloodgroup = (TextView)view.findViewById(R.id.profle_bloodtxtid);
        phon = (TextView)view.findViewById(R.id.profle_mobiletxtid);
        email = (TextView)view.findViewById(R.id.profle_gmailtxtid);
        address = (TextView)view.findViewById(R.id.profle_addresstxtid);
        nid = (TextView)view.findViewById(R.id.profle_nidtxtid);
        reference = FirebaseDatabase.getInstance().getReference("uploads").child(data);
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
                designation.setText(dataSnapshot.child("designition").getValue().toString());
                emp_id.setText(dataSnapshot.child("employeeid").getValue().toString());
                joiningdate.setText(dataSnapshot.child("joindate").getValue().toString());
                birthdate.setText(dataSnapshot.child("birthdate").getValue().toString());
                bloodgroup.setText(dataSnapshot.child("bloodgroup").getValue().toString());
                phon.setText(dataSnapshot.child("mobile").getValue().toString());
                email.setText(dataSnapshot.child("email").getValue().toString());
                address.setText(dataSnapshot.child("address").getValue().toString());
                nid.setText(dataSnapshot.child("nid").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getContext(),""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
