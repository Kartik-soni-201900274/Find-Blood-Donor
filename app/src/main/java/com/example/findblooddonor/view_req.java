package com.example.findblooddonor;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class view_req extends Fragment implements view_req_adapter.view_req_adapter_ClickInterface {
    AppCompatImageButton info_btn;
    InfoDialog infoDialog;
    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerView;
    view_req_adapter viewReqAdapter;
    String village = null;
    private LocationManager mLocationManagerNetwork;
    private LocationListener mLocationListenerNetwork;
    //"\uf8ff"
    String sf_village;
    int flag = 0;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sf_village = readlastlocation();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        View v = inflater.inflate(R.layout.view_req, container, false);
        setviewstoid(v);
        if (checkgps()) {
            fetchLastLocation();
        } else {
            firebaserecycler(village);
            viewReqAdapter.startListening();
        }


        return v;

    }


    public void setviewstoid(View v) {

        recyclerView = v.findViewById(R.id.view_req_recycler);
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


    public void firebaserecycler(String village) {
        Query query;


        if (village != null && village.length() != 0) {
            Toasty.success(getActivity(),"Location Fetched Successfully").show();
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Requests").orderByChild("village").equalTo(village);
        } else if (sf_village != null && sf_village.length() != 0) {
            Toasty.error(getActivity(),"Location Couldn't Be Fetched").show();
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Requests").orderByChild("village").equalTo(sf_village);
        } else {
            Toasty.error(getActivity(),"Location Couldn't Be Fetched").show();

            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Requests");
        }


        FirebaseRecyclerOptions<Request_Model> options =
                new FirebaseRecyclerOptions.Builder<Request_Model>()
                        .setQuery(query, Request_Model.class)
                        .build();
        viewReqAdapter = new view_req_adapter(options);
        recyclerView.setAdapter(viewReqAdapter);
        viewReqAdapter.setClickInterface(this);

    }


    @Override
    public void onStop() {
        super.onStop();
        viewReqAdapter.stopListening();
    }


    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);


        } else {

            Toasty.info(getActivity(),"Fetching Location").show();
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                try {

                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                    village = addresses.get(0).getLocality().trim().toLowerCase();

                                    savelastlocation(village);

                                } catch (IOException e) {
                                    e.printStackTrace();

                                }

                            }

                            firebaserecycler(village);
                            viewReqAdapter.startListening();

                        }
                    });

        }

    }

    public boolean checkgps() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {

            return true;


        }
        Toasty.error(getActivity(),"Please Turn On Your Location For Better Results").show();
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 123: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toasty.error(getActivity(), "Location Permission Denied").show();
                    firebaserecycler(village);
                    viewReqAdapter.startListening();

                } else {

                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fetchLastLocation();

                    }
                }
            }
        }
    }


    void savelastlocation(String location) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("location", location);
        editor.apply();
    }

    public String readlastlocation() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        return sharedPref.getString("location", "");
    }


}

