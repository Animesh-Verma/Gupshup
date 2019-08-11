package com.aplofie.infotech;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class adapterp extends
       RecyclerView.Adapter<adapterp.ViewHolder>{

    Context context;
    List<userpojo> MainImageUploadInfoList;

    public adapterp(Context context, List<userpojo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        userpojo up = MainImageUploadInfoList.get(position);

        holder.edituser.setText(up.getEmail());
        Glide.with(context).load(up.getImageuri()).into(holder.imageView);
        //holder.imageView.setImageResource(Integer.parseInt(up.getImageuri()));

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView edituser;
        public ImageView imageView;

        public ViewHolder(View itemView) {

            super(itemView);

            edituser=  itemView.findViewById(R.id.textname);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    }


