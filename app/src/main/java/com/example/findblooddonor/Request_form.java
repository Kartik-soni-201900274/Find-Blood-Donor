package com.example.findblooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class  Request_form extends Fragment {
    TextInputEditText Request_Contact_person_name, Request_Patients_name, Request_Required_unit, Request_village, Request_contact, Request_Hospital_name, Request_Required_date, Request_Additional_info;
    MaterialAutoCompleteTextView Request_select_district, Request_select_blood_group;
    Button Request_submit_btn,clear_btn;
    String contact_person_name, patient_name, district,
            blood_group, village, hospital_name,required_unit,
            required_date, contact, additional_info,curr_date_time,formatted_req_date;
    String user_phone;
    String Districts[]={"achham", "arghakhanchi", "baglung", "baitadi", "bajhang", "bajura", "banke", "bara", "bardiya", "bhaktapur", "bhojpur", "chitwan", "dadeldhura", "dailekh", "dang deukhuri", "darchula", "dhading", "dhankuta", "dhanusa", "dholkha", "dolpa", "doti", "gorkha", "gulmi", "humla","ilam", "jajarkot", "jhapa", "jumla", "kailali", "kalikot", "kanchanpur", "kapilvastu", "kaski", "kathmandu", "kavrepalanchok", "khotang", "lalitpur", "lamjung","mahottari","makwanpur", "manang", "morang", "mugu", "mustang", "myagdi", "nawalparasi", "nuwakot", "okhaldhunga", "palpa", "panchthar", "parbat", "parsa", "pyuthan", "ramechhap", "rasuwa", "rautahat", "rolpa", "rukum", "rupandehi", "salyan", "sankhuwasabha", "saptari", "sarlahi", "sindhuli", "sindhupalchok", "siraha", "solukhumbu", "sunsari", "surkhet", "syangja", "tanahu", "taplejung", "terhathum", "udayapur"};
    String Blood_Group[]={"A+","A-","B+","B-","O+","O-","AB+","AB-"};
int flag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        user_phone=user.getPhoneNumber();
        View v = inflater.inflate(R.layout.activity_request_from, container, false);
        setviewstoid(v);
        return v;

    }
    public void clearfields()
    {
        Request_Contact_person_name.setText("");
        Request_Patients_name.setText("");
        Request_village.setText("");
        Request_contact.setText("");
        Request_Hospital_name.setText("");
        Request_Required_date.setText("");
        Request_select_district.setText("");
        Request_Additional_info.setText("");
        Request_select_blood_group.setText("");
        Request_Required_unit.setText("");
    }
    public void setviewstoid(View v) {

        Request_Contact_person_name = v.findViewById(R.id.RF_contact_person_name);
        Request_Patients_name = v.findViewById(R.id.RF_Patients_name);
        Request_village = v.findViewById(R.id.RF_Village);
        Request_contact = v.findViewById(R.id.RF_Contact_Number);
        Request_Hospital_name = v.findViewById(R.id.RF_Hospital_Name);
        Request_Required_date = v.findViewById(R.id.RF_Required_Date);
        Request_select_district = v.findViewById(R.id.RF_Select_district);
        Request_Additional_info = v.findViewById(R.id.RF_Additional_Info);
        Request_select_blood_group = v.findViewById(R.id.RF_Select_Blood_type);
        Request_Required_unit=v.findViewById(R.id.RF_Required_unit);
        Request_submit_btn = v.findViewById(R.id.RF_submit_Button);
        clear_btn=v.findViewById(R.id.RF_clear_btn);
        Request_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateinput();
            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearfields();
            }
        });
        ArrayAdapter<String> district_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Districts);
        ArrayAdapter<String> blood_group_adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,Blood_Group);

        Request_select_district.setThreshold(1);
        Request_select_district.setAdapter(district_adapter);
        Request_select_blood_group.setThreshold(1);
        Request_select_blood_group.setAdapter(blood_group_adapter);

       Request_Required_date.addTextChangedListener(new DateTextWatcher());

    }

    public void populatedata() {
        contact_person_name = Request_Contact_person_name.getText().toString().trim().toLowerCase();
        patient_name = Request_Patients_name.getText().toString().trim();
        district = Request_select_district.getText().toString().trim();
        blood_group = Request_select_blood_group.getText().toString().trim();
        village = Request_village.getText().toString().trim().toLowerCase();
        hospital_name = Request_Hospital_name.getText().toString().trim();
        required_date = (Request_Required_date.getText().toString().trim());
        required_unit=(Request_Required_unit.getText().toString().trim());
        if(isValid(required_date))
        {
            formatted_req_date=formatdate(required_date);
            flag=0;
        }
        else
        {
            flag=1;
        }
        contact = Request_contact.getText().toString().trim();
        additional_info = Request_Additional_info.getText().toString().trim();
    }

    public void add_data_to_firebase()
    {

        Request_Model requestModel=new Request_Model(contact_person_name, patient_name, district, blood_group, village, hospital_name, formatted_req_date, contact, additional_info,curr_date_time,required_unit,user_phone);
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-blood-donor-6464c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference root = db.getReference();
        root.child("Requests").push().setValue(requestModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toasty.success(getActivity(), "Request Successful!", Toast.LENGTH_SHORT, true).show();
                clearfields();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toasty.error(getActivity(), "Request Unsuccessful!", Toast.LENGTH_SHORT, true).show();
            }
        });



    }
    public void validateinput()
    {

        populatedata();
        gettodaysdate_time();
        if(contact_person_name.length()==0|| patient_name.length()==0|| district.length()==0|| blood_group.length()==0|| village.length()==0|| additional_info.length()==0|| hospital_name.length()==0|| required_date.length()==0|| contact.length()==0)

        {

            Toasty.error(getActivity(), "Fields can't be empty", Toast.LENGTH_SHORT, true).show();
        }
        else if(flag==1)
        {
            Request_Required_date.setError("Invalid Date Format");
            Toasty.error(getActivity(), "Invalid Date Format", Toast.LENGTH_SHORT, true).show();
        }
//        else if(!contact.matches()
//        {
//            Toasty.error(getActivity(), "Invalid Contact Number", Toast.LENGTH_SHORT, true).show();
//        }
        else if(!Arrays.asList(Blood_Group).contains(blood_group))
        {
            Request_select_blood_group.setError("Invalid Blood Group");
            Toasty.error(getActivity(), "Invalid Blood Group", Toast.LENGTH_SHORT, true).show();
        }
        else
        {
            add_data_to_firebase();
        }

    }

    public static boolean isValid(final String input) {

        boolean checkFormat;

        if (input.matches("^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$"))
            checkFormat=true;
        else
            checkFormat=false;
        return checkFormat;

    }
    public String formatdate(String fdate) {
        String datetime = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat d = new SimpleDateFormat("d MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;


    }
    public  void gettodaysdate_time() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DateFormat df =new SimpleDateFormat("d MMM yyyy 'at' hh:mm a");
        // new SimpleDateFormat("dd/MM/yyyy 'at' h:mm a");
        curr_date_time  = df.format(Calendar.getInstance().getTime());


    }


}
