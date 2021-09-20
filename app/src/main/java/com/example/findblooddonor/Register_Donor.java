package com.example.findblooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class Register_Donor extends Fragment {
    TextInputEditText Register_full_name, Register_village, Register_contact, Register_email, dob;
    MaterialAutoCompleteTextView Register_select_district, Register_select_gender, Register_select_blood_group;
    String name, contact, email, village, blood_group, district, birth_date, gender,formatted_req_date;
    Button submit_btn,clear_btn;
    String Districts[] = {"achham", "arghakhanchi", "baglung", "baitadi", "bajhang", "bajura", "banke", "bara", "bardiya", "bhaktapur", "bhojpur", "chitwan", "dadeldhura", "dailekh", "dang deukhuri", "darchula", "dhading", "dhankuta", "dhanusa", "dholkha", "dolpa", "doti", "gorkha", "gulmi", "humla", "ilam", "jajarkot", "jhapa", "jumla", "kailali", "kalikot", "kanchanpur", "kapilvastu", "kaski", "kathmandu", "kavrepalanchok", "khotang", "lalitpur", "lamjung", "mahottari", "makwanpur", "manang", "morang", "mugu", "mustang", "myagdi", "nawalparasi", "nuwakot", "okhaldhunga", "palpa", "panchthar", "parbat", "parsa", "pyuthan", "ramechhap", "rasuwa", "rautahat", "rolpa", "rukum", "rupandehi", "salyan", "sankhuwasabha", "saptari", "sarlahi", "sindhuli", "sindhupalchok", "siraha", "solukhumbu", "sunsari", "surkhet", "syangja", "tanahu", "taplejung", "terhathum", "udayapur"};
    String Blood_Group[] = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
    String gender_array[] = {"Male", "Female", "Other"};
    int flag=0;
    @Override
    public void onResume() {
        super.onResume();


    }

    public void clearfields()
    {
         Register_full_name.setText("");
         Register_village.setText("");
          Register_contact.setText("");
          Register_email.setText("");
          dob.setText("");
        Register_select_district.setText("");
        Register_select_gender.setText("");
        Register_select_blood_group.setText("");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_register_donor, container, false);

        setviewstoid(v);
        setAutocompleteViews_adapter();
        return v;
    }

    public void setviewstoid(View v) {

        Register_full_name = v.findViewById(R.id.Register_full_name);
        Register_village = v.findViewById(R.id.Register_Village);
        Register_contact = v.findViewById(R.id.Register_Contact_Number);
        Register_email = v.findViewById(R.id.Register_Email);
        dob = v.findViewById(R.id.Register_Dob);

        Register_select_district = v.findViewById(R.id.Register_Select_district);
        Register_select_gender = v.findViewById(R.id.Register_select_Gender);
        Register_select_blood_group = v.findViewById(R.id.Register_Select_Blood_type);
        submit_btn = v.findViewById(R.id.Register_submit_Button);
        clear_btn=v.findViewById(R.id.Register_clear_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
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
        dob.addTextChangedListener(new DateTextWatcher());
    }


    public void setAutocompleteViews_adapter() {
        ArrayAdapter<String> district_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Districts);
        ArrayAdapter<String> blood_group_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Blood_Group);
        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, gender_array);

        Register_select_district.setThreshold(1);
        Register_select_district.setAdapter(district_adapter);
        Register_select_gender.setThreshold(1);
        Register_select_gender.setAdapter(gender_adapter);
        Register_select_blood_group.setThreshold(1);
        Register_select_blood_group.setAdapter(blood_group_adapter);

    }

    public void populate_data() {
        name = Register_full_name.getText().toString().trim().toLowerCase();
        contact = Register_contact.getText().toString().trim();
        email = Register_email.getText().toString().trim();
        village = Register_village.getText().toString().trim().toLowerCase();
        blood_group = Register_select_blood_group.getText().toString().trim();
        district = Register_select_district.getText().toString().trim();
        birth_date = dob.getText().toString().trim();
        gender = Register_select_gender.getText().toString().trim();
        if(isValid(birth_date))
        {
            flag=0;
            formatted_req_date=formatdate(birth_date);
        }
        else
        {
            flag=1;
        }

    }
    public void add_data_to_firebase()
    {

        Donor_Model donorModel=new Donor_Model(name, contact, email, village, blood_group, district, birth_date, gender);
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-blood-donor-6464c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference root = db.getReference();
        root.child("Donors_info").push().setValue(donorModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toasty.success(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT, true).show();
                clearfields();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toasty.error(getActivity(), "Registration Unsuccessful!", Toast.LENGTH_SHORT, true).show();
            }
        });
        ;


    }

    public void validateinput()
    {

        populate_data();
        if(name.length()==0|| contact.length()==0|| email.length()==0|| village.length()==0|| blood_group.length()==0|| district.length()==0|| birth_date.length()==0|| gender.length()==0)
        {
            Toasty.error(getActivity(), "Fields can't be empty", Toast.LENGTH_SHORT, true).show();
        }
        else if(flag==1)
        {
            Toasty.error(getActivity(), "Invalid Date Format", Toast.LENGTH_SHORT, true).show();
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
}

