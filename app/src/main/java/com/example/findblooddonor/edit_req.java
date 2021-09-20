package com.example.findblooddonor;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class edit_req extends DialogFragment {
    DataSnapshot snapshot;
    TextInputEditText Request_Contact_person_name, Request_Patients_name, Request_Required_unit, Request_village, Request_contact, Request_Hospital_name, Request_Required_date, Request_Additional_info;
    MaterialAutoCompleteTextView Request_select_district, Request_select_blood_group;
    Button Request_submit_btn, clear_btn;
    String contact_person_name, patient_name, district,
            blood_group, village, hospital_name, required_unit,
            required_date, contact, additional_info, curr_date_time, formatted_req_date;
    String user_phone;
    DatabaseReference ref;
    Request_Model model;
    String Districts[] = {"achham", "arghakhanchi", "baglung", "baitadi", "bajhang", "bajura", "banke", "bara", "bardiya", "bhaktapur", "bhojpur", "chitwan", "dadeldhura", "dailekh", "dang deukhuri", "darchula", "dhading", "dhankuta", "dhanusa", "dholkha", "dolpa", "doti", "gorkha", "gulmi", "humla", "ilam", "jajarkot", "jhapa", "jumla", "kailali", "kalikot", "kanchanpur", "kapilvastu", "kaski", "kathmandu", "kavrepalanchok", "khotang", "lalitpur", "lamjung", "mahottari", "makwanpur", "manang", "morang", "mugu", "mustang", "myagdi", "nawalparasi", "nuwakot", "okhaldhunga", "palpa", "panchthar", "parbat", "parsa", "pyuthan", "ramechhap", "rasuwa", "rautahat", "rolpa", "rukum", "rupandehi", "salyan", "sankhuwasabha", "saptari", "sarlahi", "sindhuli", "sindhupalchok", "siraha", "solukhumbu", "sunsari", "surkhet", "syangja", "tanahu", "taplejung", "terhathum", "udayapur"};
    String Blood_Group[] = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
    int flag = 0;

    public edit_req(DataSnapshot snapshot, DatabaseReference ref) {
        this.snapshot = snapshot;
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user_phone = user.getPhoneNumber();
        View v = inflater.inflate(R.layout.edit_my_request, container, false);
        setviewstoid(v);
        populateview();
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    public void populateview() {
        model = snapshot.getValue(Request_Model.class);
        Request_Contact_person_name.setText(model.contact_person_name);
        Request_Patients_name.setText(model.patient_name);
        Request_Required_unit.setText(model.required_unit);
        Request_village.setText(model.village);
        Request_contact.setText(model.contact);
        Request_Hospital_name.setText(model.hospital_name);

        Request_Additional_info.setText(model.additional_info);
        Request_select_district.setText(model.district);
        Request_select_blood_group.setText(model.blood_group);

    }


    public void cleaeditields() {
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

        Request_Contact_person_name = v.findViewById(R.id.edit_contact_person_name);
        Request_Patients_name = v.findViewById(R.id.edit_Patients_name);
        Request_village = v.findViewById(R.id.edit_Village);
        Request_contact = v.findViewById(R.id.edit_Contact_Number);
        Request_Hospital_name = v.findViewById(R.id.edit_Hospital_Name);
        Request_Required_date = v.findViewById(R.id.edit_Required_Date);
        Request_select_district = v.findViewById(R.id.edit_Select_district);
        Request_Additional_info = v.findViewById(R.id.edit_Additional_Info);
        Request_select_blood_group = v.findViewById(R.id.edit_Select_Blood_type);
        Request_Required_unit = v.findViewById(R.id.edit_Required_unit);
        Request_submit_btn = v.findViewById(R.id.edit_submit_Button);
        clear_btn = v.findViewById(R.id.edit_clear_btn);
        Request_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateinput();
            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleaeditields();
            }
        });
        ArrayAdapter<String> district_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Districts);
        ArrayAdapter<String> blood_group_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Blood_Group);

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
        required_unit = (Request_Required_unit.getText().toString().trim());
        if (isValid(required_date)) {
            formatted_req_date = formatdate(required_date);
            flag = 0;
        } else {
            flag = 1;
        }
        contact = Request_contact.getText().toString().trim();
        additional_info = Request_Additional_info.getText().toString().trim();
    }

    public void add_data_to_firebase() {

//        Request_Model requestModel=new Request_Model(contact_person_name, patient_name, district, blood_group, village, hospital_name, formatted_req_date, contact, additional_info,curr_date_time,required_unit,user_phone);

        Map<String, Object> map = new HashMap<>();
        map.put("contact_person_name", contact_person_name);
        map.put("patient_name", patient_name);
        map.put("district", district);
        map.put("blood_group", blood_group);
        map.put("village", village);
        map.put("hospital_name", hospital_name);
        map.put("required_date", formatted_req_date);
        map.put("contact", contact);
        map.put("additional_info", additional_info);
        map.put("post_date", curr_date_time);
        map.put("required_unit", required_unit);
        ref.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toasty.success(getActivity(), "Request Successful!", Toast.LENGTH_SHORT, true).show();
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toasty.error(getActivity(), "Request Unsuccessful!", Toast.LENGTH_SHORT, true).show();
            }
        });


    }

    public void validateinput() {

        populatedata();
        gettodaysdate_time();
        if (contact_person_name.length() == 0 || patient_name.length() == 0 || district.length() == 0 || blood_group.length() == 0 || village.length() == 0 || additional_info.length() == 0 || hospital_name.length() == 0 || required_date.length() == 0 || contact.length() == 0) {

            Toasty.error(getActivity(), "Fields can't be empty", Toast.LENGTH_SHORT, true).show();
        } else if (flag == 1) {
            Request_Required_date.setError("Invalid Date Format");
            Toasty.error(getActivity(), "Invalid Date Format", Toast.LENGTH_SHORT, true).show();
        }
//        else if(!contact.matches()
//        {
//            Toasty.error(getActivity(), "Invalid Contact Number", Toast.LENGTH_SHORT, true).show();
//        }
        else if (!Arrays.asList(Blood_Group).contains(blood_group)) {
            Request_select_blood_group.setError("Invalid Blood Group");
            Toasty.error(getActivity(), "Invalid Blood Group", Toast.LENGTH_SHORT, true).show();
        } else {
            add_data_to_firebase();
        }

    }

    public static boolean isValid(final String input) {

        boolean checkFormat;

        if (input.matches("^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$"))
            checkFormat = true;
        else
            checkFormat = false;
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

    public void gettodaysdate_time() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DateFormat df = new SimpleDateFormat("d MMM yyyy 'at' hh:mm a");
        // new SimpleDateFormat("dd/MM/yyyy 'at' h:mm a");
        curr_date_time = df.format(Calendar.getInstance().getTime());


    }


}
