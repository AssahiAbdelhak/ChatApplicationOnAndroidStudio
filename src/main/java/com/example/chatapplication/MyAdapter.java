package com.example.chatapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<Module> list;
    Context context;
    String user_phone_number;

    public MyAdapter(Context context, List<Module> list,String user_phonr_number){
        this.context = context;
        this.list = list;
        this.user_phone_number = user_phonr_number;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_exemple_unit,parent,false));
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.message.setText(list.get(position).getMessage());
        holder.senderName.setText(list.get(position).getSenderName());
        holder.time.setText(list.get(position).getTime());
        if(!list.get(position).getProfilePhoto().isEmpty()){
            Picasso.get().load(list.get(position).getProfilePhoto()).into(holder.image);
        }else{
            final String DEFAULT_USER_PHOTO_PROFILE = "https://cdn-icons-png.flaticon.com/512/1160/1160040.png?w=826&t=st=1669295757~exp=1669296357~hmac=43195d10420f7b5b3ea7b816fed62164425935b8a07d6f55d39b03981e1a29fc";
            Picasso.get().load(DEFAULT_USER_PHOTO_PROFILE).into(holder.image);
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
                firebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            if(list.get(position).getSenderName().equals(dataSnapshot.child("userName").getValue())){
                                Intent intent = new Intent(context,messagesActivity.class);
                                intent.putExtra("sender_number_phone",dataSnapshot.getKey());
                                intent.putExtra("user_number_phone",user_phone_number);
                                context.startActivity(intent);
                                Activity activity =  (Activity) context;
                                activity.finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView senderName,message,time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            senderName = itemView.findViewById(R.id.senderName);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
        }
    }
}
