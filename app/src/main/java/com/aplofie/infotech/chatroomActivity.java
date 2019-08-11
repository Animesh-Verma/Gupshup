package com.aplofie.infotech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class chatroomActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    EditText textmessage;
    ImageView buttonsend;
    String dec;
    String se,re;
private RelativeLayout con;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    List<chatpojo> list = new ArrayList<chatpojo>();
    //ArrayList<chatpojo> list = new ArrayList<>();
   // List<chatpojo> pist = new ArrayList<chatpojo>();

   RecyclerView.Adapter adapter ;
public int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);



        buttonsend =findViewById(R.id.buttonsend);
        textmessage=findViewById(R.id.textmessage);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Chatroom");

        recyclerView = findViewById(R.id.chatrecycler);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(chatroomActivity.this));
       // getData();


   buttonsend.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {


uploaddata();
textmessage.setText(null);
//getData();

       }
   });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    public void uploaddata()
 {
     Date d=new Date();
     SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
     String currentDateTimeString = sdf.format(d);
     //Date currentTime = Calendar.getInstance().getTime();
     Intent intent = getIntent();
     String recieverid = intent.getStringExtra("uid");
     String senderid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
     String textmsg = textmessage.getText().toString();
     String ctime = currentDateTimeString;
     Storedata(recieverid , senderid , textmsg , ctime);

 }






    public void Storedata(String recieverid , String senderid , String textmsg , String ctime)
    {

chatpojo pojo = new chatpojo();
pojo.setMessage(textmsg);
pojo.setReciever(recieverid);
pojo.setSender(senderid);
pojo.setTimestamp(ctime);
databaseReference.push().getKey();
databaseReference.push().setValue(pojo);
    }


    public void getData()
    {



        //DatabaseReference usersDatabase;
        final Intent intent = getIntent();
       final String reciever = intent.getStringExtra("uid");
        final String sender = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //databaseReference = firebaseDatabase.getReference("Chatroom");
        //usersDatabase = firebaseDatabase.getReference().child("Chatroom");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //String rec = dataSnapshot.child("reciever").getValue().toString();
                //String sen = dataSnapshot.child("sender").getValue().toString();
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String rec = dataSnapshot.child("reciever").getValue().toString();
                    String sen = dataSnapshot.child("sender").getValue().toString();
                    // se=sen;re=rec;


                    chatpojo chatdetail = dataSnapshot.getValue(chatpojo.class);
                    if (sen.equals(sender) && rec.equals(reciever) || sen.equals(reciever) && rec.equals(sender)) {

                        if (sen.equals(sender) && reciever.equals(rec)) {
                            list.add(chatdetail);
                            dec = "false";
                            Log.e("dec", "false");
                            Log.e("sender", sender);
                        }
                     else {
                        list.add(chatdetail);
                        dec = "true";
                        Log.e("dec", "true");
                        Log.e("reciever", reciever);
                    }
                    // }

                   /*     if(dec.equals(true))
                            list.add(chatdetail);
                    else
                            list.add(chatdetail);*/


                }
            }

                adapter = new chatAdapter(chatroomActivity.this, list,sender,reciever);

                recyclerView.setAdapter(adapter);
                recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}


