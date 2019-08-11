package com.aplofie.infotech;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class chatrecieveadapter extends RecyclerView.Adapter<chatrecieveadapter.MyViewHolder> {

    Context context;
    private List<chatpojo> chatpojos;



    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView msg , tsp;

        public MyViewHolder(View view){
            super(view);
            msg = view.findViewById(R.id.text_message_body);
            tsp = view.findViewById(R.id.text_message_time);

        }
    }
    public chatrecieveadapter(Context cOntext , List<chatpojo> chatpojoList)
    {
        this.context = cOntext;
        this.chatpojos = chatpojoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatrecieved, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        chatpojo cp = chatpojos.get(position);
        holder.msg.setText(cp.getMessage());
        holder.tsp.setText(cp.getTimestamp());

    }

    @Override
    public int getItemCount() {
        return chatpojos.size() ;
    }





}
