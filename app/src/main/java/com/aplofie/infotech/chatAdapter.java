package com.aplofie.infotech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {


    Context context;
    chatpojo cp;
    private List<chatpojo> Pojos = new ArrayList<>();
   // private List<chatpojo> chatpojos ;
    FirebaseAuth firebaseAuth;
    String pre,sender,reciever;







    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView msgl ,msgr, tspl,tspr,lop;

        public MyViewHolder(View itemView) {
            super(itemView);
   // msg = itemView.findViewById(R.id.text_message_body);
    //tsp = itemView.findViewById(R.id.text_message_time);


            msgl=itemView.findViewById(R.id.left_chat);
            msgr=itemView.findViewById(R.id.right_chat);
            tspl=itemView.findViewById(R.id.left_time);
            tspr=itemView.findViewById(R.id.right_time);




           // msg = itemView.findViewById(R.id.chat_left_msg_text_view);
          //  tsp = itemView.findViewById(R.id.chat_right_msg_text_view);


        }

        }



    public chatAdapter(Context cOntext , List<chatpojo> chatpojoList,String sender , String reciever)
    {
        this.sender=sender;
        this.reciever=reciever;
        //this.sen=sen;
        //this.rec=rec;
       //this.pre = dec;
         this.context = cOntext;
        //this.chatpojos = chatpojoList;
        this.Pojos= chatpojoList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatsend, parent, false);
       MyViewHolder viewHolder = new MyViewHolder(itemView);
                    return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        cp = Pojos.get(position);



            if(sender.equals(cp.getSender()) && reciever.equals(cp.getReciever()) || sender.equals(cp.getReciever()) && reciever.equals(cp.getSender())) {
                Log.d("12345", "inside ");
                if (cp.getSender().equals(sender)) {
                    //chatpojo cp = chatpojos.get(position);
                    //holder.msg.setText(cp.getMessage());
                    //holder.tsp.setText(cp.getTimestamp());

                  // RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    //par.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    //par.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                    //par.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    //par.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    //TextView textView = new TextView(context);
                    //textView.setLayoutParams(par);
                    //  textView.setText(cp.getMessage());
//holder.msg.setText(cp.getMessage());
//holder.msgl.setVisibility(View.GONE);
//int colr;
//colr= Color.parseColor("#ed5f32");
                    //holder.msgr.setLayoutParams(par);
                   // holder.msg.setBackgroundColor(colr);

                    holder.msgr.setText(cp.getMessage());
                    holder.tspr.setText(cp.getTimestamp());
                    //holder.msg.setBackgroundResource(R.drawable.boxbackground);
holder.msgl.setVisibility(View.GONE);
holder.tspl.setVisibility(View.GONE);
                    Log.d("12345", "right ");
                    // holder.tsp.setText(cp.getTimestamp());
                    //holder.msg.setText(cp.getMessage());
                } else if(sender.equals(cp.getReciever()) && reciever.equals(cp.getSender())){
//else if (sender.equals(rec))  {
                    //RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    holder.msgl.setText(cp.getMessage());
                    holder.tspl.setText(cp.getTimestamp());
                    holder.msgr.setVisibility(View.GONE);
                    holder.tspr.setVisibility(View.GONE);
                   // par.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                   // holder.msg.setLayoutParams(par);
                    //holder.msg.setText(cp.getMessage());
                    Log.d("12345", "left ");

                    // holder.tsp.setText(cp.getTimestamp());
                    // LinearLayout.LayoutParams par = (LinearLayout.LayoutParams)holder.msg.getLayoutParams();
                    //holder.msg.setLayoutParams(par);
           /*else if(position%2==0) {
                holder.msg.setText(cp.getMessage());
                holder.tsp.setText(cp.getTimestamp());
            }*///holder.msg.setText(cp.getMessage());
                }

                // }

                //}



            }

       }

    @Override
    public int getItemCount() {
            return Pojos.size() ;
        }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}




