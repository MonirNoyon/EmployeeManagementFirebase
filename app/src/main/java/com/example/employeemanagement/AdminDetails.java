package com.example.employeemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
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

public class AdminDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    FirebaseUser user;
    DatabaseReference reference,headerFooterReference;
    String header, footer;
    Toolbar toolbar;
    TextView headernavtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details);
        toolbar = (Toolbar) findViewById(R.id.toolbaradmin);

        headernavtext = (TextView)findViewById(R.id.header_navigation_id);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Admin").child("-M4UvF6TLIrCyrIN3ZWn");

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
                Toast.makeText(AdminDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.opendraqwer_id);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new HomeAdmin()).commit();
            navigationView.setCheckedItem(R.id.homeadmin_id);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.homeadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new HomeAdmin()).commit();
                break;
            case R.id.myprofileadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new ProfileAdmin()).commit();
                break;
            case R.id.updateadmin_id:
                UpdateDialog();
                break;
            case R.id.contactadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new ContactAdmin()).commit();
                break;
            case R.id.salaryadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new SalaryAdmin()).commit();
                break;
            case R.id.attendenceadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new AttendenceAdmin()).commit();
                break;
            case R.id.leaveadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new LeaveAdmin()).commit();
                break;
            case R.id.resignadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new ResignAdmin()).commit();
                break;
            case R.id.documentsadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new DocumentsAdmin()).commit();
                break;
            case R.id.header_footerid:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new HeaderFooter()).commit();
                break;
            case R.id.statusadmin_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container,new AdminStatus()).commit();
                break;
            case R.id.emplyee_manageid:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container,new EmployeeManage()).commit();
                break;
            case R.id.lgoutadmn_id:
                Intent intent = new Intent(AdminDetails.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void UpdateDialog() {
        final CharSequence[] items = {"Update Email", "Update Password", "Update Other"};
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Update");
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Update Email")) {
                    UpdateEmail();
                } else if (items[i].equals("Update Password")) {
                    UpdatePassword();
                } else if (items[i].equals("Update Other")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameadmin_container, new UpdateAdminProfile()).commit();
                }
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }
    public void UpdateEmail() {
        final AlertDialog.Builder updateAlert = new AlertDialog.Builder(AdminDetails.this);
        View view1 = getLayoutInflater().inflate(R.layout.updateemail, null);
        ImageView cancel = (ImageView) view1.findViewById(R.id.update_cancel);
        final TextInputEditText mail = (TextInputEditText) view1.findViewById(R.id.up_email);
        final TextView update_mail = (TextView) view1.findViewById(R.id.updateemail_id);
        updateAlert.setView(view1);
        final AlertDialog update_dialog = updateAlert.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_dialog.dismiss();
            }
        });
        update_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
                    user.updateEmail(mail.getText().toString().trim());
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("email", mail.getText().toString().trim());
                    reference.updateChildren(updates);
                    Toasty.success(AdminDetails.this, "Email update successfull").show();
                    update_dialog.dismiss();

                } else {
                    mail.setError("Invalid");
                }

            }
        });

        update_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        update_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        update_dialog.setCancelable(false);
        update_dialog.show();
    }
    public void UpdatePassword() {
        final AlertDialog.Builder updateAlert = new AlertDialog.Builder(AdminDetails.this);
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
                    Toasty.success(AdminDetails.this, "Password update successfull").show();
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