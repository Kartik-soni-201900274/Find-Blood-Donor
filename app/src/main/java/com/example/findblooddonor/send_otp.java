package com.example.findblooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class send_otp extends AppCompatActivity {
EditText number;
Button send_btn;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String phone;
    ProgressBar circularProgressBar;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        mAuth = FirebaseAuth.getInstance();
       setviewstoid();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if((mAuth.getCurrentUser())!=null)
        {
            Intent i=new Intent(send_otp.this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    public void setviewstoid()
    {
        circularProgressBar=findViewById(R.id.sendotp_circularProgressBar);
        number=findViewById(R.id.sendotpPhone);
        send_btn=findViewById(R.id.otp_Send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_btn.setVisibility(View.INVISIBLE);
                circularProgressBar.setVisibility(View.VISIBLE);
                phone=number.getText().toString().trim();
                if(verifyphone(phone))
                {
                    firebase_sendotp(phone);
                }


            }
        });


    }

    public boolean verifyphone(String phone)
    {
        if( phone.length()<10)
        {
            send_btn.setVisibility(View.VISIBLE);
            circularProgressBar.setVisibility(View.INVISIBLE);
            Toasty.error(this,"Invalid Number").show();
            return false;
        }
        else if(phone.length()==0)
        {
            send_btn.setVisibility(View.VISIBLE);
            circularProgressBar.setVisibility(View.INVISIBLE);
            Toasty.error(this,"Fields Can't Be Empty").show();
            return false;
        }
        else
        {
            return true;

        }
    }
    public void firebase_sendotp(String number)
    {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            send_btn.setVisibility(View.VISIBLE);
            circularProgressBar.setVisibility(View.INVISIBLE);
                Toasty.error(send_otp.this,e.getLocalizedMessage()).show();


        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId=verificationId;
            Intent i=new Intent(send_otp.this,Verify_otp.class);
            i.putExtra("phone",phone);
            i.putExtra("verificationId",verificationId);
            startActivity(i);
        }
    };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+977"+phone)       // Phone number to verify

                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(send_otp.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }


}