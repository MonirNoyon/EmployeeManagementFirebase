package com.example.employeemanagement;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    private ImageView imageView;
    private Animation smalltobig, smalltobig2, btta, btta2;

    Button adminLogin, lgin;
    String keys, getDesignition, getDatabseDesignition;
    Toolbar toolbar;
    LinearLayout reg_details;
    Spinner category;
    String getUserKey, recPassword, header, footer;
    private EditText mail, pass;
    FirebaseAuth mAuth, mAuthadmin;
    DatabaseReference reference, referenceAdmin, referencePassword, referenceHeaderFooter;
    TextView headerid, subheader;
    LinearLayout frgt_pass;
    String names[] = {"Select Category", "Office", "Market", "Patuakhali", "Barishal", "Bhola", "Jhalakhati", "Barguna", "Pirojpur"};
    final List<String> namelist = new ArrayList<>(Arrays.asList(names));
    private String getStatus;
    private String getadminKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testter);

        OpenPermission();
        createAnimation();
        reference = FirebaseDatabase.getInstance().getReference("uploads");
        referenceAdmin = FirebaseDatabase.getInstance().getReference("Admin");
        referenceHeaderFooter = FirebaseDatabase.getInstance().getReference("Header_Footer").child("Admin");
        mAuth = FirebaseAuth.getInstance();
        mAuthadmin = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar_id);

        referenceHeaderFooter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                header = dataSnapshot.child("header").getValue().toString();
                footer = dataSnapshot.child("footer").getValue().toString();
                toolbar.setTitle(header);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        frgt_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPassword();
            }
        });
        final ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(this, R.layout.category_spinner, namelist) {
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
        category.setAdapter(category_adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getDesignition = (String) adapterView.getItemAtPosition(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        toolbar.inflateMenu(R.menu.admin);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.admincontrolId:
                        createAnimation();
                        reg_details.setVisibility(View.GONE);
                        category.setVisibility(View.GONE);
                        lgin.setVisibility(View.GONE);
                        frgt_pass.setVisibility(View.GONE);
                        adminLogin.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });
    }

    public void RegistrationClick(View view) {
        Intent intent = new Intent(MainActivity.this, Registration.class);
        startActivity(intent);
    }

    public void loginClick(View view) {
        final String email = mail.getText().toString().trim();
        String password = pass.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    reference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                keys = datas.getKey();
                            }
                            reference.child(keys).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    getDatabseDesignition = dataSnapshot.child("category").getValue().toString();
                                    getStatus = dataSnapshot.child("status").getValue().toString();
                                    if (getDatabseDesignition.equals(getDesignition)) {
                                        if (getStatus.equals("1")) {
                                            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                                            intent.putExtra("getkey", keys);
                                            intent.putExtra("getEmail", email);
                                            intent.putExtra("getDesignition", getDesignition);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toasty.info(MainActivity.this, "Admin can not accept yet", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Enter category Correctly", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Login Failled", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void adminClick(View view) {
        final String admin_mail = mail.getText().toString().trim();
        final String admin_pass = pass.getText().toString().trim();

        referenceAdmin.orderByChild("email").equalTo(admin_mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    UploadData uploadData = dataSnapshot1.getValue(UploadData.class);
                    if (uploadData.getEmail().equals(admin_mail) && uploadData.getPassword().equals(admin_pass) ){
                        getadminKey = dataSnapshot1.getKey();
                    }
                }
                if (getadminKey.equals("-M4UvF6TLIrCyrIN3ZWn")){
                    Intent intent = new Intent(MainActivity.this,AdminDetails.class);
                    startActivity(intent);
                }else if (getadminKey.equals("")){
                    Toasty.error(MainActivity.this, "Please Check email and Password", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mAuthadmin.signInWithEmailAndPassword(admin_mail, admin_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Intent intent = new Intent(MainActivity.this, AdminDetails.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Toast.makeText(MainActivity.this, "Please Check email and Password", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void getPassword() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.recover_password, null);
        ImageView cancel = (ImageView) view1.findViewById(R.id.cancel_image);
        final TextView getpassword = (TextView) view1.findViewById(R.id.get_password_id);
        final TextInputEditText mail = (TextInputEditText) view1.findViewById(R.id.rec_email);
        final TextInputEditText phone = (TextInputEditText) view1.findViewById(R.id.rec_phone);
        alert.setView(view1);
        final AlertDialog dialog = alert.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        getpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mail.getText().toString().trim();
                String mobile = phone.getText().toString().trim();
                if (email.equals("") || (mobile.length() < 11 || mobile.length() > 11)) {
                    if (email.equals("")) {
                        mail.setError("Required");
                    }
                    if (mobile.length() < 11 || mobile.length() > 11) {
                        phone.setError("invalid phone number");
                    }
                } else {
                    reference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                getUserKey = dataSnapshot1.getKey();
                            }
                            if (getUserKey != null) {
                                referencePassword = FirebaseDatabase.getInstance().getReference("uploads").child(getUserKey);
                                referencePassword.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        recPassword = dataSnapshot.child("password").getValue().toString();
                                        Alerter.create(MainActivity.this)
                                                .setTitle("Password Alrert")
                                                .setText("your password is: " + recPassword)
                                                .setDuration(5000)
                                                .setBackgroundColorRes(R.color.colorBackgroundRed)
                                                .enableSwipeToDismiss()
                                                .enableProgress(true)
                                                .setProgressColorRes(R.color.colorAccent)
                                                .show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "sorry !! fill input correctly", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.show();
    }

    @AfterPermissionGranted(123)
    private void OpenPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "without permission Application might be stopped",
                    123, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void createAnimation() {
        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        smalltobig2 = AnimationUtils.loadAnimation(this, R.anim.smalltobig2);
        btta = AnimationUtils.loadAnimation(this, R.anim.btta);
        btta2 = AnimationUtils.loadAnimation(this, R.anim.btta2);

        imageView = (ImageView) findViewById(R.id.imageViewid);
        headerid = (TextView) findViewById(R.id.headerid);
        subheader = (TextView) findViewById(R.id.subheaderid);
        mail = (EditText) findViewById(R.id.emaillog_etid);
        pass = (EditText) findViewById(R.id.et_passwordlogid);
        adminLogin = (Button) findViewById(R.id.adminloginbtnID);
        lgin = (Button) findViewById(R.id.loginbtnid);
        category = (Spinner) findViewById(R.id.spinner_categoryid);
        frgt_pass = (LinearLayout) findViewById(R.id.frgt_password);
        reg_details = (LinearLayout) findViewById(R.id.reg_details_id);

        imageView.setAnimation(smalltobig);
        headerid.setAnimation(btta);
        subheader.setAnimation(btta);

        mail.setAnimation(btta2);
        pass.setAnimation(btta2);
        category.setAnimation(btta2);
        lgin.startAnimation(smalltobig);
        frgt_pass.setAnimation(btta2);
        reg_details.setAnimation(smalltobig2);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Do You want to exit ?");
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                finish();
            }
        });
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }
}
