package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class signup extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    View hview;
    TextView username;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        hview=navigationView.getHeaderView(0);
        email = (TextView)hview.findViewById(R.id.parameter);
        email.setText(MainActivity.user);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile: getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new home_fragment()).commit();
                break;
            case R.id.food: getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new food_fragment()).commit();
                break;
            case R.id.flood: getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new flood_fragment()).commit();
                break;
            case R.id.clothes: getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new clothes_fragment()).commit();
                break;
            case R.id.logout:
                Intent logout = new Intent(this,MainActivity.class);
                startActivity(logout);
                break;
            case R.id.exit:
                Intent logout2 = new Intent(this,MainActivity.class);
                startActivity(logout2);
                this.finish();
                moveTaskToBack(true);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
