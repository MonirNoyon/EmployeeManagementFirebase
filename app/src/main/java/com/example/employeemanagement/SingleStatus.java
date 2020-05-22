package com.example.employeemanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class SingleStatus extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    RadioGroup appointment, id, visiting_card, t_shirt, we_chatAccount, v2_account,
            job_confirmation, bank_account, tin_number,emp_type;
    RadioButton radio_appiont, radio_id, radio_visitingcard, radio_tshirt, radio_wechataccount, radio_v2,
            radio_jobconfirmation, radio_bank, radio_tin,radio_emptype;
    CircleImageView profile;
    TextView name, gmail;
    String mailvalue, key;
    DatabaseReference reference, reference1, referenceStatus;
    String mailKey;
    Button update,save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_status);
        initReference();
        getProfile();
        appointment.setOnCheckedChangeListener(this);
        id.setOnCheckedChangeListener(this);
        visiting_card.setOnCheckedChangeListener(this);
        t_shirt.setOnCheckedChangeListener(this);
        we_chatAccount.setOnCheckedChangeListener(this);
        v2_account.setOnCheckedChangeListener(this);
        job_confirmation.setOnCheckedChangeListener(this);
        bank_account.setOnCheckedChangeListener(this);
        tin_number.setOnCheckedChangeListener(this);
        emp_type.setOnCheckedChangeListener(this);
        referenceStatus.orderByChild("email").equalTo(mailvalue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    mailKey = dataSnapshot1.getKey();
                }
                if (mailKey != null) {
                    save.setVisibility(View.GONE);
                    update.setVisibility(View.VISIBLE);
                    referenceStatus.child(mailKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("appointment").getValue().toString().equals("Yes")) {
                                appointment.check(appointment.getChildAt(1).getId());
                            } else {
                                appointment.check(appointment.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("emp_type").getValue().toString().equals("Permanent")) {
                                emp_type.check(emp_type.getChildAt(1).getId());
                            } else {
                                emp_type.check(emp_type.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("bank_account").getValue().toString().equals("Yes")) {
                                bank_account.check(bank_account.getChildAt(1).getId());
                            } else {
                                bank_account.check(bank_account.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("id").getValue().toString().equals("Yes")) {
                                id.check(id.getChildAt(1).getId());
                            } else {
                                id.check(id.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("job_confirmation").getValue().toString().equals("Yes")) {
                                job_confirmation.check(job_confirmation.getChildAt(1).getId());
                            } else {
                                job_confirmation.check(job_confirmation.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("t_shirt").getValue().toString().equals("Yes")) {
                                t_shirt.check(t_shirt.getChildAt(1).getId());
                            } else {
                                t_shirt.check(t_shirt.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("tin_number").getValue().toString().equals("Yes")) {
                                tin_number.check(tin_number.getChildAt(1).getId());
                            } else {
                                tin_number.check(tin_number.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("v2_account").getValue().toString().equals("Yes")) {
                                v2_account.check(v2_account.getChildAt(1).getId());
                            } else {
                                v2_account.check(v2_account.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("visiting_card").getValue().toString().equals("Yes")) {
                                visiting_card.check(visiting_card.getChildAt(1).getId());
                            } else {
                                visiting_card.check(visiting_card.getChildAt(2).getId());
                            }
                            if (dataSnapshot.child("wechat_account").getValue().toString().equals("Yes")) {
                                we_chatAccount.check(we_chatAccount.getChildAt(1).getId());
                            } else {
                                we_chatAccount.check(we_chatAccount.getChildAt(2).getId());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    update.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    Toast.makeText(SingleStatus.this, "please upload employee status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleStatus.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initReference() {
        profile = (CircleImageView) findViewById(R.id.sigle_salary_profile);
        name = (TextView) findViewById(R.id.namesalaryid);
        gmail = (TextView) findViewById(R.id.gmailsalaryid);
        appointment = (RadioGroup) findViewById(R.id.appointment_radio);
        id = (RadioGroup) findViewById(R.id.id_radio);
        emp_type =(RadioGroup)findViewById(R.id.emp_type);
        visiting_card = (RadioGroup) findViewById(R.id.visitingcard_radio);
        t_shirt = (RadioGroup) findViewById(R.id.t_shirt_radio);
        we_chatAccount = (RadioGroup) findViewById(R.id.wechat_radio);
        v2_account = (RadioGroup) findViewById(R.id.v2accoutn_radio);
        job_confirmation = (RadioGroup) findViewById(R.id.job_confirmation_radio);
        bank_account = (RadioGroup) findViewById(R.id.bankaccount_radio);
        tin_number = (RadioGroup) findViewById(R.id.tinnumber_radio);
        save = (Button)findViewById(R.id.empstatus_save);
        update = (Button)findViewById(R.id.empstatus_update);
        reference = FirebaseDatabase.getInstance().getReference("uploads");
        referenceStatus = FirebaseDatabase.getInstance().getReference("Employee_Status");
        referenceStatus.orderByChild("email").equalTo(mailvalue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getProfile() {
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
                        Toast.makeText(SingleStatus.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleStatus.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()) {
            case R.id.appointment_radio:
                int selectedIde = appointment.getCheckedRadioButtonId();
                radio_appiont = (RadioButton) findViewById(selectedIde);
                break;
            case R.id.wechat_radio:
                int selectedwechat = we_chatAccount.getCheckedRadioButtonId();
                radio_wechataccount = (RadioButton) findViewById(selectedwechat);
                break;
            case R.id.v2accoutn_radio:
                int selectedv2account = v2_account.getCheckedRadioButtonId();
                radio_v2 = (RadioButton) findViewById(selectedv2account);
                break;
            case R.id.job_confirmation_radio:
                int selectedjob = job_confirmation.getCheckedRadioButtonId();
                radio_jobconfirmation = (RadioButton) findViewById(selectedjob);
                break;
            case R.id.bankaccount_radio:
                int selectedbank = bank_account.getCheckedRadioButtonId();
                radio_bank = (RadioButton) findViewById(selectedbank);
                break;
            case R.id.tinnumber_radio:
                int selectedtin = tin_number.getCheckedRadioButtonId();
                radio_tin = (RadioButton) findViewById(selectedtin);
                break;
            case R.id.visitingcard_radio:
                int selectedvisit = visiting_card.getCheckedRadioButtonId();
                radio_visitingcard = (RadioButton) findViewById(selectedvisit);
                break;
            case R.id.t_shirt_radio:
                int selectedtshirt = t_shirt.getCheckedRadioButtonId();
                radio_tshirt = (RadioButton) findViewById(selectedtshirt);
                break;

            case R.id.id_radio:
                int selectedId = id.getCheckedRadioButtonId();
                radio_id = (RadioButton) findViewById(selectedId);
                break;
            case R.id.emp_type:
                int selectedemptype = emp_type.getCheckedRadioButtonId();
                radio_emptype = (RadioButton)findViewById(selectedemptype);
                break;

        }
    }

    public void saveStatus(View view) {
        if (radio_appiont.getText().toString() != null && radio_id.getText().toString() != null && radio_visitingcard.getText().toString() != null
                && radio_tshirt.getText().toString() != null && radio_wechataccount.getText().toString() != null && radio_v2.getText().toString() != null && radio_jobconfirmation.getText().toString() != null
                && radio_tin.getText().toString() != null && radio_bank.getText().toString() != null && radio_emptype.getText().toString() !=null) {
            UploadAttendence status = new UploadAttendence(radio_appiont.getText().toString(), radio_id.getText().toString(), radio_visitingcard.getText().toString()
                    , radio_tshirt.getText().toString(), radio_wechataccount.getText().toString(), radio_v2.getText().toString(), radio_jobconfirmation.getText().toString(), radio_bank.getText().toString()
                    , radio_tin.getText().toString(), mailvalue,radio_emptype.getText().toString());
            String key = referenceStatus.push().getKey();
            referenceStatus.child(key).setValue(status);
            Toasty.success(SingleStatus.this, "Status upload successfull", Toast.LENGTH_SHORT).show();
        } else {
            Toasty.error(SingleStatus.this, "Empty filled not allow", Toast.LENGTH_SHORT).show();
        }

    }
    public void UpdateStatus(View view){
        if (radio_appiont.getText().toString() != null && radio_id.getText().toString() != null && radio_visitingcard.getText().toString() != null
                && radio_tshirt.getText().toString() != null && radio_wechataccount.getText().toString() != null && radio_v2.getText().toString() != null && radio_jobconfirmation.getText().toString() != null
                && radio_tin.getText().toString() != null && radio_bank.getText().toString() != null && radio_emptype.getText().toString() !=null) {

            UploadAttendence status = new UploadAttendence(radio_appiont.getText().toString(), radio_id.getText().toString(), radio_visitingcard.getText().toString()
                    , radio_tshirt.getText().toString(), radio_wechataccount.getText().toString(), radio_v2.getText().toString(), radio_jobconfirmation.getText().toString(), radio_bank.getText().toString()
                    , radio_tin.getText().toString(), mailvalue,radio_emptype.getText().toString());
            referenceStatus.child(mailKey).setValue(status);
            Toasty.success(SingleStatus.this, "Status upload successfull", Toast.LENGTH_SHORT).show();
        } else {
            Toasty.error(SingleStatus.this, "Empty filled not allow", Toast.LENGTH_SHORT).show();
        }
    }
}
