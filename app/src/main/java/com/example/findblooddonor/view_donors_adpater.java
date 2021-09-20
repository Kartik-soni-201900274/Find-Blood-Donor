package com.example.findblooddonor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class view_donors_adpater extends FirebaseRecyclerAdapter<Donor_Model, view_donors_adpater.single_row_holder> {
        Context context;
        com.example.findblooddonor.view_req_adapter.view_req_adapter_ClickInterface clickInterface;

        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public view_donors_adpater(@NonNull @NotNull FirebaseRecyclerOptions<Donor_Model> options) {
            super(options);

        }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull single_row_holder holder, int position, @NonNull @NotNull Donor_Model model) {
        holder.VD_name.setText(model.name);
        holder.VD_bloodgroup.setText(model.blood_group);
        holder.VD_gender.setText(model.gender);
        holder.VD_address.setText(model.village+","+model.district);

        holder.VD_phone.setText(model.contact);

    }



        @NonNull
        @NotNull
        @Override
        public single_row_holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_donors_single_row,parent,false);
            return new single_row_holder(v);
        }

        class single_row_holder extends RecyclerView.ViewHolder {
            TextView VD_bloodgroup, VD_name,VD_gender,VD_address,VD_phone;
            CardView VD_cardView;



            public single_row_holder(View itemView) {
                super(itemView);
                VD_bloodgroup=itemView.findViewById(R.id.VD_single_row_blood_group);

                VD_name = itemView.findViewById(R.id.VD_single_row_name);
                VD_gender = itemView.findViewById(R.id.VD_single_row_gender);
                VD_address = itemView.findViewById(R.id.VD_single_row_city);
                VD_phone = itemView.findViewById(R.id.VD_single_row_phone);
                VD_cardView= itemView.findViewById(R.id.VD_singlerow_cardview);
            }
        }
        public interface view_donor_adapter_ClickInterface {
            void show_dialog();


        }

    }


