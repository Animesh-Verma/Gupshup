package com.aplofie.infotech;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment1 extends android.support.v4.app.Fragment {

    public fragment1(){}
    // Creating DatabaseReference.
    DatabaseReference databaseReference;
FirebaseAuth firebaseAuth;
    // Creating RecyclerView.
//RecyclerView recyclerView;
    // Creating RecyclerView.Adapter.


    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<userpojo> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.activity_show_people, container, false);

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(getActivity());

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading");

        // Showing progress dialog.
        progressDialog.show();


        View view =  inflater.inflate(R.layout.activity_show_people, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.mRecyclerView);

        //recyclerView = getView().findViewById(R.id.mRecyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        // Adding Add Value Event Listener to databaseReference.
        // final RecyclerView finalRecyclerView = recyclerView;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    progressDialog.dismiss();

                    userpojo UploadInfo = postSnapshot.getValue(userpojo.class);

                    String em = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    if(UploadInfo.getUserid().equals(em))
                    {}
                    else {
                        list.add(UploadInfo);
                    }
                    }

                adapterpeople adapter = new adapterpeople(getActivity(),list);



                recyclerView.setAdapter(adapter);

                //RecyclerView.setAdapter(adapter);

                // Hiding the progress dialog.

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });



        return view;
    }




    public class adapterpeople extends
            RecyclerView.Adapter<ViewHolder> {
        private DatabaseReference databaseReference;

        public Context context;
        List<userpojo> MainUploadInfoList;



        //        RecyclerView recyclerView = getView().findViewById(R.id.mRecyclerView);
        public adapterpeople(Context context, List<userpojo> TempList) {

            this.MainUploadInfoList = TempList;

            this.context = context;


        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {



            final userpojo up = MainUploadInfoList.get(position);

            holder.textname.setText(up.getUsername());
                holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(getActivity(), chatroomActivity.class);
                        intent.putExtra("uid", up.getUserid());
                        startActivity(intent);


                        Toast.makeText(context, up.getUserid() + position, Toast.LENGTH_SHORT).show();
                    }
                });

                Glide.with(context).load(up.getImageuri()).into(holder.imageView);
            }


        @Override
        public int getItemCount() {

            return MainUploadInfoList.size();
        }


    }



        class ViewHolder extends RecyclerView.ViewHolder{

            public TextView textname;
            public ImageView imageView;
            LinearLayout constraintLayout;

            public ViewHolder(View itemView) {

                super(itemView);


                constraintLayout =   itemView.findViewById(R.id.row);
                textname = itemView.findViewById(R.id.textname);
                imageView = itemView.findViewById(R.id.imageView);
            }


        }


}

