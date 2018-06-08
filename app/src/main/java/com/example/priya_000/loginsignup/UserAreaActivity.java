package com.example.priya_000.loginsignup;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);



        final TextView welcome=(TextView)findViewById(R.id.tvWelcomeMsg);
        final EditText username= (EditText) findViewById(R.id.etUsername);
        final EditText etage= (EditText) findViewById(R.id.etcontact);

        Bundle data=getIntent().getExtras();
        String Udata=data.getString("Umessage");
        username.setText(Udata);

    }
}


