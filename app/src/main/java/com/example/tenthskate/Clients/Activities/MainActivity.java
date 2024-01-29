package com.example.tenthskate.Clients.Activities;

import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tenthskate.Clients.Fragment.BaseCategoryFragment;
import com.example.tenthskate.Clients.Fragment.BeamCategoryFragment;
import com.example.tenthskate.Clients.Fragment.DeckingCategoryFragment;
import com.example.tenthskate.Clients.Fragment.HardCategoryFragment;
import com.example.tenthskate.Clients.Fragment.HomeFragment;
import com.example.tenthskate.Clients.Fragment.MkekaCategoryFragment;
import com.example.tenthskate.Clients.Fragment.MyAccountFragment;
import com.example.tenthskate.Clients.Fragment.MyCartFragment;
import com.example.tenthskate.Clients.Fragment.MyOrdersFragment;
import com.example.tenthskate.Clients.Fragment.OfferZoneFragment;
import com.example.tenthskate.Clients.Fragment.OtherNeedsCategoryFragment;
import com.example.tenthskate.Clients.Fragment.TurfCategoryFragment;
import com.example.tenthskate.Clients.Fragment.search;
import com.example.tenthskate.R;
import com.example.tenthskate.Receipt.ReceiptFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public static int openCart = 0;
    public static int openHomeApp = 0;
    public static int openRoller = 0;
    public static int openHelmet = 0;
    public static int openKnee = 0;
    public static int openElbow = 0;
    public static int openWrist = 0;
    public static int openOthers = 0;
    public static int openMessages = 0;
    public static int openMyOrders = 0;

    private int RC_APP_UPDATE = 11;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;

    private TextView name, email;
    private Fragment fragment;
    //private Object ReceiptFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser==null){
            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            finish();
        }

        //////////////////////////////////////////////////////////////////////////////

        mAuth = FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser()!=null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            mDrawer = (DrawerLayout) findViewById(R.id.my_nav_drawer);
            drawerToggle = setupDrawerToggle();
            drawerToggle.syncState();

            NavigationView nvDrawer = (NavigationView) findViewById(R.id.nav_view);
            setupDrawerContent(nvDrawer);

            View header=nvDrawer.getHeaderView(0);

            name = header.findViewById(R.id.nav_header_fullName);
            email = header.findViewById(R.id.nav_header_email);

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

            userRef.child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        name.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            userRef.child("email").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        email.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

///////////////////////////////////  CATEGORY TO FRAGMENT  //////////////////////////////////////////////////////

        if (openCart == 1){
            setTitle("Cart");

            fragment = new MyCartFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openCart = 0;
        }
        else if (openHomeApp == 1){
            setTitle("Packed Kit");

            fragment = new TurfCategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openHomeApp =0;
        }
        else if (openRoller == 1){
            setTitle("Roller Skate");

            fragment = new HardCategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openRoller =0;
        }
        else if (openHelmet == 1){
            setTitle("Skate Helmet");

            fragment = new BeamCategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openHelmet =0;
        }
        else if (openKnee == 1){
            setTitle("Knee Pad");

            fragment = new MkekaCategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openKnee =0;
        }
        else if (openElbow == 1){
            setTitle("Elbow Pad");

            fragment = new BaseCategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openElbow =0;
        }
        else if (openWrist == 1){
            setTitle("Wrist Guard");

            fragment = new DeckingCategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openWrist =0;
        }
        else if (openOthers == 1){
            setTitle("Other Essentials");

            fragment = new OtherNeedsCategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openOthers =0;
        }
        else if (openMessages == 1){
            setTitle("Talk to us");

            fragment = new OtherNeedsCategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openMessages =0;
        }
        else if (openMyOrders == 1){
            setTitle("My Orders");
            fragment = new MyOrdersFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
            openMessages =0;
        }
        else {
            showHome();
        }

        /////////////////////////////////   UPDATE   /////////////////////////////////////

        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,MainActivity.this,RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE){
            Toast.makeText(MainActivity.this,"Starting download",Toast.LENGTH_SHORT).show();
            if (resultCode != RESULT_OK){
                Log.d("name","Update flow failed. Result code = "+resultCode);
            }
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {

        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.packedkit:
                fragmentClass = TurfCategoryFragment.class;
                break;
            case R.id.elbowkit:
                fragmentClass = HardCategoryFragment.class;
                break;
            case R.id.helmetkit:
                fragmentClass = BeamCategoryFragment.class;
                break;
            case R.id.rollerkit:
                fragmentClass = MkekaCategoryFragment.class;
                break;
            case R.id.kneekit:
                fragmentClass = BaseCategoryFragment.class;
                break;
            case R.id.wristkit:
                fragmentClass = DeckingCategoryFragment.class;
                break;
            case R.id.nav_other_needs_category:
                fragmentClass = OtherNeedsCategoryFragment.class;
                break;
            case R.id.nav_my_orders:
                fragmentClass = MyOrdersFragment.class;
                break;
            case R.id.nav_my_cart:
                fragmentClass = MyCartFragment.class;
                break;
            case R.id.nav_my_account:
                fragmentClass = MyAccountFragment.class;
                break;
            case R.id.feed:
                startActivity(new Intent(MainActivity.this, FeedbackActivity.class));

                break;
            case R.id.nav_my_notifications:
                fragmentClass = OfferZoneFragment.class;
                break;
            default:
                fragmentClass = search.class;
        }
        if (fragmentClass == null) {
            // Handle the null case or assign a default fragment
            fragmentClass = search.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(menuItem.getItemId() == R.id.nav_ask){
            Intent chatActivity = new Intent(this,ChatActivity.class);
            startActivity(chatActivity);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        super.onOptionsItemSelected(item);

//        if (item.getItemId()==R.id.feed){
//            setTitle("feedback");
//
//            fragment = new OrdersFragment();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();
//
//        }

        if (item.getItemId()==R.id.main_logout_option){
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        if (item.getItemId()==R.id.abt){
            new HDialog().show(getFragmentManager(), UIConsts.Fragments.ABOUT_DIALOG_TAG);
        }
        if (item.getItemId()==R.id.recei){
            setTitle("Receipts");

            fragment = new ReceiptFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_frame_layout, fragment).commit();

        }
        if (item.getItemId()==R.id.faq){
            Intent searchProduct = new Intent(MainActivity.this, com.example.tenthskate.Faq.MainActivity.class);
            startActivity(searchProduct);
        }
        if (item.getItemId()==R.id.main_search_option){
            Intent searchProduct = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(searchProduct);
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout mDrawer = findViewById(R.id.my_nav_drawer);
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (fragment instanceof HomeFragment){
                finish();
            }
            else {
                showHome();
            }
        }
    }

    private void showHome() {
        fragment = new search();
        setTitle("TENTH EMPIRE HOME");
        if(fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.my_frame_layout,fragment).commit();
        }
    }
}
