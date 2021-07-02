package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("Registered")
public class register extends AppCompatActivity {
    DatabaseHelper db;
    EditText musername;
    EditText mpass1;
    EditText mpass2;
    Button mreg;
    TextView mlog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        db = new DatabaseHelper(this);
        musername = (EditText) findViewById(R.id.username);
        mpass1 = (EditText) findViewById(R.id.password1);
        mpass2 = (EditText) findViewById(R.id.password2);
        mreg = (Button)findViewById(R.id.reg);
        mreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = musername.getText().toString().trim();
                String pwd1 = mpass1.getText().toString().trim();
                String pwd2 = mpass2.getText().toString().trim();
                if(pwd1.equals(pwd2)){
                    long val = db.addUser(user,pwd1);
                    if(val>0) {
                        Toast.makeText(register.this,"Registration Successful.!",Toast.LENGTH_SHORT).show();
                        Intent regtolog = new Intent(register.this,MainActivity.class);
                        startActivity(regtolog); }
                    else{ Toast.makeText(register.this,"user not registered.!",Toast.LENGTH_SHORT).show(); }
                }
                else{ Toast.makeText(register.this,"Password not matched.!",Toast.LENGTH_SHORT).show(); }
            }
        });
        mlog = (TextView)findViewById(R.id.log);
        mlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logonintent = new Intent(register.this,MainActivity.class);
                startActivity(logonintent);
            }
        });

    }
}


