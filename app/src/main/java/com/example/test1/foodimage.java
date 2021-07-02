package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class foodimage extends AppCompatActivity {
    ImageView fname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fname=(ImageView)findViewById(R.id.foodimage);
        fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(foodimage.this,foodrecycler.class);
                startActivity(i);
            }
        });

    }
}
