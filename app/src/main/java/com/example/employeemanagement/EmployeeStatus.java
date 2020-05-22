package com.example.employeemanagement;

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

public class EmployeeStatus extends Fragment {

    View view;
    TextView emp_type,team, appointment, id, visitingcard, t_shirt, wechat_account, v2account, job_confirmation, bank, tin;
    DatabaseReference reference;
    String getUniqueKey;
    private DatabaseReference referenceheaderfooter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.employee_status, container, false);
        initReference();
        DetailsActivity activity = (DetailsActivity) getActivity();
        String getkey = activity.getMail;
        referenceheaderfooter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                team.setText("@ "+dataSnapshot.child("footer").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.orderByChild("email").equalTo(getkey).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    getUniqueKey = dataSnapshot1.getKey();
                }
                if (getUniqueKey != null) {
                    reference.child(getUniqueKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("emp_type").getValue().toString().equals("Permanent")) {
                                emp_type.setText("Permanent");
                                emp_type.setBackgroundColor(getResources().getColor(R.color.back));
                            } else {
                            }
                            if (dataSnapshot.child("appointment").getValue().toString().equals("Yes")) {
                                appointment.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                            if (dataSnapshot.child("id").getValue().toString().equals("Yes")) {
                                id.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                            if (dataSnapshot.child("visiting_card").getValue().toString().equals("Yes")) {
                                visitingcard.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                            if (dataSnapshot.child("t_shirt").getValue().toString().equals("Yes")) {
                                t_shirt.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                            if (dataSnapshot.child("wechat_account").getValue().toString().equals("Yes")) {
                                wechat_account.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                            if (dataSnapshot.child("v2_account").getValue().toString().equals("Yes")) {
                                v2account.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                            if (dataSnapshot.child("job_confirmation").getValue().toString().equals("Yes")) {
                                job_confirmation.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                            if (dataSnapshot.child("bank_account").getValue().toString().equals("Yes")) {
                                bank.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                            if (dataSnapshot.child("tin_number").getValue().toString().equals("Yes")) {
                                tin.setBackground(getContext().getResources().getDrawable(R.drawable.emp_green_status));
                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Not Inserted yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    public void initReference() {
        emp_type = (TextView) view.findViewById(R.id.emp_type);
        appointment = (TextView) view.findViewById(R.id.text_appointment);
        id = (TextView) view.findViewById(R.id.text_idcard);
        visitingcard = (TextView) view.findViewById(R.id.textvisiting_card);
        t_shirt = (TextView) view.findViewById(R.id.texttshirtid);
        wechat_account = (TextView) view.findViewById(R.id.textwechataccount);
        v2account = (TextView) view.findViewById(R.id.textv2account);
        job_confirmation = (TextView) view.findViewById(R.id.textjobconfirmation);
        bank = (TextView) view.findViewById(R.id.textbankaccount);
        team = (TextView)view.findViewById(R.id.vivoteam_id);
        tin = (TextView) view.findViewById(R.id.texttinnumber);
        reference =FirebaseDatabase.getInstance().getReference("Employee_Status");
        referenceheaderfooter =FirebaseDatabase.getInstance().getReference("Header_Footer").child("Admin");
    }
}
