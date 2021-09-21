package com.example.findblooddonor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class Blood_bank extends Fragment {
    RecyclerView recyclerView;
    TextInputEditText search;
    Blood_Bank_adapter bloodBankAdapter;
    FirebaseRecyclerOptions<Blood_Bank_model> options;
    String name[]={"Bhaktapur Blood Bank, NRCS",
            "Central Blood Transfusion Center, NRCS",
            "TU Hospital Blood Bank",
            "Maternity & Woman Hospital Blood Bank",
            "Bir Hospital Blood Bank",
            "Grande International Hospital Blood Bank",
            "Himal Hospital Blood Bank",
            "Nepal Police Hospital Blood Bank",
            "Nobel Hospital Blood Bank",
            "Gangalal Memorial Heart Center",
            "Nepal Medicity Hospital Blood Bank",
            "Civil Service Hospital",
            "Lalitpur Blood Bank, NRCS",
            "Jhapa Blood Transfusion Service",
            "Sunsari Blood Transfusion Service, BPKHIS",
            "Morang Regional Blood Transfusion Service",
            "Dhanusa Blood Transfusion Service",
            "Parsa Blood Transfusion Service",
            "Kavre Blood Transfusion Service",
            "Dhulikhel Hospital Blood Bank",
            "Chitwan Regional Blood Transfusion Service",
            "Chitwan Medical College Blood Transfusion Unit",
            "BP Koirala Memorial Cancer Hospital Blood Bank",
            "Rupandehi Blood Transfusion Service",
            "Kaski Regional Blood Transfusion Service",
            "Dang Blood Transfusion Service",
    };
    String districts[]={"Dudhpati,Bhaktapur",
            "Soltimode, Kalimati",
            "Maharajgunj, Kathmandu",
            "Thapathali, Kathmandu",
            "New Road Gate, Kathmandu",
            "Tokaha, Kathmandu",
            "Gyaneshwar, Kathmandu",
            "Maharajgunj, Kathmandu",
            "Sinamangal, Kathmandu",
            "Bansbari, Kathmandu",
            "Bhainsepati, Lalitpur",
            "Minbhawan, Kathmandu",
            "Pulchowk, Lalitpur",
            "Devi Panchakanya, Birtamode",
            "Dharan",
            "Rangeli Road, Biratnagar",
            "Janakpur Provincial Hospital",
            "Narayani Hospital, Parsa",
            "Banepa, Kavrepalanchowk",
            "Dhulikhel Hospital, Dhulikhel",
            "Bharatpur,Chitwan",
            "CMS, Bharatpur",
            "Bharatpur-7",
            "Amarpath, Butuwal",
            "Pokhara",
            "Ghorahi, Dang"
    };
    String phone[]={"01-6620001",
            "01-4288485",
            "01-44123030",
            "01-4260405",
            "01-4221119",
            "01-5159266",
            "9862737316",
            "01-4412430",
           "01-4110842",
            "01-4371322",
            "01-4217766",
            "01-4107000",
            "01-5427033",
            "023-541833",
            "025-525555",
            "021-423326",
            "041-520870",
            "051-522504",
            "011-661431",
            "011-490497",
            "056-520880",
           "056-532937-249",
            "056-593359",
            "071-550462",
            "061-521091",
            "082-561460",
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_bloodbanks, container, false);
        setviewstoid(v);
        getfirebase_data();
        return v;
    }
    public void setviewstoid(View v)
    {

        recyclerView=v.findViewById(R.id.BB_recycler);
        search=v.findViewById(R.id.BB_search);
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(wrapContentLinearLayoutManager);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                process_search(charSequence.toString());
                Toasty.success(getActivity(),"textwatcher").show();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void process_search(String s)
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Blood Banks").orderByChild("district").startAt(s).endAt(s+"\uf8ff");
      options = new FirebaseRecyclerOptions.Builder<Blood_Bank_model>()
                        .setQuery(query, Blood_Bank_model.class)
                        .build();
        bloodBankAdapter=new Blood_Bank_adapter(options);
        recyclerView.setAdapter(bloodBankAdapter);
        bloodBankAdapter.startListening();
    }

    public void getfirebase_data()
    {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Blood Banks").orderByChild("name");
        options =
                new FirebaseRecyclerOptions.Builder<Blood_Bank_model>()
                        .setQuery(query, Blood_Bank_model.class)
                        .build();
        bloodBankAdapter=new Blood_Bank_adapter(options);
        recyclerView.setAdapter(bloodBankAdapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        bloodBankAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        bloodBankAdapter.stopListening();
    }
}
