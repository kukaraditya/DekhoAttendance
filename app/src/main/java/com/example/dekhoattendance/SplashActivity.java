package com.example.dekhoattendance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dekhoattendance.Model.LogIn;
import com.example.dekhoattendance.common.SharedPreference;


public class SplashActivity extends AppCompatActivity {
    private static String TAG = com.example.dekhoattendance.SplashActivity.class.getSimpleName();
    SharedPreference sharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        sharedPreference =  new SharedPreference();
        try {
           if (sharedPreference.getCompanyID(SplashActivity.this)!=null)
           {
               startSplash(new Intent(SplashActivity.this, LogIn.class));

           }else {
               startSplash(new Intent(SplashActivity.this, LogIn.class));

           }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void startSplash(final Intent intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } catch (Exception e) {

        }
    }

}