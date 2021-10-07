package com.example.urban;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class editform extends AppCompatActivity {
    TextInputEditText ename,edob,ebloodGroup,equalification,eaadhar,eaddress,earea;
    TextView ephoneNumber;
    DatabaseReference formref;
    Button submit;
    double dlatitude;
    double dlongitude;
    String pn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editform);
        Spinner staticSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.Specialization_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticSpinner.setAdapter(staticAdapter);

        pn=getIntent().getStringExtra("phonenumber");

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

        formref.child(pn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name=snapshot.child("Name").getValue().toString();
                    ename.setText(name);
                    String dob=snapshot.child("dob").getValue().toString();
                    edob.setText(dob);
                    String phoneNumber=snapshot.child("phoneNumber").getValue().toString();
                    ephoneNumber.setText(phoneNumber);
                    String bloodgroup=snapshot.child("BloodGroup").getValue().toString();
                    ebloodGroup.setText(bloodgroup);
                    String qualification=snapshot.child("qualification").getValue().toString();
                    equalification.setText(qualification);
                    String aadhar=snapshot.child("aadharNumber").getValue().toString();
                    eaadhar.setText(aadhar);
                    String area=snapshot.child("area").getValue().toString();
                    earea.setText(area);
                    eaadhar.setText(aadhar);
                    String address=snapshot.child("address").getValue().toString();
                    eaddress.setText(address);
                    String specia=snapshot.child("Specialization").getValue().toString();
                    int spinnerpos=staticAdapter.getPosition(specia);
                    staticSpinner.setSelection(spinnerpos);

                }
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
                        TextUtils.isEmpty(eaddress .getText().toString()) || TextUtils.isEmpty(earea.getText().toString())
                )
                {
                    Toast.makeText(editform.this, "Fill all details",Toast.LENGTH_LONG).show();
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

                    String latitude = String.valueOf(dlatitude);
                    String longitude = String.valueOf(dlongitude);
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
                    hashMap.put("latitude", latitude);
                    hashMap.put("longitude", longitude);
                    formref.child(phoneNumber).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(editform.this, "Data Uploaded Succesfully", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(editform.this,urban.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editform.this, "Failed to Upload Data", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });


    }
    }
