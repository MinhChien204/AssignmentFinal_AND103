package com.example.less3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.less3.R;
import com.example.less3.TrangChu;
import com.example.less3.model.Sinhvien;

import java.util.List;

public class SinhvienAdapter extends RecyclerView.Adapter<SinhvienAdapter.ViewHolder> {
    List<Sinhvien> list;
    Context context;
    TrangChu trangChu;

    public SinhvienAdapter(List<Sinhvien> list, Context context, TrangChu trangChu) {
        this.list = list;
        this.context = context;
        this.trangChu = trangChu;
    }

    @NonNull
    @Override
    public SinhvienAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sv_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SinhvienAdapter.ViewHolder holder, int position) {
        Sinhvien sv = list.get(position);

        holder.tvten.setText(sv.getName());
        holder.tvtuoi.setText(String.valueOf(sv.getTuoi()));
        holder.tvmssv.setText(sv.getMssv());
        Glide.with(context)
                .load(sv.getImage())
                .thumbnail(Glide.with(context).load(R.drawable.img_10))
                .into(holder.image);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trangChu.xoa(sv.get_id());
            }
        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trangChu.add(context, 1, sv);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvten, tvtuoi, tvmssv;
        ImageButton btnDelete, btnUpdate;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgAvatatr);
            tvten = itemView.findViewById(R.id.tvName);
            tvtuoi = itemView.findViewById(R.id.tvTuoi);
            tvmssv = itemView.findViewById(R.id.tvMssv);
            btnDelete = itemView.findViewById(R.id.btnDeleteSv);
            btnUpdate = itemView.findViewById(R.id.btn_EditSv);
        }
    }
}