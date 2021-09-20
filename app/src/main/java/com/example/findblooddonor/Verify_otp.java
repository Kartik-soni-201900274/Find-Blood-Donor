package com.example.findblooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class Verify_otp extends AppCompatActivity {
    String phone;
    TextView display_phone,resend_otp;
    PinView pinView;
    Button verify_btn;
    String pin;
    private String mVerificationId;
    ProgressBar circularProgressBar;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
int flag=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_verify_otp);
        Intent i=getIntent();
        phone="+977 "+i.getStringExtra("phone");
        mVerificationId=i.getStringExtra("verificationId");
        setviewstoid();
    }

    private void setviewstoid() {
        circularProgressBar=findViewById(R.id.verifyotp_circularProgressBar);
        display_phone=findViewById(R.id.verify_display_mobile);
        resend_otp=findViewById(R.id.Verify_ResendBtn);
        verify_btn =findViewById(R.id.btnVerify);
        display_phone.setText(phone);
        pinView=findViewById(R.id.firstPinView);
        circularProgressBar.setVisibility(View.INVISIBLE);
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                resend_otp.setTextColor(getResources().getColor(R.color.gray));
                resend_otp.setClickable(false);
                verify_btn.setVisibility(View.INVISIBLE);
                circularProgressBar.setVisibility(View.VISIBLE);
              firebase_sendotp();
            }
        });

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_btn.setVisibility(View.INVISIBLE);
                circularProgressBar.setVisibility(View.VISIBLE);
                pin=pinView.getText().toString().trim();
                checkpin(pin);
            }
        });

    }

    public void checkpin(String pin)
    {

        if(pin.length()<6)
        {
            verify_btn.setVisibility(View.VISIBLE);
            circularProgressBar.setVisibility(View.INVISIBLE);
            Toasty.error(this," Invalid OTP").show();
        }
        else if(mVerificationId!=null)
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, pin);
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Intent i=new Intent(Verify_otp.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();

                    }
                    else
                    {
                        verify_btn.setVisibility(View.VISIBLE);
                        circularProgressBar.setVisibility(View.INVISIBLE);
                        Toasty.error(Verify_otp.this,"Invalid OTP").show();
                    }
                }
            });
        }
    }



    public void firebase_sendotp()
    {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                flag=1;
                resend_otp.setTextColor(getResources().getColor(R.color.mycolor));
                resend_otp.setClickable(true);
                verify_btn.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.INVISIBLE);
                Toasty.error(Verify_otp.this,e.getLocalizedMessage()).show();


            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                flag=1;
                resend_otp.setTextColor(getResources().getColor(R.color.mycolor));
                resend_otp.setClickable(true);

                verify_btn.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.INVISIBLE);
                mVerificationId=verificationId;
                Toasty.success(Verify_otp.this,"OTP Sent Successfully").show();
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Verify_otp.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

}