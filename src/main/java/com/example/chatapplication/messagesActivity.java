package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MessageModel> list = new ArrayList<>();
    MessageAdapter adapter;
    ImageButton send_btn,come_later,arrow_back;
    EditText message_edit_text;
    TextView sender_name;
    CircleImageView profilePhoto;
    String user_phone_number,sender_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        sender_name = findViewById(R.id.sender_name);
        send_btn = findViewById(R.id.send_btn);
        message_edit_text = findViewById(R.id.message_input);
        profilePhoto = findViewById(R.id.profilePhoto);
        arrow_back = findViewById(R.id.arrow_back);
        come_later = findViewById(R.id.setting);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(messagesActivity.this,MainActivity.class));
                finish();
            }
        });

        come_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(messagesActivity.this, "This will be available soon!", Toast.LENGTH_SHORT).show();
            }
        });

        sender_phone_number=getIntent().getStringExtra("sender_number_phone");
        user_phone_number=getIntent().getStringExtra("user_number_phone");

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("users").child(sender_phone_number);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sender_name.setText(snapshot.child("userName").getValue(String.class));
                Picasso.get().load(snapshot.child("profilePhoto").getValue(String.class)).into(profilePhoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(message_edit_text.getText().toString().isEmpty()){
                    message_edit_text.setError("Enter a message, please");
                }else{
                    addMessageInFireBase(message_edit_text.getText().toString());
                    message_edit_text.setText("");
                }
            }
        });


        String user_number_phone = "null";


        recyclerView = findViewById(R.id.recycler_view);
        DatabaseReference firebaseDatabase1 = FirebaseDatabase.getInstance().getReference("chat").child(user_phone_number+sender_phone_number);
        firebaseDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data:snapshot.getChildren()) {
                    Boolean val;
                    String msg;
                    val = true;
                    //System.out.println("here!!!!!!!!!!!!!!!!!");
                    //System.out.println(data.child("to").getValue());
                    //the app crashes
//                    if(data.child("to").getValue().equals(user_number_phone))
//                        val = false;
                    msg  = data.child("message").getValue(String.class);
                    list.add(new MessageModel(msg,val));
                }
                //System.out.println(list);
                adapter = new MessageAdapter(messagesActivity.this,list);
                recyclerView.setLayoutManager(new LinearLayoutManager(messagesActivity.this));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addMessageInFireBase(String message){
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("chat").child(user_phone_number+sender_phone_number);

        Date date = new Date();
        long ml = date.getTime();
        firebaseDatabase.child(String.valueOf(ml)).child("message").setValue(message);
        firebaseDatabase.child(String.valueOf(ml)).child("to").setValue(sender_phone_number);

    }}