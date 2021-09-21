package com.example.findblooddonor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Blood_Bank_adapter extends FirebaseRecyclerAdapter<Blood_Bank_model, Blood_Bank_adapter.Blood_bank_single_row_holder> {


    public Blood_Bank_adapter(@NonNull @NotNull FirebaseRecyclerOptions<Blood_Bank_model> options) {
        super(options);

    }


    @Override
    protected void onBindViewHolder(@NonNull @NotNull Blood_bank_single_row_holder holder, int position, @NonNull @NotNull Blood_Bank_model model) {
        holder.BB_name.setText(model.name);
        holder.BB_district.setText(model.district);
        holder.BB_phone.setText(model.phone);

    }

    @NonNull
    @NotNull
    @Override
    public Blood_bank_single_row_holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_bank_singlerow, parent, false);
        return new Blood_bank_single_row_holder(v);
    }





    class Blood_bank_single_row_holder extends RecyclerView.ViewHolder {
        TextView BB_name, BB_district, BB_phone;
        CardView VR_cardView;


        public Blood_bank_single_row_holder(View itemView) {
            super(itemView);
            BB_name = itemView.findViewById(R.id.BB_single_row_name);
            BB_district = itemView.findViewById(R.id.BB_single_row_district);
            BB_phone = itemView.findViewById(R.id.BB_single_row_phone);
            VR_cardView = itemView.findViewById(R.id.BB_singlerow_cardview);
        }
    }
}
