package com.example.rentalkamera;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapterbarang extends RecyclerView.Adapter<Adapterbarang.UserViewHolder> {

    private ArrayList<ModelMasterbarang> dataListku;
    View viewkuu;

    public Adapterbarang(ArrayList<ModelMasterbarang> dataListku) {
        this.dataListku = dataListku;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        viewkuu = layoutInflater.inflate(R.layout.adapterbarang, parent, false);
        return new UserViewHolder(viewkuu);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        holder.txtmerkbarang.setText(dataListku.get(position).getMerkkamera());
        holder.txtwarnabarang.setText(dataListku.get(position).getWarnakamera());
        holder.txthargasewa.setText(dataListku.get(position).getHargasewa());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(holder.itemView.getContext(), EditbarangActivity.class);
                in.putExtra("id", dataListku.get(position).getId());
                in.putExtra("kodekamera", dataListku.get(position).getKodekamera());
                in.putExtra("merkkamera", dataListku.get(position).getMerkkamera());
                in.putExtra("warnakamera", dataListku.get(position).getWarnakamera());
                in.putExtra("hargakamera", dataListku.get(position).getHargasewa());
                holder.itemView.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataListku.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView txtmerkbarang, txtwarnabarang, txthargasewa;
        CardView cardview;

        UserViewHolder(View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.cardbarangview);
            txtmerkbarang = itemView.findViewById(R.id.txtmerk);
            txtwarnabarang = itemView.findViewById(R.id.txtwarna);
            txthargasewa = itemView.findViewById(R.id.txthargasewa);
        }
    }

}