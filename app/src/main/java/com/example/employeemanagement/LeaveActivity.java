package com.example.employeemanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LeaveActivity extends Fragment {
    private static final int SELECT_FILE = 109;
    private static final int SELECT_DOCUMENT = 110;
    View view;
    LinearLayout leave_reson;
    TextView sick, casual, annual, leaveWithoupay, bodytext, img_txt, doc_txt, name, designition,
            area, workplace, phone, apply_leave,footer,header_leave,shop;
    EditText reson;
    ImageView setsigneture, addDocuments;
    Uri sign_uri, doc_uri;
    DatabaseReference reference,referenceDoc,headerFooterReference;
    LinearLayout applicationText;
    String getName,getGmail;
    private Bitmap bitmap;
    long file_name = System.currentTimeMillis();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.leave_activity, container, false);
        initReference();
        headerFooterReference = FirebaseDatabase.getInstance().getReference("Header_Footer").child("Admin");
        headerFooterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                footer.setText("@ "+dataSnapshot.child("footer").getValue().toString());
                header_leave.setText("To\n"+""+dataSnapshot.child("header").getValue().toString()+"\n"+
                        dataSnapshot.child("footer").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DetailsActivity activity = (DetailsActivity) getActivity();
        String getkey = activity.values;
        referenceDoc = FirebaseDatabase.getInstance().getReference("Upload_leaveFile");
        sick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bodytext.append(" " + sick.getText().toString() + " " + view.getContext().getResources().getText(R.string.applicationd));
                leave_reson.setVisibility(View.GONE);
            }
        });
        casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bodytext.append(" " + casual.getText().toString() + " " + view.getContext().getResources().getText(R.string.applicationd));
                leave_reson.setVisibility(View.GONE);
            }
        });
        annual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bodytext.append(" " + annual.getText().toString() + " " + view.getContext().getResources().getText(R.string.applicationd));
                leave_reson.setVisibility(View.GONE);
            }
        });
        leaveWithoupay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bodytext.append(" " + leaveWithoupay.getText().toString() + " " + view.getContext().getResources().getText(R.string.applicationd));
                leave_reson.setVisibility(View.GONE);
            }
        });

        setsigneture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bodytext.append("\n" + reson.getText().toString());
                img_txt.setVisibility(View.GONE);
                reson.setVisibility(View.GONE);
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, SELECT_FILE);
            }
        });
        addDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doc_txt.setVisibility(View.GONE);
                Intent document_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(document_intent, SELECT_DOCUMENT);
            }
        });
        reference.child(getkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getName = dataSnapshot.child("name").getValue().toString();
                getGmail = dataSnapshot.child("email").getValue().toString();
                name.setText(dataSnapshot.child("name").getValue().toString());
                designition.setText("Designition: " + dataSnapshot.child("designition").getValue().toString());
                area.setText("Area: " + dataSnapshot.child("address").getValue().toString());
                workplace.setText("Work Place: "+dataSnapshot.child("workplace").getValue().toString());
                phone.setText("Phone Number: " + dataSnapshot.child("mobile").getValue().toString());
                shop.setText("Shop Name: " + dataSnapshot.child("shop").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        apply_leave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (sign_uri != null && reson.getText() !=null) {
                    Log.d("size", " " + applicationText.getWidth() + "  " + applicationText.getWidth());
                    bitmap = loadBitmapFromView(applicationText, applicationText.getWidth(), applicationText.getHeight());
                    createPdf();
                }else {
                    Toast.makeText(getContext(), "empty are not allow", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        String targetPdf = "/sdcard/"+file_name+".pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        document.close();
        Toast.makeText(getContext(), "PDF of Scroll is created!!! "+filePath, Toast.LENGTH_SHORT).show();

        final ProgressDialog progres = new ProgressDialog(getContext());
        progres.setTitle("Uploading...");
        progres.show();
        progres.setCancelable(false);

        final String saveChild = file_name+".pdf";
        final StorageReference leaveReference = FirebaseStorage.getInstance().getReference("Upload_leaveFile").child(saveChild);

        leaveReference.putFile(Uri.fromFile(new File(""+filePath))).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        leaveReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UploadFile uploadFile = new UploadFile(saveChild,uri.toString(),getName,getGmail);
                                String key = referenceDoc.push().getKey();
                                referenceDoc.child(key).setValue(uploadFile);
                                Toast.makeText(getContext(),"Uploaded Successfull",Toast.LENGTH_LONG).show();
                                progres.dismiss();
                                openGeneratedPDF();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progres.setMessage("Uploaded: "+(int)progress+"%");
            }
        });

    }

    private void openGeneratedPDF() {
        File file = new File("/sdcard/"+file_name+".pdf");
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            sign_uri = data.getData();
            Picasso.get().load(sign_uri).into(setsigneture);
        }
        if (requestCode == SELECT_DOCUMENT && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            doc_uri = data.getData();
            Picasso.get().load(doc_uri).into(addDocuments);
        }
    }

    public void initReference() {
        leave_reson = (LinearLayout) view.findViewById(R.id.leave_reason_id);
        sick = (TextView) view.findViewById(R.id.sicklLeave_id);
        casual = (TextView) view.findViewById(R.id.casualLeave_id);
        annual = (TextView) view.findViewById(R.id.annualLeave_id);
        leaveWithoupay = (TextView) view.findViewById(R.id.leaveWithoupay);
        bodytext = (TextView) view.findViewById(R.id.bodytextId);
        reson = (EditText) view.findViewById(R.id.reason_clickid);
        setsigneture = (ImageView) view.findViewById(R.id.signeture_image);
        img_txt = (TextView) view.findViewById(R.id.img_textid);
        addDocuments = (ImageView) view.findViewById(R.id.adddocuments_image);
        doc_txt = (TextView) view.findViewById(R.id.documents_text);
        name = (TextView) view.findViewById(R.id.leave_name);
        designition = (TextView) view.findViewById(R.id.leave_designition);
        area = (TextView) view.findViewById(R.id.leave_area);
        header_leave = (TextView)view.findViewById(R.id.headerleave_id);
        workplace = (TextView) view.findViewById(R.id.leave_workplace);
        phone = (TextView) view.findViewById(R.id.leave_phone);
        shop = (TextView) view.findViewById(R.id.leave_shop);
        apply_leave = (TextView) view.findViewById(R.id.apply_leavetext_id);
        footer = (TextView) view.findViewById(R.id.vivoteam_id);
        applicationText = (LinearLayout) view.findViewById(R.id.main_applicationid);
        reference = FirebaseDatabase.getInstance().getReference("uploads");
    }
}
