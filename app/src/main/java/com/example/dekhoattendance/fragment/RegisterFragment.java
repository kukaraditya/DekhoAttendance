package com.example.dekhoattendance.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dekhoattendance.R;
import com.example.dekhoattendance.common.Api;

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

public class RegisterFragment extends Fragment {

    Button reg_btn;

    EditText companyname;
    EditText address;
    EditText country;
    EditText contectno;
    EditText whatsappno;
    EditText username;
    EditText useremail;
    EditText et_password;

    ProgressDialog pDialog;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_register, container, false);

        companyname=(EditText) v.findViewById(R.id.companyname);
        address=(EditText) v.findViewById(R.id.address);
        country=(EditText) v.findViewById(R.id.country);
        contectno=(EditText) v.findViewById(R.id.contectno);
        whatsappno=(EditText) v.findViewById(R.id.whatsappno);
        username=(EditText) v.findViewById(R.id.username);
        useremail=(EditText) v.findViewById(R.id.useremail);
        et_password=(EditText) v.findViewById(R.id.et_password);

        reg_btn=(Button) v.findViewById(R.id.reg_btn);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (companyname.getText().toString().equals(null) || companyname.getText().toString().equals(""))
                {
                    companyname.setError("Please Enter Company Name");
                }else if (address.getText().toString().equals(null) || address.getText().toString().equals(""))
                {
                    address.setError("Please Enter Company Address");
                }else if (country.getText().toString().equals(null) || country.getText().toString().equals(""))
                {
                    country.setError("Please Enter Country");
                }else if (contectno.getText().toString().equals(null) || contectno.getText().toString().equals(""))
                {
                    contectno.setError("Please Enter Contect Number");
                }else  if (!isValidMobile(contectno.getText().toString()))
                {
                    contectno.setError("Please Enter Valid Contect Number");
                }else  if (!isValidMobile(whatsappno.getText().toString()))
                {
                    whatsappno.setError("Please Enter Valid Contect Number");
                }else if (whatsappno.getText().toString().equals(null) || whatsappno.getText().toString().equals(""))
                {
                    whatsappno.setError("Please Enter Whatsapp Number");
                }else if (username.getText().toString().equals(null) || username.getText().toString().equals(""))
                {
                    username.setError("Please Enter User Name");
                }else if (useremail.getText().toString().equals(null) || useremail.getText().toString().equals(""))
                {
                    useremail.setError("Please Enter User Email");
                }else if (!isValidMail(useremail.getText().toString()))
                {
                    useremail.setError("Please Enter Valid Email");
                }else if (et_password.getText().toString().equals(null) || et_password.getText().toString().equals(""))
                {
                    et_password.setError("Please Enter Password");
                }else {

                    String company_name = companyname.getText().toString();
                    String address1 = address.getText().toString();
                    String country1 = country.getText().toString();
                    String contact = contectno.getText().toString();
                    String whatsapp = whatsappno.getText().toString();
                    String name = username.getText().toString();
                    String currency = "INR";
                    String email = useremail.getText().toString();
                    String password = et_password.getText().toString();

                    company_registration(company_name, address1, country1, contact, whatsapp, name, currency, email, password);
                }
            }
        });
        return  v;
    }

    void company_registration(String company_name,String address,String country,String contact,String whatsapp,String name,String currency,String email,String password){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.loading_wait));
        pDialog.setCancelable(false);
        pDialog.show();

        HashMap<String,String> reg_data = new HashMap<>();
        reg_data.put("company_name",company_name);
        reg_data.put("address",address);
        reg_data.put("country",country);
        reg_data.put("contact",contact);
        reg_data.put("whatsapp",whatsapp);
        reg_data.put("name",name);
        reg_data.put("currency",currency);
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
        Call<String> call = api.company_registration(reg_data);
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
                                setFragment(new LoginFragment());
                            }else
                            {
                                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                            }


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
}

