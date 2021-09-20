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

import org.jetbrains.annotations.NotNull;

public class view_req_adapter extends FirebaseRecyclerAdapter<Request_Model, view_req_adapter.single_row_holder> {
    Context context;
int NOPOSITION=-1;
    view_req_adapter_ClickInterface clickInterface;

    public view_req_adapter(@NonNull @NotNull FirebaseRecyclerOptions<Request_Model> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull single_row_holder holder, int position, @NonNull @NotNull Request_Model model) {
    holder.VR_postdate.setText(model.post_date);
        holder.VR_bloodgroup.setText(model.blood_group);
        holder.VR_patients_name.setText(model.patient_name);
        holder.VR_required_date.setText(model.required_date);

//        holder.VR_required_unit.setText(model.);
       holder.VR_required_unit.setText(model.required_unit);
        holder.VR_hospital.setText(model.hospital_name);
    }

    @NonNull
    @NotNull
    @Override
    public single_row_holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_req_single_row,parent,false);
        return new single_row_holder(v);
    }

    class single_row_holder extends RecyclerView.ViewHolder {
        TextView VR_bloodgroup, VR_postdate, VR_patients_name,VR_required_date,VR_required_unit,VR_hospital,VR_contact_person;
        CardView VR_cardView;
       AppCompatImageButton info_btn;
       int position=getAdapterPosition();
        public single_row_holder(View itemView) {
            super(itemView);
            VR_bloodgroup=itemView.findViewById(R.id.VR_single_row_blood_group);
            VR_postdate = itemView.findViewById(R.id.VR_single_row_postdate);
            VR_patients_name = itemView.findViewById(R.id.VR_single_row_patients_name);
            VR_required_date = itemView.findViewById(R.id.VR_single_row_req_date);
            VR_required_unit = itemView.findViewById(R.id.VR_single_row_unit);
            VR_hospital= itemView.findViewById(R.id.VR_single_row_hospital);
            VR_cardView= itemView.findViewById(R.id.VR_singlerow_cardview);
            info_btn= itemView.findViewById(R.id.VR_info_btn);

            VR_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=NOPOSITION)
                    {
                        clickInterface.show_dialog(getSnapshots().getSnapshot(position));
                    }

                }
            });
            info_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=NOPOSITION)
                    {
                        clickInterface.show_dialog(getSnapshots().getSnapshot(position));
                    }
                }
            });

        }
    }

    public interface view_req_adapter_ClickInterface {
        void show_dialog(DataSnapshot dataSnapshot);


    }
public void setClickInterface(view_req_adapter_ClickInterface viewReqAdapterClickInterface)
{
    this.clickInterface=viewReqAdapterClickInterface;
}
}
