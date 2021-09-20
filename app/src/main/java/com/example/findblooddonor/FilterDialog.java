package com.example.findblooddonor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FilterDialog extends DialogFragment {
    Button done_btn, cancel_btn;
    Context context;
    MaterialAutoCompleteTextView district, blood_group;
    Filterdialolistener filterdialolistener;
    String Districts[]={"achham", "arghakhanchi", "baglung", "baitadi", "bajhang", "bajura", "banke", "bara", "bardiya", "bhaktapur", "bhojpur", "chitwan", "dadeldhura", "dailekh", "dang deukhuri", "darchula", "dhading", "dhankuta", "dhanusa", "dholkha", "dolpa", "doti", "gorkha", "gulmi", "humla","ilam", "jajarkot", "jhapa", "jumla", "kailali", "kalikot", "kanchanpur", "kapilvastu", "kaski", "kathmandu", "kavrepalanchok", "khotang", "lalitpur", "lamjung","mahottari","makwanpur", "manang", "morang", "mugu", "mustang", "myagdi", "nawalparasi", "nuwakot", "okhaldhunga", "palpa", "panchthar", "parbat", "parsa", "pyuthan", "ramechhap", "rasuwa", "rautahat", "rolpa", "rukum", "rupandehi", "salyan", "sankhuwasabha", "saptari", "sarlahi", "sindhuli", "sindhupalchok", "siraha", "solukhumbu", "sunsari", "surkhet", "syangja", "tanahu", "taplejung", "terhathum", "udayapur"};
    String Blood_Group[]={"A+","A-","B+","B-","O+","O-","AB+","AB-"};
    public FilterDialog(Filterdialolistener filterdialolistener) {
        this.filterdialolistener = filterdialolistener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View v = inflater.inflate(R.layout.filter_all_donors, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        setviewstoid(v);
        return v;

    }

    public void setviewstoid(View v) {
        done_btn = v.findViewById(R.id.filter_done_Btn);
        cancel_btn = v.findViewById(R.id.filter_cancel_Btn);
        district = v.findViewById(R.id.filter_donors_district);

        blood_group = v.findViewById(R.id.filter_donors_blood_group);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filterdialolistener.donbuttonclick(district.getText().toString(), blood_group.getText().toString());
                getDialog().onBackPressed();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().onBackPressed();
            }
        });
        ArrayAdapter<String> district_adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,Districts);
        ArrayAdapter<String> blood_group_adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,Blood_Group);

        district.setThreshold(1);
        district.setAdapter(district_adapter);
        blood_group.setThreshold(1);
        blood_group.setAdapter(blood_group_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = 950;//950
        params.height = 950;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }



    interface Filterdialolistener {
        void donbuttonclick(String district, String blood_group);

    }
}
