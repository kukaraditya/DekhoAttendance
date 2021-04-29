package com.example.dekhoattendance.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.dekhoattendance.HomeActivity;
import com.example.dekhoattendance.R;
import com.example.dekhoattendance.UserRegistration;
import com.google.android.material.textfield.TextInputLayout;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LogIn extends AppCompatActivity {
    TextInputLayout textInputLayout;
    CircularProgressButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_log_in);
        login=(CircularProgressButton)findViewById(R.id.cirLoginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogIn.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, UserRegistration.class));
        overridePendingTransition(R.menu.slide_in_right, R.menu.stay);
    }
}