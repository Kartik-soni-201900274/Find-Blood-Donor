package com.example.findblooddonor;



import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findblooddonor.InfoDialog;
import com.example.findblooddonor.R;
import com.example.findblooddonor.WrapContentLinearLayoutManager;
import com.example.findblooddonor.view_req_adapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class MyReq extends Fragment implements my_req_adapter.My_req_adapter_ClickInterface {
    AppCompatImageButton info_btn,delete_btn,edit_btn;
    InfoDialog infoDialog;
    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerView;
    my_req_adapter myReqAdapter;
    String village,user_phone;
    int flag = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        user_phone=user.getPhoneNumber();
        View v = inflater.inflate(R.layout.fragment_my_req, container, false);
        setviewstoid(v);
        firebaserecycler();
        return v;
    }


    public void setviewstoid(View v) {
        recyclerView = v.findViewById(R.id.My_req_recycler);
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        wrapContentLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(wrapContentLinearLayoutManager);
    }

    @Override
    public void show_dialog(DataSnapshot dataSnapshot) {
        infoDialog = new InfoDialog(dataSnapshot);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        infoDialog.show(fm, "info_dialog");
    }

    public void firebaserecycler() {
        Query query;
        query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Requests").orderByChild("user_phone").equalTo(user_phone);

        FirebaseRecyclerOptions<Request_Model> options =
                new FirebaseRecyclerOptions.Builder<Request_Model>()
                        .setQuery(query, Request_Model.class)
                        .build();
        myReqAdapter = new my_req_adapter(options);
        recyclerView.setAdapter(myReqAdapter);
        myReqAdapter.setClickInterface(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        myReqAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myReqAdapter.stopListening();
    }

    @Override
    public void edit(DataSnapshot dataSnapshot,DatabaseReference ref) {
        edit_req bottomsheet =new edit_req(dataSnapshot,ref);
        bottomsheet.show(getActivity().getSupportFragmentManager(),"bottom sheet");
    }

    @Override
    public void delete(int pos,DatabaseReference ref) {
        DeleteDialog(pos,ref);


    }

    private void DeleteDialog(int pos, DatabaseReference ref) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this request?")
                .setTitle("Delete Request")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        ref.removeValue();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}