package com.example.tenthskate.Driver.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.tenthskate.Clients.Activities.LoginActivity;
import com.example.tenthskate.Driver.Adapter.TabsAccessorAdapter;
import com.example.tenthskate.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    static String email;
    DrawerLayout dLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drivermain);
        final Intent intent = getIntent();

        email = intent.getStringExtra("name");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tenth Empire");
     //   getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new incomingFragment()).commit();
        ViewPager myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        TabsAccessorAdapter myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        TabLayout myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.appr:
            // Handle manage product option
            // return true;
            case R.id.logou:
                // Handle logout option
                AlertDialog.Builder builder = new AlertDialog.Builder(com.example.tenthskate.Driver.Activity.MainActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Perform logout actions
                        // For example, clear session, sign out, or navigate to login activity
                        // ...
                        dialog.dismiss();
                        startActivity(new Intent(com.example.tenthskate.Driver.Activity.MainActivity.this, LoginActivity.class));

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public String FragmentMethod() {

        return email;
    }
    public static String startactivity(){
        return email;
    }


}