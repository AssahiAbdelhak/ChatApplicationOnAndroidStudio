package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddContactActivity extends AppCompatActivity {
    String user_phone_num;
    EditText editTextPersonName,phoneNumber,message,photoProfileLink;
    MaterialButton btnAddSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Intent intent = getIntent();
        user_phone_num = intent.getStringExtra("user_phone_num");
        System.out.println(user_phone_num);
        editTextPersonName = findViewById(R.id.editTextPersonName);
        phoneNumber = findViewById(R.id.Phone_number);
        message = findViewById(R.id.Message);
        btnAddSend = findViewById(R.id.btn_add_send);
        photoProfileLink = findViewById(R.id.photoProfileLink);

        //addName();
        //addMessageInFireBase("Abdelhak","HelloWorld!!!");
        btnAddSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContactName();
            }
        });
    }

    private void addMessageInFireBase(String name,String message){
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("chat").child(user_phone_num+phoneNumber.getText());

        Date date = new Date();
        long ml = date.getTime();
        firebaseDatabase.child(String.valueOf(ml)).child("message").setValue(message);
        firebaseDatabase.child(String.valueOf(ml)).child("to").setValue(phoneNumber.getText().toString());
    }
//bien
    private void addContactNameInFireBase(String Name,String phoneNum){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseDatabase.getReference("users").child(phoneNum).child("userName").setValue(Name);
        firebaseDatabase.getReference("users").child(phoneNum).child("profilePhoto").setValue(photoProfileLink.getText().toString());
    }
//bien
    private void createContactName() {
        String name,phoneN,msg;
        name = editTextPersonName.getText().toString();
        phoneN = phoneNumber.getText().toString();
        msg = message.getText().toString();


        if(validateData(phoneN,name,msg)){
            addContactNameInFireBase(name,phoneN);
            addMessageInFireBase(name,msg);
            Toast.makeText(AddContactActivity.this, "Your contact has been succefully addded", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddContactActivity.this,MainActivity.class));
            finish();
        }

    }

    private List<String> getUsersNums(){
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
        List<String> userNames = new ArrayList<String>();
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    System.out.println(dataSnapshot.getKey());
                    userNames.add(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddContactActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return userNames;
    }

    private boolean validateData(String phone,String contactName, String message) {
        if(!Patterns.PHONE.matcher(phone).matches()){
            phoneNumber.setError("Please, insert a valid number phone");
            return false;
        }
        if(getUsersNums().contains(phone)){
            editTextPersonName.setError("This Contact Number phone already exists, please enter a valid contact name");
            return false;
        }
        if(message.isEmpty())
            return false;
        return true;
    }
}