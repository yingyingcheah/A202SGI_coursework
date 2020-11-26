package com.example.yingying.moneysaver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SavingListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_list);

        //Set toolbar to replace the actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Money Saver");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        //Set toggle to display the drawer layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //Tie drawer layout events to the ActionBarDrawerToggle
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if (drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else{
            super.onBackPressed();
        }

    }

    public void displaySelectedListener(int itemId){
        //Create fragment object
        Fragment fragment = null;

        //Initialize the fragment selected
        switch (itemId){
            case R.id.navmenu_dashboard:
                fragment =  new DashboardFragment();
                break;

            case R.id.navmenu_income:
                fragment =  new IncomeFragment();
                break;

            case R.id.navmenu_expense:
                fragment =  new ExpenseFragment();
                break;

            case R.id.navmenu_chart:
                fragment = new ChartFragment();
                break;

            case R.id.navmenu_reminder:
                Intent newIntent = new Intent(SavingListActivity.this, ReminderActivity.class);
                startActivity(newIntent);
                break;
        }

        //Replace the fragment
        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Pass the selected menu id
        displaySelectedListener(item.getItemId());
        return true;
    }
}
