package com.example.pravinmishra.salaamat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by pravinmishra on 02/12/17.
 */

public class UserActivity extends AppCompatActivity {

    private TextView name, username, age, height, weight, gender, smoking, drinking;
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

    }
}
