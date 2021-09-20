package com.example.findblooddonor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public  class InfoDialog extends DialogFragment {
TextView contact,contact_person,patient_name,additional_info;
AppCompatImageButton share_btn,call_btn,msg_btn;
DataSnapshot snapshot;
    Request_Model model;
    public InfoDialog(DataSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment

        View v = inflater.inflate(R.layout.info_dialog_fragment, container, false);
    setviewstoid(v);
setdata();
        return v;

    }
    
    public void setviewstoid(View v)
    {
        additional_info=v.findViewById(R.id.info_dialog_additional_info);
        patient_name=v.findViewById(R.id.info_dialog_patient_name);
        contact_person=v.findViewById(R.id.info_dialog_contact_person);
        contact=v.findViewById(R.id.info_dialog_phone_number);

        share_btn=v.findViewById(R.id.info_dialog_share_btn);
        call_btn=v.findViewById(R.id.info_dialog_call_btn);
        msg_btn=v.findViewById(R.id.info_dialog_sms_btn);
        call_btn.setOnClickListener(this::checkpermission);
        msg_btn.setOnClickListener(this::composeSMSMessage);
        share_btn.setOnClickListener(this::composeTextMessage);
    }
    
    public void setdata()
    {
         model=snapshot.getValue(Request_Model.class);
        patient_name.setText(model.patient_name);
        additional_info.setText(model.additional_info);
        contact_person.setText(model.contact_person_name);
        contact.setText(model.contact);
    }
    
    
    
    
    
    public void checkpermission(View v)
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 100);
        }
        else
        {
            dialcontact();
        }
    }
    public void dialcontact()
    {
        Intent i=new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:"+contact.getText().toString()));
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                dialcontact();
            }
            else
            {
                Toast.makeText(getActivity(),"PERMISSION DENIED",Toast.LENGTH_LONG).show();
            }
        }
    }

        public void composeSMSMessage(View v) {

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond

            intent.putExtra("sms_body", preparemsg());
            intent.putExtra("subject","URGENT BLOOD NEEDED");
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }

    public void composeTextMessage(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, preparemsg());

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent.createChooser(intent,("Share using")));
        }
    }

public String preparemsg()
{
          String message= "\nBlood Type : "+model.blood_group+" \n"+
                  "\n"+"Contact Person's Name : "+model.contact_person_name+"\n"+
                  "\n"+"Patient's Name : "+model.patient_name+"\n"+
                  "\n"+"Hospital : "+model.hospital_name+"\n"+
                  "\n"+"Required Date : "+model.required_date+"\n"+
                  "\n"+"Required Unit : "+model.required_unit+"\n"+
                  "\n"+"Case Detail : "+model.additional_info+"\n";
                     return message;



}

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = 1070;
        params.height = 800;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }



}