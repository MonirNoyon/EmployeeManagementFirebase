package com.example.employeemanagement;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleSalaryActivity extends AppCompatActivity {

    private DatabaseReference reference, reference1, salarydatabsase_reference, salarydatabsase_reference1;

    Button savedata, updatedata;
    String key, salary_key, getSpinnerMonth, getSpinnerYear;
    TextView name, gmail, gross;
    CircleImageView profile;
    String mailvalue;
    Spinner salary_month, salalry_year;
    TextInputEditText basic_salry, house_rent, medical_allwnce, food_allwnce, transport_allwnce, mobile_bill, kpi, incentive, sales_commision, other_addition, tax_deduction, other_deduction;

    String montharray[] = {"Select Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String yeararray[] = {"Select Year", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    final List<String> monthList = new ArrayList<String>(Arrays.asList(montharray));
    final List<String> yearList = new ArrayList<String>(Arrays.asList(yeararray));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_salary);

        reference = FirebaseDatabase.getInstance().getReference("uploads");
        salarydatabsase_reference = FirebaseDatabase.getInstance().getReference("upload_salary");

        savedata = (Button) findViewById(R.id.btninsert_salary);
        updatedata = (Button) findViewById(R.id.btnUpdateData);

        salary_month = (Spinner) findViewById(R.id.salary_month);
        salalry_year = (Spinner) findViewById(R.id.salary_year);
        final ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(SingleSalaryActivity.this, R.layout.categorry_spinner, monthList) {
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
        salary_month.setAdapter(monthAdapter);
        salary_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getSpinnerMonth = (String) adapterView.getItemAtPosition(i);
                    if (getSpinnerMonth != null && getSpinnerYear != null) {
                        getSelectedData();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(SingleSalaryActivity.this, R.layout.categorry_spinner, yearList) {
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
        salalry_year.setAdapter(yearAdapter);
        salalry_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getSpinnerYear = (String) adapterView.getItemAtPosition(i);
                    if (getSpinnerMonth != null && getSpinnerYear != null) {
                        getSelectedData();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        basic_salry = (TextInputEditText) findViewById(R.id.basic_salaryid);
        house_rent = (TextInputEditText) findViewById(R.id.house_rent_id);
        medical_allwnce = (TextInputEditText) findViewById(R.id.medical_allowanceid);
        food_allwnce = (TextInputEditText) findViewById(R.id.food_allowance);
        transport_allwnce = (TextInputEditText) findViewById(R.id.transport_allowance);
        mobile_bill = (TextInputEditText) findViewById(R.id.mobile_bill);
        kpi = (TextInputEditText) findViewById(R.id.kpi_id);
        incentive = (TextInputEditText) findViewById(R.id.incentive_id);
        sales_commision = (TextInputEditText) findViewById(R.id.sale_commissionid);
        other_addition = (TextInputEditText) findViewById(R.id.other_id);
        tax_deduction = (TextInputEditText) findViewById(R.id.tax_id);
        other_deduction = (TextInputEditText) findViewById(R.id.otherdeduction_id);

        name = (TextView) findViewById(R.id.namesalaryid);
        gmail = (TextView) findViewById(R.id.gmailsalaryid);
        gross = (TextView) findViewById(R.id.gross_salary);
        profile = (CircleImageView) findViewById(R.id.sigle_salary_profile);
        Intent getdata = getIntent();
        final String email = getdata.getStringExtra("mailid");
        mailvalue = email;
        reference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    key = dataSnapshot1.getKey();
                }
                reference1 = FirebaseDatabase.getInstance().getReference("uploads").child(key);
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String prof = dataSnapshot.child("profile").getValue().toString();
                        Uri pro_uri = Uri.parse(prof);
                        Picasso.get().load(pro_uri)
                                .fit()
                                .centerCrop()
                                .into(profile);
                        name.setText(dataSnapshot.child("name").getValue().toString());
                        gmail.setText(dataSnapshot.child("email").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SingleSalaryActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleSalaryActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        basic_salry.addTextChangedListener(textWatcher);
        house_rent.addTextChangedListener(textWatcher);
        medical_allwnce.addTextChangedListener(textWatcher);
        food_allwnce.addTextChangedListener(textWatcher);
        transport_allwnce.addTextChangedListener(textWatcher);
        mobile_bill.addTextChangedListener(textWatcher);
        kpi.addTextChangedListener(textWatcher);
        incentive.addTextChangedListener(textWatcher);
        sales_commision.addTextChangedListener(textWatcher);
        other_addition.addTextChangedListener(textWatcher);
        tax_deduction.addTextChangedListener(textWatcher);
        other_deduction.addTextChangedListener(textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!TextUtils.isEmpty(basic_salry.getText().toString().trim())
                    || !TextUtils.isEmpty(house_rent.getText().toString().trim())
                    || !TextUtils.isEmpty(medical_allwnce.getText().toString().trim())
                    || !TextUtils.isEmpty(food_allwnce.getText().toString().trim())
                    || !TextUtils.isEmpty(transport_allwnce.getText().toString().trim())
                    || !TextUtils.isEmpty(mobile_bill.getText().toString().trim())
                    || !TextUtils.isEmpty(kpi.getText().toString().trim())
                    || !TextUtils.isEmpty(incentive.getText().toString().trim())
                    || !TextUtils.isEmpty(sales_commision.getText().toString().trim())
                    || !TextUtils.isEmpty(other_addition.getText().toString().trim())
                    || !TextUtils.isEmpty(tax_deduction.getText().toString().trim())
                    || !TextUtils.isEmpty(other_deduction.getText().toString().trim())) {

                double basic = TextUtils.isEmpty(basic_salry.getText().toString().trim()) ? 0 : Double.parseDouble(basic_salry.getText().toString().trim());
                double house = TextUtils.isEmpty(house_rent.getText().toString().trim()) ? 0 : Double.parseDouble(house_rent.getText().toString().trim());
                double medical = TextUtils.isEmpty(medical_allwnce.getText().toString().trim()) ? 0 : Double.parseDouble(medical_allwnce.getText().toString().trim());
                double food = TextUtils.isEmpty(food_allwnce.getText().toString().trim()) ? 0 : Double.parseDouble(food_allwnce.getText().toString().trim());
                double transport = TextUtils.isEmpty(transport_allwnce.getText().toString().trim()) ? 0 : Double.parseDouble(transport_allwnce.getText().toString().trim());
                double mobil = TextUtils.isEmpty(mobile_bill.getText().toString().trim()) ? 0 : Double.parseDouble(mobile_bill.getText().toString().trim());
                double kp = TextUtils.isEmpty(kpi.getText().toString().trim()) ? 0 : Double.parseDouble(kpi.getText().toString().trim());
                double incen = TextUtils.isEmpty(incentive.getText().toString().trim()) ? 0 : Double.parseDouble(incentive.getText().toString().trim());
                double sales = TextUtils.isEmpty(sales_commision.getText().toString().trim()) ? 0 : Double.parseDouble(sales_commision.getText().toString().trim());
                double addition = TextUtils.isEmpty(other_addition.getText().toString().trim()) ? 0 : Double.parseDouble(other_addition.getText().toString().trim());
                double tax = TextUtils.isEmpty(tax_deduction.getText().toString().trim()) ? 0 : Double.parseDouble(tax_deduction.getText().toString().trim());
                double deduction = TextUtils.isEmpty(other_deduction.getText().toString().trim()) ? 0 : Double.parseDouble(other_deduction.getText().toString().trim());
                double result = basic + house + medical + food + transport + mobil + kp + incen + sales + addition + tax + deduction;
                gross.setText("Gross Salary " + result + "/-");
            } else {
                gross.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void setSalary(View view) {
        if (getSpinnerMonth != null && getSpinnerYear != null) {
            String id = salarydatabsase_reference.push().getKey();
            UploadSalary uploadSalary = new UploadSalary(basic_salry.getText().toString().trim(), house_rent.getText().toString().trim(),
                    medical_allwnce.getText().toString().trim(), food_allwnce.getText().toString().trim(), transport_allwnce.getText().toString().trim(),
                    mobile_bill.getText().toString().trim(), kpi.getText().toString().trim(), incentive.getText().toString().trim(), sales_commision.getText().toString().trim(),
                    other_addition.getText().toString().trim(), tax_deduction.getText().toString().trim(), other_deduction.getText().toString().trim(),
                    gross.getText().toString().trim(), mailvalue, name.getText().toString().trim(), getSpinnerMonth, getSpinnerYear);
            salarydatabsase_reference.child(id).setValue(uploadSalary);
            Toast.makeText(this, "Save Data Successfull", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select month and year", Toast.LENGTH_SHORT).show();
        }
    }

    public void getSelectedData() {
        salarydatabsase_reference.orderByChild("gmail").equalTo(mailvalue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    salary_key = dataSnapshot1.getKey();
                }
                if (salary_key != null) {
                    salarydatabsase_reference1 = FirebaseDatabase.getInstance().getReference("upload_salary").child(salary_key);
                    salarydatabsase_reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (getSpinnerMonth.equals(dataSnapshot.child("month").getValue().toString())
                                    &&
                                    getSpinnerYear.equals(dataSnapshot.child("year").getValue().toString())) {

                                savedata.setVisibility(View.INVISIBLE);
                                updatedata.setVisibility(View.VISIBLE);

                                basic_salry.setText(dataSnapshot.child("basic_salary").getValue().toString());
                                house_rent.setText(dataSnapshot.child("house_rent").getValue().toString());
                                medical_allwnce.setText(dataSnapshot.child("medical").getValue().toString());
                                food_allwnce.setText(dataSnapshot.child("food").getValue().toString());
                                transport_allwnce.setText(dataSnapshot.child("transport").getValue().toString());
                                mobile_bill.setText(dataSnapshot.child("mobile").getValue().toString());
                                kpi.setText(dataSnapshot.child("kpi").getValue().toString());
                                incentive.setText(dataSnapshot.child("incentive").getValue().toString());
                                sales_commision.setText(dataSnapshot.child("sales_commission").getValue().toString());
                                other_addition.setText(dataSnapshot.child("other_addition").getValue().toString());
                                tax_deduction.setText(dataSnapshot.child("tax").getValue().toString());
                                other_deduction.setText(dataSnapshot.child("other_deduction").getValue().toString());
                            } else {
                                savedata.setVisibility(View.VISIBLE);
                                updatedata.setVisibility(View.INVISIBLE);
                                basic_salry.setText("");
                                house_rent.setText("");
                                medical_allwnce.setText("");
                                food_allwnce.setText("");
                                transport_allwnce.setText("");
                                mobile_bill.setText("");
                                kpi.setText("");
                                incentive.setText("");
                                sales_commision.setText("");
                                other_addition.setText("");
                                tax_deduction.setText("");
                                other_deduction.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(SingleSalaryActivity.this, "Inner " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    basic_salry.setText("");
                    house_rent.setText("");
                    medical_allwnce.setText("");
                    food_allwnce.setText("");
                    transport_allwnce.setText("");
                    mobile_bill.setText("");
                    kpi.setText("");
                    incentive.setText("");
                    sales_commision.setText("");
                    other_addition.setText("");
                    tax_deduction.setText("");
                    other_deduction.setText("");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleSalaryActivity.this, " " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdateSalary(View view) {
        UploadSalary uploadSalary = new UploadSalary(basic_salry.getText().toString().trim(), house_rent.getText().toString().trim(),
                medical_allwnce.getText().toString().trim(), food_allwnce.getText().toString().trim(), transport_allwnce.getText().toString().trim(),
                mobile_bill.getText().toString().trim(), kpi.getText().toString().trim(), incentive.getText().toString().trim(), sales_commision.getText().toString().trim(),
                other_addition.getText().toString().trim(), tax_deduction.getText().toString().trim(), other_deduction.getText().toString().trim(),
                gross.getText().toString().trim(), mailvalue, name.getText().toString().trim(), getSpinnerMonth, getSpinnerYear);
        salarydatabsase_reference1.setValue(uploadSalary);
        Toast.makeText(this, "Update Successfull", Toast.LENGTH_SHORT).show();
    }
}
