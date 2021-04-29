package com.example.dekhoattendance.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dekhoattendance.Adepter.LeaveTypeAdepter;
import com.example.dekhoattendance.MainActivity;
import com.example.dekhoattendance.Model.LeavetypeModel;
import com.example.dekhoattendance.R;
import com.example.dekhoattendance.common.Api;
import com.example.dekhoattendance.common.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MarkAttendance extends Fragment {

    final Calendar myCalendar = Calendar.getInstance();
    EditText choosedate,employeeid;
    ProgressDialog pDialog;
    TextView emp_name;
    Button checkin_btn,checkout_btn;

    int is_alreadyattend_in = 0;
    int is_alreadyattend_out = 0;

    int markattend_status = 0;
    public static Handler emp_data;
SharedPreference sharedPreference;
    String employ_name = "";
    String employ_id = "";
    String status;
    String checkin_address="Jaipur elecronic market,Jaipur";
    String checkout_address="Jaipur elecronic market,Jaipur";
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_markattendance, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        choosedate  = view.findViewById(R.id.choosedate);
        employeeid = view.findViewById(R.id.employeeid);
        emp_name = view.findViewById(R.id.emp_name);
        checkin_btn = view.findViewById(R.id.checkin_btn);
        checkout_btn = view.findViewById(R.id.checkout_btn);
        sharedPreference = new SharedPreference();
        sharedPreference.getCompanyID(getActivity());
        emp_name.setText("");
        employeeid.addTextChangedListener(watcher);

        emp_data = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                emp_name.setText(employ_name);
                if (is_alreadyattend_in==0)
                {
                    checkin_btn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.markattend_unselected));
                }
                if (is_alreadyattend_in==1)
                {
                    checkin_btn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.marked_green));
                  //  checkin_btn.setBackground(getDrawable(R.drawable.marked_green));
                    //checkin_btn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.marked_green));
                }
                if (is_alreadyattend_out==0)
                {
                    checkout_btn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.markattend_unselected));
                }
                if (is_alreadyattend_out==1)
                {
                    checkout_btn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.marked_green));
                }
            }
        };

        updateLabel();
        checkin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="check_in";
                check_in(status,checkin_address,sharedPreference.getCompanyID(getActivity()),employ_id);
                checkin_btn.setClickable(false);
                checkin_btn.setBackgroundColor(Color.parseColor("#808080"));
            }
        });

        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="check_out";
                check_out(status,checkout_address,sharedPreference.getCompanyID(getActivity()),employ_id);
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        choosedate.setText(sdf.format(myCalendar.getTime()));
        emp_data.sendEmptyMessage(1);
    }

    void check_in(String status , String address , String last_updated, String id) {

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        MediaType mediaType = MediaType.parse("text/plain");
                        Request authorisedRequest = chain.request().newBuilder()
                                .addHeader("Access-Control-Request-Headers", "123456").build();
                        return chain.proceed(authorisedRequest);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.applink).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).client(defaultHttpClient).build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.mark_attendancec(status,address,last_updated,id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("response", response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject json = new JSONObject(response.body());

                            if (json.getString("success").equals("1")) {
                                Toast.makeText(getActivity(), "You are In", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.dismiss();
                Log.e("failer", t.getMessage());
            }
        });
    }


    void check_out(String status , String address , String last_updated, String id) {

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        MediaType mediaType = MediaType.parse("text/plain");
                        Request authorisedRequest = chain.request().newBuilder()
                                .addHeader("Access-Control-Request-Headers", "123456").build();
                        return chain.proceed(authorisedRequest);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.applink).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).client(defaultHttpClient).build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.mark_attendancec(status,address,last_updated,id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("response", response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject json = new JSONObject(response.body());

                            if (json.getString("success").equals("1")) {
                                Toast.makeText(getActivity(), "You are Out", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.dismiss();
                Log.e("failer", t.getMessage());
            }
        });
    }


    private void employee_ID(String emptext , String date) {
        Log.e("emptext",emptext);
        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        MediaType mediaType = MediaType.parse("text/plain");
                        Request authorisedRequest = chain.request().newBuilder()
                                .addHeader("Access-Control-Request-Headers", "123456") .build();
                        return chain.proceed(authorisedRequest);
                    }}).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.applink).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).client(defaultHttpClient).build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.employee_detail(emptext,date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("response",response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            //JSONObject json = new JSONObject(response.body());
                            //Log.e("json",json+"");
                            JSONObject json = new JSONObject(response.body().toString());
                            if (json.getInt("success")==1)
                            {
                                JSONObject jsonObject = json.getJSONObject("employee");
                                employ_name = jsonObject.getString("full_name");
                                employ_id = jsonObject.getString("id");
                               if (json.getInt("status")==0)
                               {

                               }
                               if (json.getInt("status")==1)
                               {
                                   JSONObject attend_data = json.getJSONObject("data");
                                   if (attend_data.getString("office_start_time")!=null)
                                   {
                                       is_alreadyattend_in = 1;
                                   }else
                                   {
                                       is_alreadyattend_in = 0;
                                   }


                                   if (attend_data.getString("office_end_time")!=null)
                                   {
                                       is_alreadyattend_out  = 1;
                                   }else
                                   {
                                       is_alreadyattend_out = 0;
                                   }

                               }
                                //status
                            }else if(json.getInt("success")==0)
                            {
                                employeeid.setError(json.getString("msg"));
                                employ_name = "";
                                is_alreadyattend_in = 0;
                                is_alreadyattend_out = 0;
                            }else
                            {
                                Toast.makeText(getActivity(), json.getString("error").toString(), Toast.LENGTH_SHORT).show();
                                employ_name = "";
                                is_alreadyattend_in = 0;
                                is_alreadyattend_out = 0;
                            }

                            emp_data.sendEmptyMessage(1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e("failer", t.getMessage());
            }
        });
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //YOUR CODE
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //YOUR CODE
        }

        @Override
        public void afterTextChanged(Editable s) {
            String outputedText = s.toString();
            if (s.length()>2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String date1 = format.format(Date.parse(choosedate.getText().toString()));
                employee_ID(outputedText, date1);
            }
            //employeeid.setText(outputedText);

        }
    };
}
