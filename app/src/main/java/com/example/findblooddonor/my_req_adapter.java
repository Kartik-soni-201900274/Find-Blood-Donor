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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class my_req_adapter extends FirebaseRecyclerAdapter<Request_Model, my_req_adapter.My_req_single_row_holder> {
    Context context;
    int NOPOSITION=-1;

     My_req_adapter_ClickInterface clickInterface;

    public my_req_adapter(@NonNull @NotNull FirebaseRecyclerOptions<Request_Model> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull My_req_single_row_holder holder, int position, @NonNull @NotNull Request_Model model) {
        holder.MR_postdate.setText(model.post_date);
        holder.MR_bloodgroup.setText(model.blood_group);
        holder.MR_patients_name.setText(model.patient_name);
        holder.MR_required_date.setText(model.required_date);
        holder.MR_required_unit.setText(model.required_unit);
        holder.MR_hospital.setText(model.hospital_name);
    }

    @NonNull
    @NotNull
    @Override
    public My_req_single_row_holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_req_single_row,parent,false);
        return new My_req_single_row_holder(v);
    }

    class My_req_single_row_holder extends RecyclerView.ViewHolder {
        TextView MR_bloodgroup,MR_postdate,MR_patients_name,MR_required_date,MR_required_unit,MR_hospital,MR_contact_person;
        CardView MR_cardView;
        AppCompatImageButton info_btn,edit_btn,delete_btn;

        public My_req_single_row_holder(View itemView) {
            super(itemView);
            MR_bloodgroup=itemView.findViewById(R.id.MR_single_row_blood_group);
            MR_postdate = itemView.findViewById(R.id.MR_single_row_postdate);
            MR_patients_name = itemView.findViewById(R.id.MR_single_row_patients_name);
            MR_required_date = itemView.findViewById(R.id.MR_single_row_req_date);
            MR_required_unit = itemView.findViewById(R.id.MR_single_row_unit);
            MR_hospital= itemView.findViewById(R.id.MR_single_row_hospital);
            MR_cardView= itemView.findViewById(R.id.MR_singlerow_cardview);
            info_btn= itemView.findViewById(R.id.MR_info_btn);
            edit_btn= itemView.findViewById(R.id.MR_edit_btn);
            delete_btn= itemView.findViewById(R.id.MR_delete_btn);

            MR_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getBindingAdapterPosition();
                    if(position!=NOPOSITION)
                    {
                        clickInterface.show_dialog(getSnapshots().getSnapshot(position));
                    }

                }
            });

            info_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getBindingAdapterPosition();
                    if(position!=NOPOSITION)
                    {
                        clickInterface.show_dialog(getSnapshots().getSnapshot(position));
                    }
                }
            });

            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getBindingAdapterPosition();
                    if(position!=NOPOSITION)
                    {
                        DatabaseReference ref= FirebaseDatabase.getInstance("https://find-blood-donor-6464c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Requests").child(getRef(position).getKey());

                        clickInterface.edit(getSnapshots().getSnapshot(position),ref);
                    }
                }
            });

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getBindingAdapterPosition();
                    if(position!=NOPOSITION)
                    {

                       DatabaseReference ref= FirebaseDatabase.getInstance("https://find-blood-donor-6464c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Requests").child(getRef(position).getKey());

                        clickInterface.delete(position,ref);
                    }

                }
            });

        }
    }
    public interface My_req_adapter_ClickInterface {
        void show_dialog(DataSnapshot dataSnapshot);
        void edit(DataSnapshot dataSnapshot,DatabaseReference ref);
        void delete(int pos,DatabaseReference ref);


    }
    public void setClickInterface(My_req_adapter_ClickInterface myReqAdapterClickInterface )
    {
        this.clickInterface=myReqAdapterClickInterface;
    }
}

