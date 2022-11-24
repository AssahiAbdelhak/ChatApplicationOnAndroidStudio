package com.example.chatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    Context context;
    List<MessageModel> list;

    public MessageAdapter(Context context, List<MessageModel> list ) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.message_unity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        holder.text.setText(list.get(position).getMessage());
        if(list.get(position).getItFrom()){
            holder.text.setBackgroundResource(R.drawable.recievedmessage);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.text.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_START);
            lp.removeRule(RelativeLayout.ALIGN_PARENT_END);
            holder.text.setPadding(80,10,30,10);
            holder.text.setLayoutParams(lp);
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.msg_unity);
        }
    }
}
