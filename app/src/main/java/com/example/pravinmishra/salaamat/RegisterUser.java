package com.example.pravinmishra.salaamat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pravinmishra on 02/12/17.
 */

public class RegisterUser extends StringRequest {
    public static final String REGISTER_REQUEST_URL ="http://salaamat.000webhostapp.com/register2.php";
    private Map<String,String> params;

    public RegisterUser(String name,String email,String password,String gender, String age,String smoking,String drinking,String weight,String height,Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params=new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("gender", gender);
        params.put("age", age);
        params.put("smoking", smoking);
        params.put("drinking", drinking);
        params.put("weight", weight);
        params.put("height", height);
//                params.put("gender", gender);
//                params.put("age", dob);



    }
    @Override
    public Map<String,String> getParams()
    {
        return params;
    }
}

