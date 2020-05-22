package com.example.employeemanagement;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class UpdateProfile extends Fragment {
    private static final int SELECT_FILE = 9;
    View view;
    ImageView profile, signeture, qrcode;
    EditText name, empid, blood, mobileno, address, shop, workplace,designition,nid,acc_number;
    Button update;
    DatabaseReference reference;
    StorageReference storageReference;
    String Imagetitle;
    private Uri profile_uri, signeture_uri, qr_uri;
    private Uri down_prourl, down_signurl, down_qrurl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_profile, container, false);
        initReference();
        imageUpdate();
        DetailsActivity activity = (DetailsActivity)getActivity();
        final String data = activity.values;
        reference = FirebaseDatabase.getInstance().getReference("uploads");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (name.getText().toString().equals("") || empid.getText().toString().equals("") || blood.getText().toString().equals("") ||
                        mobileno.getText().toString().equals("") ||  address.getText().toString().equals("") ||  shop.getText().toString().equals("") ||
                        workplace.getText().toString().equals("")) {
                    Toasty.error(getContext(), "empty field not allowed", Toast.LENGTH_SHORT).show();
                }
                else {
                    final StorageReference profile_storage = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(profile_uri));
                    profile_storage.putFile(profile_uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    profile_storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            down_prourl = uri;
                                            Map<String, Object> update_prof = new HashMap<>();
                                            update_prof.put("profile", down_prourl.toString());
                                            reference.child(data).updateChildren(update_prof);
                                            Toasty.success(getContext(),"kjfklsdjf  "+down_prourl,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                    final StorageReference sign_storage = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(signeture_uri));
                    sign_storage.putFile(signeture_uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    sign_storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            down_signurl = uri;
                                            Map<String, Object> update_sign = new HashMap<>();
                                            update_sign.put("signeture", down_signurl.toString());
                                            reference.child(data).updateChildren(update_sign);
                                        }
                                    });
                                }
                            });
                    final StorageReference qr_storage = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(qr_uri));
                    qr_storage.putFile(qr_uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    qr_storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            down_qrurl = uri;
                                            Map<String, Object> update_qr = new HashMap<>();
                                            update_qr.put("qrcode", down_qrurl.toString());
                                            reference.child(data).updateChildren(update_qr);
                                        }
                                    });
                                }
                            });
                    Toasty.success(getContext(),""+down_prourl,Toast.LENGTH_SHORT).show();
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("name", name.getText().toString().trim());
                    updates.put("employeeid", empid.getText().toString().trim());
                    updates.put("bloodgroup", blood.getText().toString().trim());
                    updates.put("mobile", mobileno.getText().toString().trim());
                    updates.put("address", address.getText().toString().trim());
                    updates.put("shop", shop.getText().toString().trim());
                    updates.put("workplace", workplace.getText().toString().trim());
                    updates.put("designition", designition.getText().toString().trim());
                    updates.put("nid", nid.getText().toString().trim());
                    updates.put("Account Number", acc_number.getText().toString().trim());
                    reference.child(data).updateChildren(updates);
                    Toasty.success(getContext(), "update successfully").show();
                    name.setText("");
                    empid.setText("");
                    blood.setText("");
                    mobileno.setText("");
                    address.setText("");
                    shop.setText("");
                    workplace.setText("");
                }
            }
        });
        return view;
    }

    public void initReference() {
        profile = (ImageView) view.findViewById(R.id.up_profile);
        signeture = (ImageView) view.findViewById(R.id.up_signeture);
        qrcode = (ImageView) view.findViewById(R.id.up_qr);
        name = (EditText) view.findViewById(R.id.up_name);
        empid = (EditText) view.findViewById(R.id.up_empid);
        blood = (EditText) view.findViewById(R.id.up_bloodgrp);
        mobileno = (EditText) view.findViewById(R.id.up_phonenumber);
        address = (EditText) view.findViewById(R.id.up_address);
        shop = (EditText) view.findViewById(R.id.up_shopname);
        workplace = (EditText) view.findViewById(R.id.up_workplace);
        designition = (EditText) view.findViewById(R.id.designition_emp);
        nid = (EditText) view.findViewById(R.id.nid_emp);
        acc_number = (EditText) view.findViewById(R.id.bank_acc_emp);
        update = (Button) view.findViewById(R.id.update_profile);
    }

    public void imageUpdate(){
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_FILE);
                Imagetitle="profile";
            }
        });
        signeture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_FILE);
                Imagetitle = "signeture";
            }
        });
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_FILE);
                Imagetitle = "qrcode";
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK && data != null && data.getData() !=null){
            if (Imagetitle.equals("profile")){
                profile_uri = data.getData();
                Picasso.get().load(profile_uri).into(profile);
            }else if (Imagetitle.equals("signeture")){
                signeture_uri = data.getData();
                Picasso.get().load(signeture_uri).into(signeture);
            }else if(Imagetitle.equals("qrcode")){
                qr_uri = data.getData();
                Picasso.get().load(qr_uri).into(qrcode);
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));
    }
}
