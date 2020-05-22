package com.example.employeemanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttendenceActivity extends Fragment {
    private Spinner spinner_month;
    View view;
    TextView present, leave, half_day, late, absent,footer;
    String attend_type, datate;
    String montharray[] = {"Select Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    final List<String> monthList = new ArrayList<String>(Arrays.asList(montharray));
    DatabaseReference reference1, reference2, reference,headerFooterReference,referenceAttendence;
    int present_Count, leave_Count, half_Count, late_Count, absent_Count, ismonthAvailable;
    String data, demeded, getSpinnerMonth,getKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.attendence_activity, container, false);
        initReference();
        headerFooterReference = FirebaseDatabase.getInstance().getReference("Header_Footer").child("Admin");
        headerFooterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                footer.setText("@ "+dataSnapshot.child("footer").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DetailsActivity activity = (DetailsActivity) getActivity();
        data = activity.getMail;
        spinner_month = (Spinner) view.findViewById(R.id.spinner_monthslctid);

        final ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.categorry_spinner, monthList) {
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
        spinner_month.setAdapter(monthAdapter);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getSpinnerMonth = (String) adapterView.getItemAtPosition(i);
                    getSelectedData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        attendenceTypeClick();
        return view;
    }

    private void attendenceTypeClick() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view1 = getLayoutInflater().inflate(R.layout.recover_attendence,null);
        final TextView attendence = (TextView)view1.findViewById(R.id.rec_attend_txt);
        alert.setView(view1);
        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = alert.create();
        late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendence.setText("Your Attendence \n\n");
                reference1 = FirebaseDatabase.getInstance().getReference("Attendence").child(getSpinnerMonth).child(demeded);
                reference1.orderByChild("type").equalTo("late").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            getKey=dataSnapshot1.getKey();
                            attendence.append(getKey+" ------------> "+dataSnapshot1.child("attend_type").getValue().toString()+"\n\n");
                            Toast.makeText(getContext(), ""+getKey, Toast.LENGTH_SHORT).show();
                        }
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendence.setText("Your Attendence \n\n");
                reference1 = FirebaseDatabase.getInstance().getReference("Attendence").child(getSpinnerMonth).child(demeded);
                reference1.orderByChild("type").equalTo("present").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            getKey=dataSnapshot1.getKey();
                            attendence.append(getKey+" ------------> "+dataSnapshot1.child("attend_type").getValue().toString()+"\n\n");
                            Toast.makeText(getContext(), ""+getKey, Toast.LENGTH_SHORT).show();
                        }
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendence.setText("Your Attendence \n\n");
                reference1 = FirebaseDatabase.getInstance().getReference("Attendence").child(getSpinnerMonth).child(demeded);
                reference1.orderByChild("type").equalTo("leave").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            getKey=dataSnapshot1.getKey();
                            attendence.append(getKey+" ------------> "+dataSnapshot1.child("attend_type").getValue().toString()+"\n\n");
                            Toast.makeText(getContext(), ""+getKey, Toast.LENGTH_SHORT).show();
                        }
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        half_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendence.setText("Your Attendence \n\n");
                reference1 = FirebaseDatabase.getInstance().getReference("Attendence").child(getSpinnerMonth).child(demeded);
                reference1.orderByChild("type").equalTo("half_day").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            getKey=dataSnapshot1.getKey();
                            attendence.append(getKey+" ------------> "+dataSnapshot1.child("attend_type").getValue().toString()+"\n\n");
                            Toast.makeText(getContext(), ""+getKey, Toast.LENGTH_SHORT).show();
                        }
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendence.setText("Your Attendence \n\n");
                reference1 = FirebaseDatabase.getInstance().getReference("Attendence").child(getSpinnerMonth).child(demeded);
                reference1.orderByChild("type").equalTo("absent").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            getKey = dataSnapshot1.getKey();
                            attendence.append(getKey + " ------------> " + dataSnapshot1.child("attend_type").getValue().toString() + "\n\n");
                        }
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void initReference() {
        present = (TextView) view.findViewById(R.id.presenttextview);
        half_day = (TextView) view.findViewById(R.id.halfdaytextview);
        late = (TextView) view.findViewById(R.id.latetextview);
        absent = (TextView) view.findViewById(R.id.absenttextview);
        leave = (TextView) view.findViewById(R.id.leavetextview);
        footer = (TextView) view.findViewById(R.id.vivoteam_id);
    }

    private void getSelectedData() {

        reference = FirebaseDatabase.getInstance().getReference("Attendence");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    datate = dataSnapshot1.getKey();
                    if (datate.equals(getSpinnerMonth)) {
                        ismonthAvailable = 1;
                    }
                }
                reference2 = FirebaseDatabase.getInstance().getReference("uploads");
                reference2.orderByChild("email").equalTo(data).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            demeded = dataSnapshot1.child("mobile").getValue().toString();
                        }
                        if (ismonthAvailable == 1) {
                            reference1 = FirebaseDatabase.getInstance().getReference("Attendence").child(getSpinnerMonth).child(demeded);
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        attend_type = dataSnapshot1.child("type").getValue().toString();
                                        if (attend_type.equals("present")) {
                                            present_Count++;
                                        } else if (attend_type.equals("leave")) {
                                            leave_Count++;
                                        } else if (attend_type.equals("half_day")) {
                                            half_Count++;
                                        } else if (attend_type.equals("late")) {
                                            late_Count++;
                                        } else if (attend_type.equals("absent")) {
                                            absent_Count++;
                                        }
                                    }
                                    present.setText("Present\n" + present_Count);
                                    leave.setText("Leave\n" + leave_Count);
                                    half_day.setText("Half-Day\n" + half_Count);
                                    late.setText("Late\n" + late_Count);
                                    absent.setText("Absent\n" + absent_Count);
                                    present_Count = 0;
                                    leave_Count = 0;
                                    half_Count = 0;
                                    late_Count = 0;
                                    absent_Count = 0;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            ismonthAvailable = 0;
                        } else {
                            Toast.makeText(getContext(), "not yet uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
