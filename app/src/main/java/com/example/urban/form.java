package com.example.urban;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class form extends AppCompatActivity {
    TextInputEditText  ename,edob,ephoneNumber,ebloodGroup,equalification,eaadhar,eaddress,earea;
    DatabaseReference formref;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Spinner staticSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.Specialization_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticSpinner.setAdapter(staticAdapter);

        ename = findViewById(R.id.name);
        edob = findViewById(R.id.dob);
        ephoneNumber = findViewById(R.id.phoneNumber);
        ebloodGroup = findViewById(R.id.blood);
        equalification = findViewById(R.id.qualification);
        eaadhar = findViewById(R.id.aadhar);
        eaddress = findViewById(R.id.address);
        earea = findViewById(R.id.area);
        submit = findViewById(R.id.submit);

        formref = FirebaseDatabase.getInstance().getReference("Form");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ename.getText().toString()) || TextUtils.isEmpty(edob.getText().toString()) || TextUtils.isEmpty(ephoneNumber.getText().toString()) ||
                        TextUtils.isEmpty(ebloodGroup.getText().toString()) ||
                        TextUtils.isEmpty(equalification .getText().toString()) || TextUtils.isEmpty(eaadhar.getText().toString()) ||
                        TextUtils.isEmpty(eaddress .getText().toString()) || TextUtils.isEmpty(earea.getText().toString())
                )
                {
                    Toast.makeText(form.this, "Fill all details",Toast.LENGTH_LONG).show();
                }
                else {
                    String name = ename.getText().toString();
                    String dob = edob.getText().toString();
                    String phoneNumber = ephoneNumber.getText().toString();
                    String bloodGroup = ebloodGroup.getText().toString();
                    String spec = staticSpinner.getSelectedItem().toString();
                    String qualification = equalification.getText().toString();
                    String aadhar = eaadhar.getText().toString();
                    String address = eaddress.getText().toString();
                    String area = earea.getText().toString();

                    HashMap hashMap = new HashMap();
                    hashMap.put("Name", name);
                    hashMap.put("dob", dob);
                    hashMap.put("phoneNumber", phoneNumber);
                    hashMap.put("BloodGroup", bloodGroup);
                    hashMap.put("Specialization", spec);
                    hashMap.put("qualification", qualification);
                    hashMap.put("aadharNumber", aadhar);
                    hashMap.put("address", address);
                    hashMap.put("area", area);
                    formref.child(phoneNumber).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(form.this, "Data Uploaded Succesfully", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(form.this,button.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(form.this, "Failed to Upload Data", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });


    }

}