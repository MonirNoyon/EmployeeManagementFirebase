package com.example.employeemanagement;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class HomeActivity extends Fragment {
    View view;
    private DatabaseReference mReference;
    private TextView nametext,shopname,area,address;
    private CircleImageView profile;

    public HomeActivity(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_activity, container, false);
        //get data
        DetailsActivity activity = (DetailsActivity)getActivity();
        final String data = activity.values;

        nametext = (TextView)view.findViewById(R.id.nametxtViewid);
        shopname = (TextView)view.findViewById(R.id.shoptext_id);
        area = (TextView)view.findViewById(R.id.area_id);
        address = (TextView)view.findViewById(R.id.address_id);
        profile = (CircleImageView) view.findViewById(R.id.profileimage_id);

        mReference = FirebaseDatabase.getInstance().getReference("uploads").child(data);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nametext.setText(dataSnapshot.child("name").getValue().toString());
                shopname.setText(dataSnapshot.child("shop").getValue().toString());
                area.setText(dataSnapshot.child("workplace").getValue().toString());
                address.setText(dataSnapshot.child("address").getValue().toString());
                String dat = dataSnapshot.child("profile").getValue().toString();
                Uri myUri = Uri.parse(dat);
                Picasso.get().load(myUri).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
