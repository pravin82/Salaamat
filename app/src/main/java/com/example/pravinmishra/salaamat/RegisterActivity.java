package com.example.pravinmishra.salaamat;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pravinmishra on 02/12/17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://salaamat.000webhostapp.com/register2.php";
    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword, signupInputAge, getSignupInputWeight, getSignupInputHeight;
    private Button btnSignUp;
    private Button btnLinkLogin;
    private RadioGroup genderRadioGroup;
    private RadioGroup smokingRadioGroup;
    private RadioGroup drinkingRadioGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        signupInputAge = (EditText) findViewById(R.id.signup_input_age);
        getSignupInputHeight = (EditText) findViewById(R.id.signup_input_height);
        getSignupInputWeight = (EditText) findViewById(R.id.signup_input_weight);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnLinkLogin = (Button) findViewById(R.id.btn_link_login);

        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
        smokingRadioGroup = (RadioGroup) findViewById(R.id.smoking_radio_group);
        drinkingRadioGroup = (RadioGroup) findViewById(R.id.drinking_radio_group);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void submitForm() {

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();


        String gender;
        if(selectedId == R.id.female_radio_btn)
            gender = "0";
        else
            gender = "1";

        int selectedId1 = smokingRadioGroup.getCheckedRadioButtonId();


        String smoking;
        if(selectedId == R.id.no_smokingradio_btn)
            smoking = "0";
        else
            smoking = "1";

        int selectedId2 = genderRadioGroup.getCheckedRadioButtonId();


        String drink;
        if(selectedId == R.id.no_radio_btn)
            drink = "0";
        else
            drink = "1";
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse =new JSONObject(response);
                    boolean success =jsonResponse.getBoolean("success");
                    if(success)
                    {
                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                        RegisterActivity.this.startActivity(intent);
                        Log.e(TAG, String.valueOf("succesful signup"));
                    }
                    else{
                        AlertDialog.Builder builder =new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Email ID is already registered")
                                .setNegativeButton("Retry",null).create()
                                .show();


                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
//        RegisterUser registerRequest = new RegisterUser(signupInputName.getText().toString(), signupInputEmail.getText().toString(), signupInputPassword.getText().toString(),signupInputAge.getText().toString(),getSignupInputHeight.getText().toString(),getSignupInputWeight.getText().toString(),drink,smoking,gender, responseListener);
//        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
//        queue.add(registerRequest);

       registerUser(signupInputName.getText().toString(),
                signupInputEmail.getText().toString(),
                signupInputPassword.getText().toString(),
                gender,
                signupInputAge.getText().toString(),
                smoking,
                drink,
                getSignupInputWeight.getText().toString(),
                getSignupInputHeight.getText().toString());
    }

    private void registerUser(final String name, final String email, final String password,
                              final String gender, final String age,final String smoking,final String drinking,final String weight,final String height ) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");

                    if (error) {
                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), "Hi " + user +", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

//                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "conn err", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);
                params.put("age", age);
                params.put("smoking", smoking);
                params.put("drinking", drinking);
                params.put("weight", weight);
                params.put("height", height);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
