package com.example.tenthskate.Shippment.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.tenthskate.Clients.Activities.LoginActivity;
import com.example.tenthskate.R;
import com.example.tenthskate.Shippment.Adapter.TabsAccessorAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Shipment Manager");

        ViewPager myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        TabsAccessorAdapter myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        TabLayout myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.conf, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.appr:
//                Intent manageProductIntent = new Intent(MainActivity.this, requestd.class);
//               // manageProductIntent.putExtra("retProductID", uploadCur.getProductID());
//                startActivity(manageProductIntent);
//                return true;
//            case R.id.logou:
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Finance Logout");
//                builder.setMessage("Are you sure you want to logout your from finance account ?")
//                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                //your deleting code
//                                //DeleteToken();
//                           //     FirebaseAuth.getInstance().signOut();
//                                Intent intent_signout = new Intent(MainActivity.this, LoginActivity.class);
//                                startActivity(intent_signout);
//                                finish();
//                                dialog.dismiss();
//                            }
//
//                        })
//                        .setNegativeButton("Cancell", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                builder.show();
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.logoutmenu, menu);
    return true;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.appr:
                // Handle manage product option
               // return true;
            case R.id.logou:
                // Handle logout option
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Perform logout actions
                        // For example, clear session, sign out, or navigate to login activity
                        // ...
                        dialog.dismiss();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

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

}