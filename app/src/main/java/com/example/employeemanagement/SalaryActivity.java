package com.example.employeemanagement;

import android.graphics.Color;
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

import es.dmoral.toasty.Toasty;

public class SalaryActivity extends Fragment {

    View view;
    Spinner monthspinner, yearspinner;
    String montharray[] = {"Select Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String yeararray[] = {"Select Year", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    final List<String> monthList = new ArrayList<String>(Arrays.asList(montharray));
    final List<String> yearList = new ArrayList<String>(Arrays.asList(yeararray));

    TextView basic_salry, house_rent, medical_allwnce, food_allwnce, transport_allwnce, mobile_bill, kpi, incentive, sales_commision, other_addition, tax_deduction, other_deduction, gross,ffooter;

    DatabaseReference reference, reference1,headerFooterReference;
    String generateGmailKey, getSpinnerMonth, getSpinnerYear, data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.salary_activity, container, false);
        monthspinner = (Spinner) view.findViewById(R.id.month_salaryid);
        yearspinner = (Spinner) view.findViewById(R.id.year_salaryid);
        reference = FirebaseDatabase.getInstance().getReference("upload_salary");
        //get Email
        DetailsActivity activity = (DetailsActivity) getActivity();
        data = activity.getMail;

        initReference();

        headerFooterReference = FirebaseDatabase.getInstance().getReference("Header_Footer").child("Admin");
        headerFooterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ffooter.setText("@ "+dataSnapshot.child("footer").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        monthspinner.setAdapter(monthAdapter);
        monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getSpinnerMonth = (String) adapterView.getItemAtPosition(i);
                    if (getSpinnerMonth != null && getSpinnerYear != null) {
                        getSelectedData();
                    } else {
                        Toasty.info(getContext(), "enter year", Toast.LENGTH_LONG).show();
                    }
                } else {
                    getSpinnerMonth = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.categorry_spinner, yearList) {
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
        yearspinner.setAdapter(yearAdapter);
        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getSpinnerYear = (String) adapterView.getItemAtPosition(i);
                    if (getSpinnerMonth != null && getSpinnerYear != null) {
                        getSelectedData();
                    } else {
                        Toasty.info(getContext(), "enter month", Toast.LENGTH_LONG).show();
                    }
                } else {
                    getSpinnerYear = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    public void initReference() {
        basic_salry = (TextView) view.findViewById(R.id.empbasic_salary);
        house_rent = (TextView) view.findViewById(R.id.emphouse_rent);
        medical_allwnce = (TextView) view.findViewById(R.id.emp_medical_allowance);
        food_allwnce = (TextView) view.findViewById(R.id.empfood_allowance);
        transport_allwnce = (TextView) view.findViewById(R.id.emptranspot_allwnce);
        mobile_bill = (TextView) view.findViewById(R.id.empmobil_bill);
        kpi = (TextView) view.findViewById(R.id.empkpi);
        incentive = (TextView) view.findViewById(R.id.emp_incentiveid);
        sales_commision = (TextView) view.findViewById(R.id.empsales_commisionid);
        other_addition = (TextView) view.findViewById(R.id.empotheraddition_id);
        tax_deduction = (TextView) view.findViewById(R.id.emptax_deductionid);
        other_deduction = (TextView) view.findViewById(R.id.empother_deductionid);
        gross = (TextView) view.findViewById(R.id.empgross_salary);
        ffooter = (TextView)view.findViewById(R.id.vivoteam_id);
    }

    public void getSelectedData() {
        reference.orderByChild("gmail").equalTo(data).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    SalaryGet salary = dataSnapshot1.getValue(SalaryGet.class);
                    if (salary.getMonth().equals(getSpinnerMonth) && salary.getYear().equals(getSpinnerYear)) {
                        generateGmailKey = dataSnapshot1.getKey();
                    }else {

                    }
                }
                if (generateGmailKey != null) {
                    reference1 = FirebaseDatabase.getInstance().getReference("upload_salary").child(generateGmailKey);
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            basic_salry.setText("Basic Salary\n" + dataSnapshot.child("basic_salary").getValue().toString() + "/-");
                            house_rent.setText("House Rent\n" + dataSnapshot.child("house_rent").getValue().toString() + "/-");
                            medical_allwnce.setText("Medical Allowance\n" + dataSnapshot.child("medical").getValue().toString() + "/-");
                            food_allwnce.setText("Food Allowance\n" + dataSnapshot.child("food").getValue().toString() + "/-");
                            transport_allwnce.setText("Transportation Allowance\n" + dataSnapshot.child("transport").getValue().toString() + "/-");
                            mobile_bill.setText("Mobile Bill\n" + dataSnapshot.child("mobile").getValue().toString() + "/-");
                            kpi.setText("Kpi\n" + dataSnapshot.child("kpi").getValue().toString() + "/-");
                            incentive.setText("Incentive\n" + dataSnapshot.child("incentive").getValue().toString() + "/-");
                            sales_commision.setText("Sales Commission\n" + dataSnapshot.child("sales_commission").getValue().toString() + "/-");
                            other_addition.setText("Other Addition\n" + dataSnapshot.child("other_addition").getValue().toString() + "/-");
                            tax_deduction.setText("Tax (Deduction)\n" + dataSnapshot.child("tax").getValue().toString() + "/-");
                            other_deduction.setText("Other Deduction\n" + dataSnapshot.child("other_deduction").getValue().toString() + "/-");
                            gross.setText("" + dataSnapshot.child("gross").getValue().toString() + "");
                            generateGmailKey = null;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), "Inner " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "salary can not uploaded yet", Toast.LENGTH_SHORT).show();
                    basic_salry.setText("Basic Salary\n000/-");
                    house_rent.setText("House Rent\n000/-");
                    medical_allwnce.setText("Medical Allowance\n000/-");
                    food_allwnce.setText("Food Allowance\n000/-");
                    transport_allwnce.setText("Transportation Allowance\n000/-");
                    mobile_bill.setText("Mobile Bill\n000/-");
                    kpi.setText("Kpi\n000/-");
                    incentive.setText("Incentive\n000/-");
                    sales_commision.setText("Sales Commission\n000/-");
                    other_addition.setText("Other (Addition)\n000/-");
                    tax_deduction.setText("Tax (Deduction)\n000/-");
                    other_deduction.setText("Other Deduction\n000/-");
                    gross.setText("Gross Salary\n000/-");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
