package com.example.employeemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentsAdmin extends Fragment {

    private static final int GET_FILE = 1;
    View view;
    FloatingActionButton addButton;
    Uri file_uri,dat;
    String category_text,file_name;
    String displayName;
    ArrayList<UploadFile> document_list;
    MyAdapterDocuments adapterDocuments;
    RecyclerView recyclerView;

    Spinner category;
    String names[] =  {"Select Category", "Office", "Market", "Patuakhali","Barishal","Bhola","Jhalakhati","Barguna","Pirojpur"};
    final List<String> namelist = new ArrayList<>(Arrays.asList(names));
    //firebase
    DatabaseReference databaseReference;

    public DocumentsAdmin() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.documentsadmin, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Uploaded_Files");
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_documents_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        document_list = new ArrayList<>();

        addButton = (FloatingActionButton) view.findViewById(R.id.documentsadd_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findDocument();
            }
        });
        category = (Spinner) view.findViewById(R.id.spinner_category);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    UploadFile file = dataSnapshot1.getValue(UploadFile.class);
                    document_list.add(file);
                }
                adapterDocuments = new MyAdapterDocuments(document_list,getContext(),"admin");
                recyclerView.setAdapter(adapterDocuments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            dat = data.getData();
            setFileType();
        } else {
            Toast.makeText(getContext(), "Failled", Toast.LENGTH_LONG).show();
        }
    }

    private void findDocument() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, GET_FILE);
    }

    public void setFileType() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        View view1 = getLayoutInflater().inflate(R.layout.setfiletype,null);
        Spinner setFilespinner = (Spinner) view1.findViewById(R.id.spinner_category);
        ImageView cancel = (ImageView)view1.findViewById(R.id.cancel_image);
        TextView upload = (TextView)view1.findViewById(R.id.upload_document);
        final ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(view.getContext(), R.layout.categorry_spinner, namelist) {
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
        setFilespinner.setAdapter(category_adapter);
        setFilespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    category_text = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        alert.setView(view1);
        final AlertDialog dialog1 = alert.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category_text != null){
                    final ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.setTitle("Uploading....");
                    dialog.show();

                    //get the current name of the document
                    String uriString = dat.toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getActivity().getContentResolver().query(dat, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                Toast.makeText(getContext(), ""+displayName, Toast.LENGTH_SHORT).show();
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        Toast.makeText(getContext(), ""+displayName, Toast.LENGTH_SHORT).show();
                    }

                    file_uri = dat;
                    StorageReference reference = FirebaseStorage.getInstance().getReference("Uploaded_Files");
                    file_name = file_uri.getLastPathSegment();
                    final StorageReference file = reference.child(""+displayName);


                    file.putFile(file_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    file_uri = uri;
                                    UploadFile uploadFile = new UploadFile(displayName,category_text,file_uri.toString());
                                    String key = databaseReference.push().getKey();
                                    databaseReference.child(key).setValue(uploadFile);
                                    Toast.makeText(getContext(),"Uploaded Successfull",Toast.LENGTH_LONG).show();
                                    dialog.setCancelable(false);
                                    dialog.dismiss();
                                    dialog1.dismiss();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded: "+(int)progress+"%");
                        }
                    });

                }else {
                    Toast.makeText(view.getContext(), "Please select category", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.setCancelable(false);
        dialog1.show();
    }
}
