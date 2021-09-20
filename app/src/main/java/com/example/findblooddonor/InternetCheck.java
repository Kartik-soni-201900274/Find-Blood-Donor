package com.example.findblooddonor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import es.dmoral.toasty.Toasty;

public class InternetCheck extends AppCompatActivity {
    Button tryagain;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet);

         tryagain=findViewById(R.id.try_again);
         tryagain.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 checkInternetAvailibility();
             }
         });



    }

    public void nointernetDialog()
    {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_wifi_off_24)
                .setTitle("Internet Connection Alert")
                .setMessage("Please Check Your Internet Connection")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public void checkInternetAvailibility()
    {

        if(isInternetAvailable())
        {
            new IsInternetActive().execute();
        }
        else {
            nointernetDialog();         }
    }

    public boolean isInternetAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {

            Log.e("isInternetAvailable:",e.toString());
            return false;
        }
    }

    class IsInternetActive extends AsyncTask<Void, Void, String>
    {
        ProgressDialog pDialog;

        InputStream is = null;
        String json = "Fail";



        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    json= "Success";
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
            pDialog.dismiss();
            if (result != null)
            {

                if(result.equals("Fail"))
                {
                    nointernetDialog();
                }
                else
                {
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            else
            {
                nointernetDialog();
            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Checking Internet...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }
    }
}

