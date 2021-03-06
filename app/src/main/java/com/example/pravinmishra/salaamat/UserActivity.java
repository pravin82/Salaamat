package com.example.pravinmishra.salaamat;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import weka.classifiers.functions.SMO;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pravinmishra on 02/12/17.
 */

public class UserActivity extends AppCompatActivity {

    private TextView name, username, age, height, weight, gender, smoking, drinking, yourapi, yourcl, yourhr;
    private Button btnLogout,btnHitAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        name = (TextView) findViewById(R.id.name);
        username = (TextView) findViewById(R.id.username);
        age = (TextView) findViewById(R.id.age);
        height = (TextView) findViewById(R.id.height);
        weight = (TextView) findViewById(R.id.weight);
        gender = (TextView) findViewById(R.id.gender);
        smoking = (TextView) findViewById(R.id.smoking);
        drinking = (TextView) findViewById(R.id.drinking);
        yourapi = (TextView) findViewById(R.id.yourapi);
        yourcl = (TextView) findViewById(R.id.yourcl);
        yourhr = (TextView) findViewById(R.id.yourhr);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnHitAPI = (Button) findViewById(R.id.btnHitAPI);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logintent=new Intent(UserActivity.this,MainActivity.class);
                startActivity(logintent);
            }
        });


        Intent intent = getIntent();
        String name_i = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        name.setText(name_i);
        username.setText(email);
        age.setText(intent.getStringExtra("age"));
        height.setText(intent.getStringExtra("height"));
        weight.setText(intent.getStringExtra("weight"));
        gender.setText(intent.getStringExtra("gender"));
        smoking.setText(intent.getStringExtra("smoking"));
        drinking.setText(intent.getStringExtra("drinking"));

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       btnHitAPI.performClick();
                        MiClass1 htt = new MiClass1();
                        try {
                            String vin = htt.sendGet();
                            String server_val=htt.miclass2CustomMethod(vin);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }, 0, 60000);

    }

    public class MiClass1 implements View.OnClickListener {
        private String vinay;

        @Override
        public void onClick(View v) {
            MiClass1 http = new MiClass1();
            try {
                this.vinay = http.sendGet();
                miclass2CustomMethod(vinay);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // getdatafrom();
        }

        private String miclass2CustomMethod(String vinay) {
           // HashMap<String, String> user1 = db.getUserDetails();
           CSVLoader loader = new CSVLoader();
            Instances trainDataset = null;
           AssetManager manager = getAssets();
            try {
                InputStream ttt = manager.open("StaticTrainData1.csv");
                loader.setSource(ttt);
                Intent intent=getIntent();
                SMO svm = new SMO();
                trainDataset = loader.getDataSet();
                trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
                svm.buildClassifier(trainDataset);
                Instance inst = new DenseInstance(trainDataset.numAttributes());
                inst.setDataset(trainDataset);
                inst.setValue(0, Integer.parseInt(intent.getStringExtra("age")));
                inst.setValue(1, Integer.parseInt(intent.getStringExtra("height")));
                inst.setValue(2, Integer.parseInt(intent.getStringExtra("weight")));
                inst.setValue(3, Integer.parseInt(intent.getStringExtra("smokinghabit")));
                inst.setValue(4, Integer.parseInt(intent.getStringExtra("alcoholhabit")));
                inst.setValue(5, Integer.parseInt(intent.getStringExtra("gender")));

                // Air Quality parameters could not be included in the model due to lack of time
             String  heart = "140";
                heart = vinay;
//                inst.setValue(6, Integer.parseInt(String.valueOf(txtYourapi)));
                String yourclass = "";
                if (heart != "") {
                    inst.setValue(6, Integer.parseInt(heart));
//                String hr = new MiClass1;
                    double predSVM = 0;
                    predSVM = svm.classifyInstance(inst);
                    String predString = trainDataset.classAttribute().value((int) predSVM);
                    //String yourclass = "You belong to " + "'"+ predString+ "'";
                    yourclass = predString;
                } else {
                    yourclass = "Null Prediction";
                }

                yourapi.setText(yourclass);
//                String phoneNumber = "9932186594";
//                String smsBody = "You belong to "+ yourclass ;
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);
                return yourclass;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

//        private void sendsms(String phoneNumber, String message) {
//            SmsManager sms = SmsManager.getDefault();
//            sms.sendTextMessage(phoneNumber, null, message, null, null);
//        }

        private String sendGet() throws IOException, InterruptedException, JSONException {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, -15);
            Date before_d = now.getTime();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String reportDate = df.format(before_d);
            String use_date = reportDate.substring(11, 16);
            yourhr.setText("Heart Rate at " + use_date);
            yourcl.setText("Your class at " + use_date);
            String url = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d/1min/time/" + use_date + "/" + use_date + ".json";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
//            con.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1REZYSkoiLCJhdWQiOiIyMjg0OFMiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJudXQgcnBybyByc2xlIiwiZXhwIjoxNDkwNTMyNTkyLCJpYXQiOjE0ODk5Mjc3OTJ9.PFmPCqiI8x7nJtnbPxKTjyBvYexhefNlHMV8FCPYLLA");
//            con.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1REZYSkoiLCJhdWQiOiIyMjg0OFMiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJudXQgcnBybyByc2xlIiwiZXhwIjoxNTIzMDQ5NDA3LCJpYXQiOjE0OTE1MTM0MDd9.GqJ-ZjPHA3nIgcTnGAg7BUNQPdx8EWIpIDv_ZpzC7fo");
            con.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1REZYSkoiLCJhdWQiOiIyMjg0OFMiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJhY3QgcnNldCBybG9jIHJ3ZWkgcmhyIHJwcm8gcm51dCByc2xlIiwiZXhwIjoxNTEyODU4NzA0LCJpYXQiOjE1MTIyNTM5MDR9.z8ugoxBgyGLKX4QQRf8CWfXNMAWxAOzAViFSAFz9mdU");
            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            StringBuilder jsonString = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                jsonString.append(inputLine);
                response.append(inputLine);
            }
            in.close();
            JSONObject jsonObj = new JSONObject(jsonString.toString());
            JSONObject final_json = null;
            Toast.makeText(getApplicationContext(), "Response "+Integer.toString(con.getResponseCode()), Toast.LENGTH_LONG).show();
            try {
                final_json = new JSONObject(jsonObj.getJSONObject("activities-heart-intraday").getJSONArray("dataset").get(0).toString());
                yourapi.setText(final_json.get("value").toString());
            } catch (JSONException e) {
                yourapi.setText("Null");
            }

            if (final_json != null) {
                return final_json.get("value").toString();
            }

            return "";
        }

    }

}
