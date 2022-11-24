package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyAdapter adapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String user_phone_number;
    RecyclerView recyclerView;
    FloatingActionButton addContactActivity;
    Button btn_log_out;
    List<Module> list = new ArrayList<Module>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        user_phone_number = intent.getStringExtra("phone_num");
        recyclerView = findViewById(R.id.recyclerView);
        addContactActivity = findViewById(R.id.add_contact_activity);
        list.clear();


        firebaseDatabase.getReference("chat").addValueEventListener(new ValueEventListener() {
            @Override
            //acces to all chat branches
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()){

                    int count = (int) snap.getChildrenCount();
                    final int[] i = {1};
                    //acces to each branch user+sender

                    firebaseDatabase.getReference("chat").child(snap.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                Module module =  new Module();
                                list.clear();
                                getName(snapshot1, new GetMsgInterface() {

                                    @Override
                                    public void onSucces(String name,String profilePhoto) {

                                        module.setSenderName(name);
                                        module.setProfilePhoto(profilePhoto);
                                        long heures = (((Long.parseLong(snapshot1.getKey()) / 1000)  / 60)/60)%24;
                                        int minutes = ((int)((Long.parseLong(snapshot1.getKey()) / 1000)/60) % 60);
                                        module.setTime(String.valueOf(heures)+":"+String.valueOf(minutes));
                                        module.setMessage(snapshot1.child("message").getValue().toString());
                                        //System.out.println(module.getSenderName());
                                        if(list.size()>1)
                                            //System.out.println(list.get(list.size()-1).getSenderName());
                                        if(list.size()>1&& module.getSenderName().equals(list.get(list.size() - 1).getSenderName()))
                                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                                        if(snapshot1.child("to").getValue()!=null&&!snapshot1.child("to").getValue().equals(user_phone_number)){
                                            if(i[0] ==count)
                                                list.add(module);
                                            i[0]++;
                                        }else{
                                            System.out.println("it's null");
                                        }

                                        adapter = new MyAdapter(MainActivity.this,list,user_phone_number);
                                        recyclerView.setAdapter(adapter);

                                    }

                                    @Override
                                    public void onError(String Error) {
                                        Toast.makeText(MainActivity.this, Error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                adapter = new MyAdapter(MainActivity.this,list,user_phone_number);
                                recyclerView.setAdapter(adapter);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error 404", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addContactActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this,AddContactActivity.class);
                intent1.putExtra("user_phone_num",user_phone_number);
                startActivity(intent1);
            }
        });

    }

    interface GetMsgInterface {
        void onSucces(String name,String profilePhoto);
        void onError(String Error);
    }

    private void getName(DataSnapshot snapshot1,GetMsgInterface getMsgInterface) {

        System.out.println("is this equals to null "+snapshot1.child("to").getValue());
        System.out.println(snapshot1.child("to").getValue());

        try {



        firebaseDatabase.getReference("users").child(String.valueOf(snapshot1.child("to").getValue())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                System.out.println(snapshot);
                try {
                    getMsgInterface.onSucces(snapshot.child("userName").getValue().toString(),snapshot.child("profilePhoto").getValue().toString());
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getMsgInterface.onError("Error!!");
            }
        });
        }catch (Exception e){
            //System.out.println(e);
        }
    }


}