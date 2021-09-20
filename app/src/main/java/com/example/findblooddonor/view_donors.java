 package com.example.findblooddonor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import es.dmoral.toasty.Toasty;


public class view_donors extends Fragment implements FilterDialog.Filterdialolistener {


    FilterDialog filterdialog;
    Button filter_btn;
    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerView;
    view_donors_adpater viewDonorsAdpater;
    String village;
    String district, blood_group;
    FirebaseRecyclerOptions<Donor_Model> options;

    public view_donors(String district, String blood_group) {
        this.district = district;
        this.blood_group = blood_group;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     

        View v = inflater.inflate(R.layout.view_donors, container, false);
        setviewstoid(v);


        firebaserecycler(district, blood_group);


        return v;

    }

    public void setviewstoid(View v) {
        filter_btn = v.findViewById(R.id.view_donors_filter_btn);
        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_dialog();
            }
        });
        recyclerView = v.findViewById(R.id.view_donors_recycler);
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(wrapContentLinearLayoutManager);


    }


    public void show_dialog() {
        filterdialog = new FilterDialog(this);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        filterdialog.show(fm, "info_dialog");
    }


    public void firebaserecycler(String district, String blood_group) {
        Query query;
        if ( district.length() == 0 && blood_group.length() == 0 ) {


            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Donors_info").orderByChild("name");
        } else if ( district.length() != 0 && blood_group.length() == 0 ) {

            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Donors_info").orderByChild("district").equalTo(district);
        } else if (district.length() == 0 && blood_group.length() != 0 ) {
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Donors_info").orderByChild("blood_group").equalTo(blood_group);
        } else {

            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Donors_info").orderByChild("district_bloodgroup").equalTo(district+blood_group);
        }




       options =
                new FirebaseRecyclerOptions.Builder<Donor_Model>()
                        .setQuery(query, Donor_Model.class)
                        .build();
        viewDonorsAdpater = new view_donors_adpater(options);
        recyclerView.setAdapter(viewDonorsAdpater);

    }

    @Override
    public void onStart() {
        super.onStart();
        viewDonorsAdpater.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewDonorsAdpater.stopListening();
    }

    @Override
    public void donbuttonclick(String district, String blood_group) {
        Query query;
        if ( district.length() == 0 && blood_group.length() == 0 ) {

            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Donors_info").orderByChild("name");
        } else if ( district.length() != 0 && blood_group.length() == 0 ) {
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Donors_info").orderByChild("district").equalTo(district);
        } else if (district.length() == 0 && blood_group.length() != 0 ) {
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Donors_info").orderByChild("blood_group").equalTo(blood_group);
        } else {

            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Donors_info").orderByChild("district_bloodgroup").equalTo(district+blood_group);
        }
        options =
                new FirebaseRecyclerOptions.Builder<Donor_Model>()
                        .setQuery(query, Donor_Model.class)
                        .build();
        viewDonorsAdpater.updateOptions(options);


    }


}
