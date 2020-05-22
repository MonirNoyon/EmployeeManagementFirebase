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

public class MyAdapterLeave extends RecyclerView.Adapter<MyViewHolderLeave> {

    ArrayList<UploadFile> nameArray;
    Context context;
    StorageReference reference;
    String doc_name;
    DatabaseReference databaseReference;

    public MyAdapterLeave(ArrayList<UploadFile> nameArray, Context context) {
        this.nameArray = nameArray;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolderLeave onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leave, parent, false);
        MyViewHolderLeave holderLeave = new MyViewHolderLeave(view);
        return holderLeave;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderLeave holder, int position) {
        doc_name = holder.doc.getText().toString();
        reference = FirebaseStorage.getInstance().getReference("Upload_leaveFile");
        final UploadFile uploadFile = nameArray.get(position);
        holder.doc.setText(uploadFile.getDoc_name());
        holder.empname.setText(uploadFile.getName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("Delete File");
                alertBuilder.setMessage("Do you want to delete " + uploadFile.getDoc_name() + " ?");
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("Upload_leaveFile");
                        databaseReference.orderByChild("doc_name").equalTo(uploadFile.getDoc_name())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            dataSnapshot1.getRef().removeValue();
                                        }
                                        Toast.makeText(context, "Successfully deleted" + uploadFile.getDoc_name(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.child(holder.doc.getText().toString()).getDownloadUrl().
                        addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(context, "" + uri.toString(), Toast.LENGTH_SHORT).show();
                                downLoadFile(uri, holder.doc.getText().toString());
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

    @Override
    public int getItemCount() {
        return nameArray.size();
    }

    public void downLoadFile(Uri uri, String s) {

        Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();
        File file = new File(Environment.getExternalStorageDirectory(), s);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("File Download");
        request.setDescription("This is downloaded file");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(Uri.fromFile(file));
        downloadManager.enqueue(request);
    }

}
