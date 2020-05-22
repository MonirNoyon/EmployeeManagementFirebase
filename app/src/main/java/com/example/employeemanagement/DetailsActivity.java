package com.example.employeemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    public String values = "";
    public String getMail = "";
    public String getDesignition = "";
    DatabaseReference headerFooterReference, reference;
    String header, footer;
    Toolbar toolbar;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        user = FirebaseAuth.getInstance().getCurrentUser();

        headerFooterReference = FirebaseDatabase.getInstance().getReference("Header_Footer").child("Admin");
        headerFooterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                header = dataSnapshot.child("header").getValue().toString();
                footer = dataSnapshot.child("footer").getValue().toString();
                toolbar.setTitle(header);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailsActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        values = getIntent().getStringExtra("getkey");
        getMail = getIntent().getStringExtra("getEmail");
        getDesignition = getIntent().getStringExtra("getDesignition");
        reference = FirebaseDatabase.getInstance().getReference("uploads").child(values);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.opendraqwer_id);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeActivity()).commit();
            navigationView.setCheckedItem(R.id.home_id);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeActivity()).commit();
                break;
            case R.id.myprofile_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ProfileActivity()).commit();
                break;
            case R.id.update_id:
                UpdateDialog();
                break;
            case R.id.salary_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new SalaryActivity()).commit();
                break;
            case R.id.attendence_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AttendenceActivity()).commit();
                break;
            case R.id.leave_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new LeaveActivity()).commit();
                break;
            case R.id.reisgn_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ReisgnActivity()).commit();
                break;
            case R.id.status_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new EmployeeStatus()).commit();
                break;
            case R.id.documents_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new DocumentsActivity()).commit();
                break;
            case R.id.logout_id:
                Intent intent = new Intent(DetailsActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void UpdateDialog() {
        final CharSequence[] items = {"Update Password", "Update Other"};
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Update");
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Update Password")) {
                    UpdatePassword();
                } else if (items[i].equals("Update Other")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new UpdateProfile()).commit();
                }
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    public void UpdatePassword() {
        final AlertDialog.Builder updateAlert = new AlertDialog.Builder(DetailsActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.updatepass, null);
        ImageView cancel = (ImageView) view1.findViewById(R.id.update_cancel);
        final TextInputEditText pass = (TextInputEditText) view1.findViewById(R.id.up_pass);
        final TextView update_pass = (TextView) view1.findViewById(R.id.update_passwordtext);
        updateAlert.setView(view1);
        final AlertDialog update_dialog = updateAlert.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_dialog.dismiss();
            }
        });
        update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().length()>6) {
                    user.updatePassword(pass.getText().toString().trim());
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("password", pass.getText().toString().trim());
                    reference.updateChildren(updates);
                    Toasty.success(DetailsActivity.this, "Password update successfull").show();
                    update_dialog.dismiss();
                }else{
                    pass.setError("Password at least 6 digit");
                }
            }
        });

        update_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        update_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        update_dialog.setCancelable(false);
        update_dialog.show();
    }
}
