package com.example.employeemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 100;
    private static final int SELECT_FILE = 101;
    EditText join_edit, birth_edit, name, emp_id, bloodgroup, phonenumber, email, address, nidno, pass,
            shop, workplace,designition,acc_number;
    TextView reg;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    CircleImageView profile, signeture, qrcode;
    String Imagetitle,getGmailKey,getPhoneKey;
    String designition_spin;
    private Animation smalltobig, smalltobig1, smalltobig2;
    CardView profile_card, sign_card, qr_card;
    String status="0";

    //spinner code
    Spinner category;
    String names[] =  {"Select Category", "Office", "Market", "Patuakhali","Barishal","Bhola","Jhalakhati","Barguna","Pirojpur"};
    final List<String> namelist = new ArrayList<>(Arrays.asList(names));


    //Firebase Reference
    private DatabaseReference mdaDatabaseReference;
    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;
    private Uri profile_uri, signeture_uri, qr_uri;
    private Uri down_prourl, down_signurl, down_qrurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        createAnimation();
        //Firebase
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mdaDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        mAuth = FirebaseAuth.getInstance();

        //spinner start
        category = (Spinner) findViewById(R.id.spinner_categoryid);
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
                designition_spin = category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        name = (EditText) findViewById(R.id.nemeet_id);
        join_edit = (EditText) findViewById(R.id.joining_date);
        birth_edit = (EditText) findViewById(R.id.dateof_birth);
        emp_id = (EditText) findViewById(R.id.employeedt_id);
        bloodgroup = (EditText) findViewById(R.id.bloodgroupet_id);
        acc_number = (EditText) findViewById(R.id.bank_acc_edit);
        phonenumber = (EditText) findViewById(R.id.mobileet_id);
        email = (EditText) findViewById(R.id.emailet_id);
        pass = (EditText) findViewById(R.id.pass_etid);
        address = (EditText) findViewById(R.id.addresset_id);
        shop = (EditText) findViewById(R.id.shopet_id);
        designition = (EditText) findViewById(R.id.designition_id);
        workplace = (EditText) findViewById(R.id.workplacet_id);
        nidno = (EditText) findViewById(R.id.nidet_id);
        profile = (CircleImageView) findViewById(R.id.addimage_id);
        signeture = (CircleImageView) findViewById(R.id.addsigneture_id);
        qrcode = (CircleImageView) findViewById(R.id.addqrcode_id);
        profile.setOnClickListener(this);
        signeture.setOnClickListener(this);
        qrcode.setOnClickListener(this);

        join_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Registration.this, R.style.Theme_Design_BottomSheetDialog,
                        dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                dialog.show();

            }
        });
        birth_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Registration.this, R.style.Theme_Design_BottomSheetDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month + 1;
                                String date = day + "-" + month + "-" + year;
                                birth_edit.setText(date);
                            }
                        }, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                dialog.show();

            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                join_edit.setText(date);
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addimage_id:
                selectImage();
                Imagetitle = "profile";
                break;
            case R.id.addsigneture_id:
                selectImage();
                Imagetitle = "signeture";
                break;
            case R.id.addqrcode_id:
                selectImage();
                Imagetitle = "qrcode";
                break;
        }
    }

    public void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder alert = new AlertDialog.Builder(Registration.this);
        alert.setTitle("Add Image");
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA);
                } else if (items[i].equals("Gallery")) {
                    Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery_intent, SELECT_FILE);
                }
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                if (Imagetitle.equals("profile")) {
                    profile_uri = data.getData();
                    profile.setImageBitmap(bitmap);
                } else if (Imagetitle.equals("signeture")) {
                    signeture_uri = data.getData();
                    signeture.setImageBitmap(bitmap);
                } else {
                    qr_uri = data.getData();
                    qrcode.setImageBitmap(bitmap);
                }

            } else if (requestCode == SELECT_FILE) {
                if (Imagetitle.equals("profile")) {
                    profile_uri = data.getData();
                    profile.setImageURI(profile_uri);
                } else if (Imagetitle.equals("signeture")) {
                    signeture_uri = data.getData();
                    signeture.setImageURI(signeture_uri);
                } else {
                    qr_uri = data.getData();
                    qrcode.setImageURI(qr_uri);
                }
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));
    }

    public void UploadData(View view) {
        if (profile_uri == null || signeture_uri == null || qr_uri == null) {
            Toasty.error(Registration.this, "add all image", Toast.LENGTH_SHORT).show();
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())) {
            email.setError("enter valid email");
            Toasty.error(Registration.this, "enter valid email", Toast.LENGTH_SHORT).show();
        } else if (pass.getText().length() < 6) {
            pass.setError("at least 6 digit required");
            Toasty.error(Registration.this, "at least 6 digit required", Toast.LENGTH_SHORT).show();
        } else if (phonenumber.getText().length() < 11 || phonenumber.getText().length()>11) {
            phonenumber.setError("enter valid phone number.");
            Toasty.error(Registration.this, "enter valid phone number.", Toast.LENGTH_SHORT).show();
        } else {
          getData();
        }

    }

    public void getData() {
        final String nameet = name.getText().toString().trim();
        final String em_id = emp_id.getText().toString().trim();
        final String join_date = join_edit.getText().toString().trim();
        final String birth_date = birth_edit.getText().toString().trim();
        final String blood = bloodgroup.getText().toString().trim();
        final String mobile = phonenumber.getText().toString().trim();
        final String mail = email.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String addrs = address.getText().toString().trim();
        final String shopname = shop.getText().toString().trim();
        final String workplacename = workplace.getText().toString().trim();
        final String nid = nidno.getText().toString().trim();
        final String desig = designition.getText().toString().trim();
        final String acc_num = acc_number.getText().toString().trim();

        final ProgressDialog progres = new ProgressDialog(this);
        progres.setTitle("Uploading...");
        progres.show();
        progres.setCancelable(false);
        final StorageReference profile_reference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(profile_uri));
        profile_reference.putFile(profile_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        profile_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                down_prourl = uri;
                            }
                        });
                        Toast.makeText(Registration.this, "Profile Uploaded Successfull", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progres.setMessage("Profile Uploaded: " + (int) progress + "%");
            }
        });
        final StorageReference signeture_reference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(signeture_uri));
        signeture_reference.putFile(signeture_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        signeture_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                down_signurl = uri;
                            }
                        });

                        Toast.makeText(Registration.this, "Signeture Uploaded Successfull", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progres.setMessage("Signeture Uploaded: " + (int) progress + "%");
            }
        });
        final StorageReference qr_reference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(qr_uri));
        qr_reference.putFile(qr_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        qr_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                down_qrurl = uri;
                                Toasty.success(Registration.this, "registration successfull", Toast.LENGTH_SHORT).show();
                                UploadData uploadData = new UploadData(nameet, designition_spin, em_id, join_date, birth_date, blood,desig, mobile, mail, password, addrs, nid
                                       ,shopname,workplacename, down_prourl.toString(), down_signurl.toString(), down_qrurl.toString(),status,acc_num);
                                String key = mdaDatabaseReference.push().getKey();
                                mdaDatabaseReference.child(key).setValue(uploadData);
                                progres.dismiss();
                                finish();
                                Intent intent = new Intent(Registration.this,MainActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progres.setMessage("QR Code Uploaded: " + (int) progress + "%");
            }
        });
        //sign-up Authentication
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           //registration successfull...>
                        } else {
                            Toasty.error(Registration.this, "Registration Failled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void createAnimation() {
        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        smalltobig1 = AnimationUtils.loadAnimation(this, R.anim.smalltobig1);
        smalltobig2 = AnimationUtils.loadAnimation(this, R.anim.smalltobig2);

        profile_card = (CardView) findViewById(R.id.profile_cardid);
        sign_card = (CardView) findViewById(R.id.signeture_cardid);
        qr_card = (CardView) findViewById(R.id.qrcard_id);

        profile_card.setAnimation(smalltobig);
        sign_card.setAnimation(smalltobig1);
        qr_card.setAnimation(smalltobig2);

    }

}
