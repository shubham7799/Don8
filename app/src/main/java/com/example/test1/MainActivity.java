package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    EditText musername;
    EditText mpass;
    Button mlogin;
    TextView mreg;
    DatabaseHelper db;

    public static String user="";
    View hview;
    TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        musername = (EditText) findViewById(R.id.username);
        mpass = (EditText) findViewById(R.id.password);
        mlogin =(Button) findViewById(R.id.login);
        mreg = (TextView) findViewById(R.id.register);
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = musername.getText().toString().trim();
                String pwd = mpass.getText().toString().trim();
                Boolean res = db.checkUser(user,pwd);
                if(user.isEmpty() || pwd.isEmpty())
                { Toast.makeText(MainActivity.this,"Fill all Fields",Toast.LENGTH_SHORT).show();
                }else{ if(res == true){
                    Intent loginintent = new Intent(MainActivity.this,signup.class);
                startActivity(loginintent);
                }
                    else { Toast.makeText(MainActivity.this,"LOGIN ERROR",Toast.LENGTH_SHORT).show(); }
                }
            }
        });

        mreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regi = new Intent(MainActivity.this,register.class);
                startActivity(regi);
            }
        });
    }
}


