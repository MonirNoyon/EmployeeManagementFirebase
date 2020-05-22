package com.example.employeemanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HeaderFooter extends Fragment {
    View view;
    TextInputEditText header,footer;
    TextView update;
    DatabaseReference reference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.header_footer,container,false);
        initReference();
        reference = FirebaseDatabase.getInstance().getReference("Header_Footer");
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (header.getText().equals("") || footer.getText().equals("")){
                    Toast.makeText(getContext(), "Please Enter Header and Footer", Toast.LENGTH_SHORT).show();
                }else{
                    UploadFile uploadFile = new UploadFile(header.getText().toString(),footer.getText().toString());
                    reference.child("Admin").setValue(uploadFile);
                    Toast.makeText(getContext(), "Uploaded Successfull", Toast.LENGTH_SHORT).show();
                    header.setText("");
                    footer.setText("");
                }
            }
        });
        return view;
    }
    public void initReference(){
        header = (TextInputEditText)view.findViewById(R.id.header_id);
        footer = (TextInputEditText)view.findViewById(R.id.footer_id);
        update = (TextView) view.findViewById(R.id.updateheaderfooter_id);
    }
}
