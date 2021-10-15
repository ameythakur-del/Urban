package com.example.urban;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class editform extends AppCompatActivity {
    TextInputEditText ename,edob,ebloodGroup,equalification,eaadhar,eaddress;
    TextView ephoneNumber;
    DatabaseReference formref,spinnerref;
    Button submit;
    String pn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editform);

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

        pn=getIntent().getStringExtra("phonenumber");

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
                    int cityspinnerpos=cityAdapter.getPosition(area);
                    citySpinner.setSelection(cityspinnerpos);
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
                        TextUtils.isEmpty(eaddress .getText().toString())
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
                    String area = citySpinner.getSelectedItem().toString();

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
