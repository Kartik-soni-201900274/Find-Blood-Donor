package com.example.findblooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.internal.operators.completable.CompletableOnErrorComplete;

public class MainActivity extends AppCompatActivity implements home.FragmentClickListener {
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    MaterialToolbar FBD_main_toolbar;
    FragmentTransaction ft;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    MaterialAutoCompleteTextView select_district, select_blood_group;
    Button find, register;
    Context context = this;
    boolean doubleBackToExitPressedOnce = false;
    final Fragment fragment_home = new home(this);
    final Fragment fragment_about_Dev = new About_dev();
    final Fragment fragment_new_req = new Request_form();
    final Fragment fragment_register_Donor = new Register_Donor();
    final Fragment fragment_view_req = new view_req();
    final Fragment fragment_my_req = new MyReq();
    Fragment fragment_all_donors_with_Arg;
    Fragment fragment_all_donors = new view_donors("", "");
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment_home;
    private FusedLocationProviderClient fusedLocationClient;


    String Districts[] = {"achham", "arghakhanchi", "baglung", "baitadi", "bajhang", "bajura", "banke", "bara", "bardiya", "bhaktapur", "bhojpur", "chitwan", "dadeldhura", "dailekh", "dang deukhuri", "darchula", "dhading", "dhankuta", "dhanusa", "dholkha", "dolpa", "doti", "gorkha", "gulmi", "humla", "ilam", "jajarkot", "jhapa", "jumla", "kailali", "kalikot", "kanchanpur", "kapilvastu", "kaski", "kathmandu", "kavrepalanchok", "khotang", "lalitpur", "lamjung", "mahottari", "makwanpur", "manang", "morang", "mugu", "mustang", "myagdi", "nawalparasi", "nuwakot", "okhaldhunga", "palpa", "panchthar", "parbat", "parsa", "pyuthan", "ramechhap", "rasuwa", "rautahat", "rolpa", "rukum", "rupandehi", "salyan", "sankhuwasabha", "saptari", "sarlahi", "sindhuli", "sindhupalchok", "siraha", "solukhumbu", "sunsari", "surkhet", "syangja", "tanahu", "taplejung", "terhathum", "udayapur"};
    String Blood_Group[] = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkInternetAvailibility(this);
        super.onCreate(savedInstanceState);
        checkgps();
        new AsyncinitFragment().execute();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setTheme(R.style.Theme_Dark_MyApplication_noAction);
        setContentView(R.layout.activity_main);
        setviewstoid();

        ft = getSupportFragmentManager().beginTransaction();

        managedrawerlayout();


    }

    public void checkInternetAvailibility(Context context) {

        if (isInternetAvailable()) {
            new IsInternetActiveinMain(context).execute();
        } else {
            Intent i = new Intent(getApplicationContext(), InternetCheck.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setviewstoid() {
        FBD_main_toolbar = findViewById(R.id.FBD_main_toolbar);
        bottomNavigationView = findViewById(R.id.FBD_main_bottomnavigation);
        drawerLayout = findViewById(R.id.FBD_main_drawerlayout);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav);
        navigationView = findViewById(R.id.FBD_main_navmenu);

    }

    public void managedrawerlayout() {
        setSupportActionBar(FBD_main_toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, FBD_main_toolbar, R.string.OpenNavMenu, R.string.CLoseNavMenu);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toasty.info(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void sethomefragment_viewstoid() {
        select_district = findViewById(R.id.home_select_district);
        select_blood_group = findViewById(R.id.home_select_blood_group);
        find = findViewById(R.id.home_find_Button);
        register = findViewById(R.id.home_bcmdonor_Button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().hide(active).show(fragment_register_Donor).commit();
                active = fragment_register_Donor;
//                Intent i=new Intent(getApplicationContext(),send_otp.class);
//                startActivity(i);
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String blood_group, district;
                blood_group = select_blood_group.getText().toString();
                district = select_district.getText().toString();

                if (blood_group.length() != 0 || district.length() != 0) {
                    fragment_all_donors_with_Arg = new view_donors(district, blood_group);
                    fm.beginTransaction().add(R.id.main_container, fragment_all_donors_with_Arg, "home").commit();
                    fm.beginTransaction().hide(active).show(fragment_all_donors_with_Arg).commit();
                    active = fragment_all_donors_with_Arg;
                } else if (blood_group.length() == 0 || blood_group == null && district.length() == 0 || district == null) {
                    Toasty.error(MainActivity.this, "Fields Cant't Be Empty").show();
                }

                select_blood_group.setText("");
                select_district.setText("");
            }
        });
        ArrayAdapter<String> district_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Districts);
        ArrayAdapter<String> blood_group_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Blood_Group);

        select_district.setThreshold(1);
        select_district.setAdapter(district_adapter);
        select_blood_group.setThreshold(1);
        select_blood_group.setAdapter(blood_group_adapter);
    }

    @Override
    public void attached() {
        sethomefragment_viewstoid();
    }

    class AsyncinitFragment extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setBottomNavigationView();
            setNavigationView();
            pDialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            initfragment();

            return null;
        }

        public void initfragment() {
            fm.beginTransaction().add(R.id.main_container, fragment_home, "home").commit();
            fm.beginTransaction().add(R.id.main_container, fragment_new_req, "req").hide(fragment_new_req).commit();
            fm.beginTransaction().add(R.id.main_container, fragment_about_Dev, "abt_Dev").hide(fragment_about_Dev).commit();
            fm.beginTransaction().add(R.id.main_container, fragment_register_Donor, "req").hide(fragment_register_Donor).commit();
            fm.beginTransaction().add(R.id.main_container, fragment_all_donors, "2").hide(fragment_all_donors).commit();
            fm.beginTransaction().add(R.id.main_container, fragment_my_req, "my_req").hide(fragment_my_req).commit();
            fm.beginTransaction().add(R.id.main_container, fragment_view_req, "vieq_req").hide(fragment_view_req).commit();

        }

        void setBottomNavigationView() {

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    if (item.getItemId() == R.id.home) {

                        fm.beginTransaction().hide(active).show(fragment_home).commit();
                        active = fragment_home;
                        return true;

                    } else if (item.getItemId() == R.id.view_req) {
                        fm.beginTransaction().hide(active).show(fragment_view_req).commit();
                        active = fragment_view_req;
                        return true;


                    } else {

                        fm.beginTransaction().hide(active).show(fragment_new_req).commit();
                        active = fragment_new_req;

                        return true;

                    }

                }
            });
        }


        void setNavigationView() {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    if (item.getItemId() == R.id.drawer_home) {
                        closeDrawer();
                        fm.beginTransaction().hide(active).show(fragment_home).commit();
                        active = fragment_home;
                        return true;

                    } else if (item.getItemId() == R.id.drawer_view_req) {
                        fm.beginTransaction().hide(active).show(fragment_view_req).commit();
                        closeDrawer();
                        return true;
                    } else if (item.getItemId() == R.id.drawer_new_req) {
                        closeDrawer();
                        fm.beginTransaction().hide(active).show(fragment_new_req).commit();
                        active = fragment_new_req;

                        return true;
                    } else if (item.getItemId() == R.id.drawer_register) {
                        fm.beginTransaction().hide(active).show(fragment_register_Donor).commit();
                        active = fragment_register_Donor;
                        closeDrawer();
                        return true;

                    } else if (item.getItemId() == R.id.drawer_all_donors) {
                        fm.beginTransaction().hide(active).show(fragment_all_donors).commit();
                        active = fragment_all_donors;
                        closeDrawer();
                        return true;

                    } else if (item.getItemId() == R.id.drawer_About_Dev) {
                        closeDrawer();
                        fm.beginTransaction().hide(active).show(fragment_about_Dev).commit();
                        active = fragment_about_Dev;

                    }
                    else if (item.getItemId() == R.id.drawer_my_req) {
                        closeDrawer();
                        fm.beginTransaction().hide(active).show(fragment_my_req).commit();
                        active = fragment_my_req;

                    }


                    return true;
                }
            });


        }

        public void closeDrawer() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }

    }


    public boolean isInternetAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {

            Log.e("isInternetAvailable:", e.toString());
            return false;
        }
    }


    class IsInternetActiveinMain extends AsyncTask<Void, Void, String> {
        InputStream is = null;
        String json = "Fail";
        ProgressDialog pDialog;
        Context context;

        public IsInternetActiveinMain(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    json = "Success";
                } else {
                    json = "Fail";
                }


            } catch (Exception e) {
                e.printStackTrace();
                json = "Fail";
            }
            return json;

        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {

                if (result.equals("Fail")) {


                    Intent i = new Intent(getApplicationContext(), InternetCheck.class);

                    startActivity(i);
                    finish();
                }

            } else {
                Intent i = new Intent(getApplicationContext(), InternetCheck.class);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }

    public void checkgps() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && readlastlocation().length()==0 ) {

            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));


        }

    }
    String readlastlocation() {
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("location", Context.MODE_PRIVATE);
        return sharedPref.getString("location", "");
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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


