package com.example.dekhoattendance.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

public class FirstFragment extends Fragment {
    EditText choosedate,clocin,clockout;
    final Calendar myCalendar = Calendar.getInstance();
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout layoutabsent;
    Spinner leavetypespinner;
    private ArrayList<LeavetypeModel> leavetypearray = new ArrayList<>();
    private SharedPreference sharedPreference;
    LeaveTypeAdepter adepter;
    EditText employeeid,reason;
    CheckBox late,halfday;
    Button markattendbtn;
    ProgressDialog pDialog;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        choosedate  = view.findViewById(R.id.choosedate);
        clocin  = view.findViewById(R.id.clocin);
        clockout  = view.findViewById(R.id.clockout);
        radioGroup = view.findViewById(R.id.radiogroup);
        layoutabsent = view.findViewById(R.id.layout_absent);
        leavetypespinner = view.findViewById(R.id.leavetypespinner);
        employeeid = view.findViewById(R.id.employeeid);
        markattendbtn = view.findViewById(R.id.markattendbtn);
        late = view.findViewById(R.id.late);
        halfday = view.findViewById(R.id.halfday);
        reason = view.findViewById(R.id.reason);
        sharedPreference = new SharedPreference();
        updateLabel();
        updateclockin();
        updateclockout();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        // Get Current time
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog checkinimpickerdialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hours,
                                          int mins) {
                        String timeSet = "";
                        if (hours > 12) {
                            hours -= 12;
                            timeSet = "PM";
                        } else if (hours == 0) {
                            hours += 12;
                            timeSet = "AM";
                        } else if (hours == 12)
                            timeSet = "PM";
                        else
                            timeSet = "AM";
                        String minutes = "";
                        if (mins < 10)
                            minutes = "0" + mins;
                        else
                            minutes = String.valueOf(mins);
                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hours).append(':')
                                .append(minutes).append(" ").append(timeSet).toString();
                        clocin.setText(aTime);
                    }
                }, hour, minute, false);

        TimePickerDialog checkoutimpickerdialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hours,
                                          int mins) {
                        String timeSet = "";
                        if (hours > 12) {
                            hours -= 12;
                            timeSet = "PM";
                        } else if (hours == 0) {
                            hours += 12;
                            timeSet = "AM";
                        } else if (hours == 12)
                            timeSet = "PM";
                        else
                            timeSet = "AM";
                                                                                                                                                                   String minutes = "";
                        if (mins < 10)
                            minutes = "0" + mins;
                        else
                            minutes = String.valueOf(mins);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hours).append(':')
                                .append(minutes).append(" ").append(timeSet).toString();
                        clockout.setText(aTime);

                    }
                }, hour, minute, false);

        clocin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkinimpickerdialog.show();
            }
        });
        clockout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutimpickerdialog.show();
            }
        });
       // checkinimpickerdialog.show();



        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.absentid)
                {
                    layoutabsent.setVisibility(View.VISIBLE);
                    updateLiveType();
                }
                if (checkedId==R.id.presentid)
                {
                    layoutabsent.setVisibility(View.GONE);
                }
            }
        });

        markattendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
String empid ="";
String date = "";
String status = "";
String leaveType = "";
String clocin_timev = "";
String clocout_timev = "";
String halfDayType = "";
String reasionn = "";
String clock_in = "";
String clock_out = "";
String is_late = "";

                empid = employeeid.getText().toString();
                date = choosedate.getText().toString();
                int radioid = radioGroup.getCheckedRadioButtonId();

                if (radioid==R.id.absentid)
                {
                    status = "absent";
                }else if (radioid ==R.id.presentid )
                {
                    status = "present";
                }

                if (leavetypearray.size()<=0)
                {
                    leaveType="";
                }else
                {
                    leaveType = leavetypearray.get(leavetypespinner.getSelectedItemPosition()).getLeaveType();
                }


                if (halfday.isChecked()==true)
                {
                    halfDayType = "yes";
                }else
                {
                    halfDayType = "no";
                }

                if (late.isChecked()==true)
                {
                    is_late = "1";
                }else
                {
                    is_late = "0";
                }

                reasionn = reason.getText().toString();
                clock_in = clocin.getText().toString();
                clock_out = clockout.getText().toString();

                if (empid.equals("")|| empid.equals(null))
                {
                    employeeid.setError("Please EnterEmployee ID");
                }else if (date.equals("")|| date.equals(null))
                {
                    Toast.makeText(getActivity(), "Please Enter Date", Toast.LENGTH_SHORT).show();
                }else {

                    HashMap<String,String> markattend = new HashMap<>();
                    markattend.put("employee_id",empid);

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String date1 = format.format(Date.parse(date));

                    markattend.put("date",date1);
                    markattend.put("status",status);
                    markattend.put("leaveType",leaveType);
                    markattend.put("halfDayType",halfDayType);
                    markattend.put("last_updated_by",sharedPreference.getCompanyID(getActivity()));
                    markattend.put("reason",reasionn);
                    markattend.put("clock_in","");
                    markattend.put("clock_out","");
                    markattend.put("clock_in_ip_address","");
                    markattend.put("clock_out_ip_address","");
                    markattend.put("working_from","");
                    markattend.put("notes","");
                    markattend.put("is_late",is_late);
                    markattend.put("office_start_time",clock_in);
                    markattend.put("office_end_time",clock_out);
                    Log.e("Markattendee",markattend+"");
                    mark_attendance(markattend);
                }
            }
        });
/*
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        */
    }
    /*
    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(radioId);
                radioButton = v.findViewById(radioId);
        if (radioId== v.findViewById(R.id.presentid).getId())
        {
            layoutabsent.setVisibility(View.GONE);
        }
        if (radioId==v.findViewById(R.id.presentid).getId())
        {
            layoutabsent.setVisibility(View.VISIBLE);
        }
        }
        */
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        choosedate.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateclockin() {
        //String myFormat = "hh:mm a";
        // In which you need put here
        //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
       // clocin.setText(sdf.format(myCalendar.getTime()));
        clocin.setText("10:00 AM");
    }
    private void updateclockout() {
       /* String myFormat = "hh:mm a";
        //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        clockout.setText(sdf.format(myCalendar.getTime()));
        */
        clockout.setText("06:00 PM");
    }

    void mark_attendance(Map<String,String> mark_attendance){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.loading_wait));
        pDialog.setCancelable(false);
        pDialog.show();



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
        Call<String> call = api.mark_attendance(mark_attendance);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
               // Log.e("response",response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            if (json.getString("success").equals("1"))
                            {
                               // JSONObject object = json.getJSONObject("data");
                                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                                resetAll();
                            }else if(json.getString("update").equals("1"))
                            {
                                Log.e("kdjsfsdfds",json.getString("update"));
                                //JSONObject object = json.getJSONObject("data");
                                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                                resetAll();
                            }else
                            {
                                Toast.makeText(getActivity(), json.getString("msg").toString(), Toast.LENGTH_SHORT).show();
                            }
                        /*
                            if(!json.isNull("success"))
                            {
                                Log.e("checksuccess","isnull");
                                startLogin(new Intent(getActivity(), MainActivity.class));
                                //setFragment(new LoginFragment());
                               // Toast.makeText(getActivity(), "success : "+json.getString("msg"), Toast.LENGTH_SHORT).show();
                            }else
                                {
                                    Toast.makeText(getActivity(), "Error : "+json.getString("msg"), Toast.LENGTH_SHORT).show();
                                }

                            */
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    pDialog.dismiss();
                }
                else
                {
                    pDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.dismiss();
                Log.e("failer", t.getMessage());
            }
        });

    }

    private void updateLiveType() {
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
        Call<String> call = api.leave_type(sharedPreference.getCompanyID(getContext()));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("response",response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject json = new JSONObject(response.body());

                            if (json.getString("success").equals("1"))
                            {
                                leavetypearray = new ArrayList<LeavetypeModel>();
                                JSONArray jsonArray = json.getJSONArray("leavetypes");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jobj = jsonArray.getJSONObject(i);
                                    LeavetypeModel ltm = new LeavetypeModel();
                                    ltm.setId(jobj.getString("id"));
                                    ltm.setLeaveType(jobj.getString("leaveType"));
                                    ltm.setNum_of_leave(jobj.getString("num_of_leave"));
                                    leavetypearray.add(ltm);
                                }
                                //JSONObject object = json.getJSONObject("leavetypes");
                                adepter = new LeaveTypeAdepter(getActivity(),leavetypearray);
                                leavetypespinner.setAdapter(adepter);
                            }else
                            {
                                Toast.makeText(getContext(), json.getString(""), Toast.LENGTH_SHORT).show();
                            }

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
    private void resetAll()
    {
        updateLabel();
        updateclockin();
        updateclockout();
        employeeid.setText("");
        reason.setText("");
        halfday.setChecked(false);
        late.setChecked(false);
    }
}