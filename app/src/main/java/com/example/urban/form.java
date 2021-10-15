package com.example.urban;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class form extends AppCompatActivity {
    TextInputEditText  ename,edob,ephoneNumber,ebloodGroup,equalification,eaadhar,eaddress;
    DatabaseReference formref,spinnerref;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Spinner staticSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> staticAdapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticSpinner.setAdapter(staticAdapter);

        Spinner citySpinner=findViewById(R.id.cityspinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter
                .createFromResource(this, R.array.taluka,
                        android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);


        ename = findViewById(R.id.name);
        edob = findViewById(R.id.dob);
        ephoneNumber = findViewById(R.id.phoneNumber);
        ebloodGroup = findViewById(R.id.blood);
        equalification = findViewById(R.id.qualification);
        eaadhar = findViewById(R.id.aadhar);
        eaddress = findViewById(R.id.address);
        submit = findViewById(R.id.submit);

        formref = FirebaseDatabase.getInstance().getReference("Form");
        spinnerref=FirebaseDatabase.getInstance().getReference().child("Chips");

        spinnerref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String newspin=snapshot.getValue().toString();
                staticAdapter.add(newspin);
                staticAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ename.getText().toString()) || TextUtils.isEmpty(edob.getText().toString()) || TextUtils.isEmpty(ephoneNumber.getText().toString()) ||
                        TextUtils.isEmpty(ebloodGroup.getText().toString()) ||
                        TextUtils.isEmpty(equalification .getText().toString()) || TextUtils.isEmpty(eaadhar.getText().toString()) ||
                        TextUtils.isEmpty(eaddress .getText().toString())
                )
                {
                    Toast.makeText(form.this, "Fill all details",Toast.LENGTH_LONG).show();
                }
                if (citySpinner.getSelectedItem().toString().equals("Select Serving Area")){
                    Toast.makeText(form.this, "Select Serving Area",Toast.LENGTH_LONG).show();
                }
                else {
                    String name = ename.getText().toString();
                    String dob = edob.getText().toString();
                    String phoneNumber = ephoneNumber.getText().toString();
                    String bloodGroup = ebloodGroup.getText().toString();
                    String spec = staticSpinner.getSelectedItem().toString();
                    String area = citySpinner.getSelectedItem().toString();
                    String qualification = equalification.getText().toString();
                    String aadhar = eaadhar.getText().toString();
                    String address = eaddress.getText().toString();

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
                    hashMap.put("count","0");
                    formref.child(phoneNumber).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(form.this, "Data Uploaded Succesfully", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(form.this,urban.class);
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