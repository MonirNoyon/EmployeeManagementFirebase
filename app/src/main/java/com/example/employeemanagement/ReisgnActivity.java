package com.example.employeemanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
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
import java.util.Calendar;

public class ReisgnActivity extends Fragment {
    private static final int SELECT_SIGNETURE = 5;
    View view;
    LinearLayout dateLinearLayout,application;
    TextView body,signeture_text,name, designition,
            area, workplace,phone,resign,footer,header_resign,shop;
    DatabaseReference reference,referenceDoc,headerFooterReference;
    ImageView effective,signeture;
    String getName,getGmail;
    Bitmap bitmap;
    long file_name = System.currentTimeMillis();
    private Uri sign_uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reisgn_activity,container,false);
        initReference();
        headerFooterReference = FirebaseDatabase.getInstance().getReference("Header_Footer").child("Admin");
        headerFooterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                footer.setText("@ "+dataSnapshot.child("footer").getValue().toString());
                header_resign.setText("To\n"+""+dataSnapshot.child("header").getValue().toString()+"\n"+
                        dataSnapshot.child("footer").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DetailsActivity activity = (DetailsActivity) getActivity();
        String getkey = activity.values;
        referenceDoc = FirebaseDatabase.getInstance().getReference("Upload_resignFile");
        effective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.Theme_Design_BottomSheetDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month + 1;
                                String date = day + "-" + month + "-" + year;
                                body.append(date);
                                dateLinearLayout.setVisibility(View.GONE);
                            }
                        }, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                dialog.show();
            }
        });
        signeture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signeture_text.setVisibility(View.GONE);
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, SELECT_SIGNETURE);
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
        resign.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (sign_uri !=null) {
                    Log.d("size", " " + application.getWidth() + "  " + application.getWidth());
                    bitmap = loadBitmapFromView(application, application.getWidth(), application.getHeight());
                    createPdf();
                }else {
                    Toast.makeText(getContext(), "empty filled are not allow", Toast.LENGTH_SHORT).show();
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
        final StorageReference leaveReference = FirebaseStorage.getInstance().getReference("Upload_resignFile").child(saveChild);

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
        if (requestCode == SELECT_SIGNETURE && resultCode == Activity.RESULT_OK && data !=null && data.getData() != null){
            sign_uri = data.getData();
            Picasso.get().load(data.getData()).into(signeture);
        }
    }

    public void initReference(){
        reference = FirebaseDatabase.getInstance().getReference("uploads");
        dateLinearLayout = (LinearLayout)view.findViewById(R.id.date_linearid);
        body = (TextView)view.findViewById(R.id.bodytextId);
        signeture_text = (TextView)view.findViewById(R.id.img_textid);
        effective = (ImageView)view.findViewById(R.id.effective_calenderid);
        signeture = (ImageView)view.findViewById(R.id.signeture_image);
        name = (TextView) view.findViewById(R.id.leave_name);
        designition = (TextView) view.findViewById(R.id.leave_designition);
        area = (TextView) view.findViewById(R.id.leave_area);
        workplace = (TextView) view.findViewById(R.id.leave_workplace);
        footer = (TextView) view.findViewById(R.id.vivoteam_id);
        phone = (TextView) view.findViewById(R.id.leave_phone);
        shop = (TextView) view.findViewById(R.id.leave_shop);
        header_resign = (TextView)view.findViewById(R.id.header_resign_id);
        resign = (TextView) view.findViewById(R.id.apply_resigntext_id);
        application= (LinearLayout)view.findViewById(R.id.main_resignApplication);
    }
}
