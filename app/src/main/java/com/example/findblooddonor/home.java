package com.example.findblooddonor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class home  extends Fragment {
String[] language={"jhapa","ilam","panchthar","morang","sunsari"};
MaterialAutoCompleteTextView select_district,select_blood_group;
Button find,register;
    FragmentClickListener fragmentClickListener;
    Context context;
    MaterialAutoCompleteTextView blood_Auto;

    public home(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        return v;
    }

    public void setviewstoid(View v)
    {

        select_district=v.findViewById(R.id.home_select_district);
        select_blood_group=v.findViewById(R.id.home_select_blood_group);
        find=v.findViewById(R.id.home_find_Button);
        register=v.findViewById(R.id.home_bcmdonor_Button);

    }
    @Override
    public void onStart() {
        super.onStart();
        fragmentClickListener = (FragmentClickListener) context;
        fragmentClickListener.attached();

    }

    interface FragmentClickListener{
        void attached();
    }


}