package com.example.employeemanagement;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class MyAdapterDocuments extends RecyclerView.Adapter<MyViewHolderDocuments> {

    ArrayList<UploadFile> name;
    Context context;
    StorageReference reference;
    DatabaseReference dbreference;
    String file_name;
    String data_name;


    public MyAdapterDocuments(ArrayList<UploadFile> name, Context context,String data_name) {
        this.name = name;
        this.context = context;
        this.data_name = data_name;
    }

    @NonNull
    @Override
    public MyViewHolderDocuments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_document,parent,false);
        MyViewHolderDocuments myHolder = new MyViewHolderDocuments(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderDocuments holder, int position) {
        file_name = holder.name.getText().toString();
        final UploadFile uploadFile = name.get(position);
        holder.name.setText(uploadFile.getFile_name());
        if (data_name.equals("admin")) {
            holder.download.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.VISIBLE);
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setTitle("Delete Documents");
                        alertBuilder.setMessage("Do you want to delete " + uploadFile.getFile_name()+ " ?");
                        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbreference = FirebaseDatabase.getInstance().getReference("Uploaded_Files");
                                dbreference.orderByChild("file_name").equalTo(uploadFile.getFile_name()).
                                        addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    dataSnapshot1.getRef().removeValue();
                                                }
                                                Toast.makeText(context, uploadFile.getFile_name()+"deleted successfully", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                }
            });
        }else {
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference = FirebaseStorage.getInstance().getReference("Uploaded_Files");
                    reference.child(holder.name.getText().toString()).getDownloadUrl().
                            addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(context, "" + uri.toString(), Toast.LENGTH_SHORT).show();
                                    downLoadFile(uri, holder.name.getText().toString());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
    public void downLoadFile(Uri uri,String s){

        Toast.makeText(context, ""+s, Toast.LENGTH_SHORT).show();
        File file = new File(Environment.getExternalStorageDirectory(),s);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("File Download");
        request.setDescription("This is downloaded file");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(Uri.fromFile(file));
        downloadManager.enqueue(request);
    }
}
