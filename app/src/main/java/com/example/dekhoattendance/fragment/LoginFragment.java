package com.example.dekhoattendance.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.dekhoattendance.MainActivity;
import com.example.dekhoattendance.R;
import com.example.dekhoattendance.common.Api;
import com.example.dekhoattendance.common.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    Button btn_login;
    TextView click_toregister;
    EditText userEmail;
    EditText userPassword;

    ProgressDialog pDialog;

    public LoginFragment() {
        // Required empty public constructor
    }
    private SharedPreference sharedPreference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_login, container, false);

        click_toregister=(TextView) v.findViewById(R.id.click_toregister);
        userEmail=(EditText) v.findViewById(R.id.userEmail);
        userPassword=(EditText) v.findViewById(R.id.userPassword);
        btn_login=(Button) v.findViewById(R.id.btn_login);
        sharedPreference = new SharedPreference();

        click_toregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new RegisterFragment());
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userEmail.getText().toString().equals("") ||userEmail.getText().toString().equals(null))
                {
                    userEmail.setError("Please Enter Email");
                }else if (!isValidMail(userEmail.getText().toString()))
                {
                    userEmail.setError("Please Enter Valid Email");
                }else if (userPassword.getText().toString().equals("") ||userPassword.getText().toString().equals(null))
                {
                    userPassword.setError("Please Enter Password");
                }else
                {
                    company_login(userEmail.getText().toString(),userPassword.getText().toString());
                }

            }
        });
        return  v;
    }

    void company_login(String email , String password){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.loading_wait));
        pDialog.setCancelable(false);
        pDialog.show();

        HashMap<String,String> reg_data = new HashMap<>();
        reg_data.put("email",email);
        reg_data.put("password",password);

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
        Call<String> call = api.company_login(reg_data);
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
                                if (json.getString("role").equals("employee")) {
                                    JSONObject object = json.getJSONObject("data");
                                    sharedPreference.save(getActivity(), object.getString("id"), object.getString("company_id"));
                                    // Log.e("checksuccess",object.getString("id")+"");
                                    // object.getString("company_id");
                                    startLogin(new Intent(getActivity(), MainActivity.class));
                                }else{
                                    JSONObject object = json.getJSONObject("data");
                                    sharedPreference.save(getActivity(), object.getString("id"), object.getString("company_id"));
                                    Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                                    // Log.e("checksuccess",object.getString("id")+"");
                                    // object.getString("company_id");
                                    //startLogin(new Intent(getActivity(), MainActivity.class));
                                }
                            }else
                            {
                                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
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

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void startLogin(final Intent intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            getActivity().finish();
        } catch (Exception e) {

        }
    }
}
